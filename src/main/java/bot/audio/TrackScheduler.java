package bot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private GuildAudioManager guildAudioManager;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        player.setVolume(50);
        this.queue = new LinkedBlockingQueue<>();
    }

    public void setGuildAudioManager(GuildAudioManager guildAudioManager){
        this.guildAudioManager = guildAudioManager;
    }

    public void queue(AudioTrack track) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public void nextTrack() {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        player.startTrack(queue.poll(), false);
    }

    public void stopTrack(){
        player.stopTrack();
    }

    public void clearTracks(){
        queue.clear();
    }

    public long getTotalDuration(){
        long duration = 0;
        for (AudioTrack track : queue)
            duration += track.getDuration();
        return duration;
    }

    public int getQueueLength(){
        return queue.size();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if (endReason.mayStartNext) {
            guildAudioManager.next();
        }
    }
}