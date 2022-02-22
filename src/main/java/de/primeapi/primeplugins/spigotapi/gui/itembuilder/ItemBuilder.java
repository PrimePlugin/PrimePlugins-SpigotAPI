package de.primeapi.primeplugins.spigotapi.gui.itembuilder;


import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

@Getter
@AllArgsConstructor
public class ItemBuilder {


    //vars
    Material material;
    Integer amount;
    Short damage;
    Byte subid;
    List<String> lore;
    String name;
    boolean lether = false;
    Color color;
    boolean skull = false;
    String skullOwner;
    String skullTexture;
    boolean glowing = false;
    boolean unbreakeble = false;
    HashMap<String, Integer> enchantments = new HashMap<>();


    //builder
    public ItemBuilder(Material material) {
        this.material = material;
    }

    public ItemBuilder(Material material, Byte subid) {
        this.material = material;
        this.subid = subid;
    }

    public ItemBuilder setUnbreakeble(boolean unbreakeble) {
        this.unbreakeble = unbreakeble;

        return this;
    }

    //methods

    public ItemBuilder setDamage(int damage) {
        this.damage = (short) damage;
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder addLore(String s) {
        List<String> lore;
        if (this.lore == null) {
            lore = new ArrayList<>();
        } else {
            lore = this.lore;
        }

        lore.add(s);

        this.lore = lore;
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, Integer level) {
        enchantments.put(enchantment.getName(), level);
        return this;
    }

    public ItemBuilder setDisplayName(String s) {
        this.name = s;
        return this;
    }

    public ItemBuilder setLeatherColor(Color color) {
        this.lether = true;
        this.color = color;
        return this;
    }

    public ItemBuilder setSkullOwner(String name) {
        this.skull = true;
        this.skullOwner = name;
        return this;
    }

    public ItemBuilder setSkullTexture(String skullTexture) {
        this.skull = true;
        this.skullTexture = skullTexture;
        return this;
    }

    public ItemBuilder setSkullTexture(SkullTexture skullTexture) {
        this.skull = true;
        this.skullTexture = skullTexture.getBase64();
        return this;
    }

    public ItemBuilder setGlowing() {
        glowing = true;
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public net.minecraft.server.v1_8_R3.ItemStack buildSkull() {
        if(!ItemUtils.getSkullCache().containsKey(skullOwner)) {
            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
            skullMeta.setOwner(skullOwner);
            head.setItemMeta(skullMeta);
            ItemUtils.getSkullCache().put(skullOwner, CraftItemStack.asNMSCopy(head));
            return CraftItemStack.asNMSCopy(head);
        } else {
            return ItemUtils.getSkullCache().get(skullOwner);
        }
    }

    public ItemStack build() {
        if (this.amount == null) this.amount = 1;
        if (this.damage == null) this.damage = 0;
        if (this.subid == null) this.subid = 0;
        if (this.lore == null) this.lore = new LinkedList<>();
        ItemStack itemStack = new ItemStack(this.material, this.amount, this.damage, this.subid);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (this.name != null) itemMeta.setDisplayName(this.name);
        itemMeta.setLore(this.lore);
        if (glowing) {
            itemMeta.addEnchant(new Glow(70), 1, true);
        }
        itemMeta.spigot().setUnbreakable(unbreakeble);
        itemStack.setItemMeta(itemMeta);
        if (this.lether) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
            leatherArmorMeta.setColor(this.color);
            itemStack.setItemMeta(leatherArmorMeta);
            return itemStack;
        }

        if (this.skull) {
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            skullMeta.setOwner(this.skullOwner);
            if (skullTexture != null) {

                GameProfile profile = new GameProfile(UUID.randomUUID(), "");
                profile.getProperties().put("textures", new Property("textures", skullTexture));
                Field profileField;
                try {
                    profileField = skullMeta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(skullMeta, profile);
                } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                    e.printStackTrace();
                }
            }
            itemStack.setItemMeta(skullMeta);
            return itemStack;
        }
        if (enchantments != null) {
            enchantments.forEach((s, integer) -> {
                itemStack.addUnsafeEnchantment(Enchantment.getByName(s), integer);
            });
        }
        return itemStack;
    }


}