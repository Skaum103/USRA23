package org.example.Bots_dev;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.model.Message;
import org.example.Utils.Tshark;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.example.Utils.*;

import static java.util.Collections.emptyList;
import static org.example.Utils.util.readToken;
import static org.example.Utils.util.waitRemoteDisconnect;

public class SlackBot {

    static Optional<List<Message>> conversationHistory = Optional.empty();
    static String token = readToken();

    public static void main(String[] args) throws InterruptedException {
        Thread tsharkS = new Thread(new Tshark("slackPcapFixGapLab8_1111.pcapng"));
        waitRemoteDisconnect();
        tsharkS.start();
        startBot();
    }

    /** Start the bot */
    public static void startBot() throws InterruptedException {
        // Initialize the Slack Client
        MethodsClient client = initializeSlack();
        // Fetch the conversation history, simualte a user reading the history
        fetchHistory("C05C2M0P4DR",client);

        int runTime = 3600;
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime endTime = time.plusSeconds(runTime);
        System.out.println("Executing until " + endTime);

        // While the current time is before the end time
        // Execute the command every 2.5 seconds to Post a message and reply to it
        while (time.isBefore(endTime)) {
            exec("C05C2M0P4DR", client);
            time = LocalDateTime.now();
            TimeUnit.SECONDS.sleep((long) 2.5);
        }
    }


    /** Initialize the Slack Client */
    static MethodsClient initializeSlack() {
        Slack slack = Slack.getInstance();
        // Initialize an API Methods client with the given token2
        return slack.methods(token);
    }


    /**
     * Post a message to a channel
     * @param methods the Slack API Methods client
     * @param message the message to be posted
     * @return the timestamp of the message
     */
    static String sendThread(MethodsClient methods, String message) {
        // Build a request object
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel("C05C2M0P4DR") // Use a channel ID `C1234567` is preferable
                .text(message)
                .build();

        return handlePostResponse(methods, request);
    }


    /**
     * Reply to a message in a thread
     * @param methods the Slack API Methods client
     * @param message the message to be posted
     * @param ts the timestamp of the message to be replied
     * @return the timestamp of the message
     */
    static String replyThread(MethodsClient methods, String message, String ts) {
        // Build a request object
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel("C05C2M0P4DR") // Use a channel ID `C1234567` is preferable
                .text(message)
                .threadTs(ts)
                .build();

        return handlePostResponse(methods, request);
    }


    /**
     * Handle the response of the post request
     * @param methods the Slack API Methods client
     * @param request the request object
     * @return the timestamp of the message
     */
    @Nullable
    private static String handlePostResponse(MethodsClient methods, ChatPostMessageRequest request) {
        try {
            // Get a response as a Java object
            ChatPostMessageResponse response = methods.chatPostMessage(request);
            if (response.isOk()) {
                return response.getTs();
            }
            else {
                System.out.println(response.getError());
            }
        }
        catch (SlackApiException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Fetch conversation history using ID
     */
    static ArrayList<String> fetchHistory(String id, MethodsClient client) {
        ArrayList<String> tss = new ArrayList<>();

        // you can get this instance via ctx.client() in a Bolt app
        Logger logger = LoggerFactory.getLogger("slack-app-log");
        try {
            // Call the conversations.history method using the built-in WebClient
            ConversationsHistoryResponse result = client.conversationsHistory(r -> r
                    // The token you used to initialize your app
                    .token(token)
                    .channel(id)
            );
            conversationHistory = Optional.ofNullable(result.getMessages());
            // Print results
            logger.info("{} messages found in {}", conversationHistory.orElse(emptyList()).size(), id);

            for (Message message:result.getMessages()
            ) {
                tss.add(message.getTs());
            }
        } catch (IOException | SlackApiException e) {
            logger.error("error: {}", e.getMessage(), e);
        }

        return tss;
    }


    /**
     * Fetch conversation history using the ID and a TS from the last example
     */
    static String fetchMessage(String channelId, String ts, MethodsClient client) {
        // you can get this instance via ctx.client() in a Bolt app
        Logger logger = LoggerFactory.getLogger("slack-app-log");
        try {
            // Call the chat.postMessage method using the built-in WebClient
            ConversationsHistoryResponse result = client.conversationsHistory(r -> r
                    // The token you used to initialize your app
                    .token(token)
                    .channel(channelId)
                    // In a more realistic app, you may store ts data in a db
                    .latest(ts)
                    // Limit results
                    .inclusive(true)
                    .limit(1)
            );
            // There should only be one result (stored in the zeroth index)
            Message message = result.getMessages().get(0);
            // Print message text
            logger.info("result {}", message.getText());
            return message.getText();
        } catch (IOException | SlackApiException e) {
            logger.error("error: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * Execute the command to post a message and reply to it
     * @param channelID the ID of the channel
     * @param client the Slack API Methods client
     */
    public static void exec(String channelID, MethodsClient client){
        // Generate a random string and post it to the channel
        String threadPost = util.generateFakerString(50,100);
        String ts = sendThread(client,threadPost);
        fetchMessage(channelID,ts,client);

        // Generate a random string and reply to the posted message
        String threadReply = util.generateFakerString(50,100);
        ts = replyThread(client,threadReply,ts);
        fetchMessage(channelID,ts,client);
    }

}
