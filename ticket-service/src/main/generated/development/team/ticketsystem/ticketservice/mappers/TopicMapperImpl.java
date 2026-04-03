package development.team.ticketsystem.ticketservice.mappers;

import development.team.ticketsystem.ticketservice.dto.topics.CreateTopicRequest;
import development.team.ticketsystem.ticketservice.dto.topics.TopicResponse;
import development.team.ticketsystem.ticketservice.entity.TopicEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-31T01:32:06+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Microsoft)"
)
@Component
public class TopicMapperImpl implements TopicMapper {

    @Override
    public TopicResponse toResponse(TopicEntity entity) {
        if ( entity == null ) {
            return null;
        }

        TopicResponse.TopicResponseBuilder topicResponse = TopicResponse.builder();

        topicResponse.id( entity.getId() );
        topicResponse.name( entity.getName() );
        topicResponse.description( entity.getDescription() );

        return topicResponse.build();
    }

    @Override
    public TopicEntity toEntity(CreateTopicRequest request) {
        if ( request == null ) {
            return null;
        }

        TopicEntity.TopicEntityBuilder topicEntity = TopicEntity.builder();

        topicEntity.name( request.getName() );
        topicEntity.description( request.getDescription() );

        return topicEntity.build();
    }

    @Override
    public void updateEntityFromRequest(CreateTopicRequest request, TopicEntity entity) {
        if ( request == null ) {
            return;
        }

        entity.setName( request.getName() );
        entity.setDescription( request.getDescription() );
    }
}
