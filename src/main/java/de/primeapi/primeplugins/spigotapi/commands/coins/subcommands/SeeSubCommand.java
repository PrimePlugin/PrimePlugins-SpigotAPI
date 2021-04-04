package de.primeapi.primeplugins.spigotapi.commands.coins.subcommands;

import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.api.SubCommand;
import de.primeapi.primeplugins.spigotapi.managers.messages.CoreMessage;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;

public class SeeSubCommand extends SubCommand {
    public SeeSubCommand() {
        super("permission");
    }

    @Override
    public boolean execute(PrimePlayer p, String[] args) {
        if (!checkPermission(p)) {
            return true;
        }
        if (args.length != 2) {
            p.sendMessage(CoreMessage.COINS_REMOVE_USAGE);
            return true;
        }
        SQLPlayer target = SQLPlayer.loadPlayerByName(args[1]);
        if (target == null) {
            p.sendMessage(CoreMessage.COINS_PLAYERNOTFOUND);
            return true;
        }
        p.sendMessage(CoreMessage.COINS_SEE_SUCCESS.replace("player", target.getRealName()).replace("coins", target.getCoins()));
        return true;
    }
}
