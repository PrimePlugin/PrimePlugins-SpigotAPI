package de.primeapi.primeplugins.spigotapi.managers.rest;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import de.primeapi.primeplugins.spigotapi.PrimeCore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.logging.Level;


/**
 * @author Lukas S. PrimeAPI
 * created on 26.05.2021
 * crated for PrimePlugins
 */
public class RestManager {

    private String server = "https://cp.primeapi.de/api.php";

    public PluginInfo getPlugininfo(String plugin){
        try {
            String url = server + "?action=plinfo&name=" + plugin;
            InputStream in = new URL(url).openStream();
            String jsonString = new Scanner(in, "UTF-8").useDelimiter("\\A").next();
            return new Gson().fromJson(jsonString, PluginInfo.class);
        }catch (Exception ex){
            PrimeCore.getInstance().getLogger().log(Level.INFO, "Info-Anfrage für " + plugin + " fehlgeschlagen: " + ex.getMessage());
            return null;
        }
    }

    public boolean validateLicense(String license, String plugin) {
        try {
            String url = server + "?action=verify&plugin=" + plugin + "&license=" + license;
            InputStream in = new URL(url).openStream();
            String jsonString = new Scanner(in, "UTF-8").useDelimiter("\\A").next();
            JsonElement element = new JsonParser().parse(jsonString);
            if (element.getAsJsonObject().get("code").getAsInt() == 200) {
                int result = element.getAsJsonObject().get("result").getAsInt();
                if (result == 1) {
                    PrimeCore.getInstance().getLogger().log(Level.INFO, "Lizenz für " + plugin + " erfolgreich überprüft");
                    return true;
                } else {
                    PrimeCore.getInstance().getLogger().log(Level.INFO, "Lizenz für " + plugin + " wurde abgelehnt: " + element.getAsJsonObject().get("resultMessage").getAsString());
                    return false;
                }
            } else {
                PrimeCore.getInstance().getLogger().log(Level.INFO, "Lizenzüberprüfung fehlgeschlagen: " + element.getAsJsonObject().get("message").getAsString());
                return false;
            }
        }catch (Exception ex){
            PrimeCore.getInstance().getLogger().log(Level.INFO, "Lizenzabfrage für " + plugin + " fehlgeschlagen: " + ex.getMessage());
            return true;
        }
    }

    public boolean downloadPlugin(String plugin, String license, String path){
        try {
            String url = server + "?action=download&plugin=" + plugin + "&license=" + license;
            InputStream in = new URL(url).openStream();
            String jsonString = new Scanner(in, "UTF-8").useDelimiter("\\A").next();
            JsonElement element = new JsonParser().parse(jsonString);
            PrimeCore.getInstance().getLogger().log(Level.INFO, element.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonParseException e){
            try {
                String url = server + "?action=download&plugin=" + plugin + "&license=" + license;
                InputStream in = new URL(url).openStream();
                Files.copy(in, Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
                return true;
            } catch (IOException ex) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public boolean downloadPlugin(String plugin, String license, Class mainClass){
        try {
            return downloadPlugin(plugin, license, new File(mainClass.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }
}

