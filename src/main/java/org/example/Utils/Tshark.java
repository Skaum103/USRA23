package org.example.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.example.Utils.*;

import static org.example.Utils.util.cmdExec;

public class Tshark implements Runnable {
    private String pcapPath;

    public Tshark(String pcapPath) {
        this.pcapPath = pcapPath;
    }

    public static void startTshark(String pcapPath) throws IOException, InterruptedException {
        util.replaceLine("tshark.sh","tshark -i en0 -a duration:3600 -w ~/" + pcapPath);
        String command = "./tshark.sh";
        cmdExec(command);

    }

    public void run() {
        try {
            startTshark(pcapPath);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
