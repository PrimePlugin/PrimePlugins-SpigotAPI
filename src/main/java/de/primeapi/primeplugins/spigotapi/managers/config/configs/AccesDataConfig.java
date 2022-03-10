package de.primeapi.primeplugins.spigotapi.managers.config.configs;

import de.primeapi.primeplugins.spigotapi.managers.config.Config;
import lombok.SneakyThrows;

public class AccesDataConfig extends Config {

	private static AccesDataConfig instance;

	public AccesDataConfig() {
		super("Access data", "plugins/primeplugins/config.yml");
		instance = this;
	}

	public static AccesDataConfig getInstance() {
		return instance;
	}

	@SneakyThrows
	@Override
	public void loadContent() {

		saveAddEntry("mysql.host", "localhost:3306");
		saveAddEntry("mysql.database", "primeplugins");
		saveAddEntry("mysql.username", "username");
		saveAddEntry("mysql.password", "password");
		save();
	}
}
