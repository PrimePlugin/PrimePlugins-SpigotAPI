package de.primeapi.primeplugins.spigotapi.sql.friend;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.sql.DatabaseTask;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Getter
public class SQLFriendRequest {
    public final int id;


    public static DatabaseTask<List<SQLFriendRequest>> getRequestsFromPlayer(SQLPlayer p) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            List<SQLFriendRequest> list = new ArrayList<>();
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
                        "SELECT * FROM prime_bungee_requests WHERE uuid = ?"
                );
                st.setString(1, p.retrieveUniqueId().complete().toString());
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    list.add(new SQLFriendRequest(rs.getInt("id")));
                }
                rs.close();
                st.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return list;
        }));
    }

    public static DatabaseTask<SQLFriendRequest> create(SQLPlayer target, SQLPlayer requester, Long time) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            SQLFriendRequest request = null;
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
                        "INSERT INTO prime_bungee_requests value (id, ?,?,?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                st.setString(1, target.retrieveUniqueId().complete().toString());
                st.setString(2, requester.retrieveUniqueId().complete().toString());
                st.setLong(3, time);
                st.executeUpdate();
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    request = new SQLFriendRequest(rs.getInt(1));
                }
                rs.close();
                st.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return request;
        }));
    }

    public DatabaseTask<SQLPlayer> retrieveRequester() {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            SQLPlayer player = null;
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
                        "SELECT * FROM prime_bungee_requests WHERE id = ?"
                );
                st.setInt(1, id);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    player = new SQLPlayer(UUID.fromString(rs.getString("requester")));
                }
                rs.close();
                st.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return player;
        }));
    }

    public DatabaseTask<Long> retrieveTime() {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            Long l = null;
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
                        "SELECT * FROM prime_bungee_requests WHERE id = ?"
                );
                st.setInt(1, id);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    l = rs.getLong("time");
                }
                rs.close();
                st.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return l;
        }));
    }

    public void delete() {
        PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
                        "DELETE FROM prime_bungee_requests WHERE id=?"
                );
                st.setInt(1, id);
                st.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }
}
