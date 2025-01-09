package es.bxg.commonlib.service;

import es.bxg.commonlib.port.IGenericCommandPort;

import java.util.Map;

public interface IGenericCommandService<P, ID> {

  IGenericCommandPort<P, ID> getPort();

  default P save(P pojo) {
    return getPort().save(pojo);
  }

  default P update(P pojo, Map<String, Object> data) {
    return getPort().patch(pojo, data);
  }

  default void delete(ID id) {
    getPort().delete(id);
  }
}
