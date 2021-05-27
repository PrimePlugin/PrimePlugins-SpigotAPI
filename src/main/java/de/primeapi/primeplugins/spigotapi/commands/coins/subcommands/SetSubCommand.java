package de.primeapi.primeplugins.spigotapi.commands.coins.subcommands;

import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.api.SubCommand;
import de.primeapi.primeplugins.spigotapi.managers.messages.CoreMessage;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;

public class SetSubCommand extends SubCommand {
    public SetSubCommand() {
        super("primecore.coins.set");
    }

    @Override
    public boolean execute(PrimePlayer p, String[] args) {
        if (!checkPermission(p)) {
            return true;
        }
        if (args.length != 3) {
            p.sendMessage(CoreMessage.COINS_SET_USAGE);
            return true;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (Exception ex) {
            p.sendMessage(CoreMessage.COINS_NONUMBER);
            return true;
        }
        SQLPlayer.loadPlayerByName(args[1]).submit(target -> {
            if (target == null) {
                p.sendMessage(CoreMessage.COINS_PLAYERNOTFOUND);
                return;
            }
            target.setCoins(amount);
            p.sendMessage(CoreMessage.COINS_SET_SUCCESS.replace("player", target.retrieveRealName().complete()).replace("coins", amount));
        });
        return true;
    }
}
