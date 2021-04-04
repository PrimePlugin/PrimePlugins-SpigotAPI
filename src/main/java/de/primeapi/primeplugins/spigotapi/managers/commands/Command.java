package de.primeapi.primeplugins.spigotapi.managers.commands;

import de.primeapi.primeplugins.spigotapi.commands.coins.CoinsCommand;
import lombok.Getter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

@Getter
public enum Command {
    COINS("coins", new CoinsCommand());

    String name;
    CommandExecutor command;
    TabCompleter completer;

    Command(String name, CommandExecutor command) {
        this.name = name;
        this.command = command;
    }

    Command(String name, CommandExecutor command, TabCompleter completer) {
        this.name = name;
        this.command = command;
        this.completer = completer;
    }
}
