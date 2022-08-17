package de.primeapi.primeplugins.spigotapi.api.command.reflections;

import de.primeapi.primeplugins.spigotapi.api.PrimePlayer;
import de.primeapi.primeplugins.spigotapi.api.command.reflections.annotations.*;
import de.primeapi.primeplugins.spigotapi.managers.messages.CoreMessage;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;

/**
 * @author Lukas S. PrimeAPI
 * created on 17.08.2022
 * crated for PrimePlugins-ROOT
 */
@AllArgsConstructor
public class CommandExecutor implements org.bukkit.command.CommandExecutor {

	final Command command;
	final Object object;
	final Set<Method> subcommands;


	@Override
	public boolean onCommand(
			CommandSender commandSender, org.bukkit.command.Command command1, String s, String[] args
	                        ) {

		Method method = subcommands.stream().filter(m -> {
			String name = m.getAnnotation(SubCommand.class).name();
			String[] names = name.split(" ");
			for (int i = 0; i < names.length; i++) {
				if (names[i].startsWith("<") || names[i].startsWith("[")) continue;
				if(i >= args.length){
					for (int i1 = i; i1 < names.length; i1++) {
						if(!names[i1].startsWith("<")) return false;
					}
				}
				if(!names[i].equalsIgnoreCase(args[i])) return false;
			}
			return true;
		}).findAny().orElse(null);
		if(method == null){
			subcommands.forEach(method1 -> commandSender.sendMessage(CoreMessage.COMMAND_USAGE.replace("cmd", command.name() + " " + method1.getAnnotation(SubCommand.class).name()).getContent()));
			return true;
		}
		String name = method.getAnnotation(SubCommand.class).name();
		String[] names = name.toLowerCase().split(" ");
		Parameter[] parameters = method.getParameters();
		Object[] objects = new Object[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i].isAnnotationPresent(SenderField.class)) {

				if (CommandSender.class.equals(parameters[i].getType())) {
					objects[i] = commandSender;
				} else if (Player.class.equals(parameters[i].getType())) {
					if (commandSender instanceof Player) {
						objects[i] = (Player) commandSender;
					} else {
						commandSender.sendMessage(
								"This command can only be executed by a player");
					}
				} else if (PrimePlayer.class.equals(parameters[i].getType())) {
					if (commandSender instanceof Player) {
						objects[i] = PrimePlayer.fromPlayer((Player) commandSender);
					} else {
						commandSender.sendMessage(
								"This command can only be executed by a player");
					}
				} else {
					objects[i] = null;
				}
				continue;
			}

			if (parameters[i].isAnnotationPresent(SingleAttribute.class)) {
				if(i >= args.length){
					if(parameters[i].getAnnotation(SingleAttribute.class).required()){
						commandSender.sendMessage(CoreMessage.COMMAND_USAGE.replace("cmd", command.name() + " " + name).getContent());
						return true;
					}else {
						parameters[i] = null;
						continue;
					}
				}
				int pos = getIndex("<" + parameters[i].getAnnotation(SingleAttribute.class).name() + ">",
				                   names);
				if (pos == -1) {
					throw new IllegalArgumentException(
							"Parameter " + parameters[i].getAnnotation(SingleAttribute.class).name() + " not found in " +
									name + "!");
				}
				if (String.class.equals(parameters[i].getType())) {
					objects[i] = args[pos];
				} else if (Integer.class.equals(parameters[i].getType()) || int.class.equals(
						parameters[i].getType())) {
					try {
						objects[i] = Integer.parseInt(args[pos]);
					} catch (Exception ex) {
						commandSender.sendMessage(
								CoreMessage.COMMAND_INVALIDINPUT.replace("pos", i + 1)
								                                .replace("type",
								                                         parameters[i].getType()
								                                                      .getSimpleName()
								                                        )
								                                .getContent());
						return true;
					}
				} else {
					objects[i] = null;
				}
			}
			if (parameters[i].isAnnotationPresent(MultiAttribute.class)) {
				if(i >= args.length){
					if(parameters[i].getAnnotation(MultiAttribute.class).required()){
						commandSender.sendMessage(CoreMessage.COMMAND_USAGE.replace("cmd", command.name() + " " + name).getContent());
						return true;
					}else {
						parameters[i] = null;
						continue;
					}
				}
				int pos = getIndex("<" + parameters[i].getAnnotation(MultiAttribute.class).name() + ">", names);
				if (pos == -1) {
					throw new IllegalArgumentException(
							"Parameter " + parameters[i].getAnnotation(MultiAttribute.class).name() + " not found in name!");
				}
				if (String.class.equals(parameters[i].getType())) {
					StringBuilder value = new StringBuilder();
					for (int j = pos; j < args.length - 1; j++) {
						value.append(args[j]).append(" ");
					}
					value.append(args[args.length - 1]);
					objects[i] = value.toString();
				} else {
					objects[i] = null;
				}
			}

		}
		try {
			method.invoke(object, objects);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}



		return true;
	}


	private <T> int getIndex(T item, T[] array) {
		for (int i = 0; i < array.length; i++) {
			if (item.equals(array[i])) return i;
		}
		return -1;
	}
}
