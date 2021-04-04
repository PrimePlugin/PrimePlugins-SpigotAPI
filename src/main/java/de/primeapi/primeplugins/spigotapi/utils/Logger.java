package de.primeapi.primeplugins.spigotapi.utils;

import org.bukkit.Bukkit;

public class Logger {

    public void sendInfo(String message){
        Bukkit.getConsoleSender().sendMessage("[PrimeCore] " + message);
    }

    public void sendDebug(String message){
        Bukkit.getConsoleSender().sendMessage("[PrimeCoreDEBUG] " + message);
    }

}
