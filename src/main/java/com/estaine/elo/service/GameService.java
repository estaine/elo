package com.estaine.elo.service;

public interface GameService {

    String registerMatch(String requesterUsername, String channelName, String request, String token);
    String registerGroupMatch(String requesterUsername, String channelName, String request, String token);

}
