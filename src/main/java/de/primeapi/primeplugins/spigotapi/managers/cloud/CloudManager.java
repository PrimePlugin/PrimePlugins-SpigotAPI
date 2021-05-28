package de.primeapi.primeplugins.spigotapi.managers.cloud;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.managers.cloud.adapter.CloudAdapter;
import de.primeapi.primeplugins.spigotapi.managers.cloud.adapter.version.CloudNetV2;
import de.primeapi.primeplugins.spigotapi.managers.cloud.adapter.version.CloudNetV3;
import de.primeapi.primeplugins.spigotapi.managers.cloud.adapter.version.SimpleCloud;
import de.primeapi.primeplugins.spigotapi.managers.cloud.types.CloudTypes;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * @author Jonas | Exceptionpilot#5555
 * Created on 28.05.2021 «» 14:51
 * Class «» CloudManager
 **/

public class CloudManager implements CloudAdapter {

    private CloudTypes found = null;

    public CloudManager() {

    }

    public void check() {
        PrimeCore.getInstance().getLogger().log(Level.INFO, "Versuche Cloud zu finden...");
        for (CloudTypes cloudTypes : CloudTypes.values()) {
            if (Bukkit.getPluginManager().getPlugin(cloudTypes.getPlugin()) != null) {
                PrimeCore.getInstance().getLogger().log(Level.INFO, "Die Cloud [" + cloudTypes.toString().toLowerCase() + "] wurde gefunden!");
                found = cloudTypes;
                return;
            }
        }
        PrimeCore.getInstance().getLogger().log(Level.WARNING, "Es wurde keine Cloud gefunden!");
    }

    @Override
    public int getPlayersInGroup(String name) {
        switch (found) {
            case CLOUDNETV2:
                return new CloudNetV2().getPlayersInGroup(name);
            case CLOUDNETV3:
                return new CloudNetV3().getPlayersInGroup(name);
            case SIMPLECLOUD:
                return new SimpleCloud().getPlayersInGroup(name);
            default:
                return -1;
        }
    }

    @Override
    public List<String> getAllLobbies(String name) {
        List<String> lobbyList = new ArrayList<>();
        switch (found) {
            case SIMPLECLOUD:
                lobbyList = new SimpleCloud().getAllLobbies(name);
                break;
            case CLOUDNETV2:
                lobbyList = new CloudNetV2().getAllLobbies(name);
                break;
            case CLOUDNETV3:
                lobbyList = new CloudNetV3().getAllLobbies(name);
                break;
        }
        return lobbyList;
    }

    @Override
    public int getPlayersOnServer(String name) {
        switch (found) {
            case CLOUDNETV2:
                return new CloudNetV2().getPlayersOnServer(name);
            case CLOUDNETV3:
                return new CloudNetV3().getPlayersOnServer(name);
            case SIMPLECLOUD:
                return new SimpleCloud().getPlayersOnServer(name);
            default:
                return -1;
        }
    }
}
