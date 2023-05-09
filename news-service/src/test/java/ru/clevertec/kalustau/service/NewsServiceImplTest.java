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
import ru.clevertec.kalustau.dto.NewsDtoRequest;
import ru.clevertec.kalustau.mapper.NewsMapper;
import ru.clevertec.kalustau.model.News;
import ru.clevertec.kalustau.repository.NewsRepository;
import ru.clevertec.kalustau.service.impl.NewsServiceImpl;
import ru.clevertec.kalustau.dto.Proto;
import ru.clevertec.kalustau.exceptions.ResourceNotFoundException;
import ru.clevertec.kalustau.service.impl.UserUtility;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.clevertec.kalustau.util.Constants.TEST_ID;
import static ru.clevertec.kalustau.util.Constants.TEST_PAGE_NO;
import static ru.clevertec.kalustau.util.Constants.TEST_PAGE_SIZE;
import static ru.clevertec.kalustau.util.Constants.TEST_SEARCH;
import static ru.clevertec.kalustau.util.Constants.TEST_SORT_BY;
import static ru.clevertec.kalustau.util.Constants.TEST_TOKEN;
import static ru.clevertec.kalustau.util.TestData.getNews;
import static ru.clevertec.kalustau.util.TestData.getNewsDtoRequest;
import static ru.clevertec.kalustau.util.TestData.getNewsDtoResponse;
import static ru.clevertec.kalustau.util.TestData.getNewsList;
import static ru.clevertec.kalustau.util.TestData.getTestSpecification;
import static ru.clevertec.kalustau.util.TestData.getUser;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    @InjectMocks
    private NewsServiceImpl newsService;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsMapper newsMapper;

    @Mock
    private UserUtility userUtility;

    @Captor
    ArgumentCaptor<News> newsCaptor;

    @Test
    void checkFindAll() {
        News news = getNewsList().get(0);
        Proto.NewsDtoResponse newsDto = getNewsDtoResponse();
        Specification<News> specification = getTestSpecification(TEST_SEARCH);

        doReturn(new PageImpl<>(getNewsList()))
                .when(newsRepository).findAll(specification, PageRequest.of(TEST_PAGE_NO, TEST_PAGE_SIZE, Sort.by(TEST_SORT_BY)));
        doReturn(newsDto)
                .when(newsMapper).newsToDto(news);

        List<Proto.NewsDtoResponse> newsDtoList = newsService.findAll(TEST_SEARCH, TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY);

        verify(newsRepository).findAll(specification, PageRequest.of(TEST_PAGE_NO, TEST_PAGE_SIZE, Sort.by(TEST_SORT_BY)));
        verify(newsMapper, times(3)).newsToDto(any());

        assertThat(newsDtoList.get(0)).isEqualTo(newsDto);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void checkFindById(Long id) {
        News news = getNews();
        Proto.NewsDtoResponse newsDto = getNewsDtoResponse();

        doReturn(Optional.of(news))
                .when(newsRepository).findById(id);
        doReturn(newsDto)
                .when(newsMapper).newsToDto(news);

        Proto.NewsDtoResponse result = newsService.findById(id);

        verify(newsRepository).findById(anyLong());
        verify(newsMapper).newsToDto(any());
        assertThat(result).isEqualTo(newsDto);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void checkFindByIdShouldThrowResourceNotFoundException(Long id) {
        doThrow(ResourceNotFoundException.class)
                .when(newsRepository).findById(anyLong());
        assertThrows(ResourceNotFoundException.class, () -> newsService.findById(id));
        verify(newsRepository).findById(anyLong());
    }

    @Test
    void checkSave() {
        News news = getNews();
        NewsDtoRequest newsDtoRequest = getNewsDtoRequest();
        Proto.NewsDtoResponse newsDtoResponse = getNewsDtoResponse();
        User user = getUser();

        doReturn(news)
                .when(newsRepository).save(newsCaptor.capture());
        doReturn(news)
                .when(newsMapper).dtoToNews(newsDtoRequest);
        doReturn(newsDtoResponse)
                .when(newsMapper).newsToDto(news);
        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);

        Proto.NewsDtoResponse result = newsService.save(newsDtoRequest, TEST_TOKEN);
        verify(newsRepository).save(news);
        verify(newsMapper).dtoToNews(newsDtoRequest);
        verify(newsMapper).newsToDto(news);
        verify(userUtility).getUserByToken(anyString());
        assertThat(result.getTitle()).isEqualTo(newsDtoRequest.getTitle());
        assertThat(result.getText()).isEqualTo(newsDtoRequest.getText());
        assertThat(newsCaptor.getValue()).isEqualTo(news);
    }

    @Test
    void checkUpdate() {
        News news = getNews();
        NewsDtoRequest newsDtoRequest = getNewsDtoRequest();
        Proto.NewsDtoResponse newsDtoResponse = getNewsDtoResponse();
        User user = getUser();

        doReturn(Optional.of(news))
                .when(newsRepository).findById(TEST_ID);
        doReturn(news)
                .when(newsRepository).save(newsCaptor.capture());
        doReturn(news)
                .when(newsMapper).dtoToNews(newsDtoRequest);
        doReturn(newsDtoResponse)
                .when(newsMapper).newsToDto(news);
        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);

        Proto.NewsDtoResponse result = newsService.update(TEST_ID, newsDtoRequest, TEST_TOKEN);

        verify(newsRepository).findById(anyLong());
        verify(newsRepository).save(news);
        verify(newsMapper).dtoToNews(newsDtoRequest);
        verify(newsMapper).newsToDto(news);
        verify(userUtility).getUserByToken(anyString());
        assertThat(result.getTitle()).isEqualTo(newsDtoRequest.getTitle());
        assertThat(result.getText()).isEqualTo(newsDtoRequest.getText());
        assertThat(newsCaptor.getValue()).isEqualTo(news);
    }

    @Test
    void checkUpdateShouldThrowResourceNotFoundException() {
        User user = getUser();
        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);
        doThrow(ResourceNotFoundException.class)
                .when(newsRepository).findById(anyLong());
        assertThrows(ResourceNotFoundException.class, () -> newsService.update(TEST_ID, getNewsDtoRequest(), TEST_TOKEN));
        verify(newsRepository).findById(anyLong());
        verify(userUtility).getUserByToken(anyString());
    }

    @Test
    void checkDeleteById() {
        User user = getUser();
        News news = getNews();

        doReturn(Optional.of(news))
                .when(newsRepository).findById(TEST_ID);
        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);
        doNothing()
                .when(newsRepository).deleteById(TEST_ID);

        newsService.deleteById(TEST_ID, TEST_TOKEN);

        verify(newsRepository).findById(anyLong());
        verify(userUtility).getUserByToken(anyString());
        verify(newsRepository).deleteById(anyLong());
    }

}