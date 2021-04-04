package de.primeapi.primeplugins.spigotapi.managers.api;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.managers.api.placeholders.CorePlaceholders;
import de.primeapi.primeplugins.spigotapi.managers.api.placeholders.cloud.CloudGroupOnlinePlaceholder;
import de.primeapi.primeplugins.spigotapi.managers.api.placeholders.cloud.CloudServerOnlinePlaceholder;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlaceholderAPIManager {


    public boolean isActive;

    public PlaceholderAPIManager(){
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            isActive = true;
            PrimeCore.getInstance().getCoreLogger().sendInfo("PlaceholderAPI gefunden");
            new CorePlaceholders().register();
            new CloudServerOnlinePlaceholder().register();
            new CloudGroupOnlinePlaceholder().register();
        }else {
            isActive = false;
            PrimeCore.getInstance().getCoreLogger().sendInfo("PlaceholderAPI wurde nicht aktiviert");
        }
    }

    public String replace(Player p, String s){
        if(isActive) {
            try {
                return PlaceholderAPI.setPlaceholders(p, s);
            }catch (Exception ex){}
        }
        return s;
    }

}
