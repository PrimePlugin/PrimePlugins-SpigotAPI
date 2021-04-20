package de.primeapi.primeplugins.spigotapi.managers.scoreboard;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.ClanAPI;
import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.managers.config.configs.CoreConfig;
import de.primeapi.primeplugins.spigotapi.managers.scoreboard.objects.*;
import de.primeapi.primeplugins.spigotapi.sql.clan.SQLClan;
import de.primeapi.primeplugins.spigotapi.sql.clan.SQLPlayerAllocation;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ScoreboardManager {


    public ScoreboardSettings defaultSettings;
    public PrefixScoreboardSetting defaultPrefix;

    public HashMap<UUID, ScoreboardSettings> customScoreboard;

    public ScoreboardManager(){
        defaultSettings = new DefaultScoreboard();
        defaultPrefix = new DefaultPrefixScoreboard();
        customScoreboard = new HashMap<>();
    }

    public void sendScoreboard(@Nonnull Player p){
        if(!CoreConfig.getInstance().getBoolean("scoreboard.use")) return;
        Scoreboard scoreboard = new net.minecraft.server.v1_8_R3.Scoreboard();
        ScoreboardObjective obj = scoreboard.registerObjective("aaa", IScoreboardCriteria.b);
        ScoreboardSettings settings;
        if(!customScoreboard.containsKey(p.getUniqueId())){
            settings = defaultSettings;
        }else{
            settings = customScoreboard.get(p.getUniqueId());
        }
        obj.setDisplayName(settings.getTitle());
        PacketPlayOutScoreboardObjective createPacket = new PacketPlayOutScoreboardObjective(obj, 0);
        PacketPlayOutScoreboardDisplayObjective display = new PacketPlayOutScoreboardDisplayObjective(1, obj);
        PacketPlayOutScoreboardObjective removePacket = new PacketPlayOutScoreboardObjective(obj, 1);
        sendPacket(removePacket, p);
        sendPacket(createPacket, p);
        sendPacket(display, p);

        List<String> list = settings.apply(p);
        int i = list.size();
        for (String s :
                list) {
            ScoreboardScore score = new ScoreboardScore(scoreboard, obj, s);
            score.setScore(i);
            PacketPlayOutScoreboardScore pa = new PacketPlayOutScoreboardScore(score);
            sendPacket(pa, p);
            i--;
        }
    }

    public void sendTeams(@Nonnull Player p){
        if(!CoreConfig.getInstance().getBoolean("prefix.use")) return;
        org.bukkit.scoreboard.Scoreboard sb;
        if(p.getScoreboard() != null){
            sb = p.getScoreboard();
        }else {
            sb = Bukkit.getScoreboardManager().getNewScoreboard();
        }

        for (Team team :
                sb.getTeams()) {
            team.unregister();
        }

        List<ScoreboradTeam> teams = defaultPrefix.getTeams(p);
        Integer i = 1;
        for (ScoreboradTeam team :
                teams) {
            Team scoreTeam = sb.registerNewTeam(String.format("%03d", i));
            scoreTeam.setPrefix(team.getPrefix());
            if(CoreConfig.getInstance().getBoolean("prefix.overrideSuffixClanTags")){
                PrimePlayer primePlayer = new PrimePlayer(team.getPlayer());
                SQLClan clan = ClanAPI.getInstance().getClanFromPlayer(primePlayer).complete();
                scoreTeam.setSuffix(CoreConfig.getInstance().getString("prefix.clanTagFormat").replace("%tag%", clan.getTag().complete()));
            }else {
                scoreTeam.setSuffix(team.getSuffix());
            }
            scoreTeam.addPlayer(team.getPlayer());
            i++;
        }
        p.setScoreboard(sb);
    }

    public void sendTeams(){
        for (Player p :
                Bukkit.getOnlinePlayers()) {
            sendTeams(p);
        }
    }


    public void sendScoreboard(){
        for (Player p :
                Bukkit.getOnlinePlayers()) {
            sendScoreboard(p);
        }
    }

    private void sendPacket(@SuppressWarnings("rawtypes") Packet packet, Player p) {
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
    }
}
