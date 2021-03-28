package com.bgsoftware.ssbdiscordstatistics.handlers;

import com.bgsoftware.ssbdiscordstatistics.SSBDiscordStatisticsPlugin;
import com.bgsoftware.ssbdiscordstatistics.commands.CmdShow;
import com.bgsoftware.ssbdiscordstatistics.commands.CmdTop;
import com.bgsoftware.ssbdiscordstatistics.commands.ICommand;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

public final class CommandsHandler {

    private final Map<String, ICommand> commands = new HashMap<>();
    private final SSBDiscordStatisticsPlugin plugin;

    public CommandsHandler(SSBDiscordStatisticsPlugin plugin){
        this.plugin = plugin;

        commands.put(plugin.getSettings().topCommandLabel, new CmdTop());
        commands.put(plugin.getSettings().showCommand, new CmdShow());
    }

    public void executeCommand(TextChannel textChannel, Member sender, String label, String[] args){
        label = label.toLowerCase();
        ICommand command = commands.get(label);

        if(command == null){
            textChannel.sendMessage("Unknown command. Type \"!help\" for help.").queue();
            return;
        }

        if(args.length < command.getMinArgs() || args.length > command.getMaxArgs()){
            textChannel.sendMessage("Usage: !" + label + " " + command.getUsage()).queue();
            return;
        }

        command.execute(plugin, textChannel, sender, args);
    }

}
