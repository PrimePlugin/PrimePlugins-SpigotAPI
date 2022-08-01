package de.primeapi.primeplugins.spigotapi.commands;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.api.error.TroubleShooter;
import de.primeapi.primeplugins.spigotapi.api.plugin.RestPlugin;
import de.primeapi.primeplugins.spigotapi.managers.debugmessage.DebugMessage;
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
            case "help": {
                TroubleShooter.getInstance().sendCheckingData(p.thePlayer());
                return true;
            }
            case "support": {
                p.thePlayer().sendMessage("§aSupport §7● []-----------------------------[]");
                p.thePlayer().sendMessage("§aSupport §7● §a1§8 - §7Lese dir das Wiki durch");
                p.thePlayer().sendMessage("§aSupport §7●§e https://wiki.primeapi.de/");
                p.thePlayer().sendMessage("§aSupport §7● §7");
                p.thePlayer().sendMessage("§aSupport §7● §a2§8 - §7Schaue dir die Youtube-Videos an");
                p.thePlayer().sendMessage("§aSupport §7●§e https://www.youtube.com/watch?v=cVVJ5oIDoU4&t=0s");
                p.thePlayer().sendMessage("§aSupport §7●§e https://www.youtube.com/watch?v=aRNVOtlqeho");
                p.thePlayer().sendMessage("§aSupport §7● §7");
                p.thePlayer().sendMessage("§aSupport §7● §a3§8 - §7Erstelle auf unserem Discord ein Ticket");
                p.thePlayer().sendMessage("§aSupport §7● §a3.1§8 - §7Sende deine Logs Bungee/Spigot");
                p.thePlayer().sendMessage("§aSupport §7● §a3.2§8 - §7Warte bis ein Teammitglied antwortet");
                p.thePlayer().sendMessage("§aSupport §7●§e https://discord.gg/XAGYh3BUkY");
                p.thePlayer().sendMessage("§aSupport §7● §7");
                p.thePlayer().sendMessage("§aSupport §7● []-----------------------------[]");
                return true;
            }
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
                            p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §e" + plugin.getName() + " §8| §7" + currVersion + "§8 | §b" + info.getLatest() + " §c§l✖");
                        } else {
                            p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §e" + plugin.getName() + " §8| §7" + currVersion + "§8 | §b" + info.getLatest() + " §a§l✔");
                        }
                    }
                    return true;
                }
                if (args[1].equalsIgnoreCase("all")) {
                    for (RestPlugin plugin : PrimeCore.getInstance().getRestManager().getPlugins()) {
                        p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §7Installiere §e" + plugin.getName() + "§7...");
                        try {
                            File f = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                            if (plugin.downloadLatestVersion(f.getPath())) {
                                p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §e" + plugin.getName() + "§7 wurde " + "§aerfolgreich " + "§7in" + " die Datein §e" + f.getName() + "§7 runtergeladen!");
                            } else {
                                p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §e" + plugin.getName() + "§7 konnte §cnicht " + "§7herruntergeladen" + " werden");
                            }
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                            p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §e" + plugin.getName() + "§7 konnte §cnicht " + "§7herruntergeladen" + " werden: §c" + e.getMessage());
                        }
                    }
                    p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §7Das Updaten aller Plugins wurde §aabgeschlossen!");

                    return true;
                } else {
                    PrimeCore.getInstance().getRestManager().getPlugins().stream().filter(plugin -> plugin.getName().equalsIgnoreCase(args[1])).forEach(plugin -> {
                        p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §7Installiere §e" + plugin.getName() + "§7...");
                        try {
                            File f = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
                            if (plugin.downloadLatestVersion(f.getPath())) {
                                p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §e" + plugin.getName() + "§7 wurde " + "§aerfolgreich §7in" + " die Datein §e" + f.getName() + "§7 runtergeladen!");
                            } else {
                                p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §e" + plugin.getName() + "§7 konnte §cnicht " + "§7herruntergeladen" + " werden");
                            }
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                            p.thePlayer().sendMessage("§8[§cSpigotAPI§8] §e" + plugin.getName() + "§7 konnte §cnicht " + "§7herruntergeladen werden: §c" + e.getMessage());
                        }
                    });
                }
            }
        }


        return true;
    }
}
