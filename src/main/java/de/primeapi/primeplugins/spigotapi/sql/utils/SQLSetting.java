package de.primeapi.primeplugins.spigotapi.sql.utils;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.util.sql.queries.Retriever;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

import java.util.Arrays;

@AllArgsConstructor
public enum SQLSetting {
    WARTUNG("false"),
    MOTD_NORMAL_1("§b§lBungeeSystem §3by PrimeAPI §8[§71.8.x-1.16.x§8]"),
    MOTD_NORMAL_2("§e§lhttps://primeapi.de"),
    MOTD_WARTUNG_1("§b§lBungeeSystem §3by PrimeAPI §8[§71.8.x-1.16.x§8]"),
    MOTD_WARTUNG_2("§e§lhttps://primeapi.de §8x §c§lWartungen"),
    SLOTS("50"),
    ;
    String standardValue;


    public static void insertDatabase() {
        Arrays.stream(SQLSetting.values()).forEach(sqlSetting -> {
            sqlSetting.getValue().submit(s -> {
            });
        });
    }

    public Retriever<String> getValue() {
        return PrimeCore.getInstance().getDb().select(
                                "SELECT value FROM prime_bungee_settings WHERE identifier = ?"
                                                           ).parameters(this.toString())
                        .execute(String.class)
                        .get()
                        .map(s -> {
                            if (s == null) {
                                setValue(standardValue);
                                return standardValue;
                            } else {return s;}
                        });
    }

    public void setValue(String value) {
        PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
            String s = PrimeCore.getInstance().getDb().select(
                                        "SELECT value FROM prime_bungee_settings WHERE identifier = ?"
                                                                   ).parameters(this.toString()).execute(String.class)
                                .get().complete();
            if (s == null) {
                PrimeCore.getInstance().getDb().update(
                        "INSERT INTO prime_bungee_settings VALUES (id,?,?)"
                                                            ).parameters(this.toString(), value).execute();
            } else {
                PrimeCore.getInstance().getDb().update(
                        "UPDATE prime_bungee_settings SET value = ? WHERE identifier = ?"
                                                            ).parameters(value, this.toString()).execute();
            }
        });
    }

    public Retriever<Boolean> getAsBoolean() {
        return new Retriever<>(() -> {
            String value = getValue().complete();
            return Boolean.valueOf(value);
        });
    }

    public Retriever<Integer> getAsInteger() {
        return new Retriever<>(() -> {
            String value = getValue().complete();
            return Integer.parseInt(value);
        });
    }

}
