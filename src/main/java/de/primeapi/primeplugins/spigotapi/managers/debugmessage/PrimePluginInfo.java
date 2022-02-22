package de.primeapi.primeplugins.spigotapi.managers.debugmessage;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.managers.config.configs.CoreConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public class PrimePluginInfo {
    private static ArrayList<CompletableFuture<PrimePluginInfo>> registeredPlugins;

    static {
        registeredPlugins = new ArrayList<>();
        registerPlugin(
                CompletableFuture.supplyAsync(() -> {
                    String name = "SpigotCore";
                    String version = PrimeCore.getInstance().getDescription().getVersion();
                    HashMap<String, Object> properties = new HashMap<>();
                    properties.put("Scoreboard", CoreConfig.getInstance().getBoolean("scoreboard.use"));
                    properties.put("Prefix", CoreConfig.getInstance().getBoolean("prefix.use"));
                    properties.put("ChatFormatter", CoreConfig.getInstance().getBoolean("chatformat.use"));
                    properties.put("AutoAFK", CoreConfig.getInstance().getBoolean("autoafk.use"));
                    return new PrimePluginInfo(name, null, version, properties);
                })
        );
    }

    String name;
    String license;
    String version;
    HashMap<String, Object> properties;

    public static void registerPlugin(CompletableFuture<PrimePluginInfo> plugin) {
        registeredPlugins.add(plugin);
    }

    public static List<PrimePluginInfo> getPlugins() {
        return registeredPlugins.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

}
