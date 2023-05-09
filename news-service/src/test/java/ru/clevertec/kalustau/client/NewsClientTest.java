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
import static ru.clevertec.kalustau.controller.config.Constants.NEWS_URL;
import static ru.clevertec.kalustau.client.util.WireMockUtil.buildNewsDTOListResponse;
import static ru.clevertec.kalustau.client.util.WireMockUtil.buildNewsDTOResponse;
import static ru.clevertec.kalustau.client.util.WireMockUtil.convertInputStreamToString;
import static ru.clevertec.kalustau.client.util.WireMockUtil.convertResponseToString;
import static ru.clevertec.kalustau.client.util.WireMockUtil.executeDeleteRequest;
import static ru.clevertec.kalustau.client.util.WireMockUtil.executeGetRequest;
import static ru.clevertec.kalustau.client.util.WireMockUtil.executePostRequest;
import static ru.clevertec.kalustau.client.util.WireMockUtil.executePutRequest;

@ExtendWith(WireMockExtension.class)
class NewsClientTest extends WireMockExtension {

    @Test
    void checkFindAll() throws IOException {
        stubFor(WireMock.get(urlPathMatching(NEWS_URL))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildNewsDTOListResponse())));

        HttpResponse httpResponse = executeGetRequest(NEWS_URL);
        String responseString = convertResponseToString(httpResponse);

        verify(getRequestedFor(urlEqualTo(NEWS_URL)));
        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(200);
        assertThat(httpResponse.getFirstHeader("Content-Type").getValue()).isEqualTo("application/json");
        assertThat(buildNewsDTOListResponse()).isEqualTo(responseString);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void checkFindById(long id) throws IOException {
        stubFor(WireMock.get(urlPathMatching(NEWS_URL + id))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(buildNewsDTOResponse())));

        HttpResponse httpResponse = executeGetRequest(NEWS_URL + id);
        String responseString = convertResponseToString(httpResponse);

        verify(getRequestedFor(urlEqualTo(NEWS_URL + id)));
        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(200);
        assertThat(httpResponse.getFirstHeader("Content-Type").getValue()).isEqualTo("application/json");
        assertThat(buildNewsDTOResponse()).isEqualTo(responseString);
    }

    @Test
    void checkSave() throws IOException {
        stubFor(WireMock.post(urlEqualTo(NEWS_URL))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(containing("\"id\": \"1\""))
                .withRequestBody(containing("\"title\": \"Some title1\""))
                .withRequestBody(containing("\"text\": \"Some text1\""))
                .willReturn(aResponse()
                        .withStatus(201)));

        InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream("json/news.json");
        String jsonString = convertInputStreamToString(jsonInputStream);
        StringEntity entity = new StringEntity(jsonString);

        HttpResponse response = executePostRequest(NEWS_URL, entity);

        verify(postRequestedFor(urlEqualTo(NEWS_URL))
                .withHeader("Content-Type", equalTo("application/json")));
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(201);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void checkUpdate(long id) throws IOException {
        stubFor(WireMock.put(urlEqualTo(NEWS_URL + id))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(containing("\"id\": \"1\""))
                .withRequestBody(containing("\"title\": \"Some title1\""))
                .withRequestBody(containing("\"text\": \"Some text1\""))
                .willReturn(aResponse()
                        .withStatus(200)));

        InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream("json/news.json");
        String jsonString = convertInputStreamToString(jsonInputStream);
        StringEntity entity = new StringEntity(jsonString);

        HttpResponse response = executePutRequest(NEWS_URL + id, entity);

        verify(putRequestedFor(urlEqualTo(NEWS_URL + id))
                .withHeader("Content-Type", equalTo("application/json")));
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void checkDelete(long id) throws IOException {
        stubFor(WireMock.delete(urlEqualTo(NEWS_URL + id))
                .willReturn(aResponse()
                                .withStatus(200)));

        HttpResponse response = executeDeleteRequest(NEWS_URL + id);

        verify(deleteRequestedFor(urlEqualTo(NEWS_URL + id)));
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
    }

}
