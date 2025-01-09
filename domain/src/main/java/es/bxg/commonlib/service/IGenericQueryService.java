package es.bxg.commonlib.service;

import es.bxg.commonlib.model.Page;
import es.bxg.commonlib.model.Pageable;
import es.bxg.commonlib.port.IGenericQueryPort;

import java.util.List;
import java.util.Map;

public interface IGenericQueryService<P, ID> {

  IGenericQueryPort<P, ID> getPort();

  default P findById(ID id) {
    return getPort().findById(id);
  }

  default P findOne(Map<String, Object> filters) {
    return getPort().findOne(filters);
  }

  default List<P> findAll(Map<String, Object> properties) {
    return getPort().findAll(properties);
  }

  default Page<P> findPage(Map<String, Object> properties, Pageable pageable) {
    return getPort().findPage(properties, pageable);
  }
}
