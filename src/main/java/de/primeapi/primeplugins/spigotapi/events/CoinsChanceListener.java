package de.primeapi.primeplugins.spigotapi.events;

import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.api.events.CoinsChanceEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CoinsChanceListener implements Listener {

    @EventHandler
    public void onCoinsChance(CoinsChanceEvent e){
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            new PrimePlayer(onlinePlayer).sendScoreboard();
        }
    }

}
