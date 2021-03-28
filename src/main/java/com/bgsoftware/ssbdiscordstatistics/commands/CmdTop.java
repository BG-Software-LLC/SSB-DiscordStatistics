package com.bgsoftware.ssbdiscordstatistics.commands;

import com.bgsoftware.ssbdiscordstatistics.SSBDiscordStatisticsPlugin;
import com.bgsoftware.ssbdiscordstatistics.utils.EmbedBuilder;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;

import java.util.List;
import java.util.function.Function;

public final class CmdTop implements ICommand {

    @Override
    public String getUsage() {
        return "[page]";
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public int getMaxArgs() {
        return 1;
    }

    @Override
    public void execute(SSBDiscordStatisticsPlugin plugin, TextChannel textChannel, Member sender, String[] args) {
        int islandsPerPage = plugin.getSettings().topCommandIslandsPerPage;
        int page = 1;

        if(args.length == 1) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (Exception ex) {
                textChannel.sendMessage(EmbedBuilder.error()
                        .withField("Error", "Invalid page " + args[0], false)
                        .build()).queue();
                return;
            }
        }

        int islandsSize = SuperiorSkyblockAPI.getGrid().getIslands().size();

        if(islandsSize == 0){
            textChannel.sendMessage(EmbedBuilder.error()
                    .withField("Error", "The island top is empty.", false)
                    .build()).queue();
            return;
        }

        int maxPage = islandsSize % islandsPerPage == 0 ? islandsSize / islandsPerPage : (islandsSize / islandsPerPage) + 1;

        if(page < 1 || page > maxPage){
            textChannel.sendMessage(EmbedBuilder.error()
                    .withField("Error", "Invalid page " + page, false)
                    .build()).queue();
            return;
        }

        Function<Island, String> valueFunction = island -> {
            switch (plugin.getSettings().topSorting.getName()){
                case "LEVEL":
                    return island.getIslandLevel().toString();
                case "RATING":
                    return island.getTotalRating() + "";
                case "PLAYERS":
                    return island.getAllPlayersInside().size() + "";
                default:
                    return island.getWorth().toString();
            }
        };

        EmbedBuilder embedBuilder = EmbedBuilder.info().withTitle("Top Islands (Page #" + page + ")");

        List<Island> islandList = SuperiorSkyblockAPI.getGrid().getIslands(plugin.getSettings().topSorting)
                .subList((page - 1) * islandsPerPage, Math.min(islandsSize, page * islandsPerPage));

        for(int i = 0; i < islandList.size(); i++) {
            embedBuilder.withField("#" + (i + 1),
                    ":man_frowning: " + islandList.get(i).getOwner().getName() + "\n" +
                    ":dollar: $" + valueFunction.apply(islandList.get(i)),
                    false);
        }

        textChannel.sendMessage(embedBuilder.build()).queue();
    }

}
