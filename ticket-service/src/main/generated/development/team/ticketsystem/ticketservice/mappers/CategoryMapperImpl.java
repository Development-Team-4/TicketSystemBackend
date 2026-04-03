package development.team.ticketsystem.ticketservice.mappers;

import development.team.ticketsystem.ticketservice.dto.categories.CategoryResponse;
import development.team.ticketsystem.ticketservice.dto.categories.CreateCategoryRequest;
import development.team.ticketsystem.ticketservice.entity.CategoryEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-31T01:32:06+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Microsoft)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryResponse toResponse(CategoryEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CategoryResponse.CategoryResponseBuilder categoryResponse = CategoryResponse.builder();

        categoryResponse.id( entity.getId() );
        categoryResponse.topicId( entity.getTopicId() );
        categoryResponse.name( entity.getName() );
        categoryResponse.description( entity.getDescription() );

        return categoryResponse.build();
    }

    @Override
    public CategoryEntity toEntity(CreateCategoryRequest request) {
        if ( request == null ) {
            return null;
        }

        CategoryEntity.CategoryEntityBuilder categoryEntity = CategoryEntity.builder();

        categoryEntity.topicId( request.getTopicId() );
        categoryEntity.name( request.getName() );
        categoryEntity.description( request.getDescription() );

        return categoryEntity.build();
    }

    @Override
    public void updateEntityFromRequest(CreateCategoryRequest request, CategoryEntity entity) {
        if ( request == null ) {
            return;
        }

        entity.setTopicId( request.getTopicId() );
        entity.setName( request.getName() );
        entity.setDescription( request.getDescription() );
    }
}
