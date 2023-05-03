package ru.clevertec.kalustau.cache.comment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.kalustau.model.Comment;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.kalustau.util.Constants.TEST_ID;
import static ru.clevertec.kalustau.util.TestData.getComment;
import static ru.clevertec.kalustau.util.TestData.getCommentList;

class CommentCacheLFUTest {

    private static CommentCacheLFU commentCacheLFU;

    @BeforeAll
    static void setUp() {
        commentCacheLFU = new CommentCacheLFU(3);
    }

    @BeforeEach
    void init() {
        commentCacheLFU.clear();
        getCommentList().stream().forEach(commentCacheLFU::save);
    }

    @Test
    void checkGetById() {
        Comment expectedComment = getCommentList().get(0);
        Optional<Comment> commentData = commentCacheLFU.getById(TEST_ID);
        assertThat(commentData.get()).isEqualTo(expectedComment);
    }

    @Test
    void checkGetUserByIdShouldReturnOptionalEmpty() {
        Optional<Comment> actualCommentData = commentCacheLFU.getById(4L);
        assertThat(actualCommentData).isEmpty();
    }

    @Test
    void checkSave() {
        Comment expectedComment = getComment();
        commentCacheLFU.save(expectedComment);
        Optional<Comment> actualCommentData = commentCacheLFU.getById(expectedComment.getId());
        assertThat(actualCommentData.get()).isEqualTo(expectedComment);
    }

    @Test
    void checkUpdate() {
        Comment comment = commentCacheLFU.getById(1L).get();
        comment.setText("New text");
        comment.setUserName("New user");
        commentCacheLFU.update(comment);
        Comment updatedComment = commentCacheLFU.getById(1L).get();
        assertThat(updatedComment.getText()).isEqualTo("New text");
        assertThat(updatedComment.getUserName()).isEqualTo("New user");
    }

    @Test
    void checkDelete() {
        Comment comment = getCommentList().get(0);
        commentCacheLFU.delete(comment.getId());
        Optional<Comment> actualCommentData = commentCacheLFU.getById(comment.getId());
        assertThat(actualCommentData).isEmpty();
    }

    @Test
    void checkCacheShouldReplaceCommentWithLowestFrequently() {
        commentCacheLFU.getById(1L);
        commentCacheLFU.getById(2L);

        Comment comment = Comment.builder().id(4L).text("New text").userName("New user").build();
        commentCacheLFU.save(comment);
        assertThat(commentCacheLFU.getById(3L)).isEmpty();
    }

}