package de.primeapi.primeplugins.spigotapi.api.plugins.nick;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.cache.Cache;
import de.primeapi.primeplugins.spigotapi.api.plugins.Skin;
import de.primeapi.primeplugins.spigotapi.utils.FlatWorldGenerator;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

@Getter
public class NickAPI {


    private static NickAPI instance;
    private final String nickworldName = "NickWorld";
    boolean online;
    private final Cache<UUID, Boolean> isNicked = new Cache<>();

    private final HashMap<UUID, String> nickedGroup = new HashMap<>();

    public NickAPI() {
        instance = this;
        online = false;
        try {
            online = Bukkit.getPluginManager().getPlugin("NickSystem") != null;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        if (online) {
            PrimeCore.getInstance().getLogger().log(Level.INFO, "NickAPI wird geladen...");
            try {
                PrimeCore.getInstance().getConnection().prepareStatement(
                        "CREATE TABLE IF NOT EXISTS prime_nick_current (" +
                                "`id` INT NOT NULL AUTO_INCREMENT UNIQUE," +
                                "`uuid` VARCHAR(36) UNIQUE NOT NULL ," +
                                "`realname` VARCHAR(36) UNIQUE NOT NULL," +
                                "`nickname` VARCHAR(36) UNIQUE NOT NULL," +
                                "primary key (id));"
                ).execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Bukkit.getScheduler().runTaskLater(
                    PrimeCore.getInstance(),
                    () -> {
                        Bukkit.createWorld(WorldCreator.name(NickAPI.getInstance().getNickworldName()).generator(new FlatWorldGenerator()));
                    },
                    10 * 20L
            );

        } else {
            PrimeCore.getInstance().getLogger().log(Level.INFO, "NickAPI wurde NICHT geladen");
        }
    }

    public static NickAPI getInstance() {
        return instance;
    }

    private static String getUUIDOffline(String name) {
        return Bukkit.getOfflinePlayer(name).getUniqueId().toString().replace("-", "");
    }

    public boolean isNicked(Player player) {
        if (!online) return false;
        Boolean cachedValue = isNicked.getCachedValue(player.getUniqueId());
        if (cachedValue != null) {
            return cachedValue;
        }
        boolean b = false;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT * FROM prime_nick_current WHERE uuid = ?");
            st.setString(1, player.getUniqueId().toString());
            ResultSet rs = st.executeQuery();
            b = rs.next();
            rs.close();
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        isNicked.cacheEntry(player.getUniqueId(), b);
        return b;
    }

    public void unnickPlayer(Player player) {
        if (!online) throw new IllegalStateException("NickAPI not online!");
        String realname = getRealname(player);
        isNicked.remove(player.getUniqueId());
        nickedGroup.remove(player.getUniqueId());
        if (realname != null) {
            changeNick(player, realname);
            Skin skin = new Skin(getUUIDOffline(realname));
            if (skin.getSkinName() != null) {
                setSkin(player, skin);
                Bukkit.getOnlinePlayers().forEach(players -> {
                    players.hidePlayer(player);
                    players.showPlayer(player);
                });
            } else {
                throw new IllegalArgumentException("The given nickname is not a valid nickname!");
            }
            isNicked.remove(player.getUniqueId());
            try {
                removeFromDatabase(player);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        PrimeCore.getInstance().getScoreboardManager().sendTeams();
        isNicked.remove(player.getUniqueId());
    }

    public String getRealname(Player player) {
        if (!online) throw new IllegalStateException("NickAPI not online!");
        String s = null;
        try {
            PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement("SELECT * FROM prime_nick_current WHERE uuid = ?");
            st.setString(1, player.getUniqueId().toString());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                s = rs.getString("realname");
            }
            rs.close();
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return s;
    }

    public String getNickGroup(Player player) {
        return nickedGroup.getOrDefault(player.getUniqueId(), null);
    }

    public void nickPlayer(Player player, String nickname, String group) {
        nickedGroup.put(player.getUniqueId(), group);
        nickPlayer(player, nickname);
    }


    //Privates Methods

    /**
     * Nicks the Player & sets the Skin
     *
     * @param player   The Player that shall be nicked
     * @param nickname The nicked name
     */
    public void nickPlayer(Player player, String nickname) {
        if (!online) throw new IllegalStateException("NickAPI not online!");
        if (isNicked(player)) {
            throw new IllegalStateException("Player already nicked!");
        }
        try {
            addToDatabase(player, player.getName(), nickname);
        } catch (SQLException e) {
            return;
        }
        changeNick(player, nickname);
        Skin skin = new Skin(getUUIDOffline(nickname));
        isNicked.remove(player.getUniqueId());
        if (skin.getSkinName() != null) {
            setSkin(player, skin);
            Bukkit.getOnlinePlayers().forEach(players -> {
                players.hidePlayer(player);
                players.showPlayer(player);
            });
        } else {
            throw new IllegalArgumentException("The given nickname is not a valid nickname!");
        }
        PrimeCore.getInstance().getScoreboardManager().sendTeams();
    }

    private void addToDatabase(Player player, String realname, String nickname) throws SQLException {
        if (!online) throw new IllegalStateException("NickAPI not online!");
        PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
                "INSERT INTO prime_nick_current values (id,?,?,?)"
        );
        st.setString(1, player.getUniqueId().toString());
        st.setString(2, realname);
        st.setString(3, nickname);
        st.execute();
    }

    public void removeFromDatabase(Player player) throws SQLException {
        if (!online) throw new IllegalStateException("NickAPI not online!");
        PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
                "DELETE FROM prime_nick_current WHERE uuid = ?"
        );
        st.setString(1, player.getUniqueId().toString());
        st.execute();
    }

    private void setSkin(Player player, Skin skin) {
        setSkin(player, skin.getSkinValue(), skin.getSkinSignatur());
    }

    private void setSkin(Player player, String value, String signature) {
        NickUtils.setSkin(player, value, signature);
    }

    private void changeNick(Player player, String name) {
        NickUtils.changeNick(player, name);
    }


}
