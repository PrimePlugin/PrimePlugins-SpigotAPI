package de.primeapi.primeplugins.spigotapi.utils.nick;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.primeapi.primeplugins.spigotapi.api.NickAPI;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class NickUtils {

    public static void setSkin(Player player, Skin skin){
        setSkin(player, skin.getSkinValue(), skin.getSkinSignatur());
    }

    public static void setSkin(Player player, String value, String signature) {
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
        Location location = player.getLocation().clone();
        World world = Bukkit.getWorld(NickAPI.getInstance().getNickworldName());
        if(world != null) {
            player.teleport(world.getSpawnLocation());
            player.teleport(location);
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
