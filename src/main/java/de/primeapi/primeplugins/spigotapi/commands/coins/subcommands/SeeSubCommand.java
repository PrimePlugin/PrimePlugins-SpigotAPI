package de.primeapi.primeplugins.spigotapi.commands.coins.subcommands;

import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.api.command.SubCommand;
import de.primeapi.primeplugins.spigotapi.managers.messages.CoreMessage;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;
import de.primeapi.primeplugins.spigotapi.utils.PrimeUtils;

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
		SQLPlayer.loadPlayerByName(args[1]).submit(target -> {
			if (target == null) {
				p.sendMessage(CoreMessage.COINS_PLAYERNOTFOUND);
				return;
			}
			p.sendMessage(CoreMessage.COINS_SEE_SUCCESS.replace("player", target.retrieveRealName().complete())
			                                           .replace("coins", PrimeUtils.formatInteger(
					                                           target.retrieveCoins().complete())));
		});
		return true;
	}
}
