package de.primeapi.primeplugins.spigotapi.api.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Lukas S. PrimeAPI
 * created on 27.05.2021
 * crated for PrimePlugins
 */
@Getter
@Setter
@AllArgsConstructor
public class CashedItem<V> {
    V value;
    Long timeout;
}
