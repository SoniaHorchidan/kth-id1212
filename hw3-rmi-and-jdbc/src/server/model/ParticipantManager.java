package server.model;

import common.Client;

import java.util.HashMap;
import java.util.Map;

public class ParticipantManager {
    private Map<String, Client> activeUsers;

    public ParticipantManager() {
        activeUsers = new HashMap<>();
    }

    public void addParticipant(String username, Client remoteObj) {
        activeUsers.put(username, remoteObj);
    }

    public void removeParticipant(String username) {
        activeUsers.remove(username);
    }

    public Client findUser(String username) {
        return activeUsers.get(username);
    }

}