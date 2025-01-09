package es.bxg.commonlib.service;

import es.bxg.commonlib.mapper.CycleAvoidingMappingContext;
import es.bxg.commonlib.mapper.IGenericDtoMapper;
import es.bxg.commonlib.model.Page;
import es.bxg.commonlib.model.Pageable;
import es.bxg.commonlib.model.dtos.BaseDto;
import es.bxg.commonlib.model.pojo.BasePojo;

import java.util.List;
import java.util.Map;

public interface IGenericQueryHandler<P extends BasePojo, D extends BaseDto, ID> {

  IGenericDtoMapper<P, D> getMapper();

  IGenericQueryService<P, ID> getQueryService();

  default D findById(ID id) {
    return getMapper().pojoToDto(getQueryService().findById(id), new CycleAvoidingMappingContext());
  }

  default D findOne(Map<String, Object> filters) {
    return getMapper().pojoToDto(getQueryService().findOne(getCleanFilters(filters)), new CycleAvoidingMappingContext());
  }

  default List<D> findAll(Map<String, Object> filters) {
    return getMapper().pojoToDto(getQueryService().findAll(getCleanFilters(filters)), new CycleAvoidingMappingContext());
  }

  default Page<D> findPage(Map<String, Object> filters, Pageable pageable) {
    return getMapper().pojoToDto(getQueryService().findPage(getCleanFilters(filters), pageable), new CycleAvoidingMappingContext());
  }

  private Map<String, Object> getCleanFilters(Map<String, Object> filters) {
    filters.remove("page");
    filters.remove("size");
    filters.remove("sort");

    return filters;
  }
}
