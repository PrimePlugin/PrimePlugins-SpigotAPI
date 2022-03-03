package de.primeapi.primeplugins.spigotapi.events;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.api.plugins.nick.NickAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PrimePlayer primePlayer = PrimePlayer.fromPlayer(event.getPlayer());
        PrimeCore.getInstance().getScoreboardManager().customScoreboard.remove(primePlayer.getUniqueId());
        PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
            try {
                NickAPI.getInstance().removeFromDatabase(event.getPlayer());
                NickAPI.getInstance().getIsNicked().unCache(event.getPlayer().getUniqueId());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }
}
