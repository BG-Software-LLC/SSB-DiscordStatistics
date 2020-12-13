package com.bgsoftware.ssbdiscordstatistics.utils;

import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;

import java.awt.*;

public final class EmbedBuilder {

    private final github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder embedBuilder =
            new github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder()
            .setFooter("SSB Discord-Statistics | Developed by Ome_R");

    private EmbedBuilder(){

    }

    public EmbedBuilder withColor(Color color){
        embedBuilder.setColor(color);
        return this;
    }

    public EmbedBuilder withTitle(String title){
        embedBuilder.setTitle(title);
        return this;
    }

    public EmbedBuilder withField(String name, String value, boolean inline){
        embedBuilder.addField(name, value, inline);
        return this;
    }

    public EmbedBuilder withBlankField(boolean inline){
        embedBuilder.addBlankField(inline);
        return this;
    }

    public MessageEmbed build(){
        return embedBuilder.build();
    }

    public static EmbedBuilder info(){
        return new EmbedBuilder().withColor(new Color(78, 135, 238));
    }

    public static EmbedBuilder error(){
        return new EmbedBuilder().withColor(new Color(255, 77, 77));
    }

}
