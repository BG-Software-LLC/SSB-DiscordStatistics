package com.bgsoftware.ssbdiscordstatistics;

import com.bgsoftware.ssbdiscordstatistics.handlers.CommandsHandler;
import com.bgsoftware.ssbdiscordstatistics.handlers.SettingsHandler;
import com.bgsoftware.ssbdiscordstatistics.listeners.CommandsListener;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblock;
import com.bgsoftware.superiorskyblock.api.commands.SuperiorCommand;
import com.bgsoftware.superiorskyblock.api.modules.ModuleLoadTime;
import com.bgsoftware.superiorskyblock.api.modules.PluginModule;
import github.scarsz.discordsrv.dependencies.jda.api.JDA;
import github.scarsz.discordsrv.dependencies.jda.api.JDABuilder;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Activity;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Icon;
import github.scarsz.discordsrv.dependencies.jda.api.requests.GatewayIntent;
import org.bukkit.event.Listener;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public final class SSBDiscordStatisticsPlugin extends PluginModule {

    private static SSBDiscordStatisticsPlugin instance;

    private SettingsHandler settingsHandler;
    private CommandsHandler commandsHandler;
    private JDA bot;

    public SSBDiscordStatisticsPlugin() {
        super("DiscordStatistics", "Ome_R");
        instance = this;
    }

    @Override
    public void onEnable(SuperiorSkyblock plugin) {
        onReload(plugin);

        try {
            loadBot();
        } catch (Exception ex) {
            log("############################################");
            log("##                                        ##");
            log("##  Error while enabling the discord bot! ##");
            log("##                                        ##");
            log("############################################");
            throw new RuntimeException(ex);
        }

        log("Successfully enabled the discord bot!");
    }

    @Override
    public void onReload(SuperiorSkyblock plugin) {
        this.settingsHandler = new SettingsHandler(this);
        this.commandsHandler = new CommandsHandler(this);
    }

    @Override
    public void onDisable(SuperiorSkyblock plugin) {
        bot.shutdown();
    }

    @Override
    public Listener[] getModuleListeners(SuperiorSkyblock superiorSkyblock) {
        return null;
    }

    @Override
    public SuperiorCommand[] getSuperiorCommands(SuperiorSkyblock superiorSkyblock) {
        return null;
    }

    @Override
    public SuperiorCommand[] getSuperiorAdminCommands(SuperiorSkyblock superiorSkyblock) {
        return null;
    }

    @Override
    public ModuleLoadTime getLoadTime() {
        return ModuleLoadTime.AFTER_HANDLERS_LOADING;
    }

    public SettingsHandler getSettings() {
        return settingsHandler;
    }

    public CommandsHandler getCommands() {
        return commandsHandler;
    }

    public JDA getBot() {
        return bot;
    }

    private void loadBot() throws LoginException {
        this.bot = JDABuilder.create(GatewayIntent.GUILD_MESSAGES).setToken(settingsHandler.botToken)
                .addEventListeners(new CommandsListener(this))
                .setActivity(Activity.playing(instance.getSettings().serverName))
                .build();

        if (!instance.getSettings().serverIcon.isEmpty()) {
            try {
                URLConnection conn = new URL(instance.getSettings().serverIcon).openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0");
                this.bot.getSelfUser().getManager().setAvatar(Icon.from(conn.getInputStream())).queue();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void log(String message) {
        instance.getLogger().info(message);
    }

    public static SSBDiscordStatisticsPlugin getPlugin() {
        return instance;
    }

}
