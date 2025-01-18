package es.bxg.commonlib.adapter.port;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.bxg.commonlib.adapter.exception.NoContentException;
import es.bxg.commonlib.adapter.mapper.CycleAvoidingMappingContext;
import es.bxg.commonlib.adapter.mapper.IGenericDboMapper;
import es.bxg.commonlib.adapter.repository.IGenericRepository;
import es.bxg.commonlib.model.Page;
import es.bxg.commonlib.model.Pageable;
import es.bxg.commonlib.model.pojo.BasePojo;
import es.bxg.commonlib.port.IGenericQueryPort;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GenericJpaQueryPort<E, P extends BasePojo, ID> implements IGenericQueryPort<P, ID> {

  private final String NOT_FOUND_ID_MESSAGE = "No se encuentra el registro con id: ";

  protected abstract IGenericDboMapper<E, P> getMapper();

  protected abstract IGenericRepository<E, ID> getRepository();

  protected abstract Class<E> getEntityClass();

  @Override
  public P findById(ID id) throws NoContentException {
    E entity = getRepository().findById(id).orElseThrow(() -> new NoContentException(HttpStatus.NO_CONTENT, NOT_FOUND_ID_MESSAGE + id));
    return getMapper().entityToPojo(entity, new CycleAvoidingMappingContext());
  }

  @Override
  public P findOne(Map<String, Object> filters) throws NoContentException {
    try {
      Example<E> example = buildExample(filters);
      E entity = getRepository().findOne(example).orElseThrow(() -> new NoContentException(HttpStatus.NO_CONTENT, NOT_FOUND_ID_MESSAGE));
      return getMapper().entityToPojo(entity, new CycleAvoidingMappingContext());
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<P> findAll(Map<String, Object> filters) {
    try {
      Example<E> example = buildExample(filters);
      return getMapper().entityToPojo(getRepository().findAll(example), new CycleAvoidingMappingContext());
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Page<P> findPage(Map<String, Object> filters, Pageable pageable) {
    try {
      org.springframework.data.domain.Page<E> sp = getRepository().findAll(buildExample(filters),
          PageRequest.of(pageable.getPageSize(), pageable.getPageNumber()));

      return new Page<>(
          getMapper().entityToPojo(sp.getContent(), new CycleAvoidingMappingContext()),
          sp.getNumber(),
          sp.getSize(),
          sp.getTotalElements()
      );
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private <E> Example<E> buildExample(Map<String, Object> filters) throws IllegalAccessException {

    Map<String, Object> cleanFilters = clearFilters(filters);

    Map<String, Object> simpleProperties = new HashMap<>();
    Map<String, Object> nestedProperties = new HashMap<>();


    for (Map.Entry<String, Object> entry : cleanFilters.entrySet()) {
      String key = entry.getKey();
      if (key.contains(".")) {
        nestedProperties.put(key, entry.getValue());
      } else {
        simpleProperties.put(key, entry.getValue());
      }
    }

    E entity = (E) new ObjectMapper().convertValue(simpleProperties, getEntityClass());

    for (Map.Entry<String, Object> entry : nestedProperties.entrySet()) {
      String key = entry.getKey();
      String[] part = key.split("\\.");

      try {
        Field field = entity.getClass().getDeclaredField(part[0]);
        field.setAccessible(true);

        Object nested = field.get(entity);
        if (nested == null) {
          nested = field.getType().getDeclaredConstructor().newInstance();
        }

        Field subfiels = nested.getClass().getDeclaredField(part[1]);
        subfiels.setAccessible(true);
        subfiels.set(nested, castValue(entry.getValue(), subfiels.getType()));

        field.set(entity, nested);

      } catch (NoSuchFieldException | InvocationTargetException | InstantiationException | NoSuchMethodException ignored) {}
    }

    ExampleMatcher matcher = ExampleMatcher.matching();
    return Example.of(entity, matcher);
  }

  private Object castValue(Object valor, Class<?> tipoCampo) {
    if (tipoCampo == Long.class || tipoCampo == long.class) {
      return Long.valueOf(valor.toString());
    } else if (tipoCampo == Integer.class || tipoCampo == int.class) {
      return Integer.valueOf(valor.toString());
    } else if (tipoCampo == Double.class || tipoCampo == double.class) {
      return Double.valueOf(valor.toString());
    } else if (tipoCampo == String.class) {
      return valor.toString();
    }
    // Agregar más casos según sea necesario
    throw new IllegalArgumentException("No se puede convertir el valor al tipo: " + tipoCampo);
  }

  private Map<String, Object> clearFilters(Map<String, Object> filters) {
    filters.remove("page");
    filters.remove("size");
    filters.remove("sort");

    return filters;
  }
}
