package org.avida.hime.hime.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
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

public class EditReplayCommand extends SlashCommand implements BotCommand{
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

    public EditReplayCommand(){
        this.name = "editreplay";
        this.help = "Adds a DB replay of your choice to your profile.";
        this.options = Collections.singletonList(new OptionData(OptionType.STRING, "link", "The replay to add.")
                .setRequired(true));
    }

    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) throws SQLException {
        /**MessageChannel channel = event.getChannel();
        String food = event.getMessage().getContentDisplay().substring(event.getMessage().getContentDisplay().indexOf(" ") + 1);
        String output = addRow(food, event);
        channel.sendMessage(output).queue();**/
    }
    public String addRow(String replay, SlashCommandEvent event) throws SQLException {
        String out = "Something went wrong.";
        String replayCheck = "";
        try{
             replayCheck = replay.substring(0,34);
        }
        catch(Exception e){
            return "This isn't a DB replay link.";
        }
        if(!replayCheck.equals("https://www.duelingbook.com/replay")){
            return "This isn't a DB replay link.";
        }
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(QUERY)

        ) {
            try {
                while (rs.next()) {
                    if (rs.getString("Id").equals(event.getUser().getId())) {
                        rs.updateString("Replay", replay);
                        rs.updateRow();
                        return "Replay updated.";
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
        return "Add user's favorite replay.";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        OptionMapping option = event.getOption("link");
        try{
            event.reply(addRow(option.getAsString(),event)).queue();
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }
}
