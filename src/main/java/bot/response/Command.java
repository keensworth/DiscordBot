package bot.response;

import bot.audio.AudioStateManager;
import bot.audio.VideoFetcher;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.net.URL;


public interface Command {
    int minParams = 0;
    int maxParams = 0;
    int execute(String[] params, Message msg);
}

class PlayCommand implements Command {
    AudioStateManager audioStateManager;
    String youtubeKey;

    public PlayCommand(AudioStateManager audioStateManager, String youtubeKey){
        this.audioStateManager = audioStateManager;
        this.youtubeKey = youtubeKey;
    }

    public int execute(String[] params, Message msg){
        VoiceChannel voiceChannel = ResponseHandler.getVoiceChannel(msg, "You must be in a voice channel to use this command.");
        if (voiceChannel == null)
            return 0;

        String[] rawMessage = msg.getContentRaw().split("\\s+", 2);
        String resource = rawMessage[1];

        if (!isValidURL(resource)){
            resource = VideoFetcher.fetchIDFromQuery(resource, youtubeKey);
        } else {
            if (!resource.contains("youtube.com")){
                ResponseHandler.sendMessage(msg, "YouTube only \uD83D\uDE14");
                return 0;
            }
        }

        if (!audioStateManager.connectedToVoiceChannel()){
            audioStateManager.initPlayer(voiceChannel.getGuild());
            audioStateManager.connect(voiceChannel);
        }

        audioStateManager.queueResource(resource, msg);
        return 0;
    }

    private boolean isValidURL(String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}


class SkipCommand implements Command {
    AudioStateManager audioStateManager;

    public SkipCommand(AudioStateManager audioStateManager){
        this.audioStateManager = audioStateManager;
    }

    public int execute(String[] params, Message msg){
        if (!audioStateManager.connectedToVoiceChannel()){
            ResponseHandler.sendMessage(msg, "You would. I am not currently connected to a voice channel.");
            return 0;
        }

        VoiceChannel voiceChannel = ResponseHandler.getVoiceChannel(msg, "You must be in a voice channel to use this command.");
        if (voiceChannel == null)
            return 0;

        audioStateManager.skip(msg);
        return 0;
    }
}


class StopCommand implements Command {
    AudioStateManager audioStateManager;

    public StopCommand(AudioStateManager audioStateManager){
        this.audioStateManager = audioStateManager;
    }

    public int execute(String[] params, Message msg){
        if (!audioStateManager.connectedToVoiceChannel()){
            ResponseHandler.sendMessage(msg, "You would. I am not currently connected to a voice channel.");
            return 0;
        }

        VoiceChannel voiceChannel = ResponseHandler.getVoiceChannel(msg, "You must be in a voice channel to use this command.");
        if (voiceChannel == null)
            return 0;

        audioStateManager.stop(msg);
        return 0;
    }
}


class ClearQueueCommand implements Command {
    AudioStateManager audioStateManager;

    public ClearQueueCommand(AudioStateManager audioStateManager){
        this.audioStateManager = audioStateManager;
    }

    public int execute(String[] params, Message msg){
        if (!audioStateManager.connectedToVoiceChannel()){
            ResponseHandler.sendMessage(msg, "You would. I am not currently connected to a voice channel.");
            return 0;
        }

        VoiceChannel voiceChannel = ResponseHandler.getVoiceChannel(msg, "You must be in a voice channel to use this command.");
        if (voiceChannel == null)
            return 0;

        audioStateManager.clearQueue(msg);
        return 0;
    }
}


class RPSCommand implements Command {
    int minParams = 1;
    int maxParams = 1;
    public int execute(String[] params, Message msg){
        if (params.length < minParams+1 || params.length > maxParams+1)
            return -1;

        // rock (0.33) - paper (0.67) - scissors (1.0)
        float roll = (float) Math.random();
        String rollName = roll < 0.33 ? "Rock." : roll < 0.67 ? "Paper." : "Scissors.";
        String result = "";
        switch (params[1]){
            case "rock":
                return roll < 0.33 ? tie(rollName, msg) : roll < 0.67 ? loss(rollName, msg) : win (rollName, msg);
            case "paper":
                return roll < 0.33 ? win(rollName, msg) : roll < 0.67 ? tie(rollName, msg) : loss (rollName, msg);
            case "scissors":
                return roll < 0.33 ? loss(rollName, msg) : roll < 0.67 ? win(rollName, msg) : tie (rollName, msg);
        }
        return 0;
    }

    private int loss(String roll, Message config){
        ResponseHandler.sendMessage(config, roll + " You lose, doinkus");
        return 0;
    }

    private int win(String roll, Message config){
        ResponseHandler.sendMessage(config, roll + " You win");
        return 0;
    }

    private int tie(String roll, Message config){
        ResponseHandler.sendMessage(config, roll + " You would");
        return 0;
    }
}


class ShutdownCommand implements Command {
    AudioStateManager audioStateManager;

    public ShutdownCommand(AudioStateManager audioStateManager){
        this.audioStateManager = audioStateManager;
    }

    public int execute(String[] params, Message msg){
        if (msg.getAuthor().getIdLong() == 295252837959467009L) {
            audioStateManager.shutdown();
            msg.getJDA().shutdown();
        }
        return 0;
    }
}
