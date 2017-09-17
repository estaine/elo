package com.estaine.elo.request;

import com.estaine.elo.entity.Game;
import com.estaine.elo.entity.Player;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import sun.net.www.http.HttpClient;

@Component
public class SlackNotifier {

    private static final String BOT_TOKEN = "xoxb-241772942851-bqUjMYDkGjS1klTK6aM2D3Dw";
    private static final String SLACK_NOTIFY_URL = "https://slack.com/api/chat.postMessage";

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
            params.add(new BasicNameValuePair("token", BOT_TOKEN));
            params.add(new BasicNameValuePair("channel", notifyee.getImChannel()));
            params.add(new BasicNameValuePair("text", notification));
            params.add(new BasicNameValuePair("as_user", "false"));
            params.add(new BasicNameValuePair("pretty", "1"));

            httpPost.setEntity(new UrlEncodedFormEntity(params));
            httpclient.execute(httpPost);
        }
    }

    private String buildMatchNotification(Player requester, Game game) {
        return requester.getUsername() + " has registered the following match:\n"
                + game.getRedTeamPlayer1() + " " + game.getRedTeamPlayer2() + " vs "
                + game.getYellowTeamPlayer1() + " " + game.getYellowTeamPlayer2()
                + game.getRedTeamGoals() + ":" + game.getYellowTeamGoals();
    }
}
