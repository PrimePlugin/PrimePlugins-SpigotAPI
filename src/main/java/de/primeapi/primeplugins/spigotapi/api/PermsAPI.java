package de.primeapi.primeplugins.spigotapi.api;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.sql.DatabaseTask;
import de.primeapi.primeplugins.spigotapi.sql.permissions.SQLGroup;
import de.primeapi.primeplugins.spigotapi.sql.permissions.SQLRanking;
import de.primeapi.primeplugins.spigotapi.sql.permissions.SQLUserPermission;
import lombok.Getter;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

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
            PrimeCore.getInstance().getLogger().log(Level.INFO, " PermsAPI wurde geladen");
        } else {
            PrimeCore.getInstance().getLogger().log(Level.INFO, " PermsAPI wurde NICHT geladen");
        }
    }

    public static PermsAPI getInstance() {
        return instance;
    }


    public DatabaseTask<SQLGroup> getHighestGroup(UUID uuid){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            List<SQLRanking> list = SQLRanking.fromUser(uuid).complete();
            if(list.size() == 0){
                return SQLGroup.fromName("default").complete();
            }
            return list.get(0).getGroup().complete();
        }));
    }

    public DatabaseTask<List<String>> getPermissions(UUID uuid){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            SQLGroup defaultGroup = SQLGroup.fromName("default").complete();
            List<String> list;
            if(defaultGroup == null){
                list = new ArrayList<>();
            }else{
                list = new ArrayList<>(defaultGroup.getPermissions().complete());
            }
            for (SQLRanking ranking :
                    SQLRanking.fromUser(uuid).complete()) {
                list.addAll(ranking.getGroup().complete().getPermissions().complete());
            }

            for (SQLUserPermission permission :
                    SQLUserPermission.fromUser(uuid).complete()) {
                if(permission.isNegative().complete()){
                    list.remove(permission.getPermission().complete());
                }else {
                    list.add(permission.getPermission().complete());
                }
            }
            return list;
        }));
    }

    public DatabaseTask<Boolean> hasSelfPermission(UUID uuid,String permission){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            List<String> list = getPermissions(uuid).complete();
            if(list.contains("*")){
                return true;
            }

            if(list.contains(permission)){
                return true;
            }

            if(!permission.contains(".")){
                return list.contains(permission);
            }


            String perm[] = permission.toLowerCase().split("\\.");
            String splitted = perm[0];
            for (int i = 1; i < perm.length; i++) {
                if(list.contains(splitted + ".*")){
                    return true;
                }
                splitted += "." + perm[i];
            }


            return false;
        }));
    }

}
