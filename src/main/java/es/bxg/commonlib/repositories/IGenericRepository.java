package es.bxg.commonlib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Repositorio genérico
 *
 * @param <E>  entidad tipo
 * @param <ID> id tipo
 */
@NoRepositoryBean
public interface IGenericRepository<E, ID>
    extends JpaRepository<E, ID> {
}
