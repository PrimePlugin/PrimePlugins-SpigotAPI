package de.primeapi.primeplugins.spigotapi.commands;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.api.RestPlugin;
import de.primeapi.primeplugins.spigotapi.api.debugmessage.DebugMessage;
import de.primeapi.primeplugins.spigotapi.managers.rest.PluginInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.net.URISyntaxException;

public class PrimeCoreCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            return true;
        }
        PrimePlayer p = new PrimePlayer((Player) commandSender);
        if (args.length == 0) {
            p.thePlayer().sendMessage("PrimeCore v" + PrimeCore.getInstance().getDescription().getVersion() + " by PrimeAPI");
            p.thePlayer().sendMessage("Website: primeapi.de");
            p.thePlayer().sendMessage("Verwendung: /primecore <reload>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "debug": {
                // /priemcore debug <secret>
                if (args.length < 2) {
                    p.thePlayer().sendMessage("§7Verwende: §e/primecore debug <Secret>");
                    return true;
                }
                p.thePlayer().sendMessage("§7Sende Daten...");
                DebugMessage.send(args[1], p.thePlayer());
                return true;
            }
            case "reload": {
                if (args.length < 2) {
                    p.thePlayer().sendMessage("§7Benutze: §e/primecore reload <all/prefix/scoreboard>");
                    return true;
                }

                switch (args[1].toLowerCase()) {
                    case "scoreboard": {
                        if (!p.checkPermission("primecore.reload.scoreboard")) {
                            return true;
                        }
                        PrimeCore.getInstance().getScoreboardManager().sendScoreboard();
                        p.thePlayer().sendMessage("§aErfolgreich");
                        return true;
                    }
                    case "prefix": {
                        if (!p.checkPermission("primecore.reload.prefix")) {
                            return true;
                        }
                        PrimeCore.getInstance().getScoreboardManager().sendTeams();
                        p.thePlayer().sendMessage("§aErfolgreich");
                        return true;
                    }
                    case "all": {
                        if (!p.checkPermission("primecore.reload.all")) {
                            return true;
                        }
                        PrimeCore.getInstance().getScoreboardManager().sendScoreboard();
                        PrimeCore.getInstance().getScoreboardManager().sendTeams();
                        p.thePlayer().sendMessage("§aErfolgreich");
                        return true;
                    }
                    default:
                        p.thePlayer().sendMessage("§7Benutze: §e/primecore reload <all/prefix/scoreboard>");
                        return true;
                }
            }
            case "update": {
                if (!p.checkPermission("primeplugins.update")) {
                    return true;
                }
                if (args.length < 2) {
                    p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §7Benutze: §e/spigotapi update <all/[PluginName]>");
                    p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §ePluginName §8| §7Aktuelle Version §8| §bNeueste Version ");
                    for (RestPlugin plugin : PrimeCore.getInstance().getRestManager().getPlugins()) {
                        PluginInfo info = plugin.getPluginInfo();
                        String currVersion = plugin.getPlugin().getDescription().getVersion();
                        if (info.isNeverVersion(currVersion)) {
                            p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §e" + plugin.getName() + " §8| §7" + currVersion + "§8 | §b" + info.getVersion() + " §c§l✖");
                        } else {
                            p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §e" + plugin.getName() + " §8| §7" + currVersion + "§8 | §b" + info.getVersion() + " §a§l✔");
                        }
                    }
                    return true;
                }
                if (args[1].equalsIgnoreCase("all")) {
                    for (RestPlugin plugin : PrimeCore.getInstance().getRestManager().getPlugins()) {
                        p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §7Installiere §e" + plugin.getName() + "§7...");
                        try {
                            File f = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                            plugin.downloadLatestVersion(f.getPath());
                            p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §e" + plugin.getName() + "§7 wurde §aerfolgreich §7in die Datein §e" + f.getName() + "§7 runtergeladen!");
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                            p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §e" + plugin.getName() + "§7 konnte §cnicht §7herruntergeladen werden: §c" + e.getMessage());
                        }
                    }
                    p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §7Das Updaten aller Plugins wurde §aabgeschlossen!");

                    return true;
                } else {
                    PrimeCore.getInstance().getRestManager().getPlugins().stream().filter(plugin -> plugin.getName().equalsIgnoreCase(args[1]))
                            .forEach(plugin -> {
                                p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §7Installiere §e" + plugin.getName() + "§7...");
                                try {
                                    File f = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                                    plugin.downloadLatestVersion(f.getPath());
                                    p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §e" + plugin.getName() + "§7 wurde §aerfolgreich §7in die Datein §e" + f.getName() + "§7 runtergeladen!");
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();
                                    p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §e" + plugin.getName() + "§7 konnte §cnicht §7herruntergeladen werden: §c" + e.getMessage());
                                }
                            });
                }
            }
        }


        return true;
    }
}
