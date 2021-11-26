package de.primeapi.primeplugins.spigotapi.events;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.RestCore;
import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.api.RestPlugin;
import de.primeapi.primeplugins.spigotapi.managers.config.configs.CoreConfig;
import de.primeapi.primeplugins.spigotapi.managers.messages.CoreMessage;
import de.primeapi.primeplugins.spigotapi.managers.rest.RestManager;
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
    public void onPlayerJoin(PlayerJoinEvent e){
        {
            SQLPlayer.create(e.getPlayer().getUniqueId(), e.getPlayer().getName()).submit(sqlPlayer -> {
                sqlPlayer.updateName(e.getPlayer().getName());
            });
        }
        PrimePlayer p = new PrimePlayer(e.getPlayer());

        if(CoreConfig.getInstance().getBoolean("scoreboard.default.applyOnJoin")){
            p.sendScoreboard();
        }


        if(!PrimeCore.getInstance().isMysql()) {
            for(int i = 0; i < 5; i++) {
                p.getPlayer().sendMessage("§8[§ePrimeCore§8] §4§lDie MySQL ist nicht verbunden§8! §7Bitte überprüfe deine Daten!");
            }
        }

        if (CoreConfig.getInstance().getBoolean("prefix.use")) {
            PrimeCore.getInstance().getScoreboardManager().sendTeams();
        }

        if(!PrimeCore.getInstance().getRestManager().isChecked()){
            List<String> updates = new ArrayList<>();
            for (RestPlugin plugin : PrimeCore.getInstance().getRestManager().getPlugins()) {
                if(plugin.isNewUpdateAvailable()){
                    update = true;
                    updates.add(plugin.getName());
                }
            }

            if(updates.size() >= 1){
                msg = "§8[§c§lCoreAPI§8] §eFür folgende Plugins ist ein update verfügbar: ";
                for (String s : updates) {
                    msg += "§b" + s + "§e, ";
                }
                msg += "\n" + "§8[§c§lCoreAPI§8] §7Verwende: /spigotapi update <all|[pluginName]>";
            }
            PrimeCore.getInstance().getRestManager().setChecked(true);
        }

        if(update && e.getPlayer().hasPermission("primeplugins.update")){
            e.getPlayer().sendMessage(msg);
        }
        MoveListener.lastMove.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

}
