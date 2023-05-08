package ru.clevertec.kalustau.client.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.experimental.UtilityClass;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import ru.clevertec.kalustau.model.Comment;
import ru.clevertec.kalustau.model.News;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

@UtilityClass
public class WireMockUtil {

    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 9000;

    private final ObjectMapper mapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    private WireMockServer wireMockServer;

    public void startServer() {
        if (wireMockServer == null) {
            wireMockServer = new WireMockServer(DEFAULT_PORT);
        }
        wireMockServer.start();
        configureFor(DEFAULT_HOST, DEFAULT_PORT);
    }

    public static void stopServer() {
        wireMockServer.stop();
    }

    public static void resetStubs() {
        wireMockServer.resetAll();
    }

    public static String buildNewsDTOResponse() throws IOException {
        return mapper.writeValueAsString(mapper.readValue(Paths.get("src/test/resources/json/news.json").toFile(),
                News.class));
    }

    public static String buildNewsDTOListResponse() throws IOException {
        return mapper.writeValueAsString(mapper.readValue(Paths.get("src/test/resources/json/newsList.json").toFile(),
                List.class));
    }

    public static String buildCommentDTOResponse() throws IOException {
        return mapper.writeValueAsString(mapper.readValue(Paths.get("src/test/resources/json/comment.json").toFile(),
                Comment.class));
    }

    public static String buildCommentDTOListResponse() throws IOException {
        return mapper.writeValueAsString(mapper.readValue(Paths.get("src/test/resources/json/commentList.json").toFile(),
                List.class));
    }

    public static HttpResponse executeGetRequest(String URL) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:9000/" + URL);
        return httpClient.execute(request);
    }

    public static HttpResponse executePostRequest(String URL, HttpEntity requestEntity) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost("http://localhost:9000/" + URL);
        request.addHeader("Content-Type", "application/json");
        request.setEntity(requestEntity);
        return httpClient.execute(request);
    }

    public static HttpResponse executePutRequest(String URL, HttpEntity requestEntity) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut request = new HttpPut("http://localhost:9000/" + URL);
        request.addHeader("Content-Type", "application/json");
        request.setEntity(requestEntity);
        return httpClient.execute(request);
    }

    public static HttpResponse executeDeleteRequest(String URL) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete request = new HttpDelete("http://localhost:9000/" + URL);
        return httpClient.execute(request);
    }

    public static String convertResponseToString(HttpResponse response) throws IOException {
        InputStream responseStream = response.getEntity().getContent();
        Scanner scanner = new Scanner(responseStream, "UTF-8");
        String responseString = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return responseString;
    }

    public static String convertInputStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8");
        String string = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return string;
    }
}
