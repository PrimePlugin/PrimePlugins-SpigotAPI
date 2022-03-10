package de.primeapi.primeplugins.spigotapi.managers.cloud.adapter.version;

import de.primeapi.primeplugins.spigotapi.managers.cloud.adapter.CloudAdapter;

import java.util.List;

/**
 * @author Jonas | Exceptionpilot#5555
 * Created on 28.05.2021 «» 15:11
 * Class «» SimpleCloud
 **/

public class SimpleCloud implements CloudAdapter {

	//TODO SIMPLECLOUD

	@Override
	public int getPlayersInGroup(String name) {
		return 0;
	}

	@Override
	public List<String> getAllLobbies(String name) {
		return null;
	}

	@Override
	public int getPlayersOnServer(String name) {
		return 0;
	}

	@Override
	public String getServerState(String name) {
		return null;
	}
}
