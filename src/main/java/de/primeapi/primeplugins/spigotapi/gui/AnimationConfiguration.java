package de.primeapi.primeplugins.spigotapi.gui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Sound;

@AllArgsConstructor
@Getter
@Setter
public class AnimationConfiguration {


	GUIBuilder.Animation animation;
	int tickSpeed;
	Sound tickSound;
	Sound finalSound;


}
