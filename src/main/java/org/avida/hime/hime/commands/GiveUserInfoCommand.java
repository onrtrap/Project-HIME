package org.avida.hime.hime.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.avida.hime.hime.BotCommand;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.util.Collections;

import static org.avida.hime.hime.Main.readConfig;

public class GiveUserInfoCommand extends SlashCommand implements BotCommand {
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

    public GiveUserInfoCommand(){
        this.name = "userinfo";
        this.help = "Gives the info of a user in the Database.";
        this.options = Collections.singletonList(new OptionData(OptionType.STRING, "name", "The user to look up.").setRequired(false));
    }
    public void run(GuildMessageReceivedEvent event, String[] args) throws SQLException {
        MessageChannel channel = event.getChannel();
        String name = event.getMessage().getAuthor().getId();
        String output = getInfo(name);
        channel.sendMessage(output).queue();
    }
    public String getInfo(String id) throws SQLException {
        String out = "Something went wrong.";
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(QUERY)

        ) {
            try {
                while (rs.next()) {
                    if (rs.getString("Id").equals(id)) {
                        out = "Name: " + rs.getString("Name") + "\nWish Tickets: " + rs.getInt("WishTickets");
                        if(rs.getString("Favorite Food") != null)
                            out += "\nFavorite Food: " + rs.getString("Favorite Food");
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

    public String getSlashInfo(String name) throws SQLException {
        String out = "Something went wrong.";
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(QUERY)

        ) {
            try {
                while (rs.next()) {
                    if (rs.getString("Name").equalsIgnoreCase(name)) {
                        out = "Name: " + rs.getString("Name") + "\nWish Tickets: " + rs.getInt("WishTickets");
                        if(rs.getString("Favorite Food") != null)
                            out += "\nFavorite Food: " + rs.getString("Favorite Food");
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
        return "Displays user's database information.";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        OptionMapping option = event.getOption("name");
        if (option == null) {
            String id = event.getUser().getId();
            try {
                event.reply(getInfo(id)).queue();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                System.out.println(option.getAsString());
                event.reply(getSlashInfo(option.getAsString())).queue();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}