package es.bxg.commonlib.controllers;

import es.bxg.commonlib.exceptions.NoContentException;
import es.bxg.commonlib.services.IGenericService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface ReadGenericController<D> {

  IGenericService getService();

  @GetMapping("/{id}")
  default D findOne(@PathVariable("id") Long id) throws NoContentException {
    return (D) getService().findById(id);
  }

  @GetMapping
  default List<D> findAll(@RequestParam Map<String, Object> filters) {
    return getService().findAll(filters);
  }

  @GetMapping({"/page"})
  default Page<D> findPage(@RequestParam(required = false) Map<String, Object> filters, Pageable pageable) {
    return this.getService().findPage(this.clearFilters(filters), pageable);
  }


  private Map<String, Object> clearFilters(Map<String, Object> filters) {
    filters.remove("page");
    filters.remove("size");
    filters.remove("sort");

    return filters;
  }
}
