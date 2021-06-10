package de.primeapi.primeplugins.spigotapi.managers.api.placeholders;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.ClanAPI;
import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.managers.messages.CoreMessage;
import de.primeapi.primeplugins.spigotapi.sql.clan.SQLClan;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

/**
 * @author Lukas S. PrimeAPI
 * created on 10.06.2021
 * crated for PrimePlugins
 */
public class ClanPlaceholder extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "clan";
    }

    @Override
    public String getAuthor() {
        return "PrimeAPI";
    }

    @Override
    public String getVersion() {
        return PrimeCore.getInstance().getDescription().getVersion();
    }


    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if(ClanAPI.getInstance().isOnline()) {
            SQLClan clan = ClanAPI.getInstance().getClanFromPlayer(new PrimePlayer(player)).complete();
            if(clan == null){
                return CoreMessage.CLANPLACEHOLDER_NOCLAN.getContent();
            }
            switch (params.toLowerCase()) {
                case "name": {
                    return clan.getRealname().complete();
                }
                case "tag": {
                    return clan.getTag().complete();
                }
                case "count":{
                    return String.valueOf(clan.getMembers().complete().size());
                }
            }
        }
       return null;
    }
}
