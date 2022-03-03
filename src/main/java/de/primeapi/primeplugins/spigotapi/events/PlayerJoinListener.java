package de.primeapi.primeplugins.spigotapi.events;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.api.plugin.RestPlugin;
import de.primeapi.primeplugins.spigotapi.managers.config.configs.CoreConfig;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerJoinListener implements Listener {

    private boolean update = false;
    private String msg = "";

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        {
            SQLPlayer.create(event.getPlayer().getUniqueId(), event.getPlayer().getName()).submit(sqlPlayer -> {
                sqlPlayer.updateName(event.getPlayer().getName());
            });
        }

        PrimePlayer primePlayer = PrimePlayer.fromPlayer(event.getPlayer());

        if (CoreConfig.getInstance().getBoolean("scoreboard.default.applyOnJoin")) {
            primePlayer.sendScoreboard();
        }

        if (!PrimeCore.getInstance().isMysql()) {
            for (int i = 0; i < 5; i++) {
                primePlayer.getPlayer().sendMessage("§8[§ePrimeCore§8] §4§lDie MySQL ist nicht verbunden§8! §7Bitte überprüfe deine Daten!");
            }
        }

        if (CoreConfig.getInstance().getBoolean("prefix.use")) {
            PrimeCore.getInstance().getScoreboardManager().sendTeams();
        }

        if (!PrimeCore.getInstance().getRestManager().isChecked()) {
            List<String> updates = new ArrayList<>();
            for (RestPlugin plugin : PrimeCore.getInstance().getRestManager().getPlugins()) {
                if (plugin.isNewUpdateAvailable()) {
                    update = true;
                    updates.add(plugin.getName());
                }
            }

            if (updates.size() >= 1) {
                update = true;
                msg = "§8[§c§lCoreAPI§8] §eFür folgende Plugins ist ein update verfügbar: ";
                for (String s : updates) {
                    msg += "§b" + s + "§e, ";
                }
                msg += "\n" + "§8[§c§lCoreAPI§8] §7Verwende: /spigotapi update <all|[pluginName]>";
            }
            PrimeCore.getInstance().getRestManager().setChecked(true);
        }

        if (update && event.getPlayer().hasPermission("primeplugins.update")) {
            event.getPlayer().sendMessage(msg);
        }
        PlayerMoveEventListener.lastMove.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

}
