package org.avida.hime.hime.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.avida.hime.hime.BotCommand;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.*;

import java.sql.ResultSet;

import static org.avida.hime.hime.Main.readConfig;

public class QueryTestCommand implements BotCommand {
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
    static final String QUERY = "SELECT id, testString FROM testTable";
    String out = getOutput();

    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) throws SQLException {
        MessageChannel channel = event.getChannel();
        channel.sendMessage("Querying the test database: " + out).queue();

    }
    public String getOutput()
    {
        String output = "";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = stmt.executeQuery(QUERY)) {
            // Extract data from result set
            while (rs.next()) {
                // Retrieve by column name
                output += rs.getString("testString") + " ";
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }

    @Override
    public String getHelp() {
        return "Tests the Database.";
    }
}
