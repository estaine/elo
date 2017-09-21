package com.estaine.elo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@ConfigurationProperties(prefix = "slack", ignoreUnknownFields = false)
@Component
@Validated
@PropertySource("classpath:slack.properties")
public class SlackProperties {

    @NotNull
    private String secretKey;

    @NotNull
    private String token;

    @NotNull
    private String channelId;

    @NotNull
    private String channelName;

}
