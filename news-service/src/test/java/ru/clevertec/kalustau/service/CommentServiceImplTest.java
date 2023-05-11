package ru.clevertec.kalustau.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.kalustau.client.dto.User;
import ru.clevertec.kalustau.exceptions.PermissionException;
import ru.clevertec.kalustau.exceptions.ResourceNotFoundException;
import ru.clevertec.kalustau.model.Comment;
import ru.clevertec.kalustau.repository.CommentRepository;
import ru.clevertec.kalustau.repository.NewsRepository;
import ru.clevertec.kalustau.service.impl.CommentServiceImpl;
import ru.clevertec.kalustau.service.impl.UserUtility;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static ru.clevertec.kalustau.util.Constants.TEST_ID;
import static ru.clevertec.kalustau.util.Constants.TEST_PAGE_NO;
import static ru.clevertec.kalustau.util.Constants.TEST_PAGE_SIZE;
import static ru.clevertec.kalustau.util.Constants.TEST_SEARCH;
import static ru.clevertec.kalustau.util.Constants.TEST_SORT_BY;
import static ru.clevertec.kalustau.util.Constants.TEST_TOKEN;
import static ru.clevertec.kalustau.util.TestData.getComment;
import static ru.clevertec.kalustau.util.TestData.getCommentList;
import static ru.clevertec.kalustau.util.TestData.getNews;
import static ru.clevertec.kalustau.util.TestData.getTestSpecification;
import static ru.clevertec.kalustau.util.TestData.getUserAdmin;
import static ru.clevertec.kalustau.util.TestData.getUserSubscriber;
import static ru.clevertec.kalustau.util.TestData.getUserWithoutRole;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private UserUtility userUtility;

    @Captor
    ArgumentCaptor<Comment> commentCaptor;

    @Test
    void checkFindAll() {
        Comment comment = getCommentList().get(0);
        Specification<Comment> specification = getTestSpecification(TEST_SEARCH);

        doReturn(new PageImpl<>(getCommentList()))
                .when(commentRepository).findAll(specification, PageRequest.of(TEST_PAGE_NO, TEST_PAGE_SIZE, Sort.by(TEST_SORT_BY)));

        List<Comment> commentList = commentService.findAll(TEST_SEARCH, TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY);

        verify(commentRepository).findAll(specification, PageRequest.of(TEST_PAGE_NO, TEST_PAGE_SIZE, Sort.by(TEST_SORT_BY)));

        assertThat(commentList.get(0)).isEqualTo(comment);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void checkFindById(Long id) {
        Comment comment = getComment();

        doReturn(Optional.of(comment))
                .when(commentRepository).findById(id);

        Comment result = commentService.findById(id);

        verify(commentRepository).findById(anyLong());
        assertThat(result).isEqualTo(comment);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void checkFindByIdShouldThrowResourceNotFoundException(Long id) {
        doThrow(ResourceNotFoundException.class)
                .when(commentRepository).findById(anyLong());
        assertThatThrownBy(() -> commentService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(commentRepository).findById(anyLong());
    }

    @Test
    void checkFindAllByNewsId() {
        Comment comment = getCommentList().get(0);

        doReturn(new PageImpl<>(getCommentList()))
                .when(commentRepository).findAllByNewsId(TEST_ID, PageRequest.of(TEST_PAGE_NO, TEST_PAGE_SIZE, Sort.by(TEST_SORT_BY)));
        doReturn(Boolean.TRUE)
                .when(newsRepository).existsById(TEST_ID);

        List<Comment> commentList = commentService.findAllByNewsId(TEST_ID,
                TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY);

        verify(newsRepository).existsById(TEST_ID);
        verify(commentRepository).findAllByNewsId(TEST_ID, PageRequest.of(TEST_PAGE_NO,
                TEST_PAGE_SIZE, Sort.by(TEST_SORT_BY)));

        assertThat(commentList.get(0)).isEqualTo(comment);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void checkFindAllByNewsIdShouldThrowResourceNotFoundException() {
        doThrow(ResourceNotFoundException.class)
                .when(newsRepository).existsById(anyLong());
        assertThatThrownBy(() -> commentService.findAllByNewsId(TEST_ID, TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(newsRepository).existsById(TEST_ID);
    }

    @Test
    void checkSave() {
        Comment comment = getComment();
        User user = getUserAdmin();

        doReturn(comment)
                .when(commentRepository).save(commentCaptor.capture());
        doReturn(Optional.of(getNews()))
                .when(newsRepository).findById(TEST_ID);
        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);

        Comment result = commentService.save(TEST_ID, comment, TEST_TOKEN);
        verify(newsRepository).findById(TEST_ID);
        verify(commentRepository).save(comment);
        verify(userUtility).getUserByToken(anyString());
        assertThat(result.getText()).isEqualTo(comment.getText());
        assertThat(commentCaptor.getValue()).isEqualTo(comment);
    }

    @Test
    void checkSaveShouldThrowPermissionExceptionForUserWithoutRole() {
        Comment comment = getComment();
        User user = getUserWithoutRole();

        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);

        assertThatThrownBy(() -> commentService.save(TEST_ID, comment, TEST_TOKEN))
                .isInstanceOf(PermissionException.class)
                .hasMessage("No permission to perform operation");
        verify(userUtility).getUserByToken(anyString());
    }

    @Test
    void checkUpdate() {
        Comment comment = getComment();
        User user = getUserAdmin();

        doReturn(Optional.of(comment))
                .when(commentRepository).findById(TEST_ID);
        doReturn(comment)
                .when(commentRepository).save(commentCaptor.capture());
        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);

        Comment result = commentService.update(TEST_ID, comment, TEST_TOKEN);

        verify(commentRepository).findById(anyLong());
        verify(commentRepository).save(comment);
        verify(userUtility).getUserByToken(anyString());
        assertThat(result.getText()).isEqualTo(comment.getText());
        assertThat(commentCaptor.getValue()).isEqualTo(comment);
    }

    @Test
    void checkUpdateShouldThrowResourceNotFoundException() {
        User user = getUserAdmin();
        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);
        doThrow(ResourceNotFoundException.class)
                .when(commentRepository).findById(anyLong());
        assertThatThrownBy(() -> commentService.update(TEST_ID, getComment(), TEST_TOKEN))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(commentRepository).findById(anyLong());
        verify(userUtility).getUserByToken(anyString());
    }

    @Test
    void checkUpdateShouldThrowPermissionExceptionForUserWithoutRole() {
        Comment comment = getComment();
        User user = getUserWithoutRole();

        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);

        assertThatThrownBy(() -> commentService.update(TEST_ID, comment, TEST_TOKEN))
                .isInstanceOf(PermissionException.class)
                .hasMessage("No permission to perform operation");

        verify(userUtility).getUserByToken(anyString());
    }

    @Test
    void checkUpdateShouldThrowPermissionExceptionForSubscriber() {
        Comment comment = getComment();
        User user = getUserSubscriber();

        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);
        doReturn(Optional.of(comment))
                .when(commentRepository).findById(TEST_ID);

        assertThatThrownBy(() -> commentService.update(TEST_ID, comment, TEST_TOKEN))
                .isInstanceOf(PermissionException.class)
                .hasMessage("No permission to perform operation");
        verify(commentRepository).findById(anyLong());
        verify(userUtility).getUserByToken(anyString());
    }

    @Test
    void checkDeleteById() {
        User user = getUserAdmin();
        Comment comment = getComment();

        doReturn(Optional.of(comment))
                .when(commentRepository).findById(TEST_ID);
        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);
        doNothing()
                .when(commentRepository).deleteById(TEST_ID);

        commentService.deleteById(TEST_ID, TEST_TOKEN);

        verify(commentRepository).findById(anyLong());
        verify(userUtility).getUserByToken(anyString());
        verify(commentRepository).deleteById(anyLong());
    }

    @Test
    void checkDeleteShouldThrowResourceNotFoundException() {
        User user = getUserAdmin();
        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);
        doThrow(ResourceNotFoundException.class)
                .when(commentRepository).findById(anyLong());
        assertThatThrownBy(() -> commentService.deleteById(TEST_ID, TEST_TOKEN))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(commentRepository).findById(anyLong());
        verify(userUtility).getUserByToken(anyString());
    }

    @Test
    void checkDeleteShouldThrowPermissionExceptionForUserWithoutRole() {
        User user = getUserWithoutRole();

        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);

        assertThatThrownBy(() -> commentService.deleteById(TEST_ID, TEST_TOKEN))
                .isInstanceOf(PermissionException.class)
                .hasMessage("No permission to perform operation");
        verify(userUtility).getUserByToken(anyString());
    }

    @Test
    void checkDeleteShouldThrowPermissionExceptionForSubscriber() {
        Comment comment = getComment();
        User user = getUserSubscriber();

        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);
        doReturn(Optional.of(comment))
                .when(commentRepository).findById(TEST_ID);

        assertThatThrownBy(() -> commentService.deleteById(TEST_ID, TEST_TOKEN))
                .isInstanceOf(PermissionException.class)
                .hasMessage("No permission to perform operation");
        verify(commentRepository).findById(anyLong());
        verify(userUtility).getUserByToken(anyString());
    }

}