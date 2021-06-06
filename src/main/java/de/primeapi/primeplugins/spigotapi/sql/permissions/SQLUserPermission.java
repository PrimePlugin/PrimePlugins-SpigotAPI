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
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class SQLUserPermission {

    public final int id;


    public static DatabaseTask<List<SQLUserPermission>> fromUser(UUID uuid){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
        List<SQLUserPermission> list = new ArrayList<>();
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT * FROM prime_perms_userpermissions WHERE uuid = ?");
            st.setString(1, uuid.toString());
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                list.add(new SQLUserPermission((rs.getInt("id"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
        }));
    }
    public static DatabaseTask<SQLUserPermission> create(UUID uuid, String permission, boolean negativ){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
        SQLUserPermission perm = null;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("INSERT INTO prime_perms_userpermissions values (id, ?,?,?)", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, uuid.toString());
            st.setString(2, permission.toLowerCase());
            st.setBoolean(3, negativ);
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            if(rs.next()){
                perm = new SQLUserPermission(rs.getInt(1));
            }
            rs.close();
            st.close();;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return perm;
        }));
    }

    public static DatabaseTask<Boolean> deletePermission(UUID uuid, String permission){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
        boolean b = false;
        for (SQLUserPermission userPermission : fromUser(uuid).complete()){
            if(userPermission.getPermission().complete().equalsIgnoreCase(permission)){
                userPermission.delete();
                b = true;
            }
        }
        return b;
        }));
    }


    public DatabaseTask<SQLPlayer> getSQLPlayer(){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
        return new SQLPlayer(getUUID().complete());
        }));
    }


    public DatabaseTask<UUID> getUUID(){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
        UUID uuid = null;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT * FROM prime_perms_userpermissions WHERE id = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
               uuid = UUID.fromString(rs.getString("uuid"));
            }
            rs.close();
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return uuid;
        }));
    }

    public DatabaseTask<String> getPermission(){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
        String s = null;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT * FROM prime_perms_userpermissions WHERE id = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                s = rs.getString("permission");
            }
            rs.close();
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return s;
        }));
    }


    public DatabaseTask<Boolean> isNegative(){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
        Boolean b = null;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT * FROM prime_perms_userpermissions WHERE id = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                int i = rs.getInt("negative");
                b = i == 1;
            }
            rs.close();
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return b;
        }));
    }

    public void delete(){
        PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("DELETE FROM prime_perms_userpermissions WHERE id =?");
                st.setInt(1, id);
                st.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

}
