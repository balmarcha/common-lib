package es.bxg.commonlib.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.bxg.commonlib.dtos.BaseDto;
import es.bxg.commonlib.exceptions.NoContentException;
import es.bxg.commonlib.interfaces.Obsoletable;
import es.bxg.commonlib.mappers.CycleAvoidingMappingContext;
import es.bxg.commonlib.mappers.IGenericMapper;
import es.bxg.commonlib.repositories.IGenericRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public abstract class GenericServiceImpl<E, P, D extends BaseDto, ID> implements IGenericService<E, P, D, ID> {

  private final String NOT_FOUND_ID_MESSAGE = "No se encuentra el registro con id: ";

  protected abstract IGenericMapper<E, P, D> getMapper();

  protected abstract IGenericRepository<E, ID> getRepository();

  protected abstract Class<E> getEntityClass();

  @Override
  public D findById(ID id) throws NoContentException {
    E entity = getRepository().findById(id).orElseThrow(() -> new NoContentException(NOT_FOUND_ID_MESSAGE + id));

    return getMapper().pojoToDto(getMapper().entityToPojo(entity, new CycleAvoidingMappingContext()), new CycleAvoidingMappingContext());
  }

  @Override
  public D findOne(Map<String, Object> filters) throws NoContentException {
    try {
      Example<E> example = buildExample(filters);
      E entity = getRepository().findOne(example).orElseThrow(() -> new NoContentException(NOT_FOUND_ID_MESSAGE));
      return getMapper().pojoToDto(getMapper().entityToPojo(entity, new CycleAvoidingMappingContext()), new CycleAvoidingMappingContext());
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<D> findAll(Map<String, Object> filters) {
    try {
      Example<E> example = buildExample(filters);
      return getMapper().pojoToDto(getMapper().entityToPojo(getRepository().findAll(example), new CycleAvoidingMappingContext()), new CycleAvoidingMappingContext());
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Page<D> findPage(Map<String, Object> filters, Pageable pageable) {
    try {
      Example<E> example = buildExample(filters);
      return getMapper().pojoToDto(getMapper().entityToPojo(getRepository().findAll(example, pageable), new CycleAvoidingMappingContext()), new CycleAvoidingMappingContext());
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public D save(D dto) {
    dto.setId(null);
    E entity = getMapper().pojoToEntity(getMapper().dtoToPojo(dto, new CycleAvoidingMappingContext()), new CycleAvoidingMappingContext());
    return getMapper().pojoToDto(getMapper().entityToPojo(getRepository().save(entity), new CycleAvoidingMappingContext()), new CycleAvoidingMappingContext());
  }

  @Override
  public D patch(ID id, Map<String, Object> fields)
      throws NoContentException {
    E entity = getRepository().findById(id).orElseThrow(() -> new NoContentException(NOT_FOUND_ID_MESSAGE + id));
    P pojo = getMapper().entityToPojo(entity, new CycleAvoidingMappingContext());

    entity = getMapper().pojoToEntity(recursivePatch(pojo, fields), new CycleAvoidingMappingContext());
    return getMapper().pojoToDto(getMapper()
        .entityToPojo(getRepository().save(entity), new CycleAvoidingMappingContext()), new CycleAvoidingMappingContext());
  }

  @Override
  public void delete(ID id) throws NoContentException {
    E entity = getRepository().findById(id).orElseThrow(() -> new NoContentException(NOT_FOUND_ID_MESSAGE + id));
    P pojo = getMapper().entityToPojo(entity, new CycleAvoidingMappingContext());

    if (pojo instanceof Obsoletable) {
      ((Obsoletable) pojo).markAsObsolete();
      getRepository().save(getMapper().pojoToEntity(pojo, new CycleAvoidingMappingContext()));
    } else {
      getRepository().delete(entity);
    }
  }

  private P recursivePatch(P pojo, Map<String, Object> fields) {

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode entityNode = objectMapper.valueToTree(pojo);
    JsonNode fieldsNode = objectMapper.valueToTree(fields);
    applyPatch(entityNode, fieldsNode);
    try {
      return (P) objectMapper.treeToValue(entityNode, pojo.getClass());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  private void applyPatch(JsonNode entityNode, JsonNode fieldsNode) {
    fieldsNode.fields().forEachRemaining(entry -> {
      String patchKey = entry.getKey();
      JsonNode patchValue = entry.getValue();

      if (entityNode.has(patchKey)) {
        JsonNode currentValue = entityNode.get(patchKey);
        if (currentValue.isObject() && patchValue.isObject()) {
          applyPatch(currentValue, patchValue);
        } else {
          ((ObjectNode) entityNode).set(patchKey, patchValue);
        }
      }
    });
  }

  private <E> Example<E> buildExample(Map<String, Object> properties) throws IllegalAccessException {
    E entity = (E) new ObjectMapper().convertValue(properties, getEntityClass());
    ExampleMatcher matcher = ExampleMatcher.matching();

    for (String key : properties.keySet()) {
      matcher = matcher.withMatcher(key, ExampleMatcher.GenericPropertyMatchers.contains());
    }

    return Example.of(entity, matcher);
  }
}
