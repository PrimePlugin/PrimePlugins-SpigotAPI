package de.primeapi.primeplugins.spigotapi.managers.versions;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.logging.Level;

@Getter
public class VersionManager {


    public final String nmsVersion;
    public MinecraftVersion currentVersion;

    public VersionManager() {
        String s = Bukkit.getVersion();
        for (MinecraftVersion value : MinecraftVersion.values()) {
            if (s.contains(value.getName())) {
                currentVersion = value;
                break;
            }
        }
        if (currentVersion == null) {
            currentVersion = MinecraftVersion.V_OTHER;
        }
        nmsVersion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        PrimeCore.getInstance().getLogger().log(Level.INFO, "Registered Minecraft-Version: " + currentVersion.getName());
    }

    public Class<?> getNMSClass(String name) throws ClassNotFoundException {
        if (currentVersion.isHigherEqualThan(MinecraftVersion.V1_17)) {
            return Class.forName("net.minecraft.server." + name);
        } else {
            return Class.forName("net.minecraft.server." + nmsVersion + "." + name);
        }
    }


}
