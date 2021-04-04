package de.primeapi.primeplugins.spigotapi.events;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.managers.config.configs.CoreConfig;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        {
            SQLPlayer sqlPlayer = SQLPlayer.create(e.getPlayer().getUniqueId(), e.getPlayer().getName());
            sqlPlayer.updateName(e.getPlayer().getName());
        }
        PrimePlayer p = new PrimePlayer(e.getPlayer());

        if(CoreConfig.getInstance().getBoolean("scoreboard.default.applyOnJoin")){
            p.sendScoreboard();
        }

        PrimeCore.getInstance().getScoreboardManager().sendTeams();
    }

}
