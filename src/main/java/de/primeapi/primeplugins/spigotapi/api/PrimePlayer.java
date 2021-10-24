package de.primeapi.primeplugins.spigotapi.api;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.managers.config.configs.CoreConfig;
import de.primeapi.primeplugins.spigotapi.managers.messages.CoreMessage;
import de.primeapi.primeplugins.spigotapi.managers.scoreboard.objects.ScoreboardSettings;
import de.primeapi.primeplugins.spigotapi.sql.DatabaseTask;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;
import de.primeapi.primeplugins.spigotapi.sql.party.PlayerParty;
import de.primeapi.primeplugins.spigotapi.sql.utils.OnlineStats;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PrimePlayer extends SQLPlayer {


    private static Set<PrimePlayer> onlinePlayers = new HashSet<>();

    final Player p;

    /**
     * @deprecated use {@link PrimePlayer#fromPlayer(Player)}
     * @param player
     */
    @Deprecated
    public PrimePlayer(Player player) {
        super(player.getUniqueId());
        this.p = player;
    }

    public static PrimePlayer fromPlayer(Player p){
        return onlinePlayers
                .stream()
                .filter(primePlayer -> primePlayer.getUniqueId().equals(p.getUniqueId()))
                .findFirst()
                .orElseGet(() -> new PrimePlayer(p));
    }

    public Player getPlayer(){ return p;}
    public Player thePlayer(){ return p;}

    public void sendMessage(CoreMessage message){
        thePlayer().sendMessage(message.getContent());
    }

    public void sendNoPerm(String permission){
        sendMessage(CoreMessage.NO_PERMS.replace("permission", permission.toLowerCase()));
    }

    public void setScoreboard(ScoreboardSettings scoreboardSettings){
        PrimeCore.getInstance().getScoreboardManager().customScoreboard.put(p.getUniqueId(), scoreboardSettings);
    }

    public void sendScoreboard(){
        if(CoreConfig.getInstance().getBoolean("scoreboard.use")) {
            PrimeCore.getInstance().getScoreboardManager().sendScoreboard(p);
        }
    }

    public UUID getUniqueId(){
        return thePlayer().getUniqueId();
    }

    public boolean hasPermission(String permission){
        return thePlayer().hasPermission(permission);
    }

    public boolean checkPermission(String permission){
        if(!thePlayer().hasPermission(permission)){
            sendNoPerm(permission);
            return false;
        }else{
            return true;
        }
    }

    public void setAFK(boolean b){
        OnlineStats.setAFK(getUniqueId(), b);
    }

    public void setParty(PlayerParty party){
        OnlineStats.setParty(getUniqueId(), party.getOwner());
    }

    public DatabaseTask<Boolean> getAFK() {
        return OnlineStats.getAFK(getUniqueId());
    }

    public DatabaseTask<PlayerParty> getParty() {
        return OnlineStats.getParty(getUniqueId());
    }


}
