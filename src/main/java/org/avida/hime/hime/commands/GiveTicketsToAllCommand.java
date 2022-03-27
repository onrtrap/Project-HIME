package org.avida.hime.hime.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.avida.hime.hime.BotCommand;


import java.sql.*;

public class GiveTicketsToAllCommand implements BotCommand {
    static final String DB_URL = "jdbc:mysql://localhost/test";
    static final String USER = "himehost";
    static final String PASS = "mermaiddancer";
    static final String QUERY = "SELECT * FROM users";
    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) throws SQLException {
        MessageChannel channel = event.getChannel();
        if(event.getAuthor().getId().equals("406462375106183168")){
            String output = wishStream(event);
            channel.sendMessage(output).queue();
        }
        else{
            channel.sendMessage("Sorry, but you can't use this command.").queue();
        }
    }

    private String wishStream(GuildMessageReceivedEvent event) throws SQLException {
        String out = "";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = stmt.executeQuery(QUERY);

        ) {
            try {
                while (rs.next()) {
                    rs.updateInt("WishTickets", rs.getInt("WishTickets") + 1);
                    rs.updateRow();
                    return "Everyone gets a Wish Ticket!";
                    // Commit row
                }
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
