package de.primeapi.primeplugins.spigotapi.managers.scoreboard.objects;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.managers.config.configs.CoreConfig;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DefaultScoreboard implements ScoreboardSettings {

	@Override
	public String getTitle() {
		return ChatColor.translateAlternateColorCodes(
				'&', CoreConfig.getInstance().getString("scoreboard.default.title"));
	}

	@Override
	public List<String> apply(Player p) {
		List<String> list = CoreConfig.getInstance().getStringList("scoreboard.default.content");
		List<String> returnValue = new ArrayList<>();
		for (String s :
				list) {
			returnValue.add(ChatColor.translateAlternateColorCodes('&', PrimeCore.getInstance()
			                                                                     .getPlaceholderAPIManager()
			                                                                     .replace(p, s)));
		}
		return returnValue;
	}
}
