package de.primeapi.primeplugins.spigotapi.api.error;

import de.primeapi.primeplugins.spigotapi.api.error.obj.ErrorObject;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TroubleShooter {


    @Getter
    private static TroubleShooter instance;
    private final List<ErrorObject> errorList = new ArrayList<>();
    int checks;

    public TroubleShooter() {
        instance = this;
    }

    public void sendCheckingData(Player player) {
        player.sendMessage("§bSystem §7● §cDein Server wird nun überprüft!");
        player.sendMessage("§bSystem §7● §aErfolgreiche checks! §8[§e" + checks + "§8]");
        for (ErrorObject s : errorList) {
            TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText(s.getErrorTypes().getS() + "\n§7" + s.getMessage() + " \n§8[§fHilfe§8]\n" + s.getErrorTypes().getS()));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, s.getHelp()));
            player.spigot().sendMessage(textComponent);
        }
    }

    public void done() {
        checks++;
    }
}
