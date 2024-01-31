package com.fatemorgan.hrbot.network;

import com.fatemorgan.hrbot.model.exceptions.NetworkException;
import org.apache.http.HttpHeaders;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class HttpConnector implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpConnector.class);
    private CloseableHttpClient client;
    private String url;

    public HttpConnector(String url){
        this.url = url;
        this.client = HttpClients.createDefault();
    }

    public String get(String endpoint) throws NetworkException {
        return get(endpoint, null);
    }

    public String get(String endpoint, String parameters) throws NetworkException {
        String requestString = buildRequestUrl(endpoint, parameters);

        HttpGet get = new HttpGet(requestString);

        return handleRequest(get, requestString);
    }

    public String post(String endpoint, String parameters, String json) throws NetworkException, UnsupportedEncodingException {
        if (json == null) throw new NetworkException("JSON is null");

        String requestString = buildRequestUrl(endpoint, parameters);

        HttpPost post = new HttpPost(requestString);
        post.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
        post.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return handleRequest(post, requestString);
    }

    private String handleRequest(HttpUriRequest request, String requestString){
        try (CloseableHttpResponse response = client.execute(request)){
            StatusLine statusLine = response.getStatusLine();
            if (statusLine != null) logErrorOrBypass(statusLine.getStatusCode(), requestString);
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    private String buildRequestUrl(String endpoint, String parameters) throws NetworkException {
        if (endpoint == null) throw new NetworkException("Request endpoint is null");
        String requestString = String.format("%s%s?%s", url, endpoint, parameters != null ? parameters : "");
        return requestString;
    }

    private void logErrorOrBypass(int httpStatus, String path){
        if (httpStatus == 200) return;

        LOGGER.error(
                "{} URL: {}",
                HttpStatus.valueOf(httpStatus),
                path != null ? path : ""
        );
    }

    @Override
    public void close() throws Exception {
        if (this.client != null) this.client.close();
    }
}
