package ru.clevertec.kalustau.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.kalustau.dto.CommentDto;
import ru.clevertec.kalustau.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment dtoToComment(CommentDto commentDto);

    CommentDto commentToDto(Comment comment);
}
