package es.bxg.commonlib.service;

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

  default D patch(ID id, Map<String, Object> fields)
      {
    P pojo = getQueryService().findById(id);
    return getMapper().pojoToDto(getCommandService().save(pojo), new CycleAvoidingMappingContext());
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
}
