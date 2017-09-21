## Properties

Need to specify slack properties location that will contains:
```
slack.secret-key=SECRET
slack.token=SLACK_TOKEN
```

Start with:

`java -jar elo-*.jar --spring.config.location=classpath:/slack.properties,file:./slack.properties`