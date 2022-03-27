package org.avida.hime.hime.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.avida.hime.hime.BotCommand;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.avida.hime.hime.Main.readConfig;

public class AddInfoTestCommand implements BotCommand {
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
    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) throws SQLException {
        MessageChannel channel = event.getChannel();
        addRow();
        channel.sendMessage("Adding a row.").queue();
    }
    public void addRow() throws SQLException {
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(QUERY)
        ) {
            rs.moveToInsertRow();
            rs.updateInt("id", 3);
            rs.updateString("testString","test");
            // Commit row
            //rs.insertRow();
        }
    }
    @Override
    public String getHelp() {
        return "Tests adding values to the database.";
    }
}
