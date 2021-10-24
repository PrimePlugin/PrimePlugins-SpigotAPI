package de.primeapi.primeplugins.spigotapi.api;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.utils.Skin;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.logging.Level;

@Getter
public class NickAPI {



    private static NickAPI instance;
    boolean online;

    public NickAPI() {
        instance = this;
        online = false;
        try {
            online = Bukkit.getPluginManager().getPlugin("NickSystem") != null;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        if (online) {
            PrimeCore.getInstance().getLogger().log(Level.INFO, "NickAPI wurde geladen");
        } else {
            PrimeCore.getInstance().getLogger().log(Level.INFO, "NickAPI wurde NICHT geladen");
        }
    }

    public static NickAPI getInstance() {
        return instance;
    }

    /**
     * Nicks the Player & sets the Skin
     * @param player The Player that shall be nicked
     * @param nickname The nicked name
     */
    public static void nickPlayer(Player player, String nickname) {
        changeNick(player, nickname);
        Skin skin = new Skin(getUUIDOffline(nickname));
        if (skin.getSkinName() != null) {
            setSkin(player, skin.getSkinValue(), skin.getSkinSignatur());
            Bukkit.getOnlinePlayers().forEach(players -> {
                players.hidePlayer(player);
                players.showPlayer(player);
            });
        }else {
            throw new IllegalArgumentException("The given nickname is not a valid nickname!");
        }
    }

    private static String getUUIDOffline(String name) {
        return Bukkit.getOfflinePlayer(name).getUniqueId().toString().replace("-", "");
    }

    private static void setSkin(Player player, String value, String signature) {
        GameProfile gameProfile = ((CraftPlayer) player).getProfile();
        (((CraftPlayer) player).getHandle()).playerConnection.sendPacket(
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
                        ((CraftPlayer) player)
                                .getHandle()));
        gameProfile.getProperties().removeAll("textures");
        gameProfile.getProperties().put("textures", new Property("textures", value, signature));
        (((CraftPlayer) player).getHandle()).playerConnection.sendPacket(
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
                        ((CraftPlayer) player)
                                .getHandle()));
        for (Player all : Bukkit.getServer().getOnlinePlayers()) {
            if (all == player) {
                continue;
            }
            if (!all.canSee(player)) {
                continue;
            }
            (((CraftPlayer) all).getHandle()).playerConnection.sendPacket(
                    new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
                            ((CraftPlayer) player)
                                    .getHandle()));
            (((CraftPlayer) all).getHandle()).playerConnection.sendPacket(
                    new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
                            ((CraftPlayer) player)
                                    .getHandle()));
            (((CraftPlayer) all).getHandle()).playerConnection
                    .sendPacket(new PacketPlayOutEntityDestroy(player.getEntityId()));
            (((CraftPlayer) all).getHandle()).playerConnection
                    .sendPacket(new PacketPlayOutNamedEntitySpawn(((CraftPlayer) player).getHandle()));
        }
    }

    public static void changeNick(Player player, String name) {
        CraftPlayer craftPlayer = (CraftPlayer) player;

        try {
            Field field = craftPlayer.getProfile().getClass().getDeclaredField("name");
            field.setAccessible(true);
            field.set(craftPlayer.getProfile(), name);
        } catch (Exception ignored) {
        }
    }



}
