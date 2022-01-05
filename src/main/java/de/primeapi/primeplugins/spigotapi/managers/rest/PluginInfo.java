package de.primeapi.primeplugins.spigotapi.managers.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Lukas S. PrimeAPI
 * created on 26.05.2021
 * crated for PrimePlugins
 */
@Getter
@Setter
@AllArgsConstructor
public class PluginInfo {
    int code;
    String message;
    int id;
    String name;
    String displayname;
    String version;
    String updateMessage;
    Long updateTime;

    public boolean isNeverVersion(String olderVersion) {
        String[] curr = olderVersion.split("\\.");
        String[] neww = getVersion().split("\\.");
        for (int i = 0; i < curr.length; i++) {
            if (neww.length > i) {
                int oldI = Integer.parseInt(curr[i]);
                int newI = Integer.parseInt(neww[i]);
                if (newI > oldI) {
                    return true;
                } else if (newI == oldI) {
                    continue;
                } else {
                    return false;
                }
            }
        }

        return false;
    }

}
