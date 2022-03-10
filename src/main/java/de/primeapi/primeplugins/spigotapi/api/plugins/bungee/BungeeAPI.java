package de.primeapi.primeplugins.spigotapi.api.plugins.bungee;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import lombok.Getter;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.logging.Level;

/**
 * @author Lukas S. PrimeAPI
 * created on 02.06.2021
 * crated for PrimePlugins
 */
@Getter
public class BungeeAPI {

	private static BungeeAPI instance;
	boolean online;

	public BungeeAPI() {
		instance = this;
		online = false;
		try {
			DatabaseMetaData md = PrimeCore.getInstance().getConnection().getMetaData();
			ResultSet rs = md.getTables(null, null, "prime_bungee_online", null);
			online = rs.next();
			rs.close();
		} catch (Exception throwables) {
			throwables.printStackTrace();
		}
		if (online) {
			PrimeCore.getInstance().getLogger().log(Level.INFO, "BungeeSystemAPI wurde geladen");
		} else {
			PrimeCore.getInstance().getLogger().log(Level.INFO, "BungeeSystemAPI wurde NICHT geladen");
		}
	}

	public static BungeeAPI getInstance() {
		return instance;
	}

}
