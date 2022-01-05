package de.primeapi.primeplugins.spigotapi.managers.chat;

import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.managers.chat.objects.ChatFormatter;
import de.primeapi.primeplugins.spigotapi.managers.chat.objects.DefaultChatFormatter;
import lombok.Getter;

@Getter
public class ChatManager {

    public ChatFormatter chatFormatter;


    public ChatManager() {
        chatFormatter = new DefaultChatFormatter();

    }

    public String format(PrimePlayer p, String message) {
        return chatFormatter.formatString(p, message);
    }

}
