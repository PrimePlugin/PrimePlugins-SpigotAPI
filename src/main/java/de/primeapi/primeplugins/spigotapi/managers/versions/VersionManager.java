package de.primeapi.primeplugins.spigotapi.managers.versions;

import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public class VersionManager {


    public MinecraftVersion currentVersion;
    public final String nmsVersion;

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
        nmsVersion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        System.out.println("Registered Minecraft-Version: " + currentVersion);
    }

    public Class<?> getNMSClass(String name) throws ClassNotFoundException {
        if(currentVersion.isHigherEqualThan(MinecraftVersion.V1_17)){
            return Class.forName("net.minecraft.server." + name);
        }else{
            return Class.forName("net.minecraft.server." + nmsVersion + "." + name);
        }
    }



}
