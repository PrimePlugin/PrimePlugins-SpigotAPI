package de.primeapi.primeplugins.spigotapi.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ColorCodes {

	BLACK('0', 15),
	DARK_BLUE('1', 11),
	DARK_GREEN('2', 13),
	DARK_AQUA('3', 9),
	DARK_RED('4', 14),
	DARK_PURPLE('5', 10),
	GOLD('6', 1),
	GRAY('7', 8),
	DARK_GRAY('8', 7),
	BLUE('9', 11),
	GREEN('a', 5),
	AQUA('b', 3),
	RED('c', 14),
	LIGHT_PURPLE('d', 2),
	YELLOW('e', 3),
	WHITE('f', 0),
	MAGIC('k', 0),
	BOLD('l', 0),
	STRIKETHROUGH('m', 0),
	UNDERLINE('n', 0),
	ITALIC('o', 0),
	RESET('r', 0);

	char code;
	byte woolSubID;

	ColorCodes(char c, int woolID) {
		this(c, (byte) woolID);
	}

	public static ColorCodes fromCode(char c) {
		return Arrays.stream(ColorCodes.values())
		             .filter(colorCodes -> colorCodes.getCode() == c)
		             .findFirst()
		             .orElse(RESET);
	}

	public static ColorCodes fromCode(String s) {
		String replace = s.replace("&", "").replace("§", "");
		if (replace.length() >= 1) {
			return fromCode(replace.charAt(0));
		}
		return RESET;
	}

}
