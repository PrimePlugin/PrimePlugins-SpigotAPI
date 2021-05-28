package de.primeapi.primeplugins.spigotapi.managers.cloud.types;

import de.primeapi.primeplugins.spigotapi.managers.cloud.adapter.CloudAdapter;
import de.primeapi.primeplugins.spigotapi.managers.cloud.adapter.version.CloudNetV2;
import de.primeapi.primeplugins.spigotapi.managers.cloud.adapter.version.CloudNetV3;

/**
 * @author Jonas | Exceptionpilot#5555
 * Copyright (c) 2021 Exceptionpilot. All rights reserved.
 * Created on 28.05.2021 «» 15:03
 * Class «» CloudTypes
 **/

public enum CloudTypes {

    CLOUDNETV2("CloudNetAPI"),
    CLOUDNETV3("CloudNet-Bridge"),
    SIMPLECLOUD("SimpleCloudAPI");

    String plugin;

    CloudTypes(String plugin) {
        this.plugin = plugin;
    }

    public String getPlugin() {
        return plugin;
    }
}
