package de.primeapi.primeplugins.spigotapi.api;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.sql.DatabaseTask;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;
import lombok.Getter;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.UUID;
import java.util.logging.Level;

/**
 * @author Lukas S. PrimeAPI
 * created on 10.05.2021
 * crated for PrimePlugins
 */
@Getter
public class CoinsAPI {

    private static CoinsAPI instance;
    boolean online;

    public CoinsAPI() {
        instance = this;
        online = true;
    }

    public static CoinsAPI getInstance() {
        return instance;
    }

    public void setCoins(UUID uuid, int coins){
        new SQLPlayer(uuid).setCoins(coins);
    }

    public void addCoins(UUID uuid, int coins){
        new SQLPlayer(uuid).addCoins(coins);
    }

    public DatabaseTask<Integer> getCoins(UUID uuid){
        return new SQLPlayer(uuid).retrieveCoins();
    }

}
