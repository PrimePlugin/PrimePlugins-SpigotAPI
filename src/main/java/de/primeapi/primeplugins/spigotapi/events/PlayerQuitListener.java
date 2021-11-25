package de.primeapi.primeplugins.spigotapi.events;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.NickAPI;
import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        PrimePlayer p = new PrimePlayer(e.getPlayer());
        PrimeCore.getInstance().getScoreboardManager().customScoreboard.remove(p.getUniqueId());
        PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
            try {
                NickAPI.getInstance().removeFromDatabase(e.getPlayer());
                NickAPI.getInstance().getIsNicked().unCache(e.getPlayer().getUniqueId());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

}
