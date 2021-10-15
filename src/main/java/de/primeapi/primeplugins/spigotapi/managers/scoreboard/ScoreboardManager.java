package de.primeapi.primeplugins.spigotapi.managers.scoreboard;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.ClanAPI;
import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.managers.config.configs.CoreConfig;
import de.primeapi.primeplugins.spigotapi.managers.scoreboard.objects.*;
import de.primeapi.primeplugins.spigotapi.managers.scoreboard.objects.utils.BPlayerBoard;
import de.primeapi.primeplugins.spigotapi.managers.scoreboard.objects.utils.Board;
import de.primeapi.primeplugins.spigotapi.managers.versions.MinecraftVersion;
import de.primeapi.primeplugins.spigotapi.sql.clan.SQLClan;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ScoreboardManager {


    public ScoreboardSettings defaultSettings;
    public PrefixScoreboardSetting defaultPrefix;
    public int updateTick;

    public HashMap<UUID, ScoreboardSettings> customScoreboard;

    public ScoreboardManager(){
        defaultSettings = new DefaultScoreboard();
        defaultPrefix = new DefaultPrefixScoreboard();
        customScoreboard = new HashMap<>();
        updateTick = CoreConfig.getInstance().getInt("scoreboard.update.seconds");
        startUpdateTick();
    }

    public void sendScoreboard(@Nonnull Player p){
        if(!CoreConfig.getInstance().getBoolean("scoreboard.use")) return;
        ScoreboardSettings settings;
        if(!customScoreboard.containsKey(p.getUniqueId())){
            settings = defaultSettings;
        }else{
            settings = customScoreboard.get(p.getUniqueId());
        }

        BPlayerBoard bPlayerBoard =
                (Board.instance().getBoard(p) != null) ?
                        Board.instance().getBoard(p) :
                        Board.instance().createBoard(p, ChatColor.translateAlternateColorCodes('&', settings.getTitle()));

        bPlayerBoard.clear();

        List<String> list = settings.apply(p);
        int i = list.size();
        for (String s :
                list) {
            bPlayerBoard.set(s, i);
            i--;
        }
    }

    public void sendTeams(@Nonnull Player p) {
        if (!CoreConfig.getInstance().getBoolean("prefix.use")) return;
        org.bukkit.scoreboard.Scoreboard sb;
        if (p.getScoreboard() != null) {
            sb = p.getScoreboard();
        } else {
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
            if(PrimeCore.getInstance().getVersionManager().currentVersion.isHigherEqualThan(MinecraftVersion.V1_16)){
                ChatColor chatColor = ChatColor.getByChar(team.getColor().replace("&", "").replace("§", ""));
                scoreTeam.setPrefix(team.getPrefix());
                try {
                    Method method = scoreTeam.getClass().getMethod("setColor", ChatColor.class);
                    method.setAccessible(true);
                    method.invoke(scoreTeam, chatColor);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }else {
                scoreTeam.setPrefix(team.getPrefix() + team.getColor());
            }
            if(CoreConfig.getInstance().getBoolean("prefix.overrideSuffixClanTags") && ClanAPI.getInstance().isOnline()){
                PrimePlayer primePlayer = new PrimePlayer(team.getPlayer());
                SQLClan clan = ClanAPI.getInstance().getClanFromPlayer(primePlayer).complete();
                if(clan != null) {
                    scoreTeam.setSuffix(CoreConfig.getInstance().getString("prefix.clanTagFormat").replace("%tag%", clan.getTag().complete()));
                }else{
                    scoreTeam.setSuffix(team.getSuffix());
                }
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

    private void startUpdateTick(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PrimeCore.getInstance(), () -> {
            try {
                sendScoreboard();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, updateTick * 20L, updateTick * 20L);
    }

}
