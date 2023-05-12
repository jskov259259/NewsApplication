package ru.clevertec.kalustau.integration.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.kalustau.integration.BaseIntegrationTest;
import ru.clevertec.kalustau.exceptions.PermissionException;
import ru.clevertec.kalustau.exceptions.ResourceNotFoundException;
import ru.clevertec.kalustau.integration.util.WireMockExtension;
import ru.clevertec.kalustau.model.News;
import ru.clevertec.kalustau.service.impl.NewsServiceImpl;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.clevertec.kalustau.integration.util.WireMockUtil.buildUserAdminResponse;
import static ru.clevertec.kalustau.integration.util.WireMockUtil.buildUserJournalistResponse;
import static ru.clevertec.kalustau.integration.util.WireMockUtil.buildUserSubscriberResponse;
import static ru.clevertec.kalustau.util.Constants.TEST_ID;
import static ru.clevertec.kalustau.util.Constants.TEST_PAGE_NO;
import static ru.clevertec.kalustau.util.Constants.TEST_PAGE_SIZE;
import static ru.clevertec.kalustau.util.Constants.TEST_SEARCH;
import static ru.clevertec.kalustau.util.Constants.TEST_SORT_BY;
import static ru.clevertec.kalustau.util.Constants.TEST_TOKEN;
import static ru.clevertec.kalustau.util.TestData.getNews;

@ExtendWith(WireMockExtension.class)
class NewsServiceImplIT extends BaseIntegrationTest {

    @Autowired
    private NewsServiceImpl newsService;

    @Test
    void checkFindAll() {
        List<News> resultList = newsService.findAll(TEST_SEARCH, TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY);
        assertThat(resultList.size()).isEqualTo(10);
    }

    @Test
    void checkFindAllWithSpecifyingUserName() {
        String search = "userName:Ignavia";
        List<News> resultList = newsService.findAll(search, TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY);
        assertThat(resultList.size()).isEqualTo(2);
    }

    @Test
    void checkFindAllWithSpecifyingIdGreaterThan() {
        String search = "id>5";
        List<News> resultList = newsService.findAll(search, TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY);
        assertThat(resultList.size()).isEqualTo(5);
    }

    @Test
    void checkFindAllWithSpecifyingIdLessThan() {
        String search = "id<5";
        List<News> resultList = newsService.findAll(search, TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY);
        assertThat(resultList.size()).isEqualTo(4);
    }

    @Test
    void checkFindAllWithSpecifyingLogicalNo() {
        String search = "userName!Ignavia";
        List<News> resultList = newsService.findAll(search, TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY);
        assertThat(resultList.size()).isEqualTo(8);
    }

    @Test
    void checkFindById() {
        News news = newsService.findById(TEST_ID);
        assertThat(news.getId()).isEqualTo(TEST_ID);
        assertThat(news.getTitle()).isEqualTo("The secret of the presidential cue card");
    }

    @Test
    void checkFindByIdShouldThrowResourceNotFoundException() {
        assertThatThrownBy(() -> newsService.findById(50L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void checkSave() throws IOException {
        stubFor(WireMock.get(urlPathMatching("/api/auth/byToken/" + TEST_TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildUserAdminResponse())));

        News news = getNews();
        News createdNews = newsService.save(news, TEST_TOKEN);

        assertThat(createdNews.getTitle()).isEqualTo("Title2");
        assertThat(createdNews.getText()).isEqualTo("Text2");
    }

    @Test
    void checkSaveShouldThrowPermissionExceptionForSubscriber() throws IOException {
        stubFor(WireMock.get(urlPathMatching("/api/auth/byToken/" + TEST_TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildUserSubscriberResponse())));

        News news = getNews();

        assertThatThrownBy(() -> newsService.save(news, TEST_TOKEN))
                .isInstanceOf(PermissionException.class)
                .hasMessage("No permission to perform operation");
    }

    @Test
    void checkUpdate() throws IOException {
        stubFor(WireMock.get(urlPathMatching("/api/auth/byToken/" + TEST_TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildUserAdminResponse())));

        News news = getNews();

        News result = newsService.update(TEST_ID, news, TEST_TOKEN);

        assertThat(result.getTitle()).isEqualTo(news.getTitle());
        assertThat(result.getText()).isEqualTo(news.getText());
    }

    @Test
    void checkUpdateShouldThrowResourceNotFoundException() throws IOException {
        stubFor(WireMock.get(urlPathMatching("/api/auth/byToken/" + TEST_TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildUserAdminResponse())));

        assertThatThrownBy(() -> newsService.update(50L, getNews(), TEST_TOKEN))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void checkUpdateShouldThrowPermissionExceptionForSubscriber() throws IOException {
        stubFor(WireMock.get(urlPathMatching("/api/auth/byToken/" + TEST_TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildUserSubscriberResponse())));

        News news = getNews();

        assertThatThrownBy(() -> newsService.update(TEST_ID, news, TEST_TOKEN))
                .isInstanceOf(PermissionException.class)
                .hasMessage("No permission to perform operation");
    }

    @Test
    void checkUpdateShouldThrowPermissionExceptionForJournalist() throws IOException {
        stubFor(WireMock.get(urlPathMatching("/api/auth/byToken/" + TEST_TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildUserJournalistResponse())));

        News news = getNews();

        assertThatThrownBy(() -> newsService.update(TEST_ID, news, TEST_TOKEN))
                .isInstanceOf(PermissionException.class)
                .hasMessage("No permission to perform operation");
    }

    @Test
    void checkDeleteById() throws IOException {
        stubFor(WireMock.get(urlPathMatching("/api/auth/byToken/" + TEST_TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildUserAdminResponse())));

        Integer sizeBefore = newsService.findAll(TEST_SEARCH, TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY).size();

        newsService.deleteById(10L, TEST_TOKEN);

        Integer sizeAfter = newsService.findAll(TEST_SEARCH, TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY).size();
        assertThat(sizeAfter).isLessThan(sizeBefore);
    }

    @Test
    void checkDeleteShouldThrowResourceNotFoundException() throws IOException {
        stubFor(WireMock.get(urlPathMatching("/api/auth/byToken/" + TEST_TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildUserAdminResponse())));

        assertThatThrownBy(() -> newsService.deleteById(50L, TEST_TOKEN))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void checkDeleteShouldThrowPermissionExceptionForSubscriber() throws IOException {
        stubFor(WireMock.get(urlPathMatching("/api/auth/byToken/" + TEST_TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildUserSubscriberResponse())));

        assertThatThrownBy(() -> newsService.deleteById(TEST_ID, TEST_TOKEN))
                .isInstanceOf(PermissionException.class)
                .hasMessage("No permission to perform operation");
    }

    @Test
    void checkDeleteShouldThrowPermissionExceptionForJournalist() throws IOException {
        stubFor(WireMock.get(urlPathMatching("/api/auth/byToken/" + TEST_TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildUserJournalistResponse())));

        assertThatThrownBy(() -> newsService.deleteById(TEST_ID, TEST_TOKEN))
                .isInstanceOf(PermissionException.class)
                .hasMessage("No permission to perform operation");
    }

}