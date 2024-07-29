package es.bxg.commonlib.controllers;

import es.bxg.commonlib.exceptions.NoContentException;
import es.bxg.commonlib.services.IGenericService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public abstract class GenericController<D> {

  protected abstract IGenericService getService();

  @GetMapping
  public List<D> findAll(@RequestParam Map<String, Object> filters) {
    return getService().findAll(filters);
  }

  @GetMapping("/page")
  public PagedModel<EntityModel<D>> findPage(@RequestParam Map<String, Object> filters, Pageable pageable,
                                             PagedResourcesAssembler<D> assembler) {
    Page<D> page = getService().findPage(filters, pageable);
    return assembler.toModel(page);
  }

  @PostMapping
  public D save(@RequestBody D dto) {
    return (D) getService().save(dto);
  }

  @PatchMapping("/{id}")
  public D update(@PathVariable Long id, @RequestBody Map<String, Object> data) {
    try {
      return (D) getService().patch(id, data);
    } catch (NoContentException e) {
      throw new RuntimeException(e);
    }
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    try {
      getService().delete(id);
    } catch (NoContentException e) {
      throw new RuntimeException(e);
    }
  }

}
