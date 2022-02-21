package de.primeapi.primeplugins.spigotapi.api.command;

import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nullable;

@AllArgsConstructor
@Getter
public abstract class SubCommand {

    @Nullable
    public String permission;

    public abstract boolean execute(PrimePlayer p, String[] args);


    public boolean checkPermission(PrimePlayer p) {
        if (permission == null) return true;
        if (!p.hasPermission(permission)) {
            p.sendNoPerm(permission);
            return false;
        }
        return true;
    }

}
