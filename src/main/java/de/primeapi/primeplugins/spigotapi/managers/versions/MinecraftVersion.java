package de.primeapi.primeplugins.spigotapi.managers.versions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum MinecraftVersion {
    V1_8("1.8", 0),
    V1_9("1.9", 1),
    V1_10("1.10", 2),
    V1_11("1.11", 3),
    V1_12("1.12", 4),
    V1_13("1.13", 5),
    V1_14("1.14", 6),
    V1_15("1.15", 7),
    V1_16("1.16", 8),
    V1_17("1.17", 9),
    V1_18("1.18", 10),
    V_OTHER("Undefined", 100)
    ;

    String name;
    int power;
    public Boolean isHigherThan(MinecraftVersion v){
        return getPower() > v.getPower();
    }
    public Boolean isHigherEqualThan(MinecraftVersion v){
        return getPower() >= v.getPower();
    }
}
