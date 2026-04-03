package development.team.ticketsystem.ticketservice.mappers;

import development.team.ticketsystem.ticketservice.dto.comments.CommentResponse;
import development.team.ticketsystem.ticketservice.dto.comments.CreateCommentRequest;
import development.team.ticketsystem.ticketservice.entity.CommentEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-31T01:32:06+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Microsoft)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentResponse toResponse(CommentEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CommentResponse.CommentResponseBuilder commentResponse = CommentResponse.builder();

        commentResponse.id( entity.getId() );
        commentResponse.ticketId( entity.getTicketId() );
        commentResponse.authorId( entity.getAuthorId() );
        commentResponse.content( entity.getContent() );
        commentResponse.createdAt( entity.getCreatedAt() );

        return commentResponse.build();
    }

    @Override
    public CommentEntity toEntity(CreateCommentRequest request) {
        if ( request == null ) {
            return null;
        }

        CommentEntity.CommentEntityBuilder commentEntity = CommentEntity.builder();

        commentEntity.content( request.getContent() );

        return commentEntity.build();
    }
}
