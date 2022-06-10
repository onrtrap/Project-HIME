package org.avida.hime.hime.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Random;

public class CoinflipCommand extends SlashCommand {
    
    public CoinflipCommand(){
        this.name = "coinflip";
        this.help = "Flips a coin.";
    }
    protected void execute(SlashCommandEvent event) {
        Random randomNum = new Random();
        int result = randomNum.nextInt(2);
        System.out.println(result);
        if(result == 0)
            event.reply("The result is Heads.").queue();
        else
            event.reply("The result is Tails.").queue();
    }
}
