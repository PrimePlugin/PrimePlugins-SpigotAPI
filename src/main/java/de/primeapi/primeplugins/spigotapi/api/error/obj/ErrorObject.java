package de.primeapi.primeplugins.spigotapi.api.error.obj;

import de.primeapi.primeplugins.spigotapi.api.error.types.ErrorTypes;
import de.primeapi.primeplugins.spigotapi.api.error.types.Messages;
import lombok.Getter;

@Getter
public class ErrorObject {

    String help;
    String message;
    ErrorTypes errorTypes;

    public ErrorObject(Messages message, ErrorTypes errorTypes) {
        this.message = message.getS();
        this.help = message.getLink();
        this.errorTypes = errorTypes;
    }
}
