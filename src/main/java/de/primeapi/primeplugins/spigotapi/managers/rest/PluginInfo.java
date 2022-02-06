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

    int id;
    String name;
    String latest;
    String displayname;

    public boolean isNeverVersion(String olderVersion) {
        String[] curr = olderVersion.split("[._]");
        String[] neww = getLatest().split("[._]");
        for (int i = 0; i < curr.length; i++) {
            if (neww.length > i) {
                try {
                    int oldI = Integer.parseInt(curr[i]);
                    int newI = Integer.parseInt(neww[i]);
                if (newI > oldI) {
                    return true;
                } else if (newI == oldI) {
                    continue;
                } else {
                    return false;
                }
                }catch (Exception EX){continue;}
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "PluginInfo{" + "id=" + id + ", name='" + name + '\'' + ", latest='" + latest + '\'' + ", displayname" +
                "='" + displayname + '\'' + '}';
    }
}
