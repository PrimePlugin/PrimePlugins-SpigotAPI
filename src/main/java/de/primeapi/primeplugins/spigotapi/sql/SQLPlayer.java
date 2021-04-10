package de.primeapi.primeplugins.spigotapi.sql;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.events.CoinsChanceEvent;
import de.primeapi.primeplugins.spigotapi.enums.PlayerSetting;
import de.primeapi.primeplugins.spigotapi.managers.config.configs.CoreConfig;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
public class SQLPlayer {
    public Integer id;
    public UUID uuid;

    public static SQLPlayer create(UUID uuid, String name){
        Integer id = null;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("INSERT INTO core_players values (id, ?,?,?,?,?)");
            st.setString(1, uuid.toString());
            st.setString(2, name.toLowerCase());
            st.setString(3, name);
            st.setInt(4, CoreConfig.getInstance().getInt("settings.coins.startAmount"));
            st.setInt(5,0);
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
            }
            rs.close();
            st.close();
        } catch (SQLException throwables) {
            return new SQLPlayer(uuid);
        }
        assert (id != null);
        return new SQLPlayer(id);
    }

    public static SQLPlayer loadPlayerByName(String name){
        Integer id = null;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT id FROM core_players WHERE name = ?;");
            st.setString(1, name.toLowerCase());
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                id = rs.getInt("id");
            }
            rs.close();
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(id == null){
            return null;
        }
        return new SQLPlayer(id);
    }


    public SQLPlayer(UUID uuid){
        this.uuid = uuid;
    }

    private SQLPlayer(Integer id){
        this.id = id;
    }

    public void load() {
        if(Objects.isNull(id)){
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT id FROM core_players WHERE uuid=?");
                st.setString(1, uuid.toString());
                ResultSet rs = st.executeQuery();
                if(rs.next()){
                    id = rs.getInt("id");
                    rs.close();
                    st.close();
                }else {
                    rs.close();
                    st.close();
                    throw new IllegalArgumentException("Player with UUID '" + uuid.toString() + "' not found");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public String getName(){
        load();
        String s = null;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT name FROM core_players WHERE id = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                s = rs.getString("name");
            }
            rs.close();
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return s;
    }
    public UUID getUniqueId(){
        if(uuid != null){
            return uuid;
        }else {
            load();
            String s = null;
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT uuid FROM core_players WHERE id = ?");
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
    }
    }
    public String getRealName(){
        load();
        String s = null;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT realname FROM core_players WHERE id = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                s = rs.getString("realname");
            }
            rs.close();
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return s;
    }
    public int getCoins(){
        load();
        int i  = 0;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT coins FROM core_players WHERE id = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                i = rs.getInt("coins");
            }
            rs.close();
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return i;
    }

    public void setCoins(int i){
        load();
        CoinsChanceEvent event = new CoinsChanceEvent(this, i);
        PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
                try {
                    PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("UPDATE core_players SET coins = ? WHERE id=?");
                    st.setInt(1, i);
                    st.setInt(2, id);
                    st.execute();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
        });
        Bukkit.getPluginManager().callEvent(event);
    }
    public void updateName(String name){
        load();
        PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("UPDATE core_players SET name = ?,realname = ? WHERE id=?");
            st.setString(1, name.toLowerCase());
            st.setString(2, name);
            st.setInt(3, id);
            st.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        });
    }

    public int retrieveOnMins(){
        load();
        int i  = 0;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT playtime FROM core_players WHERE id = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                i = rs.getInt("playtime");
            }
            rs.close();
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return i;
    }

    public void setOnMins(int i){
        load();
        PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
            try {
                PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("UPDATE core_players SET playtime = ? WHERE id=?");
                st.setInt(1, i);
                st.setInt(2, id);
                st.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public void addOnMins(int i){
        setOnMins(retrieveOnMins() + i);
    }

    public void addCoins(int i){
        setCoins(getCoins() + i);
    }

    public Integer retrieveSetting(PlayerSetting setting){
        load();
        Integer i = null;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
                    "SELECT * FROM core_settings WHERE uuid = ? AND setting = ?"
            );
            st.setString(1, getUniqueId().toString());
            st.setString(2, setting.toString());
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                i = rs.getInt("value");
            }
            rs.close();
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return i;
    }

    public int retrieveSettingSave(PlayerSetting setting){
        Integer i = retrieveSetting(setting);
        if(Objects.isNull(i)){
            return setting.getStandartValue();
        }else {
            return i;
        }
    }

    public void setSetting(PlayerSetting setting, int value){
        load();

        PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
           if(Objects.isNull(retrieveSetting(setting))){
               try {
                   PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
                           "INSERT INTO core_settings value (id, ?,?,?)"
                   );
                   st.setString(1, getUniqueId().toString());
                   st.setString(2, setting.toString());
                   st.setInt(3, value);
                   st.execute();
               }catch (SQLException e) {
                   e.printStackTrace();
               }
           }else{
               try {
                   PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
                           "UPDATE core_settings SET value = ? WHERE uuid = ? AND setting = ?"
                   );
                   st.setInt(1, value);
                   st.setString(2, getUniqueId().toString());
                   st.setString(3, setting.toString());
                   st.execute();
               } catch (SQLException throwables) {
                   throwables.printStackTrace();
               }
           }
        });
    }

}
