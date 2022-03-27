package org.avida.hime.hime.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.mysql.cj.jdbc.result.ResultSetMetaData;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.avida.hime.hime.BotCommand;

import java.sql.*;

public class AddFavoriteFood extends SlashCommand implements BotCommand {
    static final String DB_URL = "jdbc:mysql://localhost/test";
    static final String USER = "himehost";
    static final String PASS = "mermaiddancer";
    static final String QUERY = "SELECT * FROM users";

    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) throws SQLException {
        MessageChannel channel = event.getChannel();
        String food = event.getMessage().getContentDisplay().substring(event.getMessage().getContentDisplay().indexOf(" ") + 1);
       String output = addRow(food, event);
        channel.sendMessage(output).queue();
    }
    public String addRow(String food, GuildMessageReceivedEvent event) throws SQLException {
       String out = "Something went wrong.";
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(QUERY);

        ) {
            try {
                while (rs.next()) {
                    if (rs.getString("Id").equals(event.getAuthor().getId())) {
                        rs.updateString("Favorite Food", food);
                        rs.updateRow();
                        return "Food preference updated.";
                        // Commit row
                    }
                }
            }
            catch(Exception e){
                out = "User is not registered";
            }

        }
        return out;
    }
    @Override
    public String getHelp() {
        return "Add user's favorite food. Syntax: !favoritefood (userinput)";
    }

    @Override
    protected void execute(SlashCommandEvent event) {

    }

}
