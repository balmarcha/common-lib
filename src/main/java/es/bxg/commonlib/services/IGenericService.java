package es.bxg.commonlib.services;

import es.bxg.commonlib.exceptions.NoContentException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Interfaz genérica
 */
public interface IGenericService<E, P, D, ID> {

  D findById(ID id) throws NoContentException;

  List<D> findAll(Map<String, Object> properties);

  Page<D> findPage(Map<String, Object> properties, Pageable pageable);

  D save(D dto);

  void delete(ID id) throws NoContentException;

  D patch(ID id, Map<String, Object> fields)
      throws NoContentException;
}
