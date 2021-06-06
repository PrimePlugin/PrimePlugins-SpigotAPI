package de.primeapi.primeplugins.spigotapi.sql.permissions;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.sql.DatabaseTask;
import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class SQLGroupPermission {

    public final int id;

    public static DatabaseTask<List<SQLGroupPermission>> fromGroup(SQLGroup group){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
        List<SQLGroupPermission> list = new ArrayList<>();
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT * FROM prime_perms_grouppermission WHERE `group` = ?");
            st.setInt(1, group.getId());
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                list.add(new SQLGroupPermission((rs.getInt("id"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
        }));
    }
    public static DatabaseTask<SQLGroupPermission> create(SQLGroup group, String permission, boolean negativ){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
        SQLGroupPermission perm = null;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("INSERT INTO prime_perms_grouppermission values (id, ?,?,?)", Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, group.getId());
            st.setString(2, permission.toLowerCase());
            st.setBoolean(3, negativ);
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            if(rs.next()){
                perm = new SQLGroupPermission(rs.getInt(1));
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

    public static DatabaseTask<Boolean> deletePermission(SQLGroup group, String permission){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
        boolean b = false;
        for (SQLGroupPermission groupPermission : fromGroup(group).complete()){
            if(groupPermission.getPermission().complete().equalsIgnoreCase(permission)){
                groupPermission.delete();
                b = true;
            }
        }
        return b;
        }));
    }


    public DatabaseTask<SQLGroup> getGroup(){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
        SQLGroup group = null;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT * FROM prime_perms_grouppermission WHERE id = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                int i = rs.getInt("group");
                if(i != 0){
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

    public DatabaseTask<String> getPermission(){
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
        String s = null;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT * FROM prime_perms_grouppermission WHERE id = ?");
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
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT * FROM prime_perms_grouppermission WHERE id = ?");
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
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("DELETE FROM prime_perms_grouppermission WHERE id =?");
                st.setInt(1, id);
                st.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }


}
