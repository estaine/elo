package com.estaine.elo.request;

import com.estaine.elo.entity.Game;
import com.estaine.elo.entity.Player;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SlackNotifier {

    private static final String SLACK_NOTIFY_URL = "https://slack.com/api/chat.postMessage";

    @Value("${kicker-bot-token}")
    private String kickerBotToken;

    @SneakyThrows
    public void notifyMatchParticipants(Player requester, Game game) {
        String notification = buildMatchNotification(requester, game);

        notifyMatchParticipant(game.getRedTeamPlayer1(), notification);
        notifyMatchParticipant(game.getRedTeamPlayer2(), notification);
        notifyMatchParticipant(game.getYellowTeamPlayer1(), notification);
        notifyMatchParticipant(game.getYellowTeamPlayer2(), notification);

    }

    @SneakyThrows
    private void notifyMatchParticipant(Player notifyee, String notification) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(SLACK_NOTIFY_URL);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("token", kickerBotToken));
            params.add(new BasicNameValuePair("channel", notifyee.getImChannel()));
            params.add(new BasicNameValuePair("text", notification));
            params.add(new BasicNameValuePair("as_user", "false"));
            params.add(new BasicNameValuePair("pretty", "1"));

            httpPost.setEntity(new UrlEncodedFormEntity(params));
            httpclient.execute(httpPost);
        }
    }

    private String buildMatchNotification(Player requester, Game game) {
        return "*TEST DATABASE*\n"
                + requester.getFormattedSlackId() + " has registered the following match:\n"
                + game.getRedTeamPlayer1().getFormattedSlackId() + " " + game.getRedTeamPlayer2().getFormattedSlackId()
                + " *" + game.getRedTeamGoals() + ":" + game.getYellowTeamGoals() + "* "
                + game.getYellowTeamPlayer1().getFormattedSlackId() + " " + game.getYellowTeamPlayer2().getFormattedSlackId();
    }
}
