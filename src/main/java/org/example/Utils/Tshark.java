package org.example.Utils;

import java.io.IOException;

import static org.example.Utils.util.cmdExec;

public class Tshark implements Runnable {
    private final String pcapPath;

    public Tshark(String pcapPath) {
        this.pcapPath = pcapPath;
    }


    /**
     * Start tshark to capture the network traffic
     * @param pcapPath the path of the pcap file
     * @throws IOException if the file is not found
     * @throws InterruptedException if the process is interrupted
     */
    public static void startTshark(String pcapPath) throws IOException, InterruptedException {
        util.replaceLine("tshark.sh","tshark -i en0 -a duration:3600 -w ~/" + pcapPath);
        String command = "./tshark.sh";
        cmdExec(command);
    }

    @Override
    public void run() {
        try {
            startTshark(pcapPath);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
