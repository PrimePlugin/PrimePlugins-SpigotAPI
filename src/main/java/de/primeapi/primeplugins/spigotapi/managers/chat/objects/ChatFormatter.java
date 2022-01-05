package de.primeapi.primeplugins.spigotapi.managers.chat.objects;

import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;

public interface ChatFormatter {

    String formatString(PrimePlayer player, String message);
}
