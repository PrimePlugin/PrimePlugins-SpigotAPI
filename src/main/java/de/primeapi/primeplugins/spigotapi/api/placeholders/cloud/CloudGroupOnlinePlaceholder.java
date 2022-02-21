package de.primeapi.primeplugins.spigotapi.api.placeholders.cloud;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class CloudGroupOnlinePlaceholder extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "cloudgrouponline";
    }

    @Override
    public String getAuthor() {
        return "PrimeAPI";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        return String.valueOf(PrimeCore.getInstance().getCloudNetAdapter().getPlayersInGroup(params));
    }
}
