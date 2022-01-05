package de.primeapi.primeplugins.spigotapi.managers.config;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ConfigManager {


    private final List<Config> registeredConfigs;

    public ConfigManager() {
        registeredConfigs = new ArrayList<>();


        {
            File ord = new File("plugins/primeplugins");
            if (!ord.exists()) ord.mkdir();
        }
    }

    public void registerConfig(Config config) {
        registeredConfigs.add(config);
        PrimeCore.getInstance().getLogger().info("Config '" + config.getName() + "' loaded");
    }

    public List<File> getAllFiles() {
        return registeredConfigs.stream().map(Config::getFile).collect(Collectors.toList());
    }

    public void generateDirs(String path) {
        String[] dirs = path.split("/");
        String curpath = dirs[0];
        for (int i = 1; i < dirs.length - 1; i++) {
            File ord = new File(curpath);
            if (!ord.exists()) ord.mkdir();
            curpath += "/" + dirs[i];
        }
    }

}
