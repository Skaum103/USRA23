package org.example.Bots_leg;

import com.bhyoo.onedrive.client.Client;
import com.bhyoo.onedrive.container.items.FileItem;
import com.bhyoo.onedrive.container.items.FolderItem;
import com.bhyoo.onedrive.exceptions.ErrorResponseException;

import java.io.IOException;


public class OneDriveBot {

    // https://github.com/tawalaya/OneDriveJavaSDK#registration
    // or
    // https://github.com/isac322/OneDrive-SDK-java
    public static void startBot() {
        // make some files in the local folder of MS Onedrive

        // open the onedrive application
        /*
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("")
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

         */
    }

    public static void main(String[] args) throws ErrorResponseException, IOException {
        String clientId = "79e0485b-8a03-4b00-8b17-1d68c2b3205f";
        String[] scope = {"files.readwrite.all", "offline_access"};
        String redirectURL = "http://localhost:8080/";
        String clientSecret = "-WT8Q~W8JbFOuAg6GQEyzJtsRLx5CB1g1XFOfcfU";



        Client client = new Client(clientId, scope, redirectURL, clientSecret);


        // get root directory
        FolderItem folder = client.getRootDir();

        /*
        // Download a file
        FileItem file = client.getFile("XXXXXXXXXXXXXXXX!XXXX");
        String path = "/home/skaum/download";
        file.download(path);

         */

    }

}
