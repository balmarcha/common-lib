package es.bxg.commonlib.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import es.bxg.commonlib.mapper.CycleAvoidingMappingContext;
import es.bxg.commonlib.mapper.IGenericDtoMapper;
import es.bxg.commonlib.model.dtos.BaseDto;
import es.bxg.commonlib.model.interfaces.Obsoletable;
import es.bxg.commonlib.model.pojo.BasePojo;

import java.util.Map;

public interface IGenericCommandHandler<P extends BasePojo, D extends BaseDto, ID>{

  IGenericDtoMapper<P, D> getMapper();
  IGenericQueryService<P, ID> getQueryService();
  IGenericCommandService<P, ID> getCommandService();

  default D save(D dto) {
    dto.setId(null);
    return getMapper().pojoToDto(getCommandService().save(getMapper().dtoToPojo(dto, new CycleAvoidingMappingContext())), new CycleAvoidingMappingContext());
  }

  default D patch(ID id, Map<String, Object> fields) {
    P pojo = recursivePatch(getQueryService().findById(id), fields);
    return getMapper().pojoToDto(getCommandService().patch(pojo), new CycleAvoidingMappingContext());
  }

  default void delete(ID id) {
    P pojo = getQueryService().findById(id);

    if (pojo instanceof Obsoletable) {
      ((Obsoletable) pojo).markAsObsolete();
      getCommandService().save(pojo);
    } else {
      getCommandService().delete(id);
    }
  }

  private P recursivePatch(P pojo, Map<String, Object> fields) {

    ObjectMapper om = new ObjectMapper();
    om.registerModule(new JavaTimeModule());

    JsonNode entityNode = om.valueToTree(pojo);
    JsonNode fieldsNode = om.valueToTree(fields);
    applyPatch(entityNode, fieldsNode);
    try {
      return (P) om.treeToValue(entityNode, pojo.getClass());
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
}
