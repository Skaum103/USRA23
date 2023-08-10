package org.example.Utils;

import java.io.IOException;

import static org.example.Utils.util.cmdExec;

public class Tcpreplay implements Runnable {
    private String pcapPath;

    public Tcpreplay(String pcapPath) {
        this.pcapPath = pcapPath;
    }

    public static void startTcpreplay(String pcapPath) throws IOException, InterruptedException {
        util.replaceLine("tcpreplay.sh", "tshark -i en0 -a duration:3600 -w ~/" + pcapPath);
        String command = "./tcpreplay.sh";
        cmdExec(command);

    }

    public void run() {
        try {
            startTcpreplay(pcapPath);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}