package de.primeapi.primeplugins.spigotapi.gui.itembuilder;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.ItemStack;

import java.util.HashMap;

/**
 * @author Jonas D. Exceptionpilot
 * created on 22.02.2022
 * created for PrimePlugins-SpigotAPI
 */

public class ItemUtils {

    @Getter
    private static final HashMap<String, ItemStack> skullCache = new HashMap<>();
}
