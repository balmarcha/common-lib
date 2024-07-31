package es.bxg.commonlib.controllers;

import es.bxg.commonlib.exceptions.NoContentException;
import es.bxg.commonlib.services.IGenericService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface ModifyGenericController<D> {

  IGenericService getService();

  @PostMapping
  default D save(@RequestBody D dto) {
    return (D) getService().save(dto);
  }

  @PatchMapping("/{id}")
  default D update(@PathVariable Long id, @RequestBody Map<String, Object> data) {
    try {
      return (D) getService().patch(id, data);
    } catch (NoContentException e) {
      throw new RuntimeException(e);
    }
  }

  @DeleteMapping("/{id}")
  default void delete(@PathVariable Long id) {
    try {
      getService().delete(id);
    } catch (NoContentException e) {
      throw new RuntimeException(e);
    }
  }
}
