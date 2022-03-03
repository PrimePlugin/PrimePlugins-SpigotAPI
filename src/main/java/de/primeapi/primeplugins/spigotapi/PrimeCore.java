package de.primeapi.primeplugins.spigotapi;

import com.github.davidmoten.rx.jdbc.Database;
import de.primeapi.primeplugins.spigotapi.api.placeholders.PlaceholderAPIManager;
import de.primeapi.primeplugins.spigotapi.api.plugin.RestPlugin;
import de.primeapi.primeplugins.spigotapi.api.plugins.bungee.BungeeAPI;
import de.primeapi.primeplugins.spigotapi.api.plugins.clan.ClanAPI;
import de.primeapi.primeplugins.spigotapi.api.plugins.coins.CoinsAPI;
import de.primeapi.primeplugins.spigotapi.api.plugins.friends.FriendsAPI;
import de.primeapi.primeplugins.spigotapi.api.plugins.nick.NickAPI;
import de.primeapi.primeplugins.spigotapi.api.plugins.perms.PermsAPI;
import de.primeapi.primeplugins.spigotapi.commands.PrimeCoreCommand;
import de.primeapi.primeplugins.spigotapi.events.*;
import de.primeapi.primeplugins.spigotapi.managers.chat.ChatManager;
import de.primeapi.primeplugins.spigotapi.managers.cloud.CloudManager;
import de.primeapi.primeplugins.spigotapi.managers.commands.CommandsManager;
import de.primeapi.primeplugins.spigotapi.managers.config.ConfigManager;
import de.primeapi.primeplugins.spigotapi.managers.config.configs.AccesDataConfig;
import de.primeapi.primeplugins.spigotapi.managers.config.configs.CoreConfig;
import de.primeapi.primeplugins.spigotapi.managers.messages.MessageManager;
import de.primeapi.primeplugins.spigotapi.managers.rest.RestManager;
import de.primeapi.primeplugins.spigotapi.managers.scoreboard.ScoreboardManager;
import de.primeapi.primeplugins.spigotapi.managers.vault.VaultManager;
import de.primeapi.primeplugins.spigotapi.managers.versions.VersionManager;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;

@Getter
public class PrimeCore extends JavaPlugin {

    private static PrimeCore instance;
    private MessageManager messageManager;
    private ConfigManager configManager;
    private Connection connection;
    private ThreadPoolExecutor threadPoolExecutor;
    private CommandsManager commandsManager;
    private PlaceholderAPIManager placeholderAPIManager;
    private ScoreboardManager scoreboardManager;
    private ChatManager chatManager;
    private Database db;
    private ClanAPI clanAPI;
    private CoinsAPI coinsAPI;
    private BungeeAPI bungeeAPI;
    private FriendsAPI friendsAPI;
    private NickAPI nickAPI;
    private PermsAPI permsAPI;
    private RestManager restManager;
    private VaultManager vaultManager;
    private CloudManager cloudManager;
    private VersionManager versionManager;
    private boolean mysql;

    public static PrimeCore getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        getLogger().info("---------------[ PrimeAPI | core ]---------------");
        getLogger().info("Plugin: PrimeCore");
        getLogger().info("Author: PrimeAPI");
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("---------------[ PrimeAPI | core ]---------------");

        versionManager = new VersionManager();
        messageManager = new MessageManager();
        configManager = new ConfigManager();
        registerConfigs();
        registerCommands();
        initSql();
        restManager = new RestManager();
        new RestPlugin("SpigotAPI", this);
        if (!mysql) {
            Bukkit.getPluginManager().registerEvents(new InvalidListener("§4§lFehler §7● §cDie MySQL Verbindung ist fehlerhaft!"), this);
            return;
        }
        commandsManager = new CommandsManager();
        scoreboardManager = new ScoreboardManager();
        chatManager = new ChatManager();
        registerApis();
        registerChannels();
        vaultManager = new VaultManager();
        cloudManager = new CloudManager();
        registerEvents();
        this.getLogger().log(Level.INFO, () -> "Das ist ein Test");
    }

    @SneakyThrows
    @Override
    public void onDisable() {
        if (getConnection() != null) {
            if (getNickAPI().isOnline()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    getNickAPI().removeFromDatabase(player);
                }
            }
            getConnection().close();
        }
    }

    private void registerChannels() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "prime:primemessaging");
    }

    private void registerApis() {
        clanAPI = new ClanAPI();
        placeholderAPIManager = new PlaceholderAPIManager();
        coinsAPI = new CoinsAPI();
        friendsAPI = new FriendsAPI();
        bungeeAPI = new BungeeAPI();
        permsAPI = new PermsAPI();
        nickAPI = new NickAPI();
    }

    private void registerCommands() {
        getCommand("primecore").setExecutor(new PrimeCoreCommand());
        getCommand("spigotapi").setExecutor(new PrimeCoreCommand());
    }

    private void registerConfigs() {
        configManager.registerConfig(new AccesDataConfig());
        configManager.registerConfig(new CoreConfig());
    }

    private void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new CoinsChanceListener(), this);
        pluginManager.registerEvents(new GroupChanceListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);
        pluginManager.registerEvents(new PlayerChatListener(), this);
        pluginManager.registerEvents(new PlayerMoveEventListener(), this);

    }


    private void initSql() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + AccesDataConfig.getInstance().getString("mysql.host") + "/" + AccesDataConfig.getInstance().getString("mysql.database") + "?autoReconnect=true", AccesDataConfig.getInstance().getString("mysql.username"), AccesDataConfig.getInstance().getString("mysql.password"));
            getLogger().info("MySQL-Connection established");
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS `core_players` (`id` INT NOT NULL AUTO_INCREMENT UNIQUE,`uuid` VARCHAR(36) NOT NULL UNIQUE,`name` VARCHAR(16) NOT NULL UNIQUE,`realname` VARCHAR(16) NOT NULL UNIQUE,`coins` INT NOT NULL,`playtime` INT NOT NULL,PRIMARY KEY (`id`));").execute();
            connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `core_settings` (" +
                            "`id` INT NOT NULL AUTO_INCREMENT UNIQUE," +
                            "`uuid` VARCHAR(36) NOT NULL," +
                            "`setting` VARCHAR(36) NOT NULL," +
                            "`value` INT," +
                            "PRIMARY KEY (`id`));"
            ).execute();
            db = Database.from(connection).asynchronous();
            mysql = true;
            getLogger().log(Level.INFO, "Asynchronous MySQL-Connection established");
        } catch (SQLException throwables) {
            mysql = false;
            getLogger().info("MySQL-Connection failed: " + throwables.getMessage());
        }
    }

}
