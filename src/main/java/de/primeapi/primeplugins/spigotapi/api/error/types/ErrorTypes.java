package de.primeapi.primeplugins.spigotapi.api.error.types;

import lombok.Getter;

public enum ErrorTypes {

    FATAL ("§4⬛§8 ➟§7 "),
    MEDIUM("§6⬛§8 ➟§7");

    @Getter
    String s;

    ErrorTypes(String s) {
        this.s = s;
    }
}
