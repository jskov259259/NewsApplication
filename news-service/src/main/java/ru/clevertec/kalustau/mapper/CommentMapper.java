package ru.clevertec.kalustau.mapper;

import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Component;
import ru.clevertec.kalustau.model.Comment;
import ru.clevertec.kalustau.dto.Proto.CommentDto;

import java.time.Instant;
import java.time.ZoneId;

@Component
public class CommentMapper {

    public Comment dtoToComment(CommentDto commentDto) {
        return Comment.builder()
                .id(commentDto.getId())
                //                .time(commentDto.getTime())
                .text(commentDto.getText())
                .userName(commentDto.getUserName())
                .build();
    }

    public CommentDto commentToDto(Comment comment) {
        Instant instant = comment.getTime().atZone(ZoneId.systemDefault()).toInstant();
        return CommentDto
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
