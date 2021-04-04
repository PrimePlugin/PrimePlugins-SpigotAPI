package de.primeapi.primeplugins.spigotapi.commands.coins.subcommands;

import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.api.SubCommand;
import de.primeapi.primeplugins.spigotapi.managers.messages.CoreMessage;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;

public class AddSubCommand extends SubCommand {

    public AddSubCommand() {
        super("primecore.coins.add");
    }

    @Override
    public boolean execute(PrimePlayer p, String[] args) {

        if (!checkPermission(p)) {
            return true;
        }
        if (args.length != 3) {
            p.sendMessage(CoreMessage.COINS_ADD_USAGE);
            return true;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (Exception ex) {
            p.sendMessage(CoreMessage.COINS_NONUMBER);
            return true;
        }
        SQLPlayer target = SQLPlayer.loadPlayerByName(args[1]);
        if (target == null) {
            p.sendMessage(CoreMessage.COINS_PLAYERNOTFOUND);
            return true;
        }
        target.addCoins(amount);
        p.sendMessage(CoreMessage.COINS_ADD_SUCCESS.replace("player", target.getRealName()).replace("coins", amount));
        return true;
    }
}
