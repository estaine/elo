package com.estaine.elo.request;

import com.estaine.elo.entity.Game;
import com.estaine.elo.entity.Player;
import com.estaine.elo.entity.tournament.BoxGame;
import com.estaine.elo.properties.SlackProperties;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SlackNotifier {

    private static final String SLACK_NOTIFY_URL = "https://slack.com/api/chat.postMessage";

    private final SlackProperties properties;

    @Autowired
    public SlackNotifier(@NonNull SlackProperties properties) {
        this.properties = properties;
    }

    @Async
    public void sendCommonMatchNotifications(Player requester, Game game) {
        String notification = buildMatchNotification(requester, game);
        notifyPlayersAndChannel(notification, game.getAllParticipants());
    }

    @Async
    public void sendGroupMatchNotifications(Player requester, BoxGame boxGame) {
        String notification = buildGroupMatchNotification(requester, boxGame);
        notifyPlayersAndChannel(notification, boxGame.getAllParticipants());
    }

    private void notifyPlayersAndChannel(String notification, List<Player> players) {
        players.forEach(player -> sendSlackMessage(player.getImChannel(), notification));
        sendSlackMessage(properties.getChannelId(), notification);
    }

    @SneakyThrows
    private void sendSlackMessage(String conversationId, String notification) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(SLACK_NOTIFY_URL);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("token", properties.getToken()));
            params.add(new BasicNameValuePair("channel", conversationId));
            params.add(new BasicNameValuePair("text", notification));
            params.add(new BasicNameValuePair("as_user", "false"));
            params.add(new BasicNameValuePair("pretty", "1"));

            httpPost.setEntity(new UrlEncodedFormEntity(params));
            httpclient.execute(httpPost);
        }
    }

    private String buildMatchNotification(Player requester, Game game) {
        return requester.getFormattedSlackId() + " has registered the following match:\n"
                + game.getRedTeamPlayer1().getFormattedSlackId() + " " + game.getRedTeamPlayer2().getFormattedSlackId()
                + " *" + game.getRedTeamGoals() + ":" + game.getYellowTeamGoals() + "* "
                + game.getYellowTeamPlayer1().getFormattedSlackId() + " " + game.getYellowTeamPlayer2().getFormattedSlackId();
    }

    private String buildGroupMatchNotification(Player requester, BoxGame boxGame) {
        return requester.getFormattedSlackId() + " has registered the following group match in tournament : *"
                + boxGame.getBox().getTournament().getName() + "*\n"
                + boxGame.getRedTeam().getPlayer1().getFormattedSlackId() + " " + boxGame.getRedTeam().getPlayer2().getFormattedSlackId()
                + " *" + boxGame.getGame().getRedTeamGoals() + ":" + boxGame.getGame().getYellowTeamGoals() + "* "
                + boxGame.getYellowTeam().getPlayer1().getFormattedSlackId() + " " + boxGame.getYellowTeam().getPlayer2().getFormattedSlackId();
    }
}
