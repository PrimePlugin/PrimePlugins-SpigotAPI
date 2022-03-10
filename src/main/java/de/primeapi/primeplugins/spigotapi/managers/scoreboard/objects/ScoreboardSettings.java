package de.primeapi.primeplugins.spigotapi.managers.scoreboard.objects;

import org.bukkit.entity.Player;

import java.util.List;


public interface ScoreboardSettings {


	String getTitle();

	List<String> apply(Player p);

}
