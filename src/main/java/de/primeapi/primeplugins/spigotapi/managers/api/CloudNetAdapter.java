package de.primeapi.primeplugins.spigotapi.managers.api;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.ServiceInfoSnapshotUtil;
import de.dytanic.cloudnet.lib.server.info.ServerInfo;
import de.primeapi.primeplugins.spigotapi.PrimeCore;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class CloudNetAdapter {


    String cloud;

    public CloudNetAdapter() {
        if (Bukkit.getPluginManager().getPlugin("CloudNetAPI") != null) {
            PrimeCore.getInstance().getLogger().log(Level.INFO, "CloudNetv2 wurde gefunden!");
            cloud = "cn2";
        } else if (Bukkit.getPluginManager().getPlugin("CloudNet-Bridge") != null) {
            PrimeCore.getInstance().getLogger().log(Level.INFO, "CloudNetv3 wurde gefunden!");
            cloud = "cn3";
        } else {
            PrimeCore.getInstance().getLogger().log(Level.INFO, "Es wurde keine Coud gefunden!");
            cloud = "none";
        }
    }

    public List<String> getServersInGroup(String s) {
        if (cloud.equalsIgnoreCase("cn2")) {
            List<String> list = new ArrayList<>();
            for (ServerInfo serverInfo : CloudAPI.getInstance().getServers()) {
                if (serverInfo.getServerConfig().getProperties().getName().startsWith(s))
                    list.add(serverInfo.getServerConfig().getProperties().getName());
            }
            return list;
        }
        if (cloud.equalsIgnoreCase("cn3")) {
            List<String> list = new ArrayList<>();
            CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServices(s).forEach(serviceInfoSnapshot -> {
                list.add(serviceInfoSnapshot.getName());
            });
            return list;
        }

        return new ArrayList<>();
    }

    public int getPlayersOnServer(String name) {
        if (cloud.equalsIgnoreCase("cn2")) {
            return CloudAPI.getInstance().getServerInfo(name).getPlayers().size();
        }
        if (cloud.equalsIgnoreCase("cn3")) {
            return ServiceInfoSnapshotUtil.getOnlineCount(CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName(name));
        }

        return -1;
    }

    public int getPlayersInGroup(String name) {
        if (cloud.equalsIgnoreCase("cn2")) {
            return CloudAPI.getInstance().getOnlineCount(name);
        }
        if (cloud.equalsIgnoreCase("cn3")) {
            return ServiceInfoSnapshotUtil.getTaskOnlineCount(name);
        }

        return -1;
    }

}
