package ru.clevertec.kalustau.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.clevertec.kalustau.dto.CommentDtoRequest;
import ru.clevertec.kalustau.dto.Proto;
import ru.clevertec.kalustau.model.Comment;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.kalustau.util.TestData.getCommentDtoRequest;
import static ru.clevertec.kalustau.util.TestData.getCommentDtoResponse;
import static ru.clevertec.kalustau.util.TestData.getCommentList;

class CommentMapperTest {

    private static CommentMapper commentMapper;

    private Comment comment = getCommentList().get(0);;
    private CommentDtoRequest commentDtoRequest = getCommentDtoRequest();
    private Proto.CommentDtoResponse commentDtoResponse = getCommentDtoResponse();

    @BeforeAll
    static void setUp() {
        commentMapper = new CommentMapper();
    }

    @Test
    void checkDtoToNews() {
        Comment actual = commentMapper.dtoToComment(commentDtoRequest);
        assertThat(actual.getText()).isEqualTo(comment.getText());
    }

    @Test
    void checkNewsToDto() {
        Proto.CommentDtoResponse actual = commentMapper.commentToDto(comment);
        assertThat(actual.getId()).isEqualTo(commentDtoResponse.getId());
        assertThat(actual.getText()).isEqualTo(commentDtoResponse.getText());
        assertThat(actual.getUserName()).isEqualTo(commentDtoResponse.getUserName());
    }

}