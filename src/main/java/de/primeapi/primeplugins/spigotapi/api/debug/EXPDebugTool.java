package de.primeapi.primeplugins.spigotapi.api.debug;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Jonas D. Exceptionpilot
 * created on 21.02.2022
 * created for PrimePlugins-SpigotAPI
 */

public class EXPDebugTool {

    Class aClass;

    @SneakyThrows
    public  EXPDebugTool(Class aClass) {
        this.aClass = aClass;
    }

    public void executeTestMethode(String methode, Object... object) {
        if(PrimeCore.getInstance().isDebug()) {
            int i = DebugUtils.cache(methode);
            Bukkit.broadcastMessage("§8[Debug§8] §e" + methode + " was executed! " + "\n§8[§eValue§8] §8->§7 " + object + "\n§8[§eInt§8] §c" + i);
        }
    }

    public String toString() {
        return "" +
                "Name:" + aClass.getSimpleName() + "\n"
                + "Function: " + aClass.getPackage().getName() + "\n"
                + "Methodes" + Arrays.stream(aClass.getMethods()) + "\n";
    }
}
