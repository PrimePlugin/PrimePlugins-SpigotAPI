package de.primeapi.primeplugins.spigotapi.api.plugin;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.managers.rest.PluginInfo;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Lukas S. PrimeAPI
 * created on 31.05.2021
 * crated for PrimePlugins
 */
@Getter
public class RestPlugin {

    private final String name;
    public JavaPlugin plugin;
    @Setter
    public String license = "";

    public RestPlugin(String name, JavaPlugin plugin) {
        this.name = name;
        this.plugin = plugin;
        PrimeCore.getInstance().getRestManager().registerPlugin(this);
    }

    public boolean isNewUpdateAvailable() {
        try {
            return PrimeCore.getInstance().getRestManager().getPlugininfo(name).isNeverVersion(plugin.getDescription().getVersion());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean downloadLatestVersion(String path) {
        return PrimeCore.getInstance().getRestManager().downloadPlugin(getPluginInfo(), license, path);
    }

    public PluginInfo getPluginInfo() {
        return PrimeCore.getInstance().getRestManager().getPlugininfo(name);
    }


}
