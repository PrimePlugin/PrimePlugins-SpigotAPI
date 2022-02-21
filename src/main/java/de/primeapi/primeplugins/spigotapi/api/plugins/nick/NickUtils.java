package de.primeapi.primeplugins.spigotapi.api.plugins.nick;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.plugins.Skin;
import de.primeapi.primeplugins.spigotapi.api.plugins.nick.NickAPI;
import de.primeapi.primeplugins.spigotapi.managers.scoreboard.objects.utils.NMS;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.reflect.*;

public class NickUtils {

    public static void setSkin(Player player, Skin skin) {
        setSkin(player, skin.getSkinValue(), skin.getSkinSignatur());
    }

    public static void setSkin(Player player, String value, String signature) {


        try {
            Class<?> BUKKIT_PLAYER = Class.forName("org.bukkit.craftbukkit." + PrimeCore.getInstance().getVersionManager().getNmsVersion() + ".entity.CraftPlayer");
            Class<?> ENTITY_PLAYER = PrimeCore.getInstance().getVersionManager().getNMSClass("EntityPlayer");
            Class<?> HUMAN_PLAYER = PrimeCore.getInstance().getVersionManager().getNMSClass("EntityHuman");
            Field ENTITYPLAYER_CONNECTION = ENTITY_PLAYER.getField("playerConnection");
            ENTITYPLAYER_CONNECTION.setAccessible(true);

            Object craftPlayer = BUKKIT_PLAYER.cast(player);                                                            // BUKKIT_PLAYER
            GameProfile gameProfile = (GameProfile) BUKKIT_PLAYER.getMethod("getProfile").invoke(craftPlayer);   // GameProfile (non nms)
            Object handle = BUKKIT_PLAYER.getMethod("getHandle").invoke(craftPlayer);                            // ENTITY_PLAYER
            Object handleArray = Array.newInstance(ENTITY_PLAYER, 1);
            Array.set(handleArray, 0, handle);

            //EntityPlayer handle = (((CraftPlayer) player).getHandle());

            Class<?> PACKET_PLAYOUT_PLAYER_INFO = PrimeCore.getInstance().getVersionManager().getNMSClass("PacketPlayOutPlayerInfo");
            Class<?> PACKET_PLAYOUT_PLAYER_INFO_ENUM = getSubClass(PACKET_PLAYOUT_PLAYER_INFO, "EnumPlayerInfoAction");


            Constructor<?> PACKET_PLAYOUT_PLAYER_INFO_CONST = PACKET_PLAYOUT_PLAYER_INFO.getConstructor(PACKET_PLAYOUT_PLAYER_INFO_ENUM, Class.forName("[L" + ENTITY_PLAYER.getName() + ";"));
            Method PACKET_PLAYOUT_PLAYER_INFO_ENUM_BYSTRING = PACKET_PLAYOUT_PLAYER_INFO_ENUM.getMethod("valueOf", String.class);
            PACKET_PLAYOUT_PLAYER_INFO_CONST.setAccessible(true);
            PACKET_PLAYOUT_PLAYER_INFO_ENUM_BYSTRING.setAccessible(true);


            Class<?> PACKET_PLAYOUT_ENTITY_DESTROY = PrimeCore.getInstance().getVersionManager().getNMSClass("PacketPlayOutEntityDestroy");

            Constructor<?> PACKET_PLAYOUT_ENTITY_DESTROY_CONST = PACKET_PLAYOUT_ENTITY_DESTROY.getConstructor(Class.forName("[I"));

            Class<?> PACKET_PLAYOUT_ENTITY_SPAWN = PrimeCore.getInstance().getVersionManager().getNMSClass("PacketPlayOutNamedEntitySpawn");
            Constructor<?> PACKET_PLAYOUT_ENTITY_SPAWN_CONST = PACKET_PLAYOUT_ENTITY_SPAWN.getConstructor(HUMAN_PLAYER);
            Object type = PACKET_PLAYOUT_PLAYER_INFO_ENUM_BYSTRING.invoke(null, "REMOVE_PLAYER");
            Object packetType = PACKET_PLAYOUT_PLAYER_INFO_ENUM_BYSTRING.invoke(null, "REMOVE_PLAYER");
            Object removePlayerPacket = PACKET_PLAYOUT_PLAYER_INFO_CONST.newInstance(
                    packetType,
                    handleArray
            );

            NMS.sendPacket(removePlayerPacket, player);

            gameProfile.getProperties().removeAll("textures");
            gameProfile.getProperties().put("textures", new Property("textures", value, signature));


            Object addPlayerPacket = PACKET_PLAYOUT_PLAYER_INFO_CONST.newInstance(
                    PACKET_PLAYOUT_PLAYER_INFO_ENUM_BYSTRING.invoke(null, "ADD_PLAYER"),
                    handleArray
            );
            NMS.sendPacket(addPlayerPacket, player);


            for (Player all : Bukkit.getServer().getOnlinePlayers()) {
                if (all == player) {
                    continue;
                }
                if (!all.canSee(player)) {
                    continue;
                }

                NMS.sendPacket(PACKET_PLAYOUT_PLAYER_INFO_CONST.newInstance(
                        PACKET_PLAYOUT_PLAYER_INFO_ENUM_BYSTRING.invoke(null, "REMOVE_PLAYER"),
                        handle
                ), all);
                NMS.sendPacket(PACKET_PLAYOUT_PLAYER_INFO_CONST.newInstance(
                        PACKET_PLAYOUT_PLAYER_INFO_ENUM_BYSTRING.invoke(null, "ADD_PLAYER"),
                        handle
                ), all);


                NMS.sendPacket(
                        PACKET_PLAYOUT_ENTITY_DESTROY_CONST.newInstance(player.getEntityId())
                        , all);
                NMS.sendPacket(PACKET_PLAYOUT_ENTITY_SPAWN_CONST.newInstance(handle), all);


            }
            Location location = player.getLocation().clone();
            World world = Bukkit.getWorld(NickAPI.getInstance().getNickworldName());
            if (world != null) {
                player.teleport(world.getSpawnLocation());
                player.teleport(location);
            }

        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | NoSuchFieldException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static void changeNick(Player player, String name) {


        try {
            Class<?> BUKKIT_PLAYER = Class.forName("org.bukkit.craftbukkit." + PrimeCore.getInstance().getVersionManager().getNmsVersion() + ".entity.CraftPlayer");
            Object craftPlayer = BUKKIT_PLAYER.cast(player);
            GameProfile profile = (GameProfile) BUKKIT_PLAYER.getMethod("getProfile").invoke(craftPlayer);

            Field field = profile.getClass().getDeclaredField("name");
            field.setAccessible(true);
            field.set(profile, name);


        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private static Class<?> getSubClass(Class<?> c, String name) {
        for (Class<?> aClass : c.getClasses()) {
            if (aClass.getName().endsWith(name)) return aClass;
        }
        return null;
    }

}
