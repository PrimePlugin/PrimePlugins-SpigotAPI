package de.primeapi.primeplugins.spigotapi.managers.cloud.adapter.version;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.server.info.ServerInfo;
import de.primeapi.primeplugins.spigotapi.managers.cloud.adapter.CloudAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jonas | Exceptionpilot#5555
 * Created on 28.05.2021 «» 15:09
 * Class «» CloudNetV2Adapter
 **/

public class CloudNetV2 implements CloudAdapter {
    @Override
    public int getPlayersInGroup(String name) {
        int i = 0;
        for (ServerInfo server : CloudAPI.getInstance().getServers(name)) {
            i += server.getOnlineCount();
        }
        return i;
    }

    @Override
    public List<String> getAllLobbies(String name) {
        List<String> list = new ArrayList<>();
        for (ServerInfo server : CloudAPI.getInstance().getServers(name)) {
            list.add(server.getServiceId().getServerId());
        }
        Collections.sort(list);
        return list;
    }

    @Override
    public int getPlayersOnServer(String name) {
        ServerInfo info = CloudAPI.getInstance().getServerInfo(name);
        if(info == null) return -1;
        return info.getOnlineCount();
    }

    @Override
    public String getServerState(String name) {
        ServerInfo info = CloudAPI.getInstance().getServerInfo(name);
        return info.getServerState().toString();
    }
}
