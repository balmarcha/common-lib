package es.bxg.commonlib.controllers;

import es.bxg.commonlib.services.IGenericService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface ReadGenericController<D> {

  IGenericService getService();

  @GetMapping
  default List<D> findAll(@RequestParam Map<String, Object> filters) {
    return getService().findAll(filters);
  }

  @GetMapping("/page")
  default PagedModel<EntityModel<D>> findPage(@RequestParam(required = false) Map<String, Object> filters, Pageable pageable,
                                              PagedResourcesAssembler<D> assembler) {
    Page<D> page = getService().findPage(clearFilters(filters), pageable);
    return assembler.toModel(page);
  }

  private Map<String, Object> clearFilters(Map<String, Object> filters) {
    filters.remove("page");
    filters.remove("size");
    filters.remove("sort");
    filters.remove("forceFirstAndLastRels");

    return filters;
  }
}
