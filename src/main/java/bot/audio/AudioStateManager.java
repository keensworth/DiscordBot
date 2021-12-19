package bot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import console.Console;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.managers.AudioManager;
import bot.response.ResponseHandler;
import bot.response.StatusEmbedBuilder;

public class AudioStateManager implements EventListener {
    // -------------------[JDA Specific]-------------------
    // For getting guild-specific bot.audio access (per join)
    private AudioManager audioManager;
    // -------------------[LavaPlayer Specific]------------
    // For creating players, loading, and skipping music (once per join, delete on leave)
    private GuildAudioManager guildAudioManager;
    // For generating a player (once)
    private AudioPlayerManager audioPlayerManager;
    // -------------------[State Management]---------------
    private Message config;
    private boolean audioManagerCreated;
    private boolean connectedToVoiceChannel;
    private long timeOfInactivity;
    private boolean isActive;
    private long statusMessage;


    public AudioStateManager(){
        audioManagerCreated = false;
        connectedToVoiceChannel = false;
        isActive = false;
        timeOfInactivity = Long.MAX_VALUE;
        statusMessage = -1;
    }


    public void init() {
        audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
    }


    public void initPlayer(Guild guild){
        audioManager = guild.getAudioManager();
        guildAudioManager = new GuildAudioManager(audioPlayerManager, this);
        guildAudioManager.linkScheduler();
        audioManager.setSendingHandler(guildAudioManager.getSendHandler());
        audioManagerCreated = true;
    }


    public void queueResource(String resource, Message config){
        Console.print("Queueing resource");
        this.config = config;
        guildAudioManager.loadAndQueue(audioPlayerManager, resource);
    }


    public void skip(Message config){
        Console.print("Skipping resource");
        guildAudioManager.next();
        isActive = true;
        this.config = config;
    }


    public void stop(Message config){
        Console.print("Stopping resource");
        guildAudioManager.stop();
        isActive = false;
        timeOfInactivity = System.currentTimeMillis();
        this.config = config;
    }


    public void clearQueue(Message config){
        Console.print("Clearing queue");
        guildAudioManager.clearQueue();
        this.config = config;
    }


    public void connect(VoiceChannel voiceChannel){
        Console.print("Connecting to voice");
        audioManager.openAudioConnection(voiceChannel);
        connectedToVoiceChannel = true;
    }


    public void disconnect(){
        if (audioManager != null) {
            audioManager.closeAudioConnection();
            Console.print("Disconnecting from voice");
        }
        connectedToVoiceChannel = false;
        audioManagerCreated = false;
        isActive = false;
        timeOfInactivity = System.currentTimeMillis();
    }


    private void pollStatus(){
        if (guildAudioManager != null) {
            if (guildAudioManager.getTotalDuration() == 0 && isActive) {
                isActive = false;
                timeOfInactivity = System.currentTimeMillis() + guildAudioManager.audioTracks.peek().getDuration();
                Console.print("Becoming inactive: " + guildAudioManager.audioTracks.peek().getDuration() + "ms left");
            }
        }

        if (connectedToVoiceChannel && !isActive){
            if (System.currentTimeMillis() - timeOfInactivity > 300000)
                disconnect();
        }
    }

    public void updateStatusMessage(String embedTitle){
        StatusEmbedBuilder statusEmbed = new StatusEmbedBuilder();
        if (statusMessage != -1) {
            config.getChannel().deleteMessageById(statusMessage).queue();
            statusMessage = -1;
        }

        AudioTrack track = guildAudioManager.audioTracks.peek();
        if (track == null)
            return;

        EmbedBuilder statusEmbedBuilder = statusEmbed.getEmbedBuilder(
                embedTitle,
                track.getInfo().identifier,
                track.getInfo().title,
                track.getDuration(),
                guildAudioManager.getQueueLength());

        ResponseHandler.sendEmbedMessage(config, statusEmbedBuilder, this);
    }


    public long getStatusMessage(){
        return statusMessage;
    }


    public void setStatusMessage(long statusMessage){
        this.statusMessage = statusMessage;
    }


    @Override
    public void onEvent(GenericEvent event) {
        pollStatus();
    }


    public boolean audioManagerCreated(){
        return audioManagerCreated;
    }


    public boolean connectedToVoiceChannel(){
        return connectedToVoiceChannel;
    }


    public boolean setIsActive(){
        isActive = true;
        return isActive;
    }


    public void shutdown(){
        disconnect();
        audioPlayerManager.shutdown();
        if (statusMessage != -1) {
            config.getChannel().deleteMessageById(statusMessage).queue();
            statusMessage = -1;
        }
    }
}
