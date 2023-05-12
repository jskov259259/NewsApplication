package ru.clevertec.kalustau.mapper;

import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Component;
import ru.clevertec.kalustau.dto.CommentDtoRequest;
import ru.clevertec.kalustau.model.Comment;
import ru.clevertec.kalustau.dto.Proto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Component
public class CommentMapper {

    public Comment dtoToComment(CommentDtoRequest commentDtoRequest) {
        return Comment.builder()
                .text(commentDtoRequest.getText())
                .build();
    }

    public Proto.CommentDtoResponse commentToDto(Comment comment) {
        Instant instant = comment.getTime().atDate(LocalDate.now()).toInstant(ZoneOffset.UTC);
        return Proto.CommentDtoResponse
                .newBuilder()
                .setId(comment.getId())
                .setTime(Timestamp.newBuilder()
                        .setSeconds(instant.getEpochSecond())
                        .setNanos(instant.getNano())
                        .build())
                .setText(comment.getText())
                .setUserName(comment.getUserName())
                .build();
    }
}
