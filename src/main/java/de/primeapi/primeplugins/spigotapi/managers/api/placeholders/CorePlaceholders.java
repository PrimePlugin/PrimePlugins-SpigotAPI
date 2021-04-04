package de.primeapi.primeplugins.spigotapi.managers.api.placeholders;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class CorePlaceholders extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "prime";
    }

    @Override
    public String getAuthor() {
        return "PrimeAPI";
    }

    @Override
    public String getVersion() {
        return PrimeCore.getInstance().getDescription().getVersion();
    }


    @Override
    public String onPlaceholderRequest(Player player, String params) {
        PrimePlayer p = new PrimePlayer(player);
        if(params.equalsIgnoreCase("coins")){
            return String.valueOf(p.getCoins());
        }

        return null;
    }
}
