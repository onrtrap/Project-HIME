//const fs = require('fs'); 

const Discord = require('discord.js');

const client = new Discord.Client();

const {prefix, token} = require('./auth.json');

client.once('ready', () => {
	console.log('Ready desu!');
});

client.on('message', message => {
	
	if (!message.content.startsWith(prefix) || message.author.bot) return;

	const args = message.content.slice(prefix.length).trim().split(/ +/);
	const command = args.shift().toLowerCase();

switch(command)
{
	
}
	
	});
	
	client.login(token);