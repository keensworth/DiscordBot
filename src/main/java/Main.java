import bot.DiscordBot;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args)
            throws LoginException, InterruptedException
    {
        DiscordBot bot = new DiscordBot("<DISCORD-TOKEN>", "<YOUTUBE-KEY>");
        bot.run();
    }
}
