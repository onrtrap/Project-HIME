package org.avida.hime.hime;

import com.jagrosh.jdautilities.command.*;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.avida.hime.hime.commands.*;
import javax.security.auth.login.LoginException;

public class SlashCommandListener extends ListenerAdapter {

    // Load the Slash commands
    public SlashCommandListener(String token){
        Activity activity = Activity.playing("!himehelp");
        CommandClientBuilder builder = new CommandClientBuilder();
        builder.addSlashCommand(new PingCommand());
        builder.addSlashCommand(new GiveUserInfoCommand());
        //builder.addSlashCommand(new GiveWishTicketCommand());
        //builder.addSlashCommand(new SubmitArchetypeCommand());
        builder.addSlashCommand(new CoinflipCommand());
        builder.setOwnerId("406462375106183168");
        builder.setActivity(activity);
        builder.forceGuildOnly("923294381279166554");
    // Build the CommandClient instance
    CommandClient commandClient = builder.build();

    // Add it as an event listener to JDA
        try {
            JDA jda = JDABuilder.createDefault(token)
                    .addEventListeners(
                            commandClient).build();
        } catch (LoginException e) {
            e.printStackTrace();
         }

    }



}
