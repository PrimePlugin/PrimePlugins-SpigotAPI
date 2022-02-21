package de.primeapi.primeplugins.spigotapi.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IClickAction {

    void onClick(Player p, ItemStack itemStack);

}
