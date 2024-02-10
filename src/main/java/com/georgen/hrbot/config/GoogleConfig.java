package com.georgen.hrbot.config;

import com.georgen.hrbot.model.constants.GoogleDefaults;
import com.georgen.hrbot.model.settings.GoogleSettings;
import com.georgen.hrbot.model.settings.SettingsGlobalContainer;
import com.georgen.hrbot.services.GoogleSheetsService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@DependsOn("initGlobalSettings")
@Configuration
public class GoogleConfig {
    @Value("${google.token-path}")
    private String tokenPath = GoogleDefaults.DEFAULT_TOKEN_PATH;
    @Value("${google.user-id}")
    private String userID;
    @Value("${google.app-name}")
    private String appName;
    @Value("${google.receiver-port}")
    private Integer receiverPort;

    @Bean
    public JsonFactory jsonFactory(){
        return GsonFactory.getDefaultInstance();
    }

    @Bean
    @Qualifier("scopes")
    public List<String> scopes(){
        return Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    }

    @Bean
    public NetHttpTransport httpTransport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    @Bean
    public Sheets sheets(NetHttpTransport httpTransport, JsonFactory jsonFactory, Credential credentials){
        return new Sheets
                .Builder(httpTransport, jsonFactory, credentials)
                .setApplicationName(appName)
                .build();
    }

    @Bean
    public Credential credentials(GoogleAuthorizationCodeFlow authCodeFlow, LocalServerReceiver receiver) throws IOException {
        return new AuthorizationCodeInstalledApp(authCodeFlow, receiver).authorize(userID);
    }

    @Bean
    public LocalServerReceiver receiver(){
        return new LocalServerReceiver.Builder().setPort(receiverPort).build();
    }

    @Bean
    public GoogleAuthorizationCodeFlow authCodeFlow(@Qualifier("scopes") List<String> scopes,
                                                    GoogleClientSecrets secret,
                                                    NetHttpTransport httpTransport,
                                                    JsonFactory jsonFactory) throws IOException {
        String tokenPath = getGoogleSettings().getTokenPath();

        return new GoogleAuthorizationCodeFlow
                .Builder(httpTransport, jsonFactory, secret, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(tokenPath)))
                .setAccessType("online")
                .build();
    }

    @Bean
    public GoogleClientSecrets secret(JsonFactory jsonFactory) throws IOException {
        String credentialsPath = getGoogleSettings().getCredentialsPath();

        InputStream inStream = GoogleSheetsService.class.getResourceAsStream(credentialsPath);
        if (inStream == null) throw new FileNotFoundException("Resource not found: " + credentialsPath);

        return GoogleClientSecrets.load(jsonFactory, new InputStreamReader(inStream));
    }

    private GoogleSettings getGoogleSettings(){
        return SettingsGlobalContainer.getInstance().getGoogleSettings();
    }
}
