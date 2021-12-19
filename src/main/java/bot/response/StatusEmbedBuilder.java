package bot.response;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class StatusEmbedBuilder {
    public StatusEmbedBuilder(){

    }

    public EmbedBuilder getEmbedBuilder(String embedTitle, String videoId, String trackTitle, long trackDuration, int queueSize){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(embedTitle);
        eb.setColor(Color.red);

        long minutes = (trackDuration / 1000) / 60;
        long seconds = (trackDuration / 1000) % 60;
        String minutesString = String.format("%02d", minutes);
        String secondsString = String.format("%02d", seconds);


        String formattedTop = "["+trackTitle.substring(0, Math.min(trackTitle.length(), 35))+"](https://www.youtube.com/watch?v=" + videoId + ")... [" + minutesString + ":" + secondsString +"]\n";
        String formattedBottom = "(1/"+(queueSize+1)+")";

        eb.setDescription((formattedTop+formattedBottom));

        return eb;
    }
}
