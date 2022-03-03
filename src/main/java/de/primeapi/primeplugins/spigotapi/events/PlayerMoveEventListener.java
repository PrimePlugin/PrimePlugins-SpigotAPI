package de.primeapi.primeplugins.spigotapi.events;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.api.plugins.bungee.BungeeAPI;
import de.primeapi.primeplugins.spigotapi.managers.config.configs.CoreConfig;
import de.primeapi.primeplugins.spigotapi.managers.messages.CoreMessage;
import de.primeapi.primeplugins.spigotapi.sql.utils.OnlineStats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Lukas S. PrimeAPI
 * created on 02.06.2021
 * crated for PrimePlugins
 */
public class PlayerMoveEventListener implements Listener {

    public static HashMap<UUID, Long> lastMove = new HashMap<>();
    private final Set<UUID> blocked = new HashSet<>();
    boolean active = false;
    int sec;
    int delay;

    public PlayerMoveEventListener() {
        active = (BungeeAPI.getInstance().isOnline() && CoreConfig.getInstance().getBoolean("autoafk.use"));
        if (active) {
            sec = CoreConfig.getInstance().getInt("autoafk.detect.seconds");
            sec = CoreConfig.getInstance().getInt("autoafk.detect.delay");
            runCheck();
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(PrimeCore.getInstance(), () -> {
            if (active && !blocked.contains(event.getPlayer().getUniqueId())) {
                blocked.add(event.getPlayer().getUniqueId());
                OnlineStats.getAFK(event.getPlayer().getUniqueId()).submit(aBoolean -> {
                    if (aBoolean) {
                        OnlineStats.setAFK(event.getPlayer().getUniqueId(), false);
                        event.getPlayer().sendMessage(CoreMessage.AFK_OFF.getContent());
                    }
                    lastMove.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
                    blocked.remove(event.getPlayer().getUniqueId());
                });
            }
        });
    }

    private void runCheck() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PrimeCore.getInstance(), () -> {
            try {
                HashMap<UUID, Long> clone = new HashMap<>(lastMove);
                clone.forEach((uuid, aLong) -> {
                    Player target = Bukkit.getPlayer(uuid);
                    if (target == null) {
                        lastMove.remove(uuid);
                        return;
                    }
                    if (aLong + (sec * 1000L) <= System.currentTimeMillis()) {
                        OnlineStats.setAFK(target.getUniqueId(), true);
                        target.sendMessage(CoreMessage.AFK_ON.getContent());
                        lastMove.remove(uuid);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, delay * 20L, delay * 20L);
    }

}
