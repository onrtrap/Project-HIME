package org.avida.hime.hime.commands;
import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.avida.hime.hime.BotCommand;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.Integer.parseInt;

public class AddUserInfoCommand extends SlashCommand implements BotCommand{
    static final String DB_URL = "jdbc:mysql://localhost/test";
    static final String USER = "himehost";
    static final String PASS = "mermaiddancer";
    static final String QUERY = "SELECT Name, Id FROM users";

    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) throws SQLException {
        MessageChannel channel = event.getChannel();
        addRow(event.getAuthor().getName(), event.getAuthor().getId());
        channel.sendMessage("Signing " + event.getAuthor().getName() + " up!").queue();
    }
    public void addRow(String name, String id) throws SQLException {
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(QUERY);
        ) {
            rs.moveToInsertRow();
            rs.updateString("Id", id);
            rs.updateString("Name",name);
            // Commit row
            rs.insertRow();
        }
    }
    @Override
    public String getHelp() {
        return "Registers a new user.";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        MessageChannel channel = event.getChannel();
        try {
            addRow(event.getName(), event.getId());
        } catch (SQLException e) {
            channel.sendMessage("You already are in the database, or if you aren't, there was an error.").queue();
        }
        channel.sendMessage("Signing " + event.getName() + " up!").queue();
    }
}
