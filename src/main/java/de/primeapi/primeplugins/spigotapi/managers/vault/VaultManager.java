package de.primeapi.primeplugins.spigotapi.managers.vault;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import java.util.logging.Level;

/**
 * @author Lukas S. PrimeAPI
 * created on 27.05.2021
 * crated for PrimePlugins
 */
public class VaultManager {

    Economy economy;

    public VaultManager(){
        if (PrimeCore.getInstance().getServer().getPluginManager().getPlugin("Vault") == null) {
            PrimeCore.getInstance().getLogger().log(Level.INFO, "Economy-Hook wird NICHT geladen");
            return;
        }
        economy = new VaultEconomy();
        Bukkit.getServicesManager().register(Economy.class, this.economy, PrimeCore.getInstance(), ServicePriority.High);
        PrimeCore.getInstance().getLogger().log(Level.INFO, "Economy-Hook wurde geladen");
    }
}
