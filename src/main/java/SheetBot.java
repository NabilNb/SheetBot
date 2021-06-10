import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.*;
import org.javacord.api.entity.server.Server;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;

public class SheetBot {
    private static final String APPLICATION_NAME = "SheetBot";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";



    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = SheetBot.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException {

        String token = "ODUxNTUyOTkzNjg5NjAwMDMw.YL58hg.55H11_zdgkV96YZpjrYt8VWVm24";

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();


        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();


        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        final String spreadsheetId = "1u9Q9YlUkuPav-XG11wApLX5636b45yvca8QxsrfdwIo";

        // Now you must define the range in which you want your application to read the data
        final String range = "Full List!B2:G";
        ValueRange result = service.spreadsheets().values().get(spreadsheetId, range).execute();
        //System.out.println(result);

        List<List<Object>> values = result.getValues();

        Scanner scr=new Scanner(System.in);
        //System.out.println("Enter ID");
        //String ss=scr.next();

        //String ss="18201125";
        //System.out.println(values.get(1).get(1));
        /*
        for (int i = 0; i < values.size(); i++) {
            System.out.println(values.get(i) + " ");
        }


        for (int i = 0; i < values.size(); i++) {
            if(values.get(i).contains(ss)){
                System.out.println("Name: "+values.get(i).get(0));
                System.out.println("ID: "+values.get(i).get(1));
                System.out.println("Department:: "+values.get(i).get(2));
                System.out.println("Number: "+values.get(i).get(3));
                System.out.println("Mail: "+values.get(i).get(4));
                System.out.println("Intake: "+values.get(i).get(5));

            }


        }
        */

        api.addMessageCreateListener(event -> {
            if(hasInfo(values, event.getMessageContent())){
                List<Object> info=getInfo(values, event.getMessageContent());
                for (int i = 0; i < values.size(); i++) {
                    if (values.get(i).contains(event.getMessageContent())) {
                        info=values.get(i);

                        String st="Name:"+info.get(0)+"\n"+
                                "ID: "+info.get(1) +"\n"+
                                "Department: "+info.get(2) +"\n"+
                                "Number: "+info.get(3) +"\n"+
                                "Mail: "+info.get(4) +"\n"+
                                "Intake: "+info.get(5);
                        event.getChannel().sendMessage(st);
                    }
                }



            }

        });

    }

    public static boolean hasInfo(List<List<Object>> values, String ID){
        boolean flag=false;
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i).contains(ID)) {
                flag=true;
            }
        }


        return flag;
    }

    public static List<Object> getInfo(List<List<Object>> values, String ID){
        List<Object> info=null;
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i).contains(ID)) {
                info=values.get(i);
            }
        }
        return info;

    }



}


