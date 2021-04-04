package de.primeapi.primeplugins.spigotapi.events;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.api.events.GroupChanceEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GroupChanceListener implements Listener {

    @EventHandler
    public void onGroupChance(GroupChanceEvent e){
        Player player = Bukkit.getPlayer(e.getPlayer().getUniqueId());
        if(player != null){
            PrimePlayer p = new PrimePlayer(player);
            p.sendScoreboard();
            PrimeCore.getInstance().getScoreboardManager().sendTeams();
        }
    }

}
