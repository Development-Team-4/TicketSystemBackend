package development.team.ticketsystem.ticketservice.mappers;

import development.team.ticketsystem.ticketservice.dto.topics.CreateTopicRequest;
import development.team.ticketsystem.ticketservice.dto.topics.TopicResponse;
import development.team.ticketsystem.ticketservice.entity.TopicEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    TopicResponse toResponse(TopicEntity entity);

    TopicEntity toEntity(CreateTopicRequest request);

    void updateEntityFromRequest(CreateTopicRequest request, @MappingTarget TopicEntity entity);
}