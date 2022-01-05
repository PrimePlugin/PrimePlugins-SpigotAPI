package de.primeapi.primeplugins.spigotapi.sql.permissions;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.sql.DatabaseTask;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;
import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class SQLRanking {

    public final int id;

    public static DatabaseTask<List<SQLRanking>> fromUser(UUID uuid) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            List<SQLRanking> list = new ArrayList<>();
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT * FROM prime_perms_ranking WHERE uuid = ?");
                st.setString(1, uuid.toString());
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    SQLRanking curr = new SQLRanking(rs.getInt("id"));
                    if (curr.getTime().complete() == -1 || curr.getTime().complete() > System.currentTimeMillis()) {
                        if (curr.getGroup().complete().exists().complete()) {
                            list.add(curr);
                        } else {
                            curr.delete();
                        }
                    } else {
                        curr.delete();
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            Collections.sort(list, (o1, o2) -> {
                //System.out.println("Comparing: " + o1.getGroup().getName() + "[" + o1.getPotency() + "] & " + o2.getGroup().getName() + "[" + o2.getPotency() + "]");
                if (o1.getPotency().complete() > o2.getPotency().complete()) return -1;
                if (o1.getPotency().complete() < o2.getPotency().complete()) return 1;
                return 0;
            });

            return list;
        }));
    }

    public static DatabaseTask<SQLRanking> create(UUID uuid, SQLGroup group, long time, int potency) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            SQLRanking ranking = null;
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("INSERT INTO prime_perms_ranking values (id, ?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                st.setString(1, uuid.toString());
                st.setInt(2, group.id);
                st.setLong(3, time);
                st.setInt(4, potency);
                st.executeUpdate();
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    ranking = new SQLRanking(rs.getInt(1));
                }
                rs.close();
                st.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return null;
            }
            return ranking;
        }));
    }

    public DatabaseTask<SQLPlayer> getSQLPlayer() {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> new SQLPlayer(getUUID().complete())));
    }

    public DatabaseTask<UUID> getUUID() {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            String s = null;
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT * FROM prime_perms_ranking WHERE id = ?");
                st.setInt(1, id);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    s = rs.getString("uuid");
                }
                rs.close();
                st.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            assert s != null;
            return UUID.fromString(s);
        }));
    }

    public DatabaseTask<SQLGroup> getGroup() {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            SQLGroup group = null;
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT * FROM prime_perms_ranking WHERE id = ?");
                st.setInt(1, id);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    int i = rs.getInt("group");
                    if (i != 0) {
                        group = new SQLGroup(i);
                    }
                }
                rs.close();
                st.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            return group;
        }));
    }

    public DatabaseTask<Long> getTime() {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            Long l = null;
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT * FROM prime_perms_ranking WHERE id = ?");
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
            assert l != null;
            return l;
        }));
    }


    public DatabaseTask<Integer> getPotency() {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            int i = 0;
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT * FROM prime_perms_ranking WHERE id = ?");
                st.setInt(1, id);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    i = rs.getInt("potency");
                }
                rs.close();
                st.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return i;
        }));
    }

    public void delete() {
        PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("DELETE FROM prime_perms_ranking WHERE id = ?");
                st.setInt(1, id);
                st.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

}
