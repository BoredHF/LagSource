package com.lagsource.gui;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class MenuRouter implements Listener {
    private final Map<UUID, MenuView> openMenus = new ConcurrentHashMap<>();

    public void open(Player player, MenuView view) {
        openMenus.put(player.getUniqueId(), view);
        player.openInventory(view.getInventory());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        MenuView view = openMenus.get(player.getUniqueId());
        if (view == null || !event.getInventory().equals(view.getInventory())) {
            return;
        }
        event.setCancelled(true);
        try {
            view.handleClick(player, event.getRawSlot(), event.getCurrentItem());
        } catch (Exception ex) {
            player.sendMessage(ChatColor.RED + "LagSource menu error. Check console.");
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }
        MenuView view = openMenus.get(player.getUniqueId());
        if (view != null && event.getInventory().equals(view.getInventory())) {
            openMenus.remove(player.getUniqueId());
        }
    }
}
