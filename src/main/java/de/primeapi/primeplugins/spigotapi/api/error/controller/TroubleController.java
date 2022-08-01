package de.primeapi.primeplugins.spigotapi.api.error.controller;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.api.error.obj.ErrorObject;
import lombok.Getter;

public class TroubleController {

    @Getter
    private static TroubleController instance;

    public TroubleController() {
        instance = this;
    }

    public TroubleController addError(ErrorObject message) {
        PrimeCore.getInstance().getTroubleShooter().getErrorList().add(message);
        return this;
    }
}
