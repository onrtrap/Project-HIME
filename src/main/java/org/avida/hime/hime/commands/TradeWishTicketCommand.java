package org.avida.hime.hime.commands;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.avida.hime.hime.BotCommand;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.*;

import static org.avida.hime.hime.Main.readConfig;

public class TradeWishTicketCommand implements BotCommand{
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
        System.out.println(event.getMessage().getContentDisplay());
        String name = event.getMessage().getContentDisplay().substring(event.getMessage().getContentDisplay().indexOf(" ") + 1, event.getMessage().getContentDisplay().indexOf("$") - 1);
        String userId = event.getAuthor().getId();
        String amount = event.getMessage().getContentDisplay().substring(event.getMessage().getContentDisplay().indexOf("$") + 1);
        String output;
        int amt = Integer.parseInt(amount);
        if(amt > 0) {
            output = addTicket(name,userId, amt, event);
            channel.sendMessage(output).queue();
        }
        else
            channel.sendMessage("Invalid Ticket amount").queue();
    }

    public String addTicket(String name, String userId, int num, GuildMessageReceivedEvent event) throws SQLException {
        String out = "We couldn't find a registered user by that name.";
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(QUERY)) {
            if(event.getAuthor().getName().equals(name))
                return "You can't send Wish Tickets to yourself!";
            try {
                while (rs.next()) {
                    if(rs.getString("Id").equals(userId)){
                        if((rs.getInt("WishTickets") - num) >= 0) {
                            int x = rs.getInt("WishTickets") - num;
                            rs.updateInt("WishTickets", x);
                            rs.updateRow();
                        }
                        else
                            return "Please enter a valid amount of Wish Tickets.";
                    }

                }
            }
            catch(Exception e){
                out = "Something went wrong.";
            }

        }
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery(QUERY)) {
            while (rs.next()) {
                if (rs.getString("Name").equalsIgnoreCase(name)) {
                    rs.updateInt("WishTickets", rs.getInt("WishTickets") + num);
                    rs.updateRow();
                    return "Wish Tickets updated. " + rs.getString("Name") + " now has " + rs.getInt("WishTickets") + " Wish Tickets.";
                    // Commit row
                }
            }
        }
        return out;
    }

    @Override
    public String getHelp() {
        return "Gives Wish Tickets to selected person using your own Wish Tickets (Syntax: !tradeticket [Name of recipient] $[Amount to give]";
    }
}
