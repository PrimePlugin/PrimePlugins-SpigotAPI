package de.primeapi.primeplugins.spigotapi.api.cache;

import lombok.NonNull;

import java.util.HashMap;

/**
 * @author Lukas S. PrimeAPI
 * created on 27.05.2021
 * crated for PrimePlugins
 */
public class Cache<K, V> {

    public long timeout;
    private final HashMap<K, CashedItem<V>> map = new HashMap<>();

    /**
     * @param timeout Specifies a timeout in milliseconds after which the value will not be stored anymore. Use -1 for infinite
     */
    public Cache(long timeout) {
        this.timeout = timeout;
    }

    /**
     * This automatically sets the timeout to 5 Minutes
     */
    public Cache() {
        timeout = 300000;
    }

    /**
     * This cashes a Value with a specific Key
     *
     * @param key   The Key of the Entry
     * @param value The Value
     */
    public void cacheEntry(@NonNull K key, V value) {
        map.put(key, new CashedItem<>(value, System.currentTimeMillis() + timeout));
    }

    /**
     * Gets a cached value
     *
     * @param key The key the value was cached to (see: {@link Cache#cacheEntry(Object, Object)}
     * @return Null if the key is not set. Otherwise, returns the value;
     */
    public V getCachedValue(K key) {
        if (!map.containsKey(key)) {
            return null;
        }
        CashedItem<V> item = map.get(key);
        if (System.currentTimeMillis() > item.getTimeout() && item.getTimeout() != -1) {
            map.remove(key);
            return null;
        } else {
            return item.getValue();
        }
    }

    /**
     * Removes the cached V prior to timeout
     *
     * @param key The key which shall be removed
     */
    public void unCache(K key) {
        map.remove(key);
    }

    /**
     * Removes the cached V prior to timeout
     *
     * @param key The key which shall be removed
     */
    public void remove(K key) {
        map.remove(key);
    }
}
