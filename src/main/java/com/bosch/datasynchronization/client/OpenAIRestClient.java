package com.bosch.datasynchronization.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class OpenAIRestClient {
    private static final String OPEN_AI_API = "https://api.openai.com/v1/chat/completions";

    @Autowired
    private HttpClient _httpClient;
    @Value("${openai.api.key}")
    private              String             API_KEY;
    public String sendPrompt(String body) {

        HttpRequest request = createRequest(body);

        HttpResponse<String> response;
        try {
            response = _httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 500) {
                throw new RuntimeException("The OpenAI api has some internal problems. Response data: " + response.body());
            }

            if (response.statusCode() >= 400) {
                throw new RuntimeException("The request was likely bad. Response data: " + response.body());
            }

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected HttpRequest createRequest(String body) {
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(OPEN_AI_API))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return request;
    }
}
