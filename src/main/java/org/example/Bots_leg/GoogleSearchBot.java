package org.example.Bots_leg;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.CustomsearchRequestInitializer;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleSearchBot {
    public static void startBot() throws GeneralSecurityException, IOException {

        // Implement using google custom search engine
        // https://github.com/googleapis/google-api-java-client-services/tree/main/clients/google-api-services-customsearch/v1
        // https://stackoverflow.com/questions/3727662/how-can-you-search-google-programmatically-java-api

        String searchQuery = "test"; //The query to search
        String cx = "4389b7d31aa0d4525"; //Your search engine

        //Instance Custom search
        Customsearch cs = new Customsearch.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), null)
                .setApplicationName("MyApplication")
                .setGoogleClientRequestInitializer(new CustomsearchRequestInitializer("AIzaSyDASiklDchWABI4ep1vjP2X-RZrGIw2avQ"))
                .build();

        //Set search parameter
        Customsearch.Cse.List list = cs.cse().list(searchQuery).setCx(cx);

        //Execute search
        Search result = list.execute();
        if (result.getItems()!=null){
            for (Result ri : result.getItems()) {
                //Get title, link, body etc. from search
                System.out.println(ri.getTitle() + ", " + ri.getLink());
            }
        }

    }
}
