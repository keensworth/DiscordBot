package bot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import console.Console;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class GuildAudioManager {
    public final AudioPlayer player;
    public final TrackScheduler scheduler;
    private final AudioStateManager audioStateManager;
    public Queue<AudioTrack> audioTracks;
    private boolean pollOnPlay;

    public GuildAudioManager(AudioPlayerManager manager, AudioStateManager audioStateManager) {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
        this.audioStateManager = audioStateManager;
        audioTracks = new LinkedBlockingQueue<>();
        pollOnPlay = false;
    }

    public void linkScheduler(){
        scheduler.setGuildAudioManager(this);
    }

    public void loadAndQueue(AudioPlayerManager playerManager, String resourceIdentifier){
        playerManager.loadItem(resourceIdentifier, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                scheduler.queue(track);
                audioTracks.add(track);
                Console.print("Track loaded successfully: " + track.getInfo().identifier + ", " + track.getDuration() + "ms");
                if (pollOnPlay){
                    audioTracks.poll();
                    pollOnPlay = false;
                }
                audioStateManager.updateStatusMessage("Playing...");
                audioStateManager.setIsActive();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (AudioTrack track : playlist.getTracks()) {
                    scheduler.queue(track);
                }
                audioStateManager.setIsActive();
            }

            @Override
            public void noMatches() {
                // Notify the user that we've got nothing
                Console.printWarn("Track not loaded successfully");
            }

            @Override
            public void loadFailed(FriendlyException throwable) {
                // TODO
            }
        });
    }

    public void next(){
        scheduler.nextTrack();
        audioTracks.poll();
        audioStateManager.updateStatusMessage("Playing...");
        pollOnPlay = false;
    }

    public void stop(){
        scheduler.stopTrack();
        audioStateManager.updateStatusMessage("Stopped...");
        pollOnPlay = true;
    }

    public void clearQueue(){
        scheduler.clearTracks();
    }

    public long getTotalDuration(){
        return scheduler.getTotalDuration();
    }

    public int getQueueLength(){
        return scheduler.getQueueLength();
    }

    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }
}
