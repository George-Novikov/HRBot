package com.fatemorgan.hrbot.network;

import com.fatemorgan.hrbot.config.TelegramConfig;
import com.fatemorgan.hrbot.model.exceptions.NetworkException;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class HttpConnector implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpConnector.class);
    private CloseableHttpClient client;
    private String host;
    private String token;

    public HttpConnector(TelegramConfig config){
        this.host = config.getUrl();
        this.token = config.getBotToken();
        this.client = HttpClients.createDefault();
    }

    public String get(String path, String parameters) throws UnsupportedEncodingException, NetworkException {
        if (path == null) throw new NetworkException("No path specified for GET method");
        if (parameters == null) parameters = "";

        String requestString = String.format("%s%s%s?%s", host, token, path, parameters, "UTF-8");
        HttpGet get = new HttpGet(requestString);

        try (CloseableHttpResponse response = client.execute(get)){
            StatusLine statusLine = response.getStatusLine();
            if (statusLine != null) logErrorOrBypass(statusLine.getStatusCode(), requestString);
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return null;
        }
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
