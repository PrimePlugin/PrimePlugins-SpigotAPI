package de.primeapi.primeplugins.spigotapi.api.plugins.clan;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.sql.DatabaseTask;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;
import de.primeapi.primeplugins.spigotapi.sql.clan.SQLClan;
import de.primeapi.primeplugins.spigotapi.sql.clan.SQLClanInvitation;
import de.primeapi.primeplugins.spigotapi.sql.clan.SQLPlayerAllocation;
import de.primeapi.util.sql.queries.Retriever;
import lombok.Getter;
import lombok.NonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

@Getter
public class ClanAPI {

    private static ClanAPI instance;
    boolean online;

    public ClanAPI() {
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
        if (online) {
            PrimeCore.getInstance().getLogger().log(Level.INFO, "ClanAPI wurde geladen");
        } else {
            PrimeCore.getInstance().getLogger().log(Level.INFO, "ClanAPI wurde NICHT geladen");
        }
    }

    public static ClanAPI getInstance() {
        return instance;
    }

    /**
     * @param player {@link SQLPlayer} of which the clan should be returned
     * @return The {@link SQLClan} of the player <br> returns null if player has not joined any clan
     */
    @Nullable
    public Retriever<SQLClan> getClanFromPlayer(@Nonnull SQLPlayer player) {
        if (!online) return null;
        return SQLPlayerAllocation.fromPlayer(player)
                           .map(sqlPlayerAllocation -> sqlPlayerAllocation == null
                                                       ? null
                                                       : sqlPlayerAllocation.getClan().complete());
    }

    public Retriever<String> getClanColorFromPlayer(@NonNull SQLPlayer sqlPlayer) {
        return SQLPlayerAllocation.fromPlayer(sqlPlayer).map(sqlPlayerAllocation -> {
            if (sqlPlayerAllocation == null) return "&e";
            return sqlPlayerAllocation.getClan().complete().getColor().complete();
        });
    }

    /**
     * Inserts a clan into the Database
     *
     * @param name The Name of the Clan
     * @param tag  The Clantag of the Clan
     * @return The generated CLan
     */
    public DatabaseTask<SQLClan> createClan(@NonNull String name, @NonNull String tag) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            if (!online) return null;
            return SQLClan.create(name, tag).complete();
        }));
    }

    /**
     * @param name The name of the clan
     * @return The {@link SQLClan} with the name <br> returns null if no clan with this
     * name exists
     */
    public DatabaseTask<SQLClan> getClanFromName(@NonNull String name) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            if (!online) return null;
            return SQLClan.fromName(name).complete();
        }));
    }

    /**
     * @param tag The tag of the clan
     * @return The {@link SQLClan} with the name <br> returns null if no clan with this
     * name exists
     */
    public DatabaseTask<SQLClan> getClanFromTag(@NonNull String tag) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            if (!online) return null;
            return SQLClan.fromTag(tag).complete();
        }));
    }

    /**
     * Creates a clan invite
     *
     * @param player The {@link SQLPlayer} who should be invited
     * @param clan   The {@link SQLClan} to wich the player should be invited
     * @return The generated {@link SQLClanInvitation invite}
     */
    public DatabaseTask<SQLClanInvitation> createInvite(@NonNull SQLPlayer player, @NonNull SQLClan clan) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            if (!online) return null;
            return SQLClanInvitation.create(player, clan).complete();
        }));
    }

    /**
     * @param player The {@link SQLPlayer} who is being invited
     * @param clan   The {@link SQLClan} to wich the player is being invited to
     * @return A {@link SQLClanInvitation} <br> returns null if the player is not invited to the clan
     */
    public DatabaseTask<SQLClanInvitation> getInvite(@NonNull SQLPlayer player, @NonNull SQLClan clan) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            if (!online) return null;
            return SQLClanInvitation.fromPlayer(player, clan).complete();
        }));
    }

    /**
     * @param player The {@link SQLPlayer} of which all Invites should be collected
     * @return a {@link List} of {@link SQLClanInvitation} to which the player is invited to
     */
    public DatabaseTask<List<SQLClanInvitation>> getInvite(@NonNull SQLPlayer player) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            if (!online) return null;
            return SQLClanInvitation.fromPlayer(player).complete();
        }));
    }

    /**
     * Inserts a membership into the database
     *
     * @param player The {@link SQLPlayer} who is the member
     * @param clan   The {@link SQLClan} the player is now member in
     * @param rank   The rank as int of the player; 0 equals normal member
     * @return The generated {@link SQLPlayerAllocation}
     */
    public DatabaseTask<SQLPlayerAllocation> addMemberToClan(@NonNull SQLPlayer player, @NonNull SQLClan clan, @NonNull Integer rank) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            if (!online) return null;
            return SQLPlayerAllocation.create(player, clan, rank).complete();
        }));
    }

    /**
     * @param player the {@link SQLPlayer}
     * @return The {@link SQLClan} the player is member in <br> returns null if player is not part of any clan
     */
    public DatabaseTask<SQLPlayerAllocation> getMembership(SQLPlayer player) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            if (!online) return null;
            return SQLPlayerAllocation.fromPlayer(player).complete();
        }));
    }

}
