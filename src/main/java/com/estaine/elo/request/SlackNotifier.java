package com.estaine.elo.request;

import com.estaine.elo.entity.Player;
import java.net.URL;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class SlackNotifier {

    private static final String BOT_TOKEN = "xoxp-240713011895-239548176787-241322443521-241e619aa5d3c18d4bdf4b42e6ba3a34";
    private static final String SLACK_NOTIFY_URL = "https://slack.com/api/chat.postMessage";

    @SneakyThrows
    public void notify(List<String> playerIDs) {
        URL url = new URL(SLACK_NOTIFY_URL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        connection.setRequestMethod("GET");


    }
}
