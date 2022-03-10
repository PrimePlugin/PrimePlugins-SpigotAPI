package de.primeapi.primeplugins.spigotapi.managers.chat.objects;

import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.managers.config.configs.CoreConfig;
import org.bukkit.ChatColor;

public class DefaultChatFormatter implements ChatFormatter {
	@Override
	public String formatString(PrimePlayer player, String message) {
		return CoreConfig.getInstance().getString("chatformat.default")
		                 .replaceAll("%name%", player.thePlayer().getName())
		                 .replaceAll("%message%", ChatColor.translateAlternateColorCodes('&', message));
	}
}
