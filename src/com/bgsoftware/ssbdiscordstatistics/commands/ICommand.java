package com.bgsoftware.ssbdiscordstatistics.commands;

import com.bgsoftware.ssbdiscordstatistics.SSBDiscordStatisticsPlugin;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;

public interface ICommand {

    String getUsage();

    int getMinArgs();

    int getMaxArgs();

    void execute(SSBDiscordStatisticsPlugin plugin, TextChannel textChannel, Member sender, String[] args);

}
