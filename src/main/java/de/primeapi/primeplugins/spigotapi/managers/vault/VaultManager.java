package de.primeapi.primeplugins.spigotapi.managers.vault;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.plugins.perms.PermsAPI;
import de.primeapi.primeplugins.spigotapi.managers.config.configs.CoreConfig;

import java.util.logging.Level;

/**
 * @author Lukas S. PrimeAPI
 * created on 27.05.2021
 * crated for PrimePlugins
 */
public class VaultManager {


    public VaultManager() {
        if (PrimeCore.getInstance().getServer().getPluginManager().getPlugin("Vault") == null) {
            PrimeCore.getInstance().getLogger().log(Level.INFO, "Economy-Hook wird NICHT geladen");
            return;
        }
        if (CoreConfig.getInstance().getBoolean("vault.coins")) {
            new VaultEconomy();
        }
        if (PermsAPI.getInstance().isOnline() && CoreConfig.getInstance().getBoolean("vault.permissions")) {
            new VaultPermission();
            PrimeCore.getInstance().getLogger().log(Level.INFO, "Permissions-Hook wurde geladen");
        }
        PrimeCore.getInstance().getLogger().log(Level.INFO, "Economy-Hook wurde geladen");
    }
}
