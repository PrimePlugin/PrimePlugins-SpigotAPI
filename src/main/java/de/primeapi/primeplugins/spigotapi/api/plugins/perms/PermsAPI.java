package de.primeapi.primeplugins.spigotapi.api.plugins.perms;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.sql.DatabaseTask;
import de.primeapi.primeplugins.spigotapi.sql.permissions.SQLGroup;
import de.primeapi.primeplugins.spigotapi.sql.permissions.SQLGroupPermission;
import de.primeapi.primeplugins.spigotapi.sql.permissions.SQLRanking;
import de.primeapi.primeplugins.spigotapi.sql.permissions.SQLUserPermission;
import lombok.Getter;
import lombok.NonNull;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * @author Lukas S. PrimeAPI
 * created on 05.06.2021
 * crated for PrimePlugins
 */
@Getter
public class PermsAPI { //TOOD


    private static PermsAPI instance;
    boolean online;

    public PermsAPI() {
        instance = this;
        online = false;
        try {
            DatabaseMetaData md = PrimeCore.getInstance().getConnection().getMetaData();
            ResultSet rs = md.getTables(null, null, "prime_perms_groups", null);
            online = rs.next();
            rs.close();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        if (online) {
            PrimeCore.getInstance().getLogger().log(Level.INFO, "PermsAPI wurde geladen");
        } else {
            PrimeCore.getInstance().getLogger().log(Level.INFO, "PermsAPI wurde NICHT geladen");
        }
    }

    public static PermsAPI getInstance() {
        return instance;
    }


    public DatabaseTask<SQLGroup> getHighestGroup(UUID uuid) {
        if (!online) throw new IllegalStateException("PermsAPI was not loaded");
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            List<SQLRanking> list = SQLRanking.fromUser(uuid).complete();
            if (list.size() == 0) {
                return SQLGroup.fromName("default").complete();
            }
            return list.get(0).getGroup().complete();
        }));
    }

    public DatabaseTask<List<String>> getPermissions(UUID uuid) {
        if (!online) throw new IllegalStateException("PermsAPI was not loaded");
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            SQLGroup defaultGroup = SQLGroup.fromName("default").complete();
            List<String> list;
            if (defaultGroup == null) {
                list = new ArrayList<>();
            } else {
                list = new ArrayList<>(defaultGroup.getPermissions().complete());
            }
            for (SQLRanking ranking :
                    SQLRanking.fromUser(uuid).complete()) {
                list.addAll(ranking.getGroup().complete().getPermissions().complete());
            }

            for (SQLUserPermission permission :
                    SQLUserPermission.fromUser(uuid).complete()) {
                if (permission.isNegative().complete()) {
                    list.remove(permission.getPermission().complete());
                } else {
                    list.add(permission.getPermission().complete());
                }
            }
            return list;
        }));
    }

    /**
     * Adding a group for a user
     *
     * @param uuid      The UUID of the Player
     * @param groupName The name of the Group (NOT the displayname)
     * @param lenght    The timeout as UNIX Timestamp. -1 being permanent
     * @param potency   The potency of this. The higher, the more important this role is for the user
     * @throws IllegalStateException    If the PermsAPI is offline
     * @throws IllegalArgumentException If the groupname was not found
     * @throws Exception                If there was an unknown error while creating the sql record
     */
    public void addGroup(@NonNull UUID uuid, @NonNull String groupName, @NonNull Long lenght, int potency) throws Exception {
        if (!online) throw new IllegalStateException("PermsAPI was not loaded");

        SQLGroup sqlGroup = SQLGroup.fromName(groupName).complete();
        if (sqlGroup == null) throw new IllegalArgumentException("Group was not found");

        SQLRanking ranking = SQLRanking.create(uuid, sqlGroup, lenght, potency).complete();
        if (ranking == null) throw new Exception("An unknown error accrued while creating sql record");
    }


    /**
     * Adding a permission to a specific player
     *
     * @param uuid       The UUID of the Player
     * @param permission The plain permission that shall be added
     * @throws IllegalStateException If the PermsAPI is offline
     * @throws Exception             If there was an unknown error while creating the sql record
     */
    public void addPlayerPermission(@NonNull UUID uuid, @NonNull String permission) throws Exception {
        if (!online) throw new IllegalStateException("PermsAPI was not loaded");

        SQLUserPermission userPermission = SQLUserPermission.create(uuid, permission, false).complete();
        if (userPermission == null) throw new Exception("An unknown error accrued while creating sql record");
    }


    /**
     * Adding a permission to a group
     *
     * @param groupName  The name of the group
     * @param permission The plain permission that shall be added
     * @throws IllegalStateException    If the PermsAPI is offline
     * @throws IllegalArgumentException If the groupname was not found
     * @throws Exception                If there was an unknown error while creating the sql record
     */
    public void addGroupPermission(@NonNull String groupName, @NonNull String permission) throws Exception {
        if (!online) throw new IllegalStateException("PermsAPI was not loaded");

        SQLGroup sqlGroup = SQLGroup.fromName(groupName).complete();
        if (sqlGroup == null) throw new IllegalArgumentException("Group was not found");

        SQLGroupPermission userPermission = SQLGroupPermission.create(sqlGroup, permission, false).complete();
        if (userPermission == null) throw new Exception("An unknown error accrued while creating sql record");
    }

    /**
     * Removes a player from a specific group
     *
     * @param uuid      The UUID of the Player
     * @param groupName The name of the group
     * @throws IllegalStateException    If the PermsAPI is offline
     * @throws IllegalArgumentException If the groupname was not found
     */
    public void removeGroupFromUser(@NonNull UUID uuid, @NonNull String groupName) {
        if (!online) throw new IllegalStateException("PermsAPI was not loaded");

        SQLGroup sqlGroup = SQLGroup.fromName(groupName).complete();
        if (sqlGroup == null) throw new IllegalArgumentException("Group was not found");

        SQLRanking.fromUser(uuid).submit(
                sqlRankings ->
                        sqlRankings
                                .stream()
                                .filter(sqlRanking ->
                                        sqlRanking.getGroup().complete().getId() == sqlGroup.getId()
                                )
                                .forEach(
                                        SQLRanking::delete
                                )
        );

    }

    /**
     * Used to get a List of all Groups a Player is part of
     *
     * @param uuid The UUID of the Player
     * @return A {@link DatabaseTask} of a List containing all {@link SQLGroup SQLGroups}
     */
    public DatabaseTask<List<SQLGroup>> getGroups(UUID uuid) {
        return SQLRanking.fromUser(uuid).map(sqlRankings -> sqlRankings.stream().map(sqlRanking -> sqlRanking.getGroup().complete()).collect(Collectors.toList()));
    }

    public DatabaseTask<Boolean> hasSelfPermission(UUID uuid, String permission) {
        if (!online) throw new IllegalStateException("PermsAPI was not loaded");
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            List<String> list = getPermissions(uuid).complete();
            if (list.contains("*")) {
                return true;
            }

            if (list.contains(permission)) {
                return true;
            }

            if (!permission.contains(".")) {
                return list.contains(permission);
            }


            String[] perm = permission.toLowerCase().split("\\.");
            String splitted = perm[0];
            for (int i = 1; i < perm.length; i++) {
                if (list.contains(splitted + ".*")) {
                    return true;
                }
                splitted += "." + perm[i];
            }


            return false;
        }));
    }

}
