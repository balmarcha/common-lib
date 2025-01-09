package es.bxg.commonlib.rest.controllers;

import es.bxg.commonlib.adapter.exception.NoContentException;
import es.bxg.commonlib.model.dtos.BaseDto;
import es.bxg.commonlib.service.IGenericCommandHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface IGenericCommandController<D extends BaseDto> {

  IGenericCommandHandler getCommandHandler();

  @PostMapping
  default D save(@RequestBody D dto) {
    return (D) getCommandHandler().save(dto);
  }

  @PatchMapping("/{id}")
  default D update(@PathVariable Long id, @RequestBody Map<String, Object> data) {
    try {
      return (D) getCommandHandler().patch(id, data);
    } catch (NoContentException e) {
      throw new RuntimeException(e);
    }
  }

  @DeleteMapping("/{id}")
  default void delete(@PathVariable Long id) {
    try {
      getCommandHandler().delete(id);
    } catch (NoContentException e) {
      throw new RuntimeException(e);
    }
  }
}
