package de.primeapi.primeplugins.spigotapi.managers.cloud;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.managers.cloud.adapter.CloudAdapter;
import de.primeapi.primeplugins.spigotapi.managers.cloud.adapter.version.CloudNetV2;
import de.primeapi.primeplugins.spigotapi.managers.cloud.adapter.version.CloudNetV3;
import de.primeapi.primeplugins.spigotapi.managers.cloud.adapter.version.SimpleCloud;
import de.primeapi.primeplugins.spigotapi.managers.cloud.types.CloudTypes;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collections;
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
        check();
    }

    private void check() {
        PrimeCore.getInstance().getLogger().log(Level.INFO, "Versuche Cloud zu finden...");
        for (CloudTypes cloudTypes : CloudTypes.values()) {
            if (Bukkit.getPluginManager().getPlugin(cloudTypes.getPlugin()) != null) {
                PrimeCore.getInstance().getLogger().log(Level.INFO, "Die Cloud [" + cloudTypes.toString().toLowerCase() + "] wurde gefunden!");
                found = cloudTypes;
                return;
            }
        }
        PrimeCore.getInstance().getLogger().log(Level.WARNING, "Es wurde keine Cloud gefunden!");
        found = CloudTypes.NOCLOUD;
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
        Collections.sort(lobbyList);
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

    @Override
    public String getServerState(String name) {
        switch (found) {
            case CLOUDNETV2:
                return new CloudNetV2().getServerState(name);
            case CLOUDNETV3:
                return new CloudNetV3().getServerState(name);
            case SIMPLECLOUD:
                return new SimpleCloud().getServerState(name);
            default:
                return null;
        }
    }

    public void sendPlayerToServer(Player player, String server) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeUTF("Connect");
            dataOutputStream.writeUTF(server);
            player.sendPluginMessage(PrimeCore.getInstance(), "BungeeCord", byteArrayOutputStream.toByteArray());
            byteArrayOutputStream.close();
            dataOutputStream.close();
        } catch (Exception exception) {
            player.sendMessage("§cEs gab ein Fehler mit der Server connection!");
        }
    }
}
