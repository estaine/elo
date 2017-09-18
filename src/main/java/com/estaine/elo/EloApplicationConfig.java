package com.estaine.elo;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/tokens-prod.properties")
public class EloApplicationConfig {
}
