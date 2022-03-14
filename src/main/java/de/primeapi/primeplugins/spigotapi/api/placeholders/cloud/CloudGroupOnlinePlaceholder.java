package de.primeapi.primeplugins.spigotapi.api.placeholders.cloud;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.cache.Cache;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class CloudGroupOnlinePlaceholder extends PlaceholderExpansion {
	@Override
	public String getIdentifier() {
		return "cloudgrouponline";
	}

	@Override
	public String getAuthor() {
		return "PrimeAPI";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	private Cache<String, String> cache = new Cache<>(1000);

	@Override
	public String onPlaceholderRequest(Player p, String params) {
		if(cache.getCachedValue(params) != null){
			return cache.getCachedValue(params);
		}else {
			String s = String.valueOf(PrimeCore.getInstance().getCloudManager().getPlayersInGroup(params));
			cache.cacheEntry(params,s);
			return s;
		}
	}
}
