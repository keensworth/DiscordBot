package bot.listeners;

import bot.DiscordBot;
import console.Console;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReadyListener extends ListenerAdapter {
    DiscordBot bot;

    public ReadyListener(){

    }

    public void init(DiscordBot bot){
        this.bot = bot;
    }

    @Override
    public void onReady(ReadyEvent event) {
        Console.print("API is ready...");
    }

    @Override
    public void onShutdown(ShutdownEvent event){
        Console.print("Shutting down...");
        bot.shutdown();
    }
}
