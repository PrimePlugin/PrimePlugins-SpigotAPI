package de.primeapi.primeplugins.spigotapi.api;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.sql.DatabaseTask;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;
import de.primeapi.primeplugins.spigotapi.sql.clan.SQLClan;
import de.primeapi.primeplugins.spigotapi.sql.clan.SQLPlayerAllocation;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

@Getter
public class ClanAPI {

    private static ClanAPI instance;

    public static ClanAPI getInstance() {
        return instance;
    }

    boolean online;
    public ClanAPI(){
        instance = this;
        online = false;
        try {
            DatabaseMetaData md = PrimeCore.getInstance().getConnection().getMetaData();
            ResultSet rs = md.getTables(null, null, "prime_clan_clans", null);
            online = rs.next();
            rs.close();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        if(online){
            PrimeCore.getInstance().getLogger().log(Level.INFO, " ClanAPI wurde geladen");
        }else {
            PrimeCore.getInstance().getLogger().log(Level.INFO, " ClanAPI wurde NICHT geladen");
        }
    }

    @Nullable
    public DatabaseTask<SQLClan> getClanFromPlayer(@Nonnull SQLPlayer p){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            if(!online) return null;
            SQLPlayerAllocation allocation = SQLPlayerAllocation.fromPlayer(p).complete();
            if(allocation == null) return null;
            return allocation.getClan().complete();
        }));
    }



}
