package de.primeapi.primeplugins.spigotapi.managers.scoreboard.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@Getter
public class ScoreboradTeam {

	public final Player player;
	public final String prefix;
	public final String suffix;
	public final String color;

}
