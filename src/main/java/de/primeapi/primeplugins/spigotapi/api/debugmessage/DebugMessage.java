package de.primeapi.primeplugins.spigotapi.api.debugmessage;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.*;
import de.primeapi.primeplugins.spigotapi.managers.config.ConfigManager;
import de.primeapi.primeplugins.spigotapi.managers.config.configs.AccesDataConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor @AllArgsConstructor @Getter
public class DebugMessage {

    final String secret;
    List<PrimePluginInfo> primePlugins;
    List<DebugPluginInfo> plugins;
    boolean mysql;
    String mysqlDatabase;
    boolean bungeeAPI;
    boolean clanAPI;
    boolean coinsAPI;
    boolean friendsAPI;
    boolean permsAPI;
    String ipAdress;
    String serverName;
    String serverVersion;
    String javaVersion;


    public static DebugMessage send(String secret, Player sender){
        List<PrimePluginInfo> primePlugins = PrimePluginInfo.getPlugins();
        List<DebugPluginInfo> plugins = DebugPluginInfo.getPluginInfos();
        boolean mysql = PrimeCore.getInstance().getConnection() != null;
        String mysqlDatabase = AccesDataConfig.getInstance().getString("mysql.database");
        boolean bungeeAPI = BungeeAPI.getInstance().isOnline();
        boolean clanAPI = ClanAPI.getInstance().isOnline();
        boolean coinsAPI = CoinsAPI.getInstance().isOnline();
        boolean friendsAPI = FriendsAPI.getInstance().isOnline();
        boolean permsAPI = PermsAPI.getInstance().isOnline();
        String ipAdress = null;
        try {
            ipAdress = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String servername = Bukkit.getServerName();
        String serverVersion = Bukkit.getVersion();
        String javaVersion = System.getProperty("java.version");
        DebugMessage message = new DebugMessage(
                secret,
                primePlugins,
                plugins,
                mysql,
                mysqlDatabase,
                bungeeAPI,
                clanAPI,
                coinsAPI,
                friendsAPI,
                permsAPI,
                ipAdress,
                servername,
                serverVersion,
                javaVersion
        );
        Gson gson = new Gson();
        String json = gson.toJson(message);
        HttpResponse<String> s = null;
        try {
            s = Unirest.post("http://mc.primeapi.de:8083/debugs/" + secret)
                    .header("Authorization", sender.getUniqueId().toString())
                    .body(json)
                    .asString();


            Collection<File> files = new ArrayList<>();
            files.add(new File("logs/latest.log"));
            files.addAll(PrimeCore.getInstance().getConfigManager().getAllFiles());

            HttpResponse<String> fileResponse = Unirest.post("http://mc.primeapi.de:8083/debugs/" + secret + "/files")
                    .header("Authorization", sender.getUniqueId().toString()).field("files", files).asString();

            if(s.getStatus() == 200 && fileResponse.getStatus() == 200){
                sender.sendMessage("§aErfolgreich!");
            }else {
                sender.sendMessage("§4Fehler§7: §c" + s.getBody() + "§7 | §c" + fileResponse.getBody());
            }

        } catch (UnirestException e) {
            e.printStackTrace();
            sender.sendMessage("Fehler");
        }

        return message;
    }


}
