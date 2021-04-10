package de.primeapi.primeplugins.spigotapi.managers.api.placeholders;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.Locale;

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
        switch (params.toLowerCase()){
            case "coins":
                return String.valueOf(p.getCoins());
            case "onmins":
                return String.valueOf(p.retrieveOnMins());
            case "ontime_1": {
                int onmins = p.retrieveOnMins();
                int h = onmins / 60;
                int m = onmins % 60;
                return h + ":" + m;
            }
            case "ontime_2": {
                int onmins = p.retrieveOnMins();
                int h = onmins / 60;
                int m = onmins % 60;
                return h + "Stunden " + m + " Minuten";
            }
            case "ontime_3": {
                int onmins = p.retrieveOnMins();
                int h = onmins / 60;
                int m = onmins % 60;
                return h + "h " + m + " m";
            }
            case "ontime_4": {
                int onmins = p.retrieveOnMins();
                int h = onmins / 60;
                int m = onmins % 60;
                return h + "h ";
            }
        }

        return null;
    }
}
