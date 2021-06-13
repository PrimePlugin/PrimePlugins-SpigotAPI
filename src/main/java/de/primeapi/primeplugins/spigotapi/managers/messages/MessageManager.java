package de.primeapi.primeplugins.spigotapi.managers.messages;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessageManager {

    private YamlConfiguration cfg;
    private File file;

    @SneakyThrows
    public MessageManager(){
        file = new File("plugins/primeplugins/core/messages.yml");
        file.getParentFile().mkdirs();
        if(!file.exists()) file.createNewFile();
        reload();
    }


    public void reload(){
        cfg = YamlConfiguration.loadConfiguration(file);
        PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
            int i = 0;
            for (CoreMessage message : CoreMessage.values()) {
                if (cfg.contains(message.getPath())) {
                    message.setContent(ChatColor.translateAlternateColorCodes('&', cfg.getString(message.getPath())).replaceAll("%prefix%", CoreMessage.PREFIX.getContent()).replaceAll("<br>", "\n"));
                } else {
                    String s = (message.getPrefix() ? "%prefix%" : "") + message.getContent().replaceAll("§", "&");
                    cfg.set(message.getPath(), s);
                    i++;
                    message.setContent(ChatColor.translateAlternateColorCodes('&', s.replaceAll("%prefix%", CoreMessage.PREFIX.getContent())));
                }
            }
            PrimeCore.getInstance().getLogger().info("Es wurde(n) " + i + " neue Nachricht(en) in die messages.yml eingefügt!");
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
