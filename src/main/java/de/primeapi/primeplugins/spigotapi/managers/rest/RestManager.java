package de.primeapi.primeplugins.spigotapi.managers.rest;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.Unirest;
import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.plugin.RestPlugin;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.logging.Level;


/**
 * @author Lukas S. PrimeAPI
 * created on 26.05.2021
 * crated for PrimePlugins
 */
@Getter
public class RestManager {

//    private final static String SERVER = "https://cp.primeapi.de/api.php";

	private final static String HOST = "https://api.primeapi.de";

	private final Set<RestPlugin> plugins = new HashSet<>();
	@Setter
	private boolean checked = false;

	public void registerPlugin(RestPlugin plugin) {
		plugins.add(plugin);
		checked = false;
		PrimeCore.getInstance().getLogger().log(Level.INFO, "Das Plugin " + plugin.getName() + " wurde registriert!");
	}

	public PluginInfo getPlugininfo(String plugin) {
		try {
			Gson gson = new Gson();
			JsonElement element = new JsonParser().parse(Unirest.get(HOST + "/plugins").asString().getBody());
			List<JsonElement> plugins = new ArrayList<>();
			for (int i = 0; i < element.getAsJsonArray().size(); i++) {
				plugins.add(element.getAsJsonArray().get(i));
			}
			return plugins.stream()
			              .map(jsonElement -> gson.fromJson(jsonElement, PluginInfo.class))
			              .filter(pluginInfo -> pluginInfo.getName()
			                                              .equalsIgnoreCase(plugin) || pluginInfo.getDisplayname()
			                                                                                     .equalsIgnoreCase(
					                                                                                     plugin))
			              .findAny()
			              .orElse(null);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Info-Anfrage für " + plugin + " fehlgeschlagen: " + ex.getMessage());
			return null;
		}
	}


	public boolean downloadPlugin(PluginInfo pluginInfo, String license, String path) {

		String url = HOST + "/files/" + pluginInfo.getName() + "/" + pluginInfo.getLatest() + "?license=" + license;
		System.out.println("url = " + url);
        try {
	        InputStream in = new URL(url).openStream();
	        Files.copy(in, Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
	        return true;
        } catch (Exception e) {
            e.printStackTrace();
			return false;
        }

	}

	public boolean downloadPlugin(PluginInfo pluginInfo, String license, Class mainClass) {
		try {
			return downloadPlugin(pluginInfo, license, new File(
					mainClass.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return false;
		}
	}
}

