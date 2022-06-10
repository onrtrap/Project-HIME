package org.avida.hime.hime.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.avida.hime.hime.BotCommand;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.avida.hime.hime.Main.readConfig;

import static org.avida.hime.hime.Main.readConfig;

public class RankUpCommand extends SlashCommand {


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
        static final String QUERY = "SELECT Name, Id, WishTickets FROM users";

        public RankUpCommand(){
            this.name = "rankup";
            this.help = "Ranks user up if they have enough wish tickets.";
        }


        public String changeRank(SlashCommandEvent event) throws SQLException {
            String out = "Something went wrong.";
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 Statement stmt = conn.createStatement(
                         ResultSet.TYPE_SCROLL_INSENSITIVE,
                         ResultSet.CONCUR_UPDATABLE);
                 ResultSet rs = stmt.executeQuery(QUERY)

            ) {
                try {
                    while (rs.next()) {
                        if (rs.getString("Id").equals(event.getUser().getId())) {
                            List <Role> roles = event.getGuild().getMemberById(event.getUser().getId()).getRoles();
                            System.out.println(event.getUser().getId());
                            System.out.println(rs.getString("Id"));
                            ArrayList<String> aRoles = new ArrayList<String>();
                                for(int i = 0; i < roles.size(); i++) {
                                    aRoles.add(roles.get(i).getName());
                                }
                                    if(aRoles.contains("Blue Star")){
                                        out = "Please wait for a server team member to reach you.";
                                        break;
                                    }
                                    else if(aRoles.contains("Red Star")){
                                        if(rs.getInt("WishTickets") >= 10) {
                                            rs.updateInt("WishTickets", rs.getInt("WishTickets") - 10);
                                            rs.updateRow();
                                            Role blueRole = event.getGuild().getRoleById("978624908177313802");
                                            event.getGuild().addRoleToMember(rs.getString("Id"), blueRole).queue();
                                            out = "Congratulations on ranking up to Blue Star!";
                                        }
                                        else
                                            out = "You don't have enough wish tickets to rank up.";
                                        break;
                                    }
                                    else if(aRoles.contains("Rising Star")){
                                        if(rs.getInt("WishTickets") >= 5) {
                                            rs.updateInt("WishTickets", rs.getInt("WishTickets") - 5);
                                            rs.updateRow();
                                            Role redRole = event.getGuild().getRoleById("978624747850039306");
                                            System.out.println(redRole.getName());
                                            event.getGuild().addRoleToMember(rs.getString("Id"), redRole).queue();
                                            out = "Congratulations on ranking up to Red Star!";
                                        }
                                        else
                                            out = "You don't have enough wish tickets to rank up.";
                                        break;
                                    }
                                }

                            // Commit row
                        }
                    } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                    out = "There was an issue. You might not be registered in the Database, or Orphe could have just goofed.";
                }

            return out;
        }

    protected void execute(SlashCommandEvent event) {
        try {
            event.reply(changeRank(event)).queue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
