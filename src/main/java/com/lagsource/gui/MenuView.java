package com.lagsource.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface MenuView {
    Inventory getInventory();

    default void handleClick(Player player, int slot, ItemStack item) {
    }
}
