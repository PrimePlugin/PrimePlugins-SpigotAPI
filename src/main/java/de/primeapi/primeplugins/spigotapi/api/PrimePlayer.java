package de.primeapi.primeplugins.spigotapi.api;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.managers.messages.CoreMessage;
import de.primeapi.primeplugins.spigotapi.managers.scoreboard.objects.ScoreboardSettings;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PrimePlayer extends SQLPlayer {

    final Player p;

    public PrimePlayer(Player player) {
        super(player.getUniqueId());
        this.p = player;
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
        PrimeCore.getInstance().getScoreboardManager().sendScoreboard(p);
    }

    @Override
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

}
