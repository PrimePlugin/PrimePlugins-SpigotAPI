package de.primeapi.primeplugins.spigotapi.events;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.BungeeAPI;
import de.primeapi.primeplugins.spigotapi.managers.config.configs.CoreConfig;
import de.primeapi.primeplugins.spigotapi.managers.messages.CoreMessage;
import de.primeapi.primeplugins.spigotapi.sql.utils.OnlineStats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

/**
 * @author Lukas S. PrimeAPI
 * created on 02.06.2021
 * crated for PrimePlugins
 */
public class MoveListener implements Listener {

    boolean active = false;
    public static HashMap<UUID, Long> lastMove = new HashMap<>();
    int sec;
    int delay;

    public MoveListener(){
        active = (BungeeAPI.getInstance().isOnline() && CoreConfig.getInstance().getBoolean("autoafk.use"));
        if(active) {
            sec = CoreConfig.getInstance().getInt("autoafk.detect.seconds");
            sec = CoreConfig.getInstance().getInt("autoafk.detect.delay");
            runCheck();
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        if(active){
            OnlineStats.getAFK(e.getPlayer().getUniqueId()).submit(aBoolean -> {
                if(aBoolean){
                    OnlineStats.setAFK(e.getPlayer().getUniqueId(), false);
                    e.getPlayer().sendMessage(CoreMessage.AFK_OFF.getContent());
                }
                lastMove.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
            });
        }
    }

    private void runCheck(){
        PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
           while (!Thread.currentThread().isInterrupted()){
               try {
                lastMove.forEach((uuid, aLong) -> {
                    Player target = Bukkit.getPlayer(uuid);
                    if(target == null){
                        lastMove.remove(uuid);
                        return;
                    }
                    if(aLong + (sec * 1000L) <= System.currentTimeMillis()){
                        OnlineStats.setAFK(target.getUniqueId(), true);
                        target.sendMessage(CoreMessage.AFK_ON.getContent());
                        lastMove.remove(uuid);
                    }
                });
                   Thread.sleep(delay * 1000L);
               } catch (Exception e) {
               }
           }
        });
    }

}
