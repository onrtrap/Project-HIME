package org.avida.hime.hime.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.avida.hime.hime.sheets.SheetsQuickstart;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SubmitArchetypeCommand extends SlashCommand{
    public SubmitArchetypeCommand() {
        this.name = "submitarchetype";
        this.help = "Submits an archetype to the Spreadsheet.";
    }
    @Override
    protected void execute(SlashCommandEvent event) {
        try {
            SheetsQuickstart spreadsheet = new SheetsQuickstart("1lXdYLq8bFNQ5Pqp1rXebVQNlwd2EAiSlFk7va4V6PIA");
            spreadsheet.createValues2Column("Test", "Test", "Deck Submissions!B4", "Deck Submissions!E4");
            //event.reply((Message) spreadsheet.appendTest().getValues().get(0).get(1)).queue();
            event.reply("Test complete.").queue();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
