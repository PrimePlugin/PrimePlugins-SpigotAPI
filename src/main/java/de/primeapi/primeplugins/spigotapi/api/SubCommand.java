package de.primeapi.primeplugins.spigotapi.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

@AllArgsConstructor @Getter
public abstract class SubCommand {

    @Nullable
    public String permission;
    public abstract boolean execute(PrimePlayer p, String[] args);


    public boolean checkPermission(PrimePlayer p){
        if(permission == null) return true;
        if(!p.hasPermission(permission)){
            p.sendNoPerm(permission);
            return false;
        }
        return true;
    }

}
