package com.bgsoftware.ssbdiscordstatistics.commands;

import com.bgsoftware.ssbdiscordstatistics.SSBDiscordStatisticsPlugin;
import com.bgsoftware.ssbdiscordstatistics.utils.EmbedBuilder;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.PlayerRole;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;

import javax.annotation.Nullable;

public final class CmdShow implements ICommand {

    @Nullable
    private static final PlayerRole ADMIN_ROLE = SuperiorSkyblockAPI.getSuperiorSkyblock().getRoles().getLastRole().getPreviousRole();
    @Nullable
    private static final PlayerRole MOD_ROLE = ADMIN_ROLE == null ? null : ADMIN_ROLE.getPreviousRole();
    @Nullable
    private static final PlayerRole MEMBER_ROLE = SuperiorSkyblockAPI.getSuperiorSkyblock().getRoles().getDefaultRole();

    @Override
    public String getUsage() {
        return "<player-name/island-name>";
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public int getMaxArgs() {
        return 1;
    }

    @Override
    public void execute(SSBDiscordStatisticsPlugin plugin, TextChannel textChannel, Member sender, String[] args) {
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(args[0]);
        Island island = superiorPlayer == null ? SuperiorSkyblockAPI.getGrid().getIsland(args[0]) : superiorPlayer.getIsland();

        if (island == null) {
            textChannel.sendMessage(EmbedBuilder.error()
                    .withField("Error", "Invalid island " + args[0], false)
                    .build()).queue();
            return;
        }

        EmbedBuilder embedBuilder = EmbedBuilder.info()
                .withTitle("Island Information")
                .withField(":man_frowning: Leader", island.getOwner().getName(), true)
                .withField(":notepad_spiral: Name", island.getName(), true)
                .withField(":clock7: Creation Time", island.getCreationTimeDate(), true)
                .withField(":bank: Bank", "$" + island.getIslandBank().getBalance(), true)
                .withField(":dollar: Worth", "$" + island.getWorth(), true)
                .withField(":coin: Level", island.getIslandLevel() + "", true)
                .withBlankField(false);

        if (ADMIN_ROLE != null) {
            embedBuilder.withField(":red_circle: Admins", getIslandMemberNames(island, ADMIN_ROLE), true);
        }

        if (MOD_ROLE != null) {
            embedBuilder.withField(":green_circle: Moderators", getIslandMemberNames(island, MOD_ROLE), true);
        }

        if (MEMBER_ROLE != null) {
            embedBuilder.withField(":blue_circle: Members", getIslandMemberNames(island, MEMBER_ROLE), true);
        }


        textChannel.sendMessage(embedBuilder.build()).queue();
    }

    private static String getIslandMemberNames(Island island, PlayerRole playerRole) {
        StringBuilder stringBuilder = new StringBuilder();
        island.getIslandMembers(false).stream().filter(superiorPlayer -> superiorPlayer.getPlayerRole() == playerRole)
                .forEach(superiorPlayer -> stringBuilder.append("\n").append(superiorPlayer.getName()));
        return stringBuilder.length() == 0 ? "" : stringBuilder.substring(1);
    }

}
