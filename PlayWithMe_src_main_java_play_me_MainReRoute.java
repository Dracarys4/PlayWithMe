package play.me;

//General Java Imports
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Arrays;

//Spreadsheet API Imports
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

//Exceptions
import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainReRoute {

    private static Sheets sheetsService;
    private static String SPREADSHEET_ID = "1opFXMU_RDuS4OujxnpROcOJ2MMMrMHxyuYv0CkUMGYw";
    private static final String APPLICATION_NAME = "Play With Me";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static Credential authorize() throws IOException, GeneralSecurityException{
        InputStream in = MainReRoute.class.getResourceAsStream("/credentials.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        Credential cred = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver())
                .authorize("PlayWithMe");
        return cred;
    }

    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        Credential cred = authorize();
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, cred)
                .setApplicationName(APPLICATION_NAME).build();
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException{
        sheetsService = getSheetsService();

        if(BotMain.m.returnFlag() == 1){
            ValueRange response = sheetsService.spreadsheets().values()
                    .get(SPREADSHEET_ID, args[1])
                    .execute();

            List<List<Object>> values = response.getValues();

            boolean is = values == null || values.isEmpty();

            if(is){
                BotMain.m.nothing();
            }
            else{
                String msg = "";
                for(int i = 0; i < values.size(); i++)
                {
                    msg += values.get(i);
                }
                BotMain.m.sendValues(msg);
            }
        }
        else {
            ValueRange upd = new ValueRange()
                    .setValues(Arrays.asList(Arrays.asList(args[2])));

            UpdateValuesResponse output = sheetsService.spreadsheets().values()
                    .update(SPREADSHEET_ID, args[1], upd)
                    .setValueInputOption("RAW")
                    .execute();
        }
    }
}
