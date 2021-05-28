package de.primeapi.primeplugins.spigotapi;

import com.github.davidmoten.rx.jdbc.Database;
import de.dytanic.cloudnet.ext.bridge.player.ICloudOfflinePlayer;
import de.primeapi.primeplugins.spigotapi.api.ClanAPI;
import de.primeapi.primeplugins.spigotapi.api.CoinsAPI;
import de.primeapi.primeplugins.spigotapi.api.FriendsAPI;
import de.primeapi.primeplugins.spigotapi.commands.PrimeCoreCommand;
import de.primeapi.primeplugins.spigotapi.events.*;
import de.primeapi.primeplugins.spigotapi.managers.api.CloudNetAdapter;
import de.primeapi.primeplugins.spigotapi.managers.chat.ChatManager;
import de.primeapi.primeplugins.spigotapi.managers.cloud.CloudManager;
import de.primeapi.primeplugins.spigotapi.managers.commands.CommandsManager;
import de.primeapi.primeplugins.spigotapi.managers.config.ConfigManager;
import de.primeapi.primeplugins.spigotapi.managers.api.PlaceholderAPIManager;
import de.primeapi.primeplugins.spigotapi.managers.config.configs.AccesDataConfig;
import de.primeapi.primeplugins.spigotapi.managers.config.configs.CoreConfig;
import de.primeapi.primeplugins.spigotapi.managers.rest.RestManager;
import de.primeapi.primeplugins.spigotapi.managers.scoreboard.ScoreboardManager;
import de.primeapi.primeplugins.spigotapi.managers.messages.MessageManager;
import de.primeapi.primeplugins.spigotapi.managers.vault.VaultManager;
import de.primeapi.primeplugins.spigotapi.utils.Logger;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.GroupDataEntity;
import org.bukkit.Bukkit;
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
    public static PrimeCore getInstance() {
        return instance;
    }
    private MessageManager messageManager;
    private ConfigManager configManager;
    private Connection connection;
    private ThreadPoolExecutor threadPoolExecutor;
    private CommandsManager commandsManager;
    private PlaceholderAPIManager placeholderAPIManager;
    private Logger coreLogger;
    private ScoreboardManager scoreboardManager;
    private ChatManager chatManager;
    private CloudNetAdapter cloudNetAdapter;
    private Database db;
    private ClanAPI clanAPI;
    private CoinsAPI coinsAPI;
    private FriendsAPI friendsAPI;
    private RestManager restManager;
    private VaultManager vaultManager;
    private CloudManager cloudManager;

    @Override
    public void onEnable() {
        instance = this;
        coreLogger = new Logger();
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        getCoreLogger().sendInfo("---------------[ PrimeAPI | core ]---------------");
        getCoreLogger().sendInfo("Plugin: PrimeCore");
        getCoreLogger().sendInfo("Author: PrimeAPI");
        getCoreLogger().sendInfo("Version: " + getDescription().getVersion());
        getCoreLogger().sendInfo("---------------[ PrimeAPI | core ]---------------");


        messageManager = new MessageManager();
        configManager = new ConfigManager();
        commandsManager = new CommandsManager();
        cloudNetAdapter = new CloudNetAdapter();
        placeholderAPIManager = new PlaceholderAPIManager();
        scoreboardManager = new ScoreboardManager();
        chatManager = new ChatManager();

        registerConfigs();
        initSql();
        registerEvents();

        getCommand("primecore").setExecutor(new PrimeCoreCommand());

        clanAPI = new ClanAPI();
        coinsAPI = new CoinsAPI();
        friendsAPI = new FriendsAPI();
        getServer().getMessenger().registerOutgoingPluginChannel( this, "primemessaging");
        restManager = new RestManager();
        vaultManager = new VaultManager();
        cloudManager = new CloudManager();
        cloudManager.check();
    }

    @Override
    public void onDisable() {
        try {
            getConnection().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void registerConfigs(){
        configManager.registerConfig(new AccesDataConfig());
        configManager.registerConfig(new CoreConfig());
    }

    private void registerEvents(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new CoinsChanceListener(), this);
        pm.registerEvents(new GroupChanceListener(), this);
        pm.registerEvents(new PlayerQuitListener(), this);
        pm.registerEvents(new PlayerChatListener(), this);
    }


    private void initSql(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + AccesDataConfig.getInstance().getString("mysql.host") + "/" + AccesDataConfig.getInstance().getString("mysql.database") + "?autoReconnect=true", AccesDataConfig.getInstance().getString("mysql.username"), AccesDataConfig.getInstance().getString("mysql.password"));
            getCoreLogger().sendInfo("MySQL-Connection established");
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
            getLogger().log(Level.INFO, "Asynchronous MySQL-Connection established");
        } catch (SQLException throwables) {
            getCoreLogger().sendInfo("MySQL-Connection failed: " + throwables.getMessage());
        }
    }

}
