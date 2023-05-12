package ru.clevertec.kalustau.integration.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.experimental.UtilityClass;
import ru.clevertec.kalustau.client.dto.User;

import java.io.IOException;
import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

@UtilityClass
public class WireMockUtil {

    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 8081;

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

    public static String buildUserAdminResponse() throws IOException {
        return mapper.writeValueAsString(mapper.readValue(Paths.get("src/test/resources/json/userAdmin.json").toFile(),
                User.class));
    }

    public static String buildUserJournalistResponse() throws IOException {
        return mapper.writeValueAsString(mapper.readValue(Paths.get("src/test/resources/json/userJournalist.json").toFile(),
                User.class));
    }

    public static String buildUserSubscriberResponse() throws IOException {
        return mapper.writeValueAsString(mapper.readValue(Paths.get("src/test/resources/json/userSubscriber.json").toFile(),
                User.class));
    }

}
