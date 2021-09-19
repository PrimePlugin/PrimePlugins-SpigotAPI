package de.primeapi.primeplugins.spigotapi.managers.versions;

import org.bukkit.Bukkit;

public class VersionManager {


    public MinecraftVersion currentVersion;

    public VersionManager(){
        String s = Bukkit.getVersion();
        for (MinecraftVersion value : MinecraftVersion.values()) {
            if(s.contains(value.getName())){
                currentVersion = value;
                break;
            }
        }
        if(currentVersion == null){
            currentVersion = MinecraftVersion.V_OTHER;
        }

        System.out.println("Registered Minecraft-Version: " + currentVersion.toString());
    }
}
