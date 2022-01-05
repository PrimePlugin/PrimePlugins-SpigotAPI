package de.primeapi.primeplugins.spigotapi.managers.commands;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CommandsManager {


    public ArrayList<Command> registeredCommands = new ArrayList<>();

    public CommandsManager() {
        File file = new File("plugins/primeplugins/core/commands.yml");
        PrimeCore.getInstance().getConfigManager().generateDirs("plugins/primeplugins/core/commands.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        String active = "";
        int activeCount = 0;
        String deactive = "";
        int deactiveCount = 0;

        for (Command c : Command.values()) {
            boolean b;

            if (cfg.contains("commands." + c.getName())) {
                b = cfg.getBoolean("commands." + c.getName());
            } else {
                b = true;
                PrimeCore.getInstance().getLogger().info("Command '" + c.getName() + "' wurde eingetragen!");
                cfg.set("commands." + c.getName(), true);
            }

            if (b) {
                PrimeCore.getInstance().getCommand(c.getName()).setExecutor(c.getCommand());
                if (c.getCompleter() != null) {
                    PrimeCore.getInstance().getCommand(c.getName()).setTabCompleter(c.getCompleter());
                }
                registeredCommands.add(c);
                active += c.getName() + ", ";
                activeCount++;
            } else {
                deactive += c.getName() + ", ";
                deactiveCount++;
            }
        }
        PrimeCore.getInstance().getLogger().info("Aktivierte Commands (" + activeCount + "): " + active);
        PrimeCore.getInstance().getLogger().info("Deaktivierte Commands (" + deactiveCount + "): " + deactive);


        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
