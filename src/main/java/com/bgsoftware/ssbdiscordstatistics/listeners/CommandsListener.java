package com.bgsoftware.ssbdiscordstatistics.listeners;

import com.bgsoftware.ssbdiscordstatistics.SSBDiscordStatisticsPlugin;
import github.scarsz.discordsrv.dependencies.jda.api.events.message.guild.GuildMessageReceivedEvent;
import github.scarsz.discordsrv.dependencies.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public final class CommandsListener extends ListenerAdapter {

    private final SSBDiscordStatisticsPlugin plugin;

    public CommandsListener(SSBDiscordStatisticsPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e) {
        if(e.getAuthor().isBot() || !e.getMessage().getContentRaw().startsWith("!"))
            return;

        String commandRaw = e.getMessage().getContentRaw().substring(1);
        String[] argsRaw = commandRaw.split(" ");

        String label = argsRaw[0];
        String[] args = new String[argsRaw.length - 1];

        System.arraycopy(argsRaw, 1, args, 0, argsRaw.length - 1);

        plugin.getCommands().executeCommand(e.getChannel(), e.getMember(), label, args);
    }

}
