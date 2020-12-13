package com.bgsoftware.ssbdiscordstatistics.handlers;

import com.bgsoftware.ssbdiscordstatistics.SSBDiscordStatisticsPlugin;
import com.bgsoftware.ssbdiscordstatistics.config.CommentedConfiguration;
import com.bgsoftware.superiorskyblock.api.island.SortingType;

import java.io.File;

public final class SettingsHandler {

    public final String botToken;
    public final String serverName;
    public final String serverIcon;
    public final String topCommandLabel;
    public final int topCommandIslandsPerPage;
    public final SortingType topSorting;
    public final String showCommand;

    public SettingsHandler(SSBDiscordStatisticsPlugin plugin){
        File file = new File(plugin.getDataFolder(), "config.yml");

        if(!file.exists())
            plugin.saveResource("config.yml", false);

        CommentedConfiguration cfg = CommentedConfiguration.loadConfiguration(file);
        cfg.syncWithConfig(file, plugin.getResource("config.yml"));

        botToken = cfg.getString("bot-token");
        serverName = cfg.getString("server-name");
        serverIcon = cfg.getString("server-icon");
        topCommandLabel = cfg.getString("top-command.label");
        topCommandIslandsPerPage = cfg.getInt("top-command.islands-per-page");
        topSorting = SortingType.getByName(cfg.getString("top-command.top-sorting").toUpperCase());
        showCommand = cfg.getString("show-command");
    }

}
