package es.bxg.commonlib.rest.controllers;

import es.bxg.commonlib.adapter.exception.NoContentException;
import es.bxg.commonlib.model.Page;
import es.bxg.commonlib.model.Pageable;
import es.bxg.commonlib.model.dtos.BaseDto;
import es.bxg.commonlib.service.IGenericQueryHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface IGenericQueryController<D extends BaseDto> {

  IGenericQueryHandler getQueryHandler();

  @GetMapping("/{id}")
  default D findOne(@PathVariable("id") Long id) throws NoContentException {
    return (D) getQueryHandler().findById(id);
  }

  @GetMapping
  default List<D> findAll(@RequestParam Map<String, Object> filters) {
    return getQueryHandler().findAll(filters);
  }

  @GetMapping({"/page"})
  default Page<D> findPage(@RequestParam(required = false) Map<String, Object> filters, Pageable pageable) {
    return getQueryHandler().findPage(filters, pageable);
  }
}
