package com.fatemorgan.hrbot.handlers;

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
import com.google.api.services.sheets.v4.SheetsScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleSheets {
    private static final JsonFactory JSONFACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKEN_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_PATH = "/credentials.json";

    @Value("${google.user-id}")
    private String googleUserID;

    public void printSheets() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();


    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream inStream = GoogleSheets.class.getResourceAsStream(CREDENTIALS_PATH);
        if (inStream == null) throw new FileNotFoundException("Resource not found: " + CREDENTIALS_PATH);

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSONFACTORY, new InputStreamReader(inStream));

        GoogleAuthorizationCodeFlow authFlow = new GoogleAuthorizationCodeFlow
                .Builder(HTTP_TRANSPORT, JSONFACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKEN_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(authFlow, receiver).authorize(googleUserID);
    }
}
