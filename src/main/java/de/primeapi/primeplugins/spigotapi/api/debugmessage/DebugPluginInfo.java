package de.primeapi.primeplugins.spigotapi.api.debugmessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class DebugPluginInfo {

    String name;
    String version;
    String author;

    public static List<DebugPluginInfo> getPluginInfos() {
        return Arrays.stream(Bukkit.getPluginManager().getPlugins())
                .filter(plugin -> !plugin.getDescription().getAuthors().contains("PrimeAPI"))
                .map(plugin -> {
                    try {
                        return new DebugPluginInfo(
                                plugin.getDescription().getName(),
                                plugin.getDescription().getVersion(),
                                plugin.getDescription().getAuthors().get(0)
                        );
                    }catch (Exception ex){
                        return null;
                    }
                })
                .collect(Collectors.toList())
                ;
    }


}
