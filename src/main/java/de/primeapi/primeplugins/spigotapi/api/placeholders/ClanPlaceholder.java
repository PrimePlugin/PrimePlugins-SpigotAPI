package de.primeapi.primeplugins.spigotapi.api.placeholders;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.api.plugins.clan.ClanAPI;
import de.primeapi.primeplugins.spigotapi.managers.config.configs.CoreConfig;
import de.primeapi.primeplugins.spigotapi.managers.messages.CoreMessage;
import de.primeapi.primeplugins.spigotapi.sql.clan.SQLClan;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

/**
 * @author Lukas S. PrimeAPI
 * created on 10.06.2021
 * crated for PrimePlugins
 */
public class ClanPlaceholder extends PlaceholderExpansion {
	@Override
	public String getIdentifier() {
		return "clan";
	}

	@Override
	public String getAuthor() {
		return "PrimeAPI";
	}

	@Override
	public String getVersion() {
		return PrimeCore.getInstance().getDescription().getVersion();
	}


	@Override
	public String onPlaceholderRequest(Player player, String params) {
		if (ClanAPI.getInstance().isOnline()) {
			SQLClan clan = ClanAPI.getInstance().getClanFromPlayer(new PrimePlayer(player)).complete();
			if (clan == null) {
				if (params.equalsIgnoreCase("tag_formatted")) {
					return CoreConfig.getInstance().getString("clanplaceholder.formattedTag.noClan");
				}
				return CoreMessage.CLANPLACEHOLDER_NOCLAN.getContent();
			}
			switch (params.toLowerCase()) {
				case "name": {
					return clan.getRealname().complete();
				}
				case "tag": {
					return clan.getTag().complete();
				}
				case "tag_formatted": {
					return CoreConfig.getInstance()
					                 .getString("clanplaceholder.formattedTag.format")
					                 .replaceAll("%tag%", clan.getTag().complete());
				}
				case "count": {
					return String.valueOf(clan.getMembers().complete().size());
				}
			}
		}
		return null;
	}
}
