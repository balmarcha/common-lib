package es.bxg.commonlib.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public interface IGenericMapper<E, P, D> {
  D pojoToDto(P pojo, @Context CycleAvoidingMappingContext context);

  E pojoToEntity(P pojo, @Context CycleAvoidingMappingContext context);

  P entityToPojo(E entity, @Context CycleAvoidingMappingContext context);

  P dtoToPojo(D dto, @Context CycleAvoidingMappingContext context);

  List<D> pojoToDto(Iterable<P> pojos, @Context CycleAvoidingMappingContext context);

  List<E> pojoToEntity(Iterable<P> pojos, @Context CycleAvoidingMappingContext context);

  List<P> entityToPojo(Iterable<E> entities, @Context CycleAvoidingMappingContext context);

  List<P> dtoToPojo(Iterable<D> entities, @Context CycleAvoidingMappingContext context);

  // Métodos para paginación
  default Page<D> pojoToDto(Page<P> pojos, @Context CycleAvoidingMappingContext context) {
    return new PageImpl<>(pojoToDto(pojos.getContent(), context), pojos.getPageable(), pojos.getTotalElements());
  }

  default Page<E> pojoToEntity(Page<P> pojos, @Context CycleAvoidingMappingContext context) {
    return new PageImpl<>(pojoToEntity(pojos.getContent(), context), pojos.getPageable(), pojos.getTotalElements());
  }

  default Page<P> entityToPojo(Page<E> entities, @Context CycleAvoidingMappingContext context) {
    return new PageImpl<>(entityToPojo(entities.getContent(), context), entities.getPageable(), entities.getTotalElements());
  }

  default Page<P> dtoToPojo(Page<D> dtos, @Context CycleAvoidingMappingContext context) {
    return new PageImpl<>(dtoToPojo(dtos.getContent(), context), dtos.getPageable(), dtos.getTotalElements());
  }

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  E partialUpdate(D dto, @MappingTarget E entity);
}

