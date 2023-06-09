package ru.clevertec.kalustau.integration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.kalustau.integration.BaseIntegrationTest;
import ru.clevertec.kalustau.model.Comment;
import ru.clevertec.kalustau.repository.CommentRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.kalustau.util.Constants.TEST_ID;
import static ru.clevertec.kalustau.util.Constants.TEST_PAGE_NO;
import static ru.clevertec.kalustau.util.Constants.TEST_PAGE_SIZE;
import static ru.clevertec.kalustau.util.Constants.TEST_SEARCH;
import static ru.clevertec.kalustau.util.Constants.TEST_SORT_BY;
import static ru.clevertec.kalustau.util.TestData.getComment;
import static ru.clevertec.kalustau.util.TestData.getTestSpecification;

class CommentRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void checkFindAllShouldReturn10() {
        Specification<Comment> specification = getTestSpecification(TEST_SEARCH);
        Page<Comment> pagedResult = commentRepository.findAll(specification,
                PageRequest.of(TEST_PAGE_NO, TEST_PAGE_SIZE, Sort.by(TEST_SORT_BY)));
        assertThat(pagedResult.getContent().size()).isEqualTo(10);
    }

    @Test
    void checkFindByIdShouldReturnOptionalComment() {
        Optional<Comment> commentData = commentRepository.findById(TEST_ID);
        assertThat(commentData.get().getId()).isEqualTo(TEST_ID);
        assertThat(commentData.get().getText()).isEqualTo("Nice news");
        assertThat(commentData.get().getUserName()).isEqualTo("Cellular");
    }

    @Test
    void checkFindByIdShouldReturnOptionalEmpty() {
        Optional<Comment> commentData = commentRepository.findById(21L);
        assertThat(commentData).isEmpty();
    }

    @Test
    void checkFindAllByNewsId() {
        Page<Comment> pagedResult = commentRepository.findAllByNewsId(TEST_ID,
                PageRequest.of(TEST_PAGE_NO, TEST_PAGE_SIZE, Sort.by(TEST_SORT_BY)));
        assertThat(pagedResult.getContent().size()).isEqualTo(3);
    }

    @Test
    void checkSave() {
        Comment comment = getComment();
        Comment createdComment = commentRepository.save(comment);
        assertThat(createdComment.getText()).isEqualTo("Text2");
        assertThat(createdComment.getUserName()).isEqualTo("User2");
    }

    @Test
    void checkUpdate() {
        Comment comment = getComment();
        commentRepository.save(comment);

        comment.setText("New text");
        comment.setUserName("New username");
        Comment updatedComment = commentRepository.save(comment);
        assertThat(updatedComment.getText()).isEqualTo("New text");
        assertThat(updatedComment.getUserName()).isEqualTo("New username");
    }

    @Test
    void deleteById() {
        Integer sizeBefore = commentRepository.findAll(PageRequest.of(TEST_PAGE_NO, TEST_PAGE_SIZE+10, Sort.by(TEST_SORT_BY)))
                .getContent().size();

        commentRepository.deleteById(20L);

        Integer sizeAfter = commentRepository.findAll(PageRequest.of(TEST_PAGE_NO, TEST_PAGE_SIZE+10, Sort.by(TEST_SORT_BY)))
                .getContent().size();
        assertThat(sizeAfter).isLessThan(sizeBefore);
    }

}