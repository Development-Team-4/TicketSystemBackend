package development.team.ticketsystem.ticketservice.Mappers;

import development.team.ticketsystem.ticketservice.DTO.comments.CommentResponse;
import development.team.ticketsystem.ticketservice.DTO.comments.CreateCommentRequest;
import development.team.ticketsystem.ticketservice.Entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentResponse toResponse(CommentEntity entity);

    CommentEntity toEntity(CreateCommentRequest request);
}