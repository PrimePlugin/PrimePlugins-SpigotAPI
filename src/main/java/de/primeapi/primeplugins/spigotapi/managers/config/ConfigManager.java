package de.primeapi.primeplugins.spigotapi.managers.config;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ConfigManager {


    private List<Config> registeredConfigs;

    public ConfigManager() {
        registeredConfigs = new ArrayList<>();


        {
            File ord = new File("plugins/primeplugins");
            if(!ord.exists()) ord.mkdir();
        }
    }

    public void registerConfig(Config config){
        registeredConfigs.add(config);
        PrimeCore.getInstance().getCoreLogger().sendInfo("Config '" + config.getName() + "' loaded");
    }

    public void generateDirs(String path){
        String[] dirs = path.split("/");
        String curpath = dirs[0];
        for (int i = 1; i < dirs.length - 1; i++) {
            File ord = new File(curpath);
            if(!ord.exists()) ord.mkdir();
            curpath+= "/" + dirs[i];
        }
    }

}
