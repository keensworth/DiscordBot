package bot;

import bot.audio.AudioStateManager;
import bot.listeners.DisconnectListener;
import bot.listeners.MuteListener;
import bot.listeners.ReadyListener;
import bot.message.MessageListener;
import bot.message.MessageParser;
import bot.response.CLICommandManager;
import bot.response.CommandManager;
import bot.response.PhraseManager;
import console.Console;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import server.MultiClientServer;

import javax.security.auth.login.LoginException;

public class DiscordBot {
    public JDA jda;
    public AudioStateManager audioStateManager;
    CommandManager commandManager;
    PhraseManager phraseManager;
    CLICommandManager cliCommandManager;
    public MessageParser messageParser;
    Console console;
    ReadyListener readyListener;
    public MultiClientServer server;

    String token;
    String youtubeKey;

    boolean running;

    public DiscordBot(String discordToken, String youtubeKey) throws LoginException, InterruptedException {
        this.token = discordToken;
        this.youtubeKey = youtubeKey;
    }

    public void run() throws LoginException, InterruptedException {
        console = new Console();
        server = new MultiClientServer(8088, this);
        audioStateManager = new AudioStateManager();
        audioStateManager.init();
        commandManager = new CommandManager();
        commandManager.init(audioStateManager, youtubeKey);
        cliCommandManager = new CLICommandManager();
        phraseManager = new PhraseManager();
        readyListener = new ReadyListener();
        messageParser = new MessageParser( commandManager, phraseManager, cliCommandManager);

        // build JDA
        jda = JDABuilder.createDefault(token)
                .addEventListeners(readyListener)
                .addEventListeners(new MessageListener(messageParser))
                .addEventListeners(audioStateManager)
                .addEventListeners(new MuteListener())
                .addEventListeners(new DisconnectListener(audioStateManager))
                .setActivity(Activity.playing("The Game"))
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();

        // block until JDA is ready
        jda.awaitReady();

        // initialize members
        cliCommandManager.init(this);
        readyListener.init(this);
        running = true;

        // start server
        Console.printBanner();
        server.start();

        // listen for commands from client/server
        poll();
    }

    public void shutdown(){
        server.close();
        running = false;
    }

    private void poll(){
        while (running){
            String consoleInput = Console.promptUser("");
            messageParser.parseCommandLineMessage(consoleInput);
        }
    }
}
