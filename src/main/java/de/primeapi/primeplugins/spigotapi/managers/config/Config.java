package de.primeapi.primeplugins.spigotapi.managers.config;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Config {

    @Getter
    private YamlConfiguration configuration;

    @Getter
    private File file;

    @Getter
    private final String name;


    public Config(String name, String pathname){
        this.name = name;
        file = new File(pathname);
        PrimeCore.getInstance().getConfigManager().generateDirs(pathname);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        configuration = YamlConfiguration.loadConfiguration(file);
            loadContent();
            try {
                save();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        reload();
    }

    public abstract void loadContent();

    public void reload(){
        configuration = YamlConfiguration.loadConfiguration(file);
    }


    public void saveAddEntry(String path, Object object){
        if(!configuration.isSet(path)) {
            if(object instanceof String){
                String s = (String) object;
                s.replaceAll("§", "&");
                configuration.set(path, s);
            }else {
                configuration.set(path, object);
            }
        }
    }
    public void saveAddEntry(String path, List<String> object){
        if(!configuration.isSet(path)) {
            List<String> list = new ArrayList<>();
            for (String s :
                    object) {
                list.add(s.replaceAll("§","&"));
            }
            configuration.set(path, list);
        }
    }

    public void save() throws IOException {
        configuration.save(file);
    }


    public String getString(String path){
        return configuration.getString(path);
    }
    public Boolean getBoolean(String path){
        return configuration.getBoolean(path);
    }
    public Integer getInt(String path){
        return configuration.getInt(path);
    }
    public List<String> getStringList(String path){
        return configuration.getStringList(path);
    }
    public List<Integer> getIntegerList(String path){
        return configuration.getIntegerList(path);
    }
}
