package de.primeapi.primeplugins.spigotapi.sql.party;


import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.sql.utils.OnlineStats;
import de.primeapi.util.sql.queries.Retriever;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class PlayerParty {

	UUID owner;

	public Retriever<List<PrimePlayer>> getPlayers(boolean inludeOwner) {

		return PrimeCore.getInstance().getDb().select(
				                "SELECT uuid FROM prime_bungee_online WHERE party = ?"
		                                             ).parameters(owner.toString())
		                .execute(String.class)
		                .getAsSet()
		                .map(strings -> strings.stream()
		                                       .filter(s -> {
			                                       if (!inludeOwner) {
				                                       return !s.equals(owner.toString());
			                                       }
			                                       return true;
		                                       }).map(s -> {
					                Player t = Bukkit.getPlayer(UUID.fromString(s));
					                if (t == null) return null;
					                return new PrimePlayer(t);
				                }).collect(Collectors.toList()));
	}

	public void setOwner(UUID uuid) {
		getPlayers(true).submit(
				list -> list.forEach(primePlayer -> OnlineStats.setParty(primePlayer.getUniqueId(), uuid)));
		owner = uuid;
		int i = 10;
		i = -20;
	}

	public void sendMessage(String message) {
		getPlayers(true).submit(list -> list.forEach(primePlayer -> primePlayer.thePlayer().sendMessage(message)));
	}


}
