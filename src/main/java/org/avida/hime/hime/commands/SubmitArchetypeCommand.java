package org.avida.hime.hime.commands;

import com.jagrosh.jdautilities.command.SlashCommand;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import org.avida.hime.hime.sheets.SheetAPI;

import java.io.IOException;


public class SubmitArchetypeCommand extends SlashCommand{
    public SubmitArchetypeCommand() {
        this.name = "submitarchetype";
        this.help = "Submits an archetype to the Spreadsheet.";
    }
    @Override
    protected void execute(SlashCommandEvent event) {
        try {
            SheetAPI spreadsheet = new SheetAPI("1lXdYLq8bFNQ5Pqp1rXebVQNlwd2EAiSlFk7va4V6PIA");
            spreadsheet.createValues2Column("Test", "Test", "Deck Submissions!B4", "Deck Submissions!E4");
            //event.reply((Message) spreadsheet.appendTest().getValues().get(0).get(1)).queue();
            event.reply("Test complete.").queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
