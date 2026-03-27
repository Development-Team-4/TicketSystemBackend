package development.team.ticketsystem.ticketservice.mappers;

import development.team.ticketsystem.ticketservice.dto.comments.CommentResponse;
import development.team.ticketsystem.ticketservice.dto.comments.CreateCommentRequest;
import development.team.ticketsystem.ticketservice.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentResponse toResponse(CommentEntity entity);

    CommentEntity toEntity(CreateCommentRequest request);
}