package development.team.ticketsystem.ticketservice.mappers;

import development.team.ticketsystem.ticketservice.dto.categories.CategoryResponse;
import development.team.ticketsystem.ticketservice.dto.categories.CreateCategoryRequest;
import development.team.ticketsystem.ticketservice.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toResponse(CategoryEntity entity);

    CategoryEntity toEntity(CreateCategoryRequest request);

    void updateEntityFromRequest(CreateCategoryRequest request, @MappingTarget CategoryEntity entity);
}