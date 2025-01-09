package es.bxg.commonlib.adapter.mapper;

import org.mapstruct.Context;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public interface IGenericDboMapper<E, P> {
  E pojoToEntity(P pojo, @Context CycleAvoidingMappingContext context);

  P entityToPojo(E entity, @Context CycleAvoidingMappingContext context);

  List<E> pojoToEntity(Iterable<P> pojos, @Context CycleAvoidingMappingContext context);

  List<P> entityToPojo(Iterable<E> entities, @Context CycleAvoidingMappingContext context);

  default Page<E> pojoToEntity(Page<P> pojos, @Context CycleAvoidingMappingContext context) {
    return new PageImpl<>(pojoToEntity(pojos.getContent(), context), pojos.getPageable(), pojos.getTotalElements());
  }

  default Page<P> entityToPojo(Page<E> entities, @Context CycleAvoidingMappingContext context) {
    return new PageImpl<>(entityToPojo(entities.getContent(), context), entities.getPageable(), entities.getTotalElements());
  }
}

