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
import ru.clevertec.kalustau.dto.CommentDto;
import ru.clevertec.kalustau.mapper.CommentMapper;
import ru.clevertec.kalustau.model.Comment;
import ru.clevertec.kalustau.repository.CommentRepository;
import ru.clevertec.kalustau.repository.NewsRepository;
import ru.clevertec.kalustau.service.impl.CommentServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.clevertec.kalustau.util.Constants.TEST_ID;
import static ru.clevertec.kalustau.util.Constants.TEST_PAGE_NO;
import static ru.clevertec.kalustau.util.Constants.TEST_PAGE_SIZE;
import static ru.clevertec.kalustau.util.Constants.TEST_SORT_BY;
import static ru.clevertec.kalustau.util.TestData.getComment;
import static ru.clevertec.kalustau.util.TestData.getCommentDto;
import static ru.clevertec.kalustau.util.TestData.getCommentList;
import static ru.clevertec.kalustau.util.TestData.getNews;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private CommentMapper commentMapper;

    @Captor
    ArgumentCaptor<Comment> commentCaptor;

    @Test
    void checkFindAll() {
        Comment comment = getCommentList().get(0);
        CommentDto commentDto = getCommentDto();

        doReturn(new PageImpl<>(getCommentList()))
                .when(commentRepository).findAll(PageRequest.of(TEST_PAGE_NO, TEST_PAGE_SIZE, Sort.by(TEST_SORT_BY)));
        doReturn(commentDto)
                .when(commentMapper).commentToDto(comment);

        List<CommentDto> commentDtoList = commentService.findAll(TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY);

        verify(commentRepository).findAll(PageRequest.of(TEST_PAGE_NO, TEST_PAGE_SIZE, Sort.by(TEST_SORT_BY)));
        verify(commentMapper, times(3)).commentToDto(any());

        assertThat(commentDtoList.get(0)).isEqualTo(commentDto);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void checkFindById(Long id) {
        Comment comment = getComment();
        CommentDto commentDto = getCommentDto();

        doReturn(Optional.of(comment))
                .when(commentRepository).findById(id);
        doReturn(commentDto)
                .when(commentMapper).commentToDto(comment);

        CommentDto result = commentService.findById(id);

        verify(commentRepository).findById(anyLong());
        verify(commentMapper).commentToDto(any());
        assertThat(result).isEqualTo(commentDto);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void checkFindByIdShouldThrowRuntimeException(Long id) {
        doThrow(RuntimeException.class)
                .when(commentRepository).findById(anyLong());
        assertThrows(RuntimeException.class, () -> commentService.findById(id));
        verify(commentRepository).findById(anyLong());
    }

    @Test
    void checkFindAllByNewsId() {
        Comment comment = getCommentList().get(0);
        CommentDto commentDto = getCommentDto();

        doReturn(new PageImpl<>(getCommentList()))
                .when(commentRepository).findAllByNewsId(TEST_ID, PageRequest.of(TEST_PAGE_NO, TEST_PAGE_SIZE, Sort.by(TEST_SORT_BY)));
        doReturn(Boolean.TRUE)
                .when(newsRepository).existsById(TEST_ID);
        doReturn(commentDto)
                .when(commentMapper).commentToDto(comment);

        List<CommentDto> commentDtoList = commentService.findAllByNewsId(TEST_ID,
                TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY);

        verify(newsRepository).existsById(TEST_ID);
        verify(commentRepository).findAllByNewsId(TEST_ID, PageRequest.of(TEST_PAGE_NO,
                TEST_PAGE_SIZE, Sort.by(TEST_SORT_BY)));
        verify(commentMapper, times(3)).commentToDto(any());

        assertThat(commentDtoList.get(0)).isEqualTo(commentDto);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void checkFindAllByNewsIdShouldThrowRuntimeException() {
        doThrow(RuntimeException.class)
                .when(newsRepository).existsById(anyLong());
        assertThrows(RuntimeException.class, () ->
                commentService.findAllByNewsId(TEST_ID, TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY));
        verify(newsRepository).existsById(TEST_ID);
    }

    @Test
    void checkSave() {
        Comment comment = getComment();
        CommentDto commentDto = getCommentDto();

        doReturn(comment)
                .when(commentRepository).save(commentCaptor.capture());
        doReturn(Optional.of(getNews()))
                .when(newsRepository).findById(TEST_ID);
        doReturn(comment)
                .when(commentMapper).dtoToComment(commentDto);
        doReturn(commentDto)
                .when(commentMapper).commentToDto(comment);

        CommentDto result = commentService.save(TEST_ID, commentDto);
        verify(newsRepository).findById(TEST_ID);
        verify(commentRepository).save(comment);
        verify(commentMapper).dtoToComment(commentDto);
        verify(commentMapper).commentToDto(comment);
        assertThat(result).isEqualTo(commentDto);
        assertThat(commentCaptor.getValue()).isEqualTo(comment);
    }

    @Test
    void checkUpdate() {
        Comment comment = getComment();
        CommentDto commentDto = getCommentDto();

        doReturn(Optional.of(comment))
                .when(commentRepository).findById(TEST_ID);
        doReturn(comment)
                .when(commentRepository).save(commentCaptor.capture());
        doReturn(comment)
                .when(commentMapper).dtoToComment(commentDto);
        doReturn(commentDto)
                .when(commentMapper).commentToDto(comment);

        CommentDto result = commentService.update(commentDto);

        verify(commentRepository).findById(anyLong());
        verify(commentRepository).save(comment);
        verify(commentMapper).dtoToComment(commentDto);
        verify(commentMapper).commentToDto(comment);
        assertThat(result).isEqualTo(commentDto);
        assertThat(commentCaptor.getValue()).isEqualTo(comment);
    }

    @Test
    void checkUpdateShouldThrowRuntimeFoundException() {
        doThrow(RuntimeException.class)
                .when(commentRepository).findById(anyLong());
        assertThrows(RuntimeException.class, () -> commentService.update(getCommentDto()));
        verify(commentRepository).findById(anyLong());
    }

    @Test
    void checkDeleteById() {
        doNothing()
                .when(commentRepository).deleteById(TEST_ID);
        commentService.deleteById(TEST_ID);
        verify(commentRepository).deleteById(anyLong());
    }

}