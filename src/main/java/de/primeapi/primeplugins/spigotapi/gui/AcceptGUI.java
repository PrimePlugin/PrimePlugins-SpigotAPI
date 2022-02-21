package de.primeapi.primeplugins.spigotapi.gui;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.managers.messages.CoreMessage;
import gui.GUIBuilder;
import gui.itembuilder.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
@Setter
public class AcceptGUI {

    String inventoryTitle;
    String message;
    CallBack<Player> accepted;
    CallBack<Player> declined;

    public void open(Player p) {
        p.openInventory(
                new GUIBuilder(
                        3 * 9,
                        inventoryTitle
                )
                        .addItem(
                                12,
                                new ItemBuilder(Material.SKULL_ITEM, (byte) 3)
                                        .setDisplayName(CoreMessage.ACCEPTGUI_ACCEPT.getContent())
                                        .setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDMxMmNhNDYzMmRlZjVmZmFmMmViMGQ5ZDdjYzdiNTVhNTBjNGUzOTIwZDkwMzcyYWFiMTQwNzgxZjVkZmJjNCJ9fX0=")
                                        .build(),
                                (p1, itemStack) -> {
                                    accepted.run(p1);
                                }
                        )
                        .addItem(
                                14,
                                new ItemBuilder(Material.SKULL_ITEM, (byte) 3)
                                        .setDisplayName(CoreMessage.ACCEPTGUI_DECLINE.getContent())
                                        .setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==")
                                        .build(),
                                (p1, itemStack) -> {
                                    declined.run(p1);
                                }
                        )
                        .addItem(
                                13,
                                new ItemBuilder(Material.BOOK)
                                        .setDisplayName(message)
                                        .build()
                        )
                        .build(PrimeCore.getInstance())
        );
    }


    public interface CallBack<T> {
        void run(T value);
    }

}
