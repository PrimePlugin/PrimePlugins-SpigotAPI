package de.primeapi.primeplugins.spigotapi.managers.vault;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.plugins.perms.PermsAPI;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;

/**
 * @author Lukas S. PrimeAPI
 * created on 05.06.2021
 * crated for PrimePlugins
 */
public class VaultPermission extends Permission {

    public VaultPermission() {
        Bukkit.getServicesManager().register(Permission.class, this, PrimeCore.getInstance(), ServicePriority.High);
    }


    @Override
    public String getName() {
        return "Primeperms";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean hasSuperPermsCompat() {
        return false;
    }

    @Override
    public boolean playerHas(String world, String playername, String permission) {
        Player p = Bukkit.getPlayer(playername);
        boolean b = PermsAPI.getInstance().hasSelfPermission(p.getUniqueId(), permission).complete();
        return b;
    }

    @Override
    public boolean playerAdd(String s, String s1, String s2) {
        return false;
    }

    @Override
    public boolean playerRemove(String s, String s1, String s2) {
        return false;
    }

    @Override
    public boolean groupHas(String s, String s1, String s2) {
        return false;
    }

    @Override
    public boolean groupAdd(String s, String s1, String s2) {
        return false;
    }

    @Override
    public boolean groupRemove(String s, String s1, String s2) {
        return false;
    }

    @Override
    public boolean playerInGroup(String s, String s1, String s2) {
        return false;
    }

    @Override
    public boolean playerAddGroup(String s, String s1, String s2) {
        return false;
    }

    @Override
    public boolean playerRemoveGroup(String s, String s1, String s2) {
        return false;
    }

    @Override
    public String[] getPlayerGroups(String s, String s1) {
        return new String[0];
    }

    @Override
    public String getPrimaryGroup(String s, String s1) {
        return null;
    }

    @Override
    public String[] getGroups() {
        return new String[0];
    }

    @Override
    public boolean hasGroupSupport() {
        return false;
    }
}
