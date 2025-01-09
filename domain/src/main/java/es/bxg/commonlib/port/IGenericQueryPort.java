package es.bxg.commonlib.port;

import es.bxg.commonlib.model.Page;
import es.bxg.commonlib.model.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Interfaz genérica
 */
public interface IGenericQueryPort<P, ID> {

  P findById(ID id);

  P findOne(Map<String, Object> filters);

  List<P> findAll(Map<String, Object> properties);

  Page<P> findPage(Map<String, Object> properties, Pageable pageable);
}
