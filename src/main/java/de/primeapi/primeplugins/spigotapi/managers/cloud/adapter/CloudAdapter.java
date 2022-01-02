package de.primeapi.primeplugins.spigotapi.managers.cloud.adapter;

import java.util.List;

/**
 * @author Jonas | Exceptionpilot#5555
 * Created on 28.05.2021 «» 15:08
 * Class «» CloudAdapter
 **/

public interface CloudAdapter {

    int getPlayersInGroup(String name);

    List<String> getAllLobbies(String name);

    int getPlayersOnServer(String name);

    String getServerState(String name);
}
