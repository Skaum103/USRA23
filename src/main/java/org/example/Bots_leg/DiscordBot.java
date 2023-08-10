package org.example.Bots_leg;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class DiscordBot {
    public static void startBot() throws InterruptedException {

        System.out.println("Starting Discord Test.");

        System.out.println("Initializing JDA bot.");
        JDA bot = JDABuilder.createDefault("MTEwODA2NDY2NTcxNzcxOTA0MA.GG4j3P.ERSmzM3fDonC_PM2KXHHRYIXxyoZriBbJu_psk")
                .setActivity(Activity.playing("Intellij IDEA"))
                .build();
        bot.awaitReady();
        System.out.println("Bot initialized and ready.");

        System.out.println("Sending message to channel.");
        TextChannel channel = bot.getTextChannelById("1108065519363440682");
        sendMessage(channel,"Hello");
        System.out.println("Done.");

    }


    static void sendMessage(TextChannel channel, String message) {
        channel.sendMessage(message).queue();
    }

    /*
    Src.ip, Dest.ip
    Src.port, Dest.port
    Protocol
    600ms
     */

}
