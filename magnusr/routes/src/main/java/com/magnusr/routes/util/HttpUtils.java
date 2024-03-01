package com.magnusr.routes.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpUtils
 *
 * A convenient class for making API calls.
 */
public class HttpUtils {

    HttpClient client;
    HttpRequest request;
    ObjectMapper mapper;

    public static HttpUtils createHttpClient() {
        HttpUtils httpClientWrapper = new HttpUtils();
        httpClientWrapper.client = HttpClient.newHttpClient();
        httpClientWrapper.mapper = new ObjectMapper();

        return httpClientWrapper;
    }

    public HttpUtils() {

    }

    public HttpUtils withGetRequest(String uri, HashMap<String, String> headers) {
        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(uri));
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                requestBuilder
                        .header(header.getKey(), header.getValue());
            }
        }

        requestBuilder.GET();

        request = requestBuilder.build();

        return this;
    }

    public <T> T execute(Class<T> clazz) throws InterruptedException, IOException {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        try {
            T result = mapper.readValue(response.body(), clazz);

            return result;
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            return null;
        }
    }
}
