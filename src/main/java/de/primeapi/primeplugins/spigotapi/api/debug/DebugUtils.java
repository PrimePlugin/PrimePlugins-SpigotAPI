package de.primeapi.primeplugins.spigotapi.api.debug;

import lombok.Getter;

import java.util.HashMap;

/**
 * @author Jonas D. Exceptionpilot
 * created on 21.02.2022
 * created for PrimePlugins-SpigotAPI
 */

public class DebugUtils {

    @Getter
    private static final HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();

    public static Integer cache(String s) {
        if(stringIntegerHashMap.containsKey(s)) {
            int i = stringIntegerHashMap.get(s);
            stringIntegerHashMap.remove(s);
            stringIntegerHashMap.put(s, i++);
            return stringIntegerHashMap.get(s);
        } else {
            stringIntegerHashMap.put(s, 1);
            return 1;
        }
    }

}
