package de.primeapi.primeplugins.spigotapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum PlayerSetting {
    FRIEND_REQEUSTS(1),
    FRIEND_JUMP(1),
    FRIEND_MSG(1),
    FRIEND_NOTIFY(1),

    LOBBY_SAVELOCATION(1);


    int standartValue;
}
