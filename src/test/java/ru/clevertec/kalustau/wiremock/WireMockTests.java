package ru.clevertec.kalustau.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class WireMockTests {

    @ClassRule
    public WireMockRule wm = new WireMockRule(wireMockConfig().port(8080));

    @Test
    public void assertWiremockSetup() throws IOException {
        // Arrange - setup wiremock stubs
        configureStubs();

        // execute request through the http client
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://localhost:8080/test/abc")
                .get()
                .build();

        // Act - call the endpoint
        Response response = client.newCall(request).execute();

        // Assert - verify the response
        assertEquals("Test success!", response.body());
        verify(exactly(1),getRequestedFor(urlEqualTo("/test/abc")));

    }

    // configure stubs for wiremock
    private void configureStubs() {
        configureFor("localhost", 8080);
        stubFor(get(urlEqualTo("/test/abc"))
                .willReturn(aResponse().withBody("Test success!")));
    }

}

