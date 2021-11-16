package de.primeapi.primeplugins.spigotapi.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@Getter
@AllArgsConstructor
public class InvalidListener implements Listener {

    String message;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().sendMessage(message);
        e.getPlayer().sendMessage(message);
        e.getPlayer().sendMessage(message);
        e.getPlayer().sendMessage(message);
        e.getPlayer().sendMessage(message);
        e.getPlayer().sendMessage(message);
        e.getPlayer().sendMessage(message);
        e.getPlayer().sendMessage(message);
        e.getPlayer().sendMessage(message);
        e.getPlayer().sendMessage(message);
    }

}