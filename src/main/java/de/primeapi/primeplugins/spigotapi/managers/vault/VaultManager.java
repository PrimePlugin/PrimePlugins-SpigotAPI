package de.primeapi.primeplugins.spigotapi.managers.vault;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.PermsAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import java.util.logging.Level;

/**
 * @author Lukas S. PrimeAPI
 * created on 27.05.2021
 * crated for PrimePlugins
 */
public class VaultManager {


    public VaultManager(){
        if (PrimeCore.getInstance().getServer().getPluginManager().getPlugin("Vault") == null) {
            PrimeCore.getInstance().getLogger().log(Level.INFO, "Economy-Hook wird NICHT geladen");
            return;
        }
        new VaultEconomy();
        if(PermsAPI.getInstance().isOnline()){
            new VaultPermission();
            PrimeCore.getInstance().getLogger().log(Level.INFO, "Permissions-Hook wurde geladen");
        }
        PrimeCore.getInstance().getLogger().log(Level.INFO, "Economy-Hook wurde geladen");
    }
}
