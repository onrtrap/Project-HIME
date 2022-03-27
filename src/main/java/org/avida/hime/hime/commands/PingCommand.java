package org.avida.hime.hime.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.avida.hime.hime.BotCommand;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PingCommand extends SlashCommand implements BotCommand{

	public PingCommand(){
		this.name = "ping";
		this.help = "Displays the gateway ping";
	}
	@Override
	public void run(GuildMessageReceivedEvent event, String[] args) {
		event.getMessage().reply("My ping: " + event.getJDA().getGatewayPing()).queue();
	}

	@Override
	public String getHelp() {
		return "Displays the gateway ping";
	}

	@Override
	protected void execute(SlashCommandEvent slashCommandEvent) {
		slashCommandEvent.reply("My ping: "+ slashCommandEvent.getJDA().getGatewayPing()).queue();
	}
}
