package de.primeapi.primeplugins.spigotapi.api.command.reflections;

import de.primeapi.primeplugins.spigotapi.api.command.reflections.annotations.Command;
import de.primeapi.primeplugins.spigotapi.api.command.reflections.annotations.SubCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Lukas S. PrimeAPI
 * created on 17.08.2022
 * crated for PrimePlugins-ROOT
 */
public class CommandHandler {

	public static void loadCommands(JavaPlugin plugin, String parentPackage) throws NoSuchMethodException,
			InvocationTargetException, InstantiationException, IllegalAccessException {
		Reflections reflection = new Reflections(parentPackage);

		Set<Class<?>> classes = reflection.getTypesAnnotatedWith(Command.class);

		for (Class<?> aClass : classes) {
			Command command = aClass.getAnnotation(Command.class);

			Constructor<?> constructor = aClass.getConstructor();
			Set<Method> sub = Arrays.stream(aClass.getMethods())
			                        .filter(method -> method.isAnnotationPresent(SubCommand.class))
			                        .collect(
					                        Collectors.toSet());

			plugin.getCommand(command.name()).setExecutor(new CommandExecutor(command, constructor.newInstance(),
			                                                                  sub
			));


		}

	}

	private static <T> int getIndex(T item, T[] array) {
		for (int i = 0; i < array.length; i++) {
			if (item.equals(array[i])) return i;
		}
		return -1;
	}

}
