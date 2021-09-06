package de.primeapi.primeplugins.spigotapi.api;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.managers.rest.PluginInfo;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Lukas S. PrimeAPI
 * created on 31.05.2021
 * crated for PrimePlugins
 */
@Getter
public class RestPlugin {

    private String name;
    public JavaPlugin plugin;
    @Setter
    public String license = "";

    public RestPlugin(String name, JavaPlugin plugin) {
        this.name = name;
        this.plugin = plugin;
        PrimeCore.getInstance().getRestManager().registerPlugin(this);
    }
    /**
     * @deprecated
     * This function will no longer be supported, as there are massive security lacks.
     * Call a custom http request instead.
     * @param license The License to be checked
     * @return Weather or not the license is valid
     */
    public boolean checkLicense(String license){
        setLicense(license);
        try{
            return PrimeCore.getInstance().getRestManager().validateLicense(license ,name);
        }catch (Exception ex){
            return false;
        }
    }

    public boolean isNewUpdateAvailable(){
        try {
            return PrimeCore.getInstance().getRestManager().getPlugininfo(name).isNeverVersion(plugin.getDescription().getVersion());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    public void downloadLatestVersion(String path){
        PrimeCore.getInstance().getRestManager().downloadPlugin(name, license, path);
    }

    public PluginInfo getPluginInfo(){
        return PrimeCore.getInstance().getRestManager().getPlugininfo(name);
    }


}
