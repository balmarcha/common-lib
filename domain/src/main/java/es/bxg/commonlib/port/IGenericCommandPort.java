package es.bxg.commonlib.port;

import java.util.Map;

/**
 * Interfaz genérica
 */
public interface IGenericCommandPort<P, ID> {
  P save(P pojo);

  void delete(ID id);

  P patch(P pojo);
}
