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

class CommentCacheLRUTest {

    private static CommentCacheLRU commentCacheLRU;

    @BeforeAll
    static void setUp() {
        commentCacheLRU = new CommentCacheLRU(3);
    }

    @BeforeEach
    void init() {
        commentCacheLRU.clear();
        getCommentList().stream().forEach(commentCacheLRU::save);
    }

    @Test
    void checkGetById() {
        Comment expectedComment = getCommentList().get(0);
        Optional<Comment> commentData = commentCacheLRU.getById(TEST_ID);
        assertThat(commentData.get()).isEqualTo(expectedComment);
    }

    @Test
    void checkGetUserByIdShouldReturnOptionalEmpty() {
        Optional<Comment> actualCommentData = commentCacheLRU.getById(4L);
        assertThat(actualCommentData).isEmpty();
    }

    @Test
    void checkSave() {
        Comment expectedComment = getComment();
        commentCacheLRU.save(expectedComment);
        Optional<Comment> actualCommentData = commentCacheLRU.getById(expectedComment.getId());
        assertThat(actualCommentData.get()).isEqualTo(expectedComment);
    }

    @Test
    void checkUpdate() {
        Comment comment = commentCacheLRU.getById(1L).get();
        comment.setText("New text");
        comment.setUserName("New user");
        commentCacheLRU.update(comment);
        Comment updatedComment = commentCacheLRU.getById(1L).get();
        assertThat(updatedComment.getText()).isEqualTo("New text");
        assertThat(updatedComment.getUserName()).isEqualTo("New user");
    }

    @Test
    void checkDelete() {
        Comment comment = getCommentList().get(0);
        commentCacheLRU.delete(comment.getId());
        Optional<Comment> actualCommentData = commentCacheLRU.getById(comment.getId());
        assertThat(actualCommentData).isEmpty();
    }

    @Test
    void checkCacheShouldReplaceCommentWithLongAgoUsage() {
        Comment comment = Comment.builder().id(4L).text("New text").userName("New user").build();
        commentCacheLRU.save(comment);
        assertThat(commentCacheLRU.getById(1L)).isEmpty();
    }

}