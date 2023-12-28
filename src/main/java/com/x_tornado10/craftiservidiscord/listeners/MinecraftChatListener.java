package com.x_tornado10.craftiservidiscord.listeners;

import com.x_tornado10.craftiservidiscord.craftiservi_discord;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;

public class MinecraftChatListener implements Listener {

    private final craftiservi_discord p = craftiservi_discord.getInstance();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        p.sendMessage(e.getPlayer(), e.getMessage(), false, Color.GRAY);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        p.sendMessage(e.getPlayer(), e.getPlayer().getDisplayName() + " joined the game.", true, Color.GREEN);
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        p.sendMessage(e.getPlayer(), e.getPlayer().getDisplayName() + " left the game.", true, Color.RED);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        String deathMessage = e.getDeathMessage() == null ? player.getDisplayName() + " died." : e.getDeathMessage();
        p.sendMessage(player, deathMessage, true, Color.RED);
    }

    @EventHandler
    public void onAdvancemant(PlayerAdvancementDoneEvent e) {
        String advancementKey = e.getAdvancement().getKey().getKey();
        String display = p.getAdvancementToDisplayMap().get(advancementKey);
        if (display == null) return;

        p.sendMessage(e.getPlayer(), e.getPlayer().getDisplayName() + " has made the advancemant [" + display + "]", true, Color.CYAN);
    }
}