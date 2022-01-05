package de.primeapi.primeplugins.spigotapi.managers.config.configs;

import de.primeapi.primeplugins.spigotapi.managers.config.Config;

import java.util.ArrayList;
import java.util.List;

public class CoreConfig extends Config {

    private static CoreConfig instance;

    public CoreConfig() {
        super("Core-Config", "plugins/primeplugins/core/config.yml");
        instance = this;
    }

    public static CoreConfig getInstance() {
        return instance;
    }

    @Override
    public void loadContent() {
        saveAddEntry("settings.coins.startAmount", 1000);

        {
            List<String> scoreboard = new ArrayList<>();
            scoreboard.add("§1 ");
            scoreboard.add("§7Rang");
            scoreboard.add("§8➥ §e%rank_display%");
            scoreboard.add("§2 ");
            scoreboard.add("§7Coins");
            scoreboard.add("§8➥ §e%prime_coins%");
            scoreboard.add("§3 ");
            scoreboard.add("§7Spielzeit");
            scoreboard.add("§8➥ §e%prime_ontime_4%");
            scoreboard.add("§4 ");
            saveAddEntry("scoreboard.use", true);
            saveAddEntry("scoreboard.default.title", "§7» §eServer.de §7«");
            saveAddEntry("scoreboard.default.content", scoreboard);
            saveAddEntry("scoreboard.default.applyOnJoin", true);
            saveAddEntry("scoreboard.update.seconds", 5);
            saveAddEntry("scoreboard.onlyOnState.use", true);
            saveAddEntry("scoreboard.onlyOnState.state", "LOBBY");

            saveAddEntry("prefix.use", true);
            saveAddEntry("prefix.defaultPrefix", "");
            saveAddEntry("prefix.defaultColor", "§7");
            saveAddEntry("prefix.defaultSuffix", "");
            saveAddEntry("prefix.overrideSuffixClanTags", true);
            saveAddEntry("prefix.clanTagFormat", " §7[§e%tag%§7]");
            saveAddEntry("prefix.onlyOnState.use", true);
            saveAddEntry("prefix.onlyOnState.state", "LOBBY");

            saveAddEntry("chatformat.use", true);
            saveAddEntry("chatformat.default", "§7%name% §8| §7%message%");

            saveAddEntry("autoafk.use", true);
            saveAddEntry("autoafk.detect.seconds", 30);
            saveAddEntry("autoafk.detect.delay", 30);

            saveAddEntry("vault.permissions", true);
            saveAddEntry("vault.coins", true);

            saveAddEntry("clanplaceholder.formattedTag.format", "§8[§e%tag%§8]");
            saveAddEntry("clanplaceholder.formattedTag.noClan", "");
        }

    }
}
