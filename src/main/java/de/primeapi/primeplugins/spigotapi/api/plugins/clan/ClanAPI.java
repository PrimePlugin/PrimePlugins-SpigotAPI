package de.primeapi.primeplugins.spigotapi.api.plugins.clan;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
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
    public Retriever<SQLClan> createClan(@NonNull String name, @NonNull String tag) {
        if (!online) return null;
        return SQLClan.create(name, tag);
    }

    /**
     * @param name The name of the clan
     * @return The {@link SQLClan} with the name <br> returns null if no clan with this
     * name exists
     */
    public Retriever<SQLClan> getClanFromName(@NonNull String name) {
        if (!online) return null;
        return SQLClan.fromName(name);
    }

    /**
     * @param tag The tag of the clan
     * @return The {@link SQLClan} with the name <br> returns null if no clan with this
     * name exists
     */
    public Retriever<SQLClan> getClanFromTag(@NonNull String tag) {
        if (!online) return null;
        return SQLClan.fromTag(tag);
    }

    /**
     * Creates a clan invite
     *
     * @param player The {@link SQLPlayer} who should be invited
     * @param clan   The {@link SQLClan} to wich the player should be invited
     * @return The generated {@link SQLClanInvitation invite}
     */
    public Retriever<SQLClanInvitation> createInvite(@NonNull SQLPlayer player, @NonNull SQLClan clan) {
        if (!online) return null;
        return SQLClanInvitation.create(player, clan);
    }

    /**
     * @param player The {@link SQLPlayer} who is being invited
     * @param clan   The {@link SQLClan} to wich the player is being invited to
     * @return A {@link SQLClanInvitation} <br> returns null if the player is not invited to the clan
     */
    public Retriever<SQLClanInvitation> getInvite(@NonNull SQLPlayer player, @NonNull SQLClan clan) {
        if (!online) return null;
        return SQLClanInvitation.fromPlayer(player, clan);
    }

    /**
     * @param player The {@link SQLPlayer} of which all Invites should be collected
     * @return a {@link List} of {@link SQLClanInvitation} to which the player is invited to
     */
    public Retriever<List<SQLClanInvitation>> getInvite(@NonNull SQLPlayer player) {
        if (!online) return null;
        return SQLClanInvitation.fromPlayer(player);
    }

    /**
     * Inserts a membership into the database
     *
     * @param player The {@link SQLPlayer} who is the member
     * @param clan   The {@link SQLClan} the player is now member in
     * @param rank   The rank as int of the player; 0 equals normal member
     * @return The generated {@link SQLPlayerAllocation}
     */
    public Retriever<SQLPlayerAllocation> addMemberToClan(
            @NonNull SQLPlayer player,
            @NonNull SQLClan clan,
            @NonNull Integer rank
                                                         ) {
        if (!online) return null;
        return SQLPlayerAllocation.create(player, clan, rank);
    }

    /**
     * @param player the {@link SQLPlayer}
     * @return The {@link SQLClan} the player is member in <br> returns null if player is not part of any clan
     */
    public Retriever<SQLPlayerAllocation> getMembership(SQLPlayer player) {
        if (!online) return null;
        return SQLPlayerAllocation.fromPlayer(player);
    }

}
