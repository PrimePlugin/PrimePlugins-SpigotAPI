package de.primeapi.primeplugins.spigotapi.sql.permissions;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.sql.DatabaseTask;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Getter
public class SQLGroup {

    final int id;

    public static DatabaseTask<SQLGroup> fromName(String name) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            int i = 0;
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT id FROM prime_perms_groups WHERE name = ?");
                st.setString(1, name.toLowerCase());
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    i = rs.getInt("id");
                }
                rs.close();
                st.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (i == 0) {
                return null;
            } else {
                return new SQLGroup(i);
            }
        }));
    }

    public static DatabaseTask<SQLGroup> create(String name, String display) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            SQLGroup group = null;
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("INSERT INTO prime_perms_groups values (id, ?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                st.setString(1, name.toLowerCase());
                st.setInt(2, 0);
                st.setString(3, display);
                st.setString(4, "");
                st.setString(5, "");
                st.setString(6, "");
                st.setInt(7, 100);
                st.executeUpdate();
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    group = new SQLGroup(rs.getInt(1));
                }
                rs.close();
                st.close();
            } catch (SQLException throwables) {
                return null;
            }
            return group;
        }));
    }

    public DatabaseTask<String> getName() {
        return getString("name");
    }

    public DatabaseTask<String> getDisplayName() {
        return getString("display_name");
    }

    public DatabaseTask<String> getPrefix() {
        return getString("prefix");
    }

    public DatabaseTask<String> getSuffix() {
        return getString("suffix");
    }

    public DatabaseTask<Integer> getWeight() {
        return getInteger("weight");
    }

    public DatabaseTask<String> getColor() {
        return getString("color");
    }

    private DatabaseTask<String> getString(String row) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            String s = "";
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT * FROM prime_perms_groups WHERE id = ?");
                st.setInt(1, id);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    s = rs.getString(row);
                }
                rs.close();
                st.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            return s;
        }));
    }

    private DatabaseTask<Integer> getInteger(String row) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            Integer i = 0;
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT * FROM prime_perms_groups WHERE id = ?");
                st.setInt(1, id);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    i = rs.getInt(row);
                }
                rs.close();
                st.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            return i;
        }));
    }

    public DatabaseTask<SQLGroup> getInheritance() {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            SQLGroup group = null;
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT * FROM prime_perms_groups WHERE id = ?");
                st.setInt(1, id);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    int i = rs.getInt("inherit");
                    if (i != 0) {
                        group = new SQLGroup(i);
                    }
                }
                rs.close();
                st.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String name = getName().complete();

            if (group == null) {
                if (!name.equals("default")) {
                    return SQLGroup.fromName("default").complete();
                }
                return null;
            }
            if(group.getName().complete().equalsIgnoreCase(name)){
                if (!name.equals("default")) {
                    return SQLGroup.fromName("default").complete();
                }else{
                    return null;
                }
            }
            return group;
        }));
    }

    public DatabaseTask<List<String>> getPermissions() {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            List<String> list;
            SQLGroup inheritance = getInheritance().complete();
            if (inheritance == null) {
                list = new ArrayList<>();
            } else {
                list = inheritance.getPermissions().complete();
            }
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT * FROM prime_perms_grouppermission WHERE `group` = ?");
                st.setInt(1, id);
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    if (rs.getInt("negative") == 1) {
                        list.remove(rs.getString("permission"));
                    } else {
                        list.add(rs.getString("permission"));
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            return list;
        }));
    }

    public DatabaseTask<Boolean> exists() {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            boolean b = false;
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT * FROM prime_perms_groups WHERE `id` = ?");
                st.setInt(1, id);
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    b = true;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return b;
        }));
    }


}
