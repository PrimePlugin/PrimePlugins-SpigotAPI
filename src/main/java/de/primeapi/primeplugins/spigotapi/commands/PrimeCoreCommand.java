package de.primeapi.primeplugins.spigotapi.commands;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.managers.messages.CoreMessage;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrimeCoreCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player)) {
            return true;
        }
        PrimePlayer p = new PrimePlayer((Player) commandSender);
        if(args.length == 0){
            p.thePlayer().sendMessage("PrimeCore v" + PrimeCore.getInstance().getDescription().getVersion() + " by PrimeAPI");
            p.thePlayer().sendMessage("Website: primeapi.de");
            p.thePlayer().sendMessage("Verwendung: /primecore <reload>");
            return true;
        }

        switch (args[0].toLowerCase()){
            case "reload": {
                if (args.length < 2) {
                    p.thePlayer().sendMessage("§7Benutze: §e/primecore reload <all/prefix/scoreboard>");
                    return true;
                }

                switch (args[1].toLowerCase()) {
                    case "scoreboard":
                    {
                        if(!p.checkPermission("primecore.reload.scoreboard")){
                            return true;
                        }
                        PrimeCore.getInstance().getScoreboardManager().sendScoreboard();
                        p.thePlayer().sendMessage("§aErfolgreich");
                        return true;
                    }
                    case "prefix":
                    {
                        if(!p.checkPermission("primecore.reload.prefix")){
                            return true;
                        }
                        PrimeCore.getInstance().getScoreboardManager().sendTeams();
                        p.thePlayer().sendMessage("§aErfolgreich");
                        return true;
                    }
                    case "all":
                    {
                        if(!p.checkPermission("primecore.reload.all")){
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
        }




        return true;
    }
}
