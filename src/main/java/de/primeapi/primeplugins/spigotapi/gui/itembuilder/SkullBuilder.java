package de.primeapi.primeplugins.spigotapi.gui.itembuilder;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import net.minecraft.server.v1_8_R3.ItemStack;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;


/**
 * @author Jonas D. Exceptionpilot
 * created on 22.02.2022
 * created for PrimePlugins-SpigotAPI
 */

public class SkullBuilder {

    private static final HashMap<String, ItemStack> skullCache = new HashMap();
    private static BukkitTask skullCacheClear = null;
    private final org.bukkit.inventory.ItemStack stack;
    private final ArrayList<String> allLores;

    public static void startCacheClear(Plugin plugin) {
        if (skullCacheClear == null && plugin != null) {
            skullCacheClear = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, skullCache::clear, 36000L, 36000L);
        }
    }

    public SkullBuilder(Material material) {
        this.stack = new org.bukkit.inventory.ItemStack(material);
        this.allLores = new ArrayList();
    }

    public SkullBuilder(org.bukkit.inventory.ItemStack stack) {
        this.stack = stack;
        this.allLores = new ArrayList();
    }

    public static SkullBuilder getPlayerSkull(String player) {
        if (!skullCache.containsKey(player)) {
            org.bukkit.inventory.ItemStack head = new org.bukkit.inventory.ItemStack(Material.SKULL_ITEM, 1, (short)3);
            SkullMeta meta = (SkullMeta)head.getItemMeta();
            meta.setOwner(player);
            head.setItemMeta(meta);
            skullCache.put(player, CraftItemStack.asNMSCopy(head));
            return new SkullBuilder(head);
        } else {
            return new SkullBuilder(CraftItemStack.asBukkitCopy((ItemStack)skullCache.get(player)));
        }
    }

    public static SkullBuilder getUrlSkull(String url) {
        if (!skullCache.containsKey("http://textures.minecraft.net/texture/" + url)) {
            org.bukkit.inventory.ItemStack skull = new org.bukkit.inventory.ItemStack(Material.SKULL_ITEM, 1, (short)3);
            if (url != null && !url.isEmpty()) {
                url = "http://textures.minecraft.net/texture/" + url;
                ItemMeta skullMeta = skull.getItemMeta();
                GameProfile profile = new GameProfile(UUID.randomUUID(), (String)null);
                byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
                profile.getProperties().put("textures", new Property("textures", new String(encodedData)));

                try {
                    Field profileField = skullMeta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(skullMeta, profile);
                } catch (Throwable var6) {
                    var6.printStackTrace();
                    return new SkullBuilder(skull);
                }

                skull.setItemMeta(skullMeta);
                skullCache.put(url, CraftItemStack.asNMSCopy(skull));
                return new SkullBuilder(skull);
            } else {
                return new SkullBuilder(skull);
            }
        } else {
            return new SkullBuilder(CraftItemStack.asBukkitCopy((ItemStack)skullCache.get("http://textures.minecraft.net/texture/" + url)));
        }
    }

    public SkullBuilder setType(Material material) {
        this.stack.setType(material);
        return this;
    }

    public SkullBuilder setDisplayName(String name) {
        ItemMeta meta = this.stack.getItemMeta();
        meta.setDisplayName(name);
        this.stack.setItemMeta(meta);
        return this;
    }

    public SkullBuilder addLore(String... lore) {
        this.allLores.addAll(Arrays.asList(lore));
        return this;
    }

    public SkullBuilder setAmount(int i) {
        this.stack.setAmount(i);
        return this;
    }

    public SkullBuilder addEnchantment(Enchantment enchantment, int level) {
        ItemMeta meta = this.stack.getItemMeta();
        meta.addEnchant(enchantment, level, true);
        this.stack.setItemMeta(meta);
        return this;
    }

    public SkullBuilder setUnbreakable(boolean b) {
        ItemMeta meta = this.stack.getItemMeta();
        meta.spigot().setUnbreakable(b);
        meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_UNBREAKABLE});
        this.stack.setItemMeta(meta);
        return this;
    }

    public SkullBuilder addItemFlags(ItemFlag... flag) {
        ItemMeta meta = this.stack.getItemMeta();
        meta.addItemFlags(flag);
        this.stack.setItemMeta(meta);
        return this;
    }

    public SkullBuilder setDurability(short durability) {
        this.stack.setDurability(durability);
        return this;
    }

    public SkullBuilder setLeatherColor(String colorCode) {
        if (!this.stack.getType().toString().contains("LEATHER")) {
            return this;
        } else {
            colorCode = "0x" + colorCode;
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)this.stack.getItemMeta();
            leatherArmorMeta.setColor(Color.fromRGB(java.awt.Color.decode(colorCode).getRed(), java.awt.Color.decode(colorCode).getGreen(), java.awt.Color.decode(colorCode).getBlue()));
            this.stack.setItemMeta(leatherArmorMeta);
            return this;
        }
    }

    public SkullBuilder setSkullOwner(String owner) {
        if (!this.stack.getType().equals(Material.SKULL_ITEM)) {
            return this;
        } else {
            SkullMeta meta = (SkullMeta)this.stack.getItemMeta();
            meta.setOwner(owner);
            this.stack.setItemMeta(meta);
            return this;
        }
    }

    public SkullBuilder setFireworkColor(Color... colors) {
        if (!this.stack.getType().equals(Material.FIREWORK_CHARGE)) {
            return this;
        } else {
            FireworkEffectMeta meta = (FireworkEffectMeta)this.stack.getItemMeta();
            meta.setEffect(FireworkEffect.builder().withColor(colors).build());
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_POTION_EFFECTS});
            this.stack.setItemMeta(meta);
            return this;
        }
    }

    public org.bukkit.inventory.ItemStack build() {
        ItemMeta meta = this.stack.getItemMeta();
        meta.setLore(this.allLores);
        this.stack.setItemMeta(meta);
        return this.stack;
    }
}
