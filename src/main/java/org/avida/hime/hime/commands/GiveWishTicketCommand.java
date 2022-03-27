package org.avida.hime.hime.commands;
import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.avida.hime.hime.BotCommand;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GiveWishTicketCommand extends SlashCommand implements BotCommand{
    static final String DB_URL = "jdbc:mysql://localhost/test";
    static final String USER = "himehost";
    static final String PASS = "mermaiddancer";
    static final String QUERY = "SELECT * FROM users";

    public GiveWishTicketCommand(){
        this.name = "giveticket";
        this.help = "Gives ticket to specified user.";
        this.enabledRoles = new String[]{"923297029986320466"};
        this.defaultEnabled = false;

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "name", "The person the tickets will be given to.").setRequired(true));
        options.add(new OptionData(OptionType.INTEGER, "amount", "The amount of tickets to be given.").setRequired(true));
        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        OptionMapping nameOption = event.getOption("name");
        OptionMapping amtOption = event.getOption("amount");
        try {
            event.reply(addTicket(nameOption.getAsString(),Integer.parseInt(amtOption.getAsString()))).queue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void run(GuildMessageReceivedEvent event, String[] args) throws SQLException {
        MessageChannel channel = event.getChannel();
        System.out.println(event.getMessage().getContentDisplay());
        String name = event.getMessage().getContentDisplay().substring(event.getMessage().getContentDisplay().indexOf(" ") + 1, event.getMessage().getContentDisplay().indexOf("$") - 1);
        String amount = event.getMessage().getContentDisplay().substring(event.getMessage().getContentDisplay().indexOf("$") + 1);
        String output = "";
        int amt = Integer.parseInt(amount);
        if(event.getAuthor().getId().equals("406462375106183168")){
            output = addTicket(name,amt);
            channel.sendMessage(output).queue();
        }
        else{
            channel.sendMessage("Sorry, but you can't use this command.").queue();
        }
    }

    public String addTicket(String name, int num) throws SQLException {
        String out = "We couldn't find a registered user by that name.";
        if(num > 0) {
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 Statement stmt = conn.createStatement(
                         ResultSet.TYPE_SCROLL_INSENSITIVE,
                         ResultSet.CONCUR_UPDATABLE);
                 ResultSet rs = stmt.executeQuery("SELECT * FROM bank");
            ) {
                rs.next();
                if (rs.getInt("wishtickets") >= num) {
                    rs.updateInt("wishtickets", rs.getInt("wishtickets") - num);
                    rs.updateRow();
                } else
                    return "There aren't enough tickets in the bank.";
            }
        }
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(QUERY);

        ) {
            try {
                while (rs.next()) {
                    if (rs.getString("Name").equalsIgnoreCase(name)) {
                        rs.updateInt("WishTickets", rs.getInt("WishTickets") + num);
                        rs.updateRow();
                        return "Wish Tickets updated. " + rs.getString("Name") + " now has " + rs.getInt("WishTickets") + " Wish Tickets.";
                        // Commit row
                    }
                }
            }
            catch(Exception e){
                out = "Something went wrong.";
            }

        }
        return out;
    }

    @Override
    public String getHelp() {
        return "Gives Wish Ticket to selected person (Orphe only)";
    }
}
