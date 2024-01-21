package com.x_tornado10.craftiservi_discord.listeners;

import com.x_tornado10.craftiservi_discord.craftiservi_discord;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class DiscordListener extends ListenerAdapter {

    private final craftiservi_discord p = craftiservi_discord.getInstance();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.getChannel().equals(p.getChatChannel())) return;

        Member member = e.getMember();
        if (member == null || member.getUser().isBot()) return;

        String message = e.getMessage().getContentDisplay();
        Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "<" + member.getEffectiveName() + ">" + ChatColor.RESET + " " + message);
    }
}
