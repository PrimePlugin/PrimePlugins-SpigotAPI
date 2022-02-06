package de.primeapi.primeplugins.spigotapi;

import de.primeapi.primeplugins.spigotapi.api.RestPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author Lukas S. PrimeAPI
 * created on 31.05.2021
 * crated for PrimePlugins
 */
public class RestCore extends RestPlugin {

    public RestCore(PrimeCore plugin) {
        super("SpigotAPI", plugin);
    }


}
