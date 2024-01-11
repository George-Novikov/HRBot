package com.fatemorgan.hrbot.handlers;

import com.fatemorgan.hrbot.config.GoogleConfig;
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
import com.google.api.services.sheets.v4.model.ValueRange;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSheets.class);
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKEN_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_PATH = "/credentials.json";

    private GoogleConfig config;

    public GoogleSheets(GoogleConfig config) {
        this.config = config;
    }

    public void printSheets() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String range = "A1:G10";

        Sheets service = new Sheets
                .Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("Google Sheets API")
                .build();

        ValueRange valueRange = service
                .spreadsheets()
                .values()
                .get(config.getSpreadsheetID(), range)
                .execute();

        List<List<Object>> values = valueRange.getValues();

        if (values != null){
            for (List row : values){
                for (Object cell : row){
                    LOGGER.info("Cell: {}", cell);
                }
            }
        }
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream inStream = GoogleSheets.class.getResourceAsStream(CREDENTIALS_PATH);
        if (inStream == null) throw new FileNotFoundException("Resource not found: " + CREDENTIALS_PATH);

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inStream));

        GoogleAuthorizationCodeFlow authFlow = new GoogleAuthorizationCodeFlow
                .Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKEN_PATH)))
                .setAccessType("online")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(authFlow, receiver).authorize("defendandlove@gmail.com");
    }
}
