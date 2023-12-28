package com.x_tornado10.craftiservidiscord;

import com.x_tornado10.craftiservidiscord.listeners.DiscordListener;
import com.x_tornado10.craftiservidiscord.listeners.MinecraftChatListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


import java.awt.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public final class craftiservi_discord extends JavaPlugin {
    private static craftiservi_discord instance;
    private JDA jda;
    private TextChannel chatChannel;
    private PluginManager pm;
    private final Map<String, String > advancementToDisplayMap = new HashMap<>();
    private Logger logger;

    @Override
    public void onLoad() {
        instance = this;
        pm = Bukkit.getPluginManager();
        logger = getLogger();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        String bot_token = getConfig().getString("bot-token");
        try {
            jda = JDABuilder.createDefault(bot_token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_EMOJIS_AND_STICKERS, GatewayIntent.GUILD_INVITES, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.SCHEDULED_EVENTS)
                    .build()
                    .awaitReady();
        } catch (InterruptedException e) {
            logger.severe("Couldn't connect with discord!");
            logger.severe("Please restart the server!");
        } catch (InvalidTokenException e) {
            logger.warning("Couldn't start Plugin. Please verify you set the right bot token in config.yml!");
        }

        if (jda == null) {
            pm.disablePlugin(this);
            logger.severe("Couldn't connect with discord!");
            logger.severe("Disabling Plugin!");
            return;
        }

        String chatChannelID = getConfig().getString("chat-channel-id");
        try {
                chatChannel = jda.getTextChannelById(Objects.requireNonNull(chatChannelID));
                Objects.requireNonNull(chatChannel).getId();
        } catch (NumberFormatException | NullPointerException e) {
            logger.warning("Couldn't start Plugin. Please verify you set the right channel-id in config.yml!");
        }

        ConfigurationSection advancementMap = getConfig().getConfigurationSection("advancementMap");
        if (advancementMap != null) {
            for (String key : advancementMap.getKeys(false)) {
                advancementToDisplayMap.put(key, advancementMap.getString(key));
            }
        }

        jda.addEventListener(new DiscordListener());
        pm.registerEvents(new MinecraftChatListener(), this);

        saveResource("error-icon.png", true);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (jda != null) jda.shutdownNow();

    }

    public static craftiservi_discord getInstance() {
        return instance;
    }

    public void sendMessage(Player p, String content, boolean contentInAuthorLine, Color color) {

        if (chatChannel == null || content == null) return;

        String url = "https://mc-heads.net/avatar/" + p.getUniqueId();
        String error_url = "https://crafti-servi.com/error-icon.png";

        EmbedBuilder builder = new EmbedBuilder()
                .setColor(color)
                .setAuthor(
                        contentInAuthorLine ? content : p.getDisplayName(),
                        null,
                        checkURL(url) ? url : checkURL(error_url) ? error_url : null
                );
        if (!contentInAuthorLine) {
            builder.setDescription(content);
        }

        chatChannel.sendMessageEmbeds(builder.build()).queue();

    }

    public static boolean checkURL(String URL){
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(URL).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        }
        catch (Exception e) {
            return false;
        }
    }

    public Map<String, String> getAdvancementToDisplayMap() {
        return advancementToDisplayMap;
    }

    public TextChannel getChatChannel() {
        return chatChannel;
    }
}
