package com.estaine.elo.request;

import com.estaine.elo.entity.Match;
import com.estaine.elo.entity.Player;
import com.estaine.elo.entity.tournament.GroupMatch;
import com.estaine.elo.entity.tournament.GroupMatch;
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
    public void sendCommonMatchNotifications(Player requester, Match match) {
        String notification = buildMatchNotification(requester, match);
        notifyPlayersAndChannel(notification, match.getAllParticipants());
    }

    @Async
    public void sendGroupMatchNotifications(Player requester, GroupMatch groupMatch) {
        String notification = buildGroupMatchNotification(requester, groupMatch);
        notifyPlayersAndChannel(notification, groupMatch.getAllParticipants());
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

    private String buildMatchNotification(Player requester, Match match) {
        return requester.getFormattedSlackId() + " has registered the following match:\n"
                + match.getRedTeamPlayer1().getFormattedSlackId() + " " + match.getRedTeamPlayer2().getFormattedSlackId()
                + " *" + match.getRedTeamGoals() + ":" + match.getYellowTeamGoals() + "* "
                + match.getYellowTeamPlayer1().getFormattedSlackId() + " " + match.getYellowTeamPlayer2().getFormattedSlackId();
    }

    private String buildGroupMatchNotification(Player requester, GroupMatch groupMatch) {
        return requester.getFormattedSlackId() + " has registered the following group match in tournament : *"
                + groupMatch.getGroup().getTournament().getName() + "*\n"
                + groupMatch.getRedTeam().getPlayer1().getFormattedSlackId() + " " + groupMatch.getRedTeam().getPlayer2().getFormattedSlackId()
                + " *" + groupMatch.getMatch().getRedTeamGoals() + ":" + groupMatch.getMatch().getYellowTeamGoals() + "* "
                + groupMatch.getYellowTeam().getPlayer1().getFormattedSlackId() + " " + groupMatch.getYellowTeam().getPlayer2().getFormattedSlackId();
    }
}
