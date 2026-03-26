package development.team.ticketsystem.ticketservice.Mappers;

import development.team.ticketsystem.ticketservice.DTO.categories.CategoryResponse;
import development.team.ticketsystem.ticketservice.DTO.categories.CreateCategoryRequest;
import development.team.ticketsystem.ticketservice.Entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryResponse toResponse(CategoryEntity entity);

    CategoryEntity toEntity(CreateCategoryRequest request);

    void updateEntityFromRequest(CreateCategoryRequest request, @MappingTarget CategoryEntity entity);
}