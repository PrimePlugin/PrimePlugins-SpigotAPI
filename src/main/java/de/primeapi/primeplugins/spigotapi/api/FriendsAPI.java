package de.primeapi.primeplugins.spigotapi.api;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.sql.DatabaseTask;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;
import de.primeapi.primeplugins.spigotapi.sql.friend.SQLFriendEntry;
import de.primeapi.primeplugins.spigotapi.sql.friend.SQLFriendRequest;
import lombok.Getter;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * @author Lukas S. PrimeAPI
 * created on 09.05.2021
 * crated for PrimePlugins
 */
@Getter
public class FriendsAPI {

    private static FriendsAPI instance;
    boolean online;

    public FriendsAPI() {
        instance = this;
        online = false;
        try {
            DatabaseMetaData md = PrimeCore.getInstance().getConnection().getMetaData();
            ResultSet rs = md.getTables(null, null, "prime_bungee_friends", null);
            online = rs.next();
            rs.close();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        if (online) {
            PrimeCore.getInstance().getLogger().log(Level.INFO, " FriendsAPI wurde geladen");
        } else {
            PrimeCore.getInstance().getLogger().log(Level.INFO, " FriendsAPI wurde NICHT geladen");
        }
    }

    public static FriendsAPI getInstance() {
        return instance;
    }

    /**
     * @param player {@link SQLPlayer}
     * @return A List of {@link SQLPlayer} who are friends of player
     */
    public DatabaseTask<List<SQLPlayer>> getFriends(SQLPlayer player) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            if (!online) return null;
            return SQLFriendEntry.getFriendsFromPlayer(player).complete().stream()
                    .map(sqlFriendEntry -> sqlFriendEntry.retrieveFriend().complete())
                    .collect(Collectors.toList());
        }));
    }

    /**
     * Removes a Friendship from the database
     * <br>
     * You have to call {@link FriendsAPI#endFriendship(SQLPlayer, SQLPlayer) FriendsAPI#endFriendship(player2, player1)} in order to
     * remove this friendship completely.
     * @param player1 First Player
     * @param player2 Second Player
     */
    public void endFriendship(SQLPlayer player1, SQLPlayer player2) {
        if (!online) return;
        SQLFriendEntry entry1 = SQLFriendEntry.getFromPlayers(player1, player2).complete();
        SQLFriendEntry entry2 = SQLFriendEntry.getFromPlayers(player2, player1).complete();
        if (entry1 != null) entry1.delete();
        if (entry2 != null) entry2.delete();
    }

    /**
     * @param player1 The first {@link SQLPlayer}
     * @param player2 The friend player1 as {@link SQLPlayer}
     * @param time The Time as Unix timestamp as the creation time of the friendship
     * @return The created {@link SQLFriendEntry}
     */
    public DatabaseTask<SQLFriendEntry> createFriendship(SQLPlayer player1, SQLPlayer player2, Long time){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            if (!online) return null;
            return SQLFriendEntry.create(player1, player2, time).complete();
        }));
    }


    /**
     * @param player {@link SQLPlayer}
     * @return A List of {@link SQLFriendRequest} of the player
     */
    public DatabaseTask<List<SQLFriendRequest>> getRequestsFromPlayer(SQLPlayer player){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            if (!online) return null;
            return SQLFriendRequest.getRequestsFromPlayer(player).complete();
        }));
    }

    /**
     * Creates an Friendrequest
     * @param target The {@link SQLPlayer} who is being requested
     * @param requester The {@link SQLPlayer} whi requested the friendship
     * @param time The Time as UNIX timestamp of the creation of the Request
     * @return The generated {@link SQLFriendRequest}
     */
    public DatabaseTask<SQLFriendRequest> createRequest(SQLPlayer target, SQLPlayer requester, Long time){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            if (!online) return null;
            return SQLFriendRequest.create(target, requester, time).complete();
        }));
    }



}
