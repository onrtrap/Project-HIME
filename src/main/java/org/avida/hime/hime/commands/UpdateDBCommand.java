package org.avida.hime.hime.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.avida.hime.hime.BotCommand;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.avida.hime.hime.Main.readConfig;

public class UpdateDBCommand implements BotCommand {

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
    static final String QUERY = "SELECT Name, Id FROM users";


    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) throws SQLException {
        MessageChannel channel = event.getChannel();
        String output;
        if(event.getAuthor().getId().equals("406462375106183168")){
            output = updateDatabase(event);
            channel.sendMessage(output).queue();
        }
        else{
            channel.sendMessage("Sorry, but you can't use this command.").queue();
        }
    }

    public String updateDatabase(GuildMessageReceivedEvent event) throws SQLException {
        String out = "Task not complete.";

        ArrayList<Member> memberList = new ArrayList<Member>((Collection<? extends Member>) event.getGuild().loadMembers());
        ArrayList<Member> existingMembers = new ArrayList<Member>();
        System.out.println("memberList starting size is " + memberList.size());
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = stmt.executeQuery(QUERY)
        ) {
            try {
                while (rs.next()) {
                    existingMembers.add(event.getGuild().getMemberById(rs.getString("Id")));
                }
                System.out.println("Task 1 Complete. existingMembers Size = " + existingMembers.size());
                System.out.println("memberList Size = " + memberList.size());

                for(int i = 0; i < existingMembers.size(); i++){
                    if(memberList.contains(existingMembers.get(i)))
                        memberList.remove(existingMembers.get(i));
                }
                System.out.println("Task 2 Complete. memberList Size = " + memberList.size());

                for(int k = 0; k < memberList.size(); k++){
                    System.out.println(memberList.get(k).getUser().getName());
                }
                for(int j = 0; j < memberList.size(); j++) {
                    rs.moveToInsertRow();
                    rs.updateString("Id", memberList.get(j).getId());
                    rs.updateString("Name", memberList.get(j).getUser().getName());
                    // Commit row
                    rs.insertRow();
                }
                out = "Task Complete.";
            }
            catch(Exception e){
                out = "Something went wrong with the Database.";
            }
        }

        /**try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")
        ) {
            try {
                for(int j = 0; j < memberList.size(); j++) {
                    rs.moveToInsertRow();
                    rs.updateString("Id", memberList.get(j).getId());
                    rs.updateString("Name", memberList.get(j).getUser().getName());
                    // Commit row
                    rs.insertRow();
                }
                out = "Task Complete.";
            }
            catch(Exception e){
                out = "Something went wrong with the Database.";
            }
        }**/
        return out;
    }

    @Override
    public String getHelp() {
        return "Updates the user Database. (Orphe Only)";
    }
}
