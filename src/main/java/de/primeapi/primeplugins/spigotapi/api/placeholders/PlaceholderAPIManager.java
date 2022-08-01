package de.primeapi.primeplugins.spigotapi.api.placeholders;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.error.obj.ErrorObject;
import de.primeapi.primeplugins.spigotapi.api.error.types.ErrorTypes;
import de.primeapi.primeplugins.spigotapi.api.error.types.Messages;
import de.primeapi.primeplugins.spigotapi.api.placeholders.cloud.CloudGroupOnlinePlaceholder;
import de.primeapi.primeplugins.spigotapi.api.placeholders.cloud.CloudServerOnlinePlaceholder;
import de.primeapi.primeplugins.spigotapi.api.plugins.clan.ClanAPI;
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
            PrimeCore.getInstance().getTroubleShooter().done();
        } else {
            isActive = false;
            PrimeCore.getInstance().getLogger().info("PlaceholderAPI wurde nicht aktiviert");
            PrimeCore.getInstance().getTroubleController().addError(new ErrorObject(Messages.PLACEHOLDER_API, ErrorTypes.MEDIUM));
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
