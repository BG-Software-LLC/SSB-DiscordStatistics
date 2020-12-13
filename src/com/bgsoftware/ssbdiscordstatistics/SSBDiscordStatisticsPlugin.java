package com.bgsoftware.ssbdiscordstatistics;

import com.bgsoftware.ssbdiscordstatistics.handlers.CommandsHandler;
import com.bgsoftware.ssbdiscordstatistics.handlers.SettingsHandler;
import com.bgsoftware.ssbdiscordstatistics.listeners.CommandsListener;
import github.scarsz.discordsrv.dependencies.jda.api.JDA;
import github.scarsz.discordsrv.dependencies.jda.api.JDABuilder;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Activity;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Icon;
import github.scarsz.discordsrv.dependencies.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public final class SSBDiscordStatisticsPlugin extends JavaPlugin {

    private static SSBDiscordStatisticsPlugin plugin;

    private SettingsHandler settingsHandler;
    private CommandsHandler commandsHandler;
    private JDA bot;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        reloadPlugin();

        try {
            loadBot();
        }catch (Exception ex){
            log("############################################");
            log("##                                        ##");
            log("##  Error while enabling the discord bot! ##");
            log("##                                        ##");
            log("############################################");
            Bukkit.getScheduler().runTask(this, () -> Bukkit.getPluginManager().disablePlugin(this));
            return;
        }

        log("Successfully enabled the discord bot!");
    }

    @Override
    public void onDisable() {
        bot.shutdown();
    }

    public void reloadPlugin(){
        this.settingsHandler = new SettingsHandler(this);
        this.commandsHandler = new CommandsHandler(this);
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
                .setActivity(Activity.playing(plugin.getSettings().serverName))
                .build();

        if(!plugin.getSettings().serverIcon.isEmpty()) {
            try {
                URLConnection conn = new URL(plugin.getSettings().serverIcon).openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0");
                this.bot.getSelfUser().getManager().setAvatar(Icon.from(conn.getInputStream())).queue();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void log(String message){
        plugin.getLogger().info(message);
    }

    public static SSBDiscordStatisticsPlugin getPlugin() {
        return plugin;
    }

}
