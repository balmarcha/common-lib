package es.bxg.commonlib.adapter.port;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.bxg.commonlib.adapter.exception.NoContentException;
import es.bxg.commonlib.adapter.mapper.CycleAvoidingMappingContext;
import es.bxg.commonlib.adapter.mapper.IGenericDboMapper;
import es.bxg.commonlib.adapter.repository.IGenericRepository;
import es.bxg.commonlib.model.interfaces.Obsoletable;
import es.bxg.commonlib.model.pojo.BasePojo;
import es.bxg.commonlib.port.IGenericCommandPort;
import org.springframework.http.HttpStatus;

import java.util.Map;

public abstract class GenericJpaCommandPort<E, P extends BasePojo, ID> implements IGenericCommandPort<P, ID> {

  private final String NOT_FOUND_ID_MESSAGE = "No se encuentra el registro con id: ";

  protected abstract IGenericDboMapper<E, P> getMapper();

  protected abstract IGenericRepository<E, ID> getRepository();

  @Override
  public P save(P pojo) {
    pojo.setId(null);
    E entity = getMapper().pojoToEntity(pojo, new CycleAvoidingMappingContext());
    return getMapper().entityToPojo(getRepository().save(entity), new CycleAvoidingMappingContext());
  }

  @Override
  public P patch(P pojo)
      throws NoContentException {

    E entity = getMapper().pojoToEntity(pojo, new CycleAvoidingMappingContext());
    return getMapper()
        .entityToPojo(getRepository().save(entity), new CycleAvoidingMappingContext());
  }

  @Override
  public void delete(ID id) throws NoContentException {
    E entity = getRepository().findById(id).orElseThrow(() -> new NoContentException(HttpStatus.NO_CONTENT, NOT_FOUND_ID_MESSAGE + id));
    P pojo = getMapper().entityToPojo(entity, new CycleAvoidingMappingContext());

    if (pojo instanceof Obsoletable) {
      ((Obsoletable) pojo).markAsObsolete();
      getRepository().save(getMapper().pojoToEntity(pojo, new CycleAvoidingMappingContext()));
    } else {
      getRepository().delete(entity);
    }
  }
}
