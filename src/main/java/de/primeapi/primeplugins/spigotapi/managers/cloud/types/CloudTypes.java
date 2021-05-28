package de.primeapi.primeplugins.spigotapi.managers.cloud.types;

/**
 * @author Jonas | Exceptionpilot#5555
 * Created on 28.05.2021 «» 15:03
 * Class «» CloudTypes
 **/

public enum CloudTypes {

    NOCLOUD("DasPlugindarfnichtgefundenwerden"),
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
