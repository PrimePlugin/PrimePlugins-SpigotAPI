package de.primeapi.primeplugins.spigotapi.commands.coins;

import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.commands.coins.subcommands.AddSubCommand;
import de.primeapi.primeplugins.spigotapi.commands.coins.subcommands.RemoveSubCommand;
import de.primeapi.primeplugins.spigotapi.commands.coins.subcommands.SeeSubCommand;
import de.primeapi.primeplugins.spigotapi.commands.coins.subcommands.SetSubCommand;
import de.primeapi.primeplugins.spigotapi.managers.messages.CoreMessage;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsCommand implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player)) {
            return false;
        }
        PrimePlayer p = new PrimePlayer((Player) commandSender);

        if(args.length == 0){
            p.sendMessage(CoreMessage.COINS_AMOUNT.replace("coins", p.getCoins()));
            return true;
        }

        switch (args[0].toLowerCase()){
            case "help":
                p.sendMessage(CoreMessage.COINS_USAGE);
                break;
            case "add":
            {
                return new AddSubCommand().execute(p, args);
            }
            case "set":
            {
                return new SetSubCommand().execute(p, args);
            }
            case "remove":
            {
                return new RemoveSubCommand().execute(p, args);
            }
            case "see":
            case "get":
            {
                return new SeeSubCommand().execute(p, args);
            }
            default:
                p.sendMessage(CoreMessage.COINS_USAGE);

        }



        return true;
    }
}
