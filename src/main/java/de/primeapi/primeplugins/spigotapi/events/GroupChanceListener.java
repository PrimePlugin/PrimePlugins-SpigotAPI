package de.primeapi.primeplugins.spigotapi.events;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.managers.events.GroupChanceEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GroupChanceListener implements Listener {

    @EventHandler
    public void onGroupChance(GroupChanceEvent e) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            new PrimePlayer(onlinePlayer).sendScoreboard();
            PrimeCore.getInstance().getScoreboardManager().sendTeams();
        }
    }

}
