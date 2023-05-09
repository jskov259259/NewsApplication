package ru.clevertec.kalustau.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.clevertec.kalustau.client.util.WireMockExtension;

import java.io.IOException;
import java.io.InputStream;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.kalustau.client.util.WireMockUtil.buildCommentDTOListResponse;
import static ru.clevertec.kalustau.client.util.WireMockUtil.buildCommentDTOResponse;
import static ru.clevertec.kalustau.controller.config.Constants.COMMENTS_URL;
import static ru.clevertec.kalustau.client.util.WireMockUtil.convertInputStreamToString;
import static ru.clevertec.kalustau.client.util.WireMockUtil.convertResponseToString;
import static ru.clevertec.kalustau.client.util.WireMockUtil.executeDeleteRequest;
import static ru.clevertec.kalustau.client.util.WireMockUtil.executeGetRequest;
import static ru.clevertec.kalustau.client.util.WireMockUtil.executePostRequest;
import static ru.clevertec.kalustau.client.util.WireMockUtil.executePutRequest;
import static ru.clevertec.kalustau.controller.config.Constants.NEWS_URL;

@ExtendWith(WireMockExtension.class)
class ControllerClientTest extends WireMockExtension {

    @Test
    void checkFindAll() throws IOException {
        stubFor(WireMock.get(urlPathMatching(COMMENTS_URL))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildCommentDTOListResponse())));

        HttpResponse httpResponse = executeGetRequest(COMMENTS_URL);
        String responseString = convertResponseToString(httpResponse);

        verify(getRequestedFor(urlEqualTo(COMMENTS_URL)));
        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(200);
        assertThat(httpResponse.getFirstHeader("Content-Type").getValue()).isEqualTo("application/json");
        assertThat(buildCommentDTOListResponse()).isEqualTo(responseString);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void checkFindById(long id) throws IOException {
        stubFor(WireMock.get(urlPathMatching(COMMENTS_URL + id))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildCommentDTOResponse())));

        HttpResponse httpResponse = executeGetRequest(COMMENTS_URL + id);
        String responseString = convertResponseToString(httpResponse);

        verify(getRequestedFor(urlEqualTo(COMMENTS_URL + id)));
        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(200);
        assertThat(httpResponse.getFirstHeader("Content-Type").getValue()).isEqualTo("application/json");
        assertThat(buildCommentDTOResponse()).isEqualTo(responseString);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void checkFindAllByNewsId(long id) throws IOException {
        stubFor(WireMock.get(urlPathMatching(NEWS_URL + "/" + id + "/comments"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildCommentDTOListResponse())));

        HttpResponse httpResponse = executeGetRequest(NEWS_URL + "/" + id + "/comments");
        String responseString = convertResponseToString(httpResponse);

        verify(getRequestedFor(urlEqualTo(NEWS_URL + "/" + id + "/comments")));
        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(200);
        assertThat(httpResponse.getFirstHeader("Content-Type").getValue()).isEqualTo("application/json");
        assertThat(buildCommentDTOListResponse()).isEqualTo(responseString);
    }

    @Test
    void checkSave() throws IOException {
        stubFor(WireMock.post(urlEqualTo(NEWS_URL + "/1/comments"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(containing("\"id\": \"1\""))
                .withRequestBody(containing("\"text\": \"Some text1\""))
                .withRequestBody(containing("\"userName\": \"User1\""))
                .willReturn(aResponse()
                        .withStatus(201)));

        InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream("json/comment.json");
        String jsonString = convertInputStreamToString(jsonInputStream);
        StringEntity entity = new StringEntity(jsonString);

        HttpResponse response = executePostRequest(NEWS_URL + "/1/comments", entity);

        verify(postRequestedFor(urlEqualTo(NEWS_URL + "/1/comments"))
                .withHeader("Content-Type", equalTo("application/json")));
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(201);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void checkUpdate(long id) throws IOException {
        stubFor(WireMock.put(urlEqualTo(COMMENTS_URL + id))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(containing("\"id\": \"1\""))
                .withRequestBody(containing("\"text\": \"Some text1\""))
                .withRequestBody(containing("\"userName\": \"User1\""))
                .willReturn(aResponse()
                        .withStatus(200)));

        InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream("json/comment.json");
        String jsonString = convertInputStreamToString(jsonInputStream);
        StringEntity entity = new StringEntity(jsonString);

        HttpResponse response = executePutRequest(COMMENTS_URL + id, entity);

        verify(putRequestedFor(urlEqualTo(COMMENTS_URL + id))
                .withHeader("Content-Type", equalTo("application/json")));
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void checkDelete(long id) throws IOException {
        stubFor(WireMock.delete(urlEqualTo(COMMENTS_URL + id))
                .willReturn(aResponse()
                        .withStatus(200)));

        HttpResponse response = executeDeleteRequest(COMMENTS_URL + id);

        verify(deleteRequestedFor(urlEqualTo(COMMENTS_URL + id)));
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
    }

}
