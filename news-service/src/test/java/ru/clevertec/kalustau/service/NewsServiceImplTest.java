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
import ru.clevertec.kalustau.model.News;
import ru.clevertec.kalustau.repository.NewsRepository;
import ru.clevertec.kalustau.service.impl.NewsServiceImpl;
import ru.clevertec.kalustau.exceptions.ResourceNotFoundException;
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
import static ru.clevertec.kalustau.util.TestData.getNews;
import static ru.clevertec.kalustau.util.TestData.getNewsList;
import static ru.clevertec.kalustau.util.TestData.getTestSpecification;
import static ru.clevertec.kalustau.util.TestData.getUserAdmin;
import static ru.clevertec.kalustau.util.TestData.getUserJournalist;
import static ru.clevertec.kalustau.util.TestData.getUserSubscriber;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    @InjectMocks
    private NewsServiceImpl newsService;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private UserUtility userUtility;

    @Captor
    ArgumentCaptor<News> newsCaptor;

    @Test
    void checkFindAll() {
        News news = getNewsList().get(0);
        Specification<News> specification = getTestSpecification(TEST_SEARCH);

        doReturn(new PageImpl<>(getNewsList()))
                .when(newsRepository).findAll(specification, PageRequest.of(TEST_PAGE_NO, TEST_PAGE_SIZE, Sort.by(TEST_SORT_BY)));

        List<News> newsList = newsService.findAll(TEST_SEARCH, TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY);

        verify(newsRepository).findAll(specification, PageRequest.of(TEST_PAGE_NO, TEST_PAGE_SIZE, Sort.by(TEST_SORT_BY)));

        assertThat(newsList.get(0)).isEqualTo(news);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void checkFindById(Long id) {
        News news = getNews();

        doReturn(Optional.of(news))
                .when(newsRepository).findById(id);

        News result = newsService.findById(id);

        verify(newsRepository).findById(anyLong());
        assertThat(result).isEqualTo(news);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void checkFindByIdShouldThrowResourceNotFoundException(Long id) {
        doThrow(ResourceNotFoundException.class)
                .when(newsRepository).findById(anyLong());
        assertThatThrownBy(() -> newsService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(newsRepository).findById(anyLong());
    }

    @Test
    void checkSave() {
        News news = getNews();
        User user = getUserAdmin();

        doReturn(news)
                .when(newsRepository).save(newsCaptor.capture());
        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);

        News result = newsService.save(news, TEST_TOKEN);
        verify(newsRepository).save(news);
        verify(userUtility).getUserByToken(anyString());
        assertThat(result.getTitle()).isEqualTo(news.getTitle());
        assertThat(result.getText()).isEqualTo(news.getText());
        assertThat(newsCaptor.getValue()).isEqualTo(news);
    }

    @Test
    void checkSaveShouldThrowPermissionExceptionForSubscriber() {
        News news = getNews();
        User user = getUserSubscriber();

        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);

        assertThatThrownBy(() -> newsService.save(news, TEST_TOKEN))
                .isInstanceOf(PermissionException.class)
                .hasMessage("No permission to perform operation");
        verify(userUtility).getUserByToken(anyString());
    }

    @Test
    void checkUpdate() {
        News news = getNews();
        User user = getUserAdmin();

        doReturn(Optional.of(news))
                .when(newsRepository).findById(TEST_ID);
        doReturn(news)
                .when(newsRepository).save(newsCaptor.capture());
        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);

        News result = newsService.update(TEST_ID, news, TEST_TOKEN);

        verify(newsRepository).findById(anyLong());
        verify(newsRepository).save(news);
        verify(userUtility).getUserByToken(anyString());
        assertThat(result.getTitle()).isEqualTo(news.getTitle());
        assertThat(result.getText()).isEqualTo(news.getText());
        assertThat(newsCaptor.getValue()).isEqualTo(news);
    }

    @Test
    void checkUpdateShouldThrowResourceNotFoundException() {
        User user = getUserAdmin();
        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);
        doThrow(ResourceNotFoundException.class)
                .when(newsRepository).findById(anyLong());
        assertThatThrownBy(() -> newsService.update(TEST_ID, getNews(), TEST_TOKEN))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(newsRepository).findById(anyLong());
        verify(userUtility).getUserByToken(anyString());
    }

    @Test
    void checkUpdateShouldThrowPermissionExceptionForSubscriber() {
        News news = getNews();
        User user = getUserSubscriber();

        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);

        assertThatThrownBy(() -> newsService.update(TEST_ID, news, TEST_TOKEN))
                .isInstanceOf(PermissionException.class)
                .hasMessage("No permission to perform operation");

        verify(userUtility).getUserByToken(anyString());
    }

    @Test
    void checkUpdateShouldThrowPermissionExceptionForJournalist() {
        News news = getNews();
        User user = getUserJournalist();

        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);
        doReturn(Optional.of(news))
                .when(newsRepository).findById(TEST_ID);

        assertThatThrownBy(() -> newsService.update(TEST_ID, news, TEST_TOKEN))
                .isInstanceOf(PermissionException.class)
                .hasMessage("No permission to perform operation");
        verify(newsRepository).findById(anyLong());
        verify(userUtility).getUserByToken(anyString());
    }

    @Test
    void checkDeleteById() {
        User user = getUserAdmin();
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

    @Test
    void checkDeleteShouldThrowResourceNotFoundException() {
        User user = getUserAdmin();
        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);
        doThrow(ResourceNotFoundException.class)
                .when(newsRepository).findById(anyLong());
        assertThatThrownBy(() -> newsService.deleteById(TEST_ID, TEST_TOKEN))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(newsRepository).findById(anyLong());
        verify(userUtility).getUserByToken(anyString());
    }

    @Test
    void checkDeleteShouldThrowPermissionExceptionForSubscriber() {
        User user = getUserSubscriber();

        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);

        assertThatThrownBy(() -> newsService.deleteById(TEST_ID, TEST_TOKEN))
                .isInstanceOf(PermissionException.class)
                .hasMessage("No permission to perform operation");
        verify(userUtility).getUserByToken(anyString());
    }

    @Test
    void checkDeleteShouldThrowPermissionExceptionForJournalist() {
        News news = getNews();
        User user = getUserJournalist();

        doReturn(user)
                .when(userUtility).getUserByToken(TEST_TOKEN);
        doReturn(Optional.of(news))
                .when(newsRepository).findById(TEST_ID);

        assertThatThrownBy(() -> newsService.deleteById(TEST_ID, TEST_TOKEN))
                .isInstanceOf(PermissionException.class)
                .hasMessage("No permission to perform operation");
        verify(newsRepository).findById(anyLong());
        verify(userUtility).getUserByToken(anyString());
    }

}