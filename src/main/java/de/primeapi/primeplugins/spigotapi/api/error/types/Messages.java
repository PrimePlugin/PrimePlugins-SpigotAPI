package de.primeapi.primeplugins.spigotapi.api.error.types;

import lombok.Getter;

@Getter
public enum Messages {

    PLACEHOLDER_API("Ohne die PlaceholderAPI sind einige Funktionen eingeschränkt", "https://wiki.primeapi.de/core-api/placeholder-core"),
    MYSQL_NOT_CONNECTED("Die MySQL Verbindung war nicht erfolgreich", "https://wiki.primeapi.de/core-api/loslegen");

    String s;
    String link;

    Messages(String s, String link) {
        this.s = s;
        this.link = link;
    }
}
