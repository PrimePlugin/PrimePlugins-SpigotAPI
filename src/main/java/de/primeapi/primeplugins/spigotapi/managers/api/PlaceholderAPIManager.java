package de.primeapi.primeplugins.spigotapi.managers.api;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.ClanAPI;
import de.primeapi.primeplugins.spigotapi.managers.api.placeholders.ClanPlaceholder;
import de.primeapi.primeplugins.spigotapi.managers.api.placeholders.CorePlaceholders;
import de.primeapi.primeplugins.spigotapi.managers.api.placeholders.cloud.CloudGroupOnlinePlaceholder;
import de.primeapi.primeplugins.spigotapi.managers.api.placeholders.cloud.CloudServerOnlinePlaceholder;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlaceholderAPIManager {


    public boolean isActive;

    public PlaceholderAPIManager() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            isActive = true;
            PrimeCore.getInstance().getLogger().info("PlaceholderAPI gefunden");
            new CorePlaceholders().register();
            new CloudServerOnlinePlaceholder().register();
            new CloudGroupOnlinePlaceholder().register();
            if (ClanAPI.getInstance().isOnline()) {
                new ClanPlaceholder().register();
            }
        } else {
            isActive = false;
            PrimeCore.getInstance().getLogger().info("PlaceholderAPI wurde nicht aktiviert");
        }
    }

    public String replace(Player p, String s) {
        if (isActive) {
            try {
                return PlaceholderAPI.setPlaceholders(p, s);
            } catch (Exception ex) {
            }
        }
        return s;
    }

}
