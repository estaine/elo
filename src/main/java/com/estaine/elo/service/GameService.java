package com.estaine.elo.service;

public interface GameService {

    String registerMatch(String requester, String channelName, String request, String token);
    String registerTournamentMatch(String requester, String channelName, String request, String token);
}
