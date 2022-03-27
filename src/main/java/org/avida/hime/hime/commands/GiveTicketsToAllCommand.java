package org.avida.hime.hime.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.avida.hime.hime.BotCommand;
import org.json.JSONObject;


import java.io.IOException;
import java.sql.*;

import static org.avida.hime.hime.Main.readConfig;

public class GiveTicketsToAllCommand implements BotCommand {
    static JSONObject json;

    static {
        try {
            json = readConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static final String DB_URL = json.getString("DB");
    static final String USER = json.getString("SQLUsername");
    static final String PASS = json.getString("SQLPassword");
    static final String QUERY = "SELECT * FROM users";
    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) throws SQLException {
        MessageChannel channel = event.getChannel();
        if(event.getAuthor().getId().equals("406462375106183168")){
            String output = wishStream();
            channel.sendMessage(output).queue();
        }
        else{
            channel.sendMessage("Sorry, but you can't use this command.").queue();
        }
    }

    private String wishStream() throws SQLException {
        String out;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = stmt.executeQuery(QUERY)

        ) {
            try {
                while (rs.next()) {
                    rs.updateInt("WishTickets", rs.getInt("WishTickets") + 1);
                    rs.updateRow();
                    // Commit row
                }
                return "Everyone gets a Wish Ticket!";
            } catch (Exception e) {
                out = "Something went wrong.";
            }
        }
        return out;
    }

    @Override
    public String getHelp() {
        return "Gives Wish Tickets to Everyone! (Orphe only)";
    }
}
