package ru.clevertec.kalustau.integration.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.kalustau.integration.BaseIntegrationTest;
import ru.clevertec.kalustau.exceptions.PermissionException;
import ru.clevertec.kalustau.exceptions.ResourceNotFoundException;
import ru.clevertec.kalustau.integration.util.WireMockExtension;
import ru.clevertec.kalustau.model.Comment;
import ru.clevertec.kalustau.service.impl.CommentServiceImpl;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.clevertec.kalustau.integration.util.WireMockUtil.buildUserAdminResponse;
import static ru.clevertec.kalustau.integration.util.WireMockUtil.buildUserSubscriberResponse;
import static ru.clevertec.kalustau.util.Constants.TEST_ID;
import static ru.clevertec.kalustau.util.Constants.TEST_PAGE_NO;
import static ru.clevertec.kalustau.util.Constants.TEST_PAGE_SIZE;
import static ru.clevertec.kalustau.util.Constants.TEST_SEARCH;
import static ru.clevertec.kalustau.util.Constants.TEST_SORT_BY;
import static ru.clevertec.kalustau.util.Constants.TEST_TOKEN;
import static ru.clevertec.kalustau.util.TestData.getComment;

@ExtendWith(WireMockExtension.class)
class CommentServiceImplIT extends BaseIntegrationTest {

    @Autowired
    private CommentServiceImpl commentService;

    @Test
    void checkFindAll() {
        List<Comment> resultList = commentService.findAll(TEST_SEARCH, TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY);
        assertThat(resultList.size()).isEqualTo(10);
    }

    @Test
    void checkFindById() {
        Comment comment = commentService.findById(TEST_ID);
        assertThat(comment.getId()).isEqualTo(TEST_ID);
        assertThat(comment.getText()).isEqualTo("Nice news");
        assertThat(comment.getUserName()).isEqualTo("Cellular");
    }

    @Test
    void checkFindByIdShouldThrowResourceNotFoundException() {
        assertThatThrownBy(() -> commentService.findById(50L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void checkSave() throws IOException {
        stubFor(WireMock.get(urlPathMatching("/api/auth/byToken/" + TEST_TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildUserAdminResponse())));

        Comment comment = getComment();
        Comment createdComment = commentService.save(TEST_ID, comment, TEST_TOKEN);

        assertThat(createdComment.getText()).isEqualTo("Text2");
        assertThat(createdComment.getUserName()).isEqualTo("Admin");
    }

    @Test
    void checkUpdate() throws IOException {
        stubFor(WireMock.get(urlPathMatching("/api/auth/byToken/" + TEST_TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildUserAdminResponse())));

        Comment comment = getComment();
        Comment result = commentService.update(TEST_ID, comment, TEST_TOKEN);

        assertThat(result.getText()).isEqualTo("Text2");
        assertThat(result.getUserName()).isEqualTo("Cellular");
    }

    @Test
    void checkUpdateShouldThrowResourceNotFoundException() throws IOException {
        stubFor(WireMock.get(urlPathMatching("/api/auth/byToken/" + TEST_TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildUserAdminResponse())));

        assertThatThrownBy(() -> commentService.update(50L, getComment(), TEST_TOKEN))
                .isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    void checkUpdateShouldThrowPermissionExceptionForSubscriber() throws IOException {
        stubFor(WireMock.get(urlPathMatching("/api/auth/byToken/" + TEST_TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildUserSubscriberResponse())));

        Comment comment = getComment();

        assertThatThrownBy(() -> commentService.update(TEST_ID, comment, TEST_TOKEN))
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

        Integer sizeBefore = commentService.findAll(TEST_SEARCH, 0, 20, TEST_SORT_BY).size();

        commentService.deleteById(20L, TEST_TOKEN);

        Integer sizeAfter = commentService.findAll(TEST_SEARCH, 0, 20, TEST_SORT_BY).size();
        assertThat(sizeAfter).isLessThan(sizeBefore);
    }

    @Test
    void checkDeleteShouldThrowResourceNotFoundException() throws IOException {
        stubFor(WireMock.get(urlPathMatching("/api/auth/byToken/" + TEST_TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildUserAdminResponse())));

        assertThatThrownBy(() -> commentService.deleteById(50L, TEST_TOKEN))
                .isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void checkDeleteShouldThrowPermissionExceptionForSubscriber() throws IOException {
        stubFor(WireMock.get(urlPathMatching("/api/auth/byToken/" + TEST_TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildUserSubscriberResponse())));

        assertThatThrownBy(() -> commentService.deleteById(TEST_ID, TEST_TOKEN))
                .isInstanceOf(PermissionException.class)
                .hasMessage("No permission to perform operation");

    }

}