package de.primeapi.primeplugins.spigotapi.utils;

import org.bukkit.entity.Player;

public interface AcceptCallback {
    
    public void accepted(Player p);
    public void declined(Player p);
    
}
