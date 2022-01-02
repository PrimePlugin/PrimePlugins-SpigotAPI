package de.primeapi.primeplugins.spigotapi.managers.cloud.adapter.version;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty;
import de.dytanic.cloudnet.ext.bridge.ServiceInfoSnapshotUtil;
import de.primeapi.primeplugins.spigotapi.managers.cloud.adapter.CloudAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jonas | Exceptionpilot#5555
 * Created on 28.05.2021 «» 15:10
 * Class «» CloudNetV3
 **/

public class CloudNetV3 implements CloudAdapter {

    @Override
    public int getPlayersInGroup(String name) {
        return ServiceInfoSnapshotUtil.getTaskOnlineCount(name);
    }

    @Override
    public List<String> getAllLobbies(String name) {
        List<String> list = new ArrayList<>();
        CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServices(name).forEach(serviceInfoSnapshot -> {
            list.add(serviceInfoSnapshot.getName());
        });
        return list;
    }

    @Override
    public int getPlayersOnServer(String name) {
        return ServiceInfoSnapshotUtil.getOnlineCount(CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName(name));
    }

    public String getServerState(String name){
        return CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName(name).getProperty(BridgeServiceProperty.STATE).orElse(null);
    }

}
