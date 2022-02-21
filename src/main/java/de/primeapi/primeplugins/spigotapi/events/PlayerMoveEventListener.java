package de.primeapi.primeplugins.spigotapi.events;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
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
    boolean active = false;
    int sec;
    int delay;
    private final Set<UUID> blocked = new HashSet<>();

    public PlayerMoveEventListener() {
        active = (BungeeAPI.getInstance().isOnline() && CoreConfig.getInstance().getBoolean("autoafk.use"));
        if (active) {
            sec = CoreConfig.getInstance().getInt("autoafk.detect.seconds");
            sec = CoreConfig.getInstance().getInt("autoafk.detect.delay");
            runCheck();
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (active && !blocked.contains(e.getPlayer().getUniqueId())) {
            blocked.add(e.getPlayer().getUniqueId());
            OnlineStats.getAFK(e.getPlayer().getUniqueId()).submit(aBoolean -> {
                if (aBoolean) {
                    OnlineStats.setAFK(e.getPlayer().getUniqueId(), false);
                    e.getPlayer().sendMessage(CoreMessage.AFK_OFF.getContent());
                }
                lastMove.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
                blocked.remove(e.getPlayer().getUniqueId());
            });
        }
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
