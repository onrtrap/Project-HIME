package org.avida.hime.hime;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;

public interface BotCommand {
	void run(GuildMessageReceivedEvent event,String[] args) throws SQLException;
	String getHelp();
}
