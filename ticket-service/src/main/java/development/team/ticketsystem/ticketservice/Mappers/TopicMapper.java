package development.team.ticketsystem.ticketservice.Mappers;

import development.team.ticketsystem.ticketservice.DTO.topics.CreateTopicRequest;
import development.team.ticketsystem.ticketservice.DTO.topics.TopicResponse;
import development.team.ticketsystem.ticketservice.Entity.TopicEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    TopicMapper INSTANCE = Mappers.getMapper(TopicMapper.class);

    TopicResponse toResponse(TopicEntity entity);

    TopicEntity toEntity(CreateTopicRequest request);

    void updateEntityFromRequest(CreateTopicRequest request, @MappingTarget TopicEntity entity);
}