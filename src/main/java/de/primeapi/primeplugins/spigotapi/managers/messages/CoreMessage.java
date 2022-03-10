package de.primeapi.primeplugins.spigotapi.managers.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum CoreMessage {

	PREFIX("§bSystem §7●", false),
	NO_PERMS("§c Die fehlt die Berechtigung §8'§e%permission%§8'§c!", true),

	COINS_AMOUNT("§7 Du hast §e%coins% Coins§7!", true),
	COINS_USAGE("§7 Verwendung: §e/coins <set/add/remove/see>", true),
	COINS_NONUMBER("§4 Fehler: §cDer angegebene Wert ist keine Zahl!", true),
	COINS_PLAYERNOTFOUND("§4 Fehler: §cDer angegebene Spieler wurde nicht in der Datenbank gefunden!", true),
	COINS_ADD_USAGE("§7 Verwendung: §e/coins add <Spieler> <coins>", true),
	COINS_ADD_SUCCESS("§7 Du hast den Spieler §e%player% §aerfolgreich §6%coins% §7hinzugefügt!", true),
	COINS_SET_USAGE("§7 Verwendung: §e/coins set <Spieler> <coins>", true),
	COINS_SET_SUCCESS("§7 Du hast die Coins von §e%player% §aerfolgreich §7auf §6%coins% Coins §7gesetzt!", true),
	COINS_REMOVE_USAGE("§7 Verwendung: §e/coins remove <Spieler> <coins>", true),
	COINS_REMOVE_SUCCESS("§7 Du hast den Spieler §e%player% §aerfolgreich §6%coins% §7entfernt!", true),
	COINS_SEE_USAGE("§7 Verwendung: §e/coins see <Spieler>", true),
	COINS_SEE_SUCCESS("§7 Der Spieler §e%player% §7hat §6%coins% Coins§7!", true),

	AFK_ON("§7 Du wurdest als §cAFK§7 markiert!", true),
	AFK_OFF("§7 Du bist nun §anicht mehr §7als AFK markiert!", true),

	CLANPLACEHOLDER_NOCLAN("Kein Clan", false),

	ACCEPTGUI_ACCEPT("§aAkzeptieren", false),
	ACCEPTGUI_DECLINE("§cAbbrechen", false),

	PLACEHOLDER("DO NOT TOUCH", false);

	String path;
	@Setter
	String content;
	Boolean prefix;

	CoreMessage(String content, Boolean prefix) {
		this.content = content;
		this.prefix = prefix;
		this.path = this.toString().replaceAll("_", ".").toLowerCase();
	}

	CoreMessage(String path, String content, Boolean prefix) {
		this.content = content;
		this.prefix = prefix;
		this.path = path;
	}


	public CoreMessage replace(String key, String value) {
		if (!key.startsWith("%")) {
			key = "%" + key + "%";
		}
		String s = getContent().replaceAll(key, value);
		PLACEHOLDER.setContent(s);
		return PLACEHOLDER;
	}

	public CoreMessage replace(String key, Object value) {
		return replace(key, String.valueOf(value));
	}
}
