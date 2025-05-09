# zax-bot
A Slack bot for playing interactive fiction games like those made by Infocom 
in the 1980s.

Implemented as a Slack bot wrapper around Zax, a Java based Z-Machine Emulator.

## Configuration
Create a configuration.properties file with the following:
```
    slack-client-secret=slack client secret here
    slack-signing-secret=slack signing secret here
    api-token=fill in your api token here
    game-directory=/path/to/folder/for/games
``` 
## Running from source

To run the bot, type: `gradlew run`
