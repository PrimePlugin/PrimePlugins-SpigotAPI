package de.primeapi.primeplugins.spigotapi.api.events;

import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public class CoinsChanceEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    final SQLPlayer player;
    final int newAmount;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }
}
