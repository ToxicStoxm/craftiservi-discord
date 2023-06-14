package com.x_tornado10.craftiservidiscord;

import com.x_tornado10.craftiservi;
import com.x_tornado10.craftiservidiscord.listeners.DiscordListener;
import com.x_tornado10.craftiservidiscord.listeners.MinecraftChatListener;
import com.x_tornado10.logger.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.apache.logging.log4j.CloseableThreadContext;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.Configuration;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public final class craftiservi_discord extends JavaPlugin {
    private static craftiservi_discord instance;
    private JDA jda;
    private TextChannel chatChannel;
    private PluginLoader pl;
    private PluginManager pm;
    private final String cs_name = "Craftiservi";
    private Logger logger;
    private craftiservi cs;
    private final Map<String, String > advancementToDisplayMap = new HashMap<>();

    @Override
    public void onLoad() {

        instance = this;

        pl = getPluginLoader();
        pm = Bukkit.getPluginManager();

        /*
        cs = craftiservi.getInstance();
        logger = cs.getCustomLogger();


        if (pm.getPlugin(cs_name) == null) {

            getLogger().warning("Dependency: [" + cs_name + "] was not found!");
            return;

        }

        Plugin craftiservi = pm.getPlugin(cs_name);

        logger.dc_info("Dependency: [" + cs_name +"] was found and validated!");
        logger.dc_info("Dependency: " + craftiservi.getName() + " v" + craftiservi.getDescription().getVersion() + ", Authors: " + craftiservi.getDescription().getAuthors());

         */
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        /*
        if (pm.getPlugin(cs_name) == null || !pm.getPlugin(cs_name).isEnabled()) {

            getLogger().severe("Dependency: [" + cs_name + "] was not found or is not enabled!");
            getLogger().severe("Dependency: Plugin can not start without [" + cs_name + "]!");
            getLogger().severe("Disabling Plugin!");
            pl.disablePlugin(this);
            return;

        }

        Plugin craftiservi = pm.getPlugin(cs_name);

        logger.dc_info("Dependency: [" + cs_name +"] is enabled!");
        logger.dc_info("Dependency: " + craftiservi.getName() + " v" + craftiservi.getDescription().getVersion() + ", Authors: " + craftiservi.getDescription().getAuthors());
        logger.dc_info("Dependency: Validated!");

         */

        saveDefaultConfig();
        String bot_token = getConfig().getString("bot-token");
        try {
            jda = JDABuilder.createDefault(bot_token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_EMOJIS_AND_STICKERS, GatewayIntent.GUILD_INVITES, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.SCHEDULED_EVENTS)
                    .build()
                    .awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (jda == null) {
            pm.disablePlugin(this);
            logger.dc_severe("Couldn't connect with discord!");
            logger.dc_severe("Disabling Plugin!");
            return;
        }

        String chatChannelID = getConfig().getString("chat-channel-id");
        if (chatChannelID != null) {
            chatChannel = jda.getTextChannelById(chatChannelID);
        }

        ConfigurationSection advancementMap = getConfig().getConfigurationSection("advancementMap");
        if (advancementMap != null) {
            for (String key : advancementMap.getKeys(false)) {
                advancementToDisplayMap.put(key, advancementMap.getString(key));
            }
        }

        jda.addEventListener(new DiscordListener());
        pm.registerEvents(new MinecraftChatListener(), this);

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

        EmbedBuilder builder = new EmbedBuilder()
                .setColor(color)
                .setAuthor(
                        contentInAuthorLine ? content : p.getDisplayName(),
                        null,
                        "https://mc-heads.net/avatar/" + p.getUniqueId().toString()
                );
        if (!contentInAuthorLine) {
            builder.setDescription(content);
        }

        chatChannel.sendMessageEmbeds(builder.build()).queue();

    }

    public Map<String, String> getAdvancementToDisplayMap() {
        return advancementToDisplayMap;
    }

    public TextChannel getChatChannel() {
        return chatChannel;
    }
}
