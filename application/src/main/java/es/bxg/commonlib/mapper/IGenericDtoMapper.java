package es.bxg.commonlib.mapper;

import es.bxg.commonlib.model.Page;
import org.mapstruct.Context;

import java.util.List;

public interface IGenericDtoMapper<P, D> {
  D pojoToDto(P pojo, @Context CycleAvoidingMappingContext context);

  P dtoToPojo(D dto, @Context CycleAvoidingMappingContext context);

  List<D> pojoToDto(Iterable<P> pojos, @Context CycleAvoidingMappingContext context);

  List<P> dtoToPojo(Iterable<D> entities, @Context CycleAvoidingMappingContext context);

  // Métodos para paginación
  default Page<D> pojoToDto(Page<P> pojos, @Context CycleAvoidingMappingContext context) {
    return new Page(pojoToDto(pojos.getContent(), context), pojos.getPageNumber(), pojos.getPageSize(), pojos.getTotalElements());
  }
}

