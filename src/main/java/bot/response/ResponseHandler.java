package bot.response;

import bot.audio.AudioStateManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.io.File;

public class ResponseHandler {
    public ResponseHandler(){ }

    public static void sendMessage(Message config, String message){
        MessageChannel channel = config.getChannel();
        channel.sendMessage(message).queue();
    }

    public static void sendMessage(MessageChannel config, String message){
        config.sendMessage(message).queue();
    }


    public static void sendEmbedMessage(Message config, EmbedBuilder embed, AudioStateManager audioStateManager){
        MessageChannel channel = config.getChannel();
        channel.sendMessageEmbeds(embed.build()).queue((message) -> {
            long messageId = message.getIdLong();
            audioStateManager.setStatusMessage(messageId);
        });
    }


    public static void sendFile(Message config, File file){
        MessageChannel channel = config.getChannel();
        channel.sendFile(file).queue();
    }


    public static void sendTestEmbed(Message config){
        MessageChannel channel = config.getChannel();
        EmbedBuilder eb = new EmbedBuilder();
        channel.sendMessageEmbeds(eb.build()).queue();
    }


    public static VoiceChannel getVoiceChannel(Message config, String failResponse){
        VoiceChannel voiceChannel = null;

        try {
            voiceChannel = config.getMember().getVoiceState().getChannel();
        } catch (NullPointerException ignored){ }

        if (voiceChannel == null)
            ResponseHandler.sendMessage(config, failResponse);

        return voiceChannel;
    }
}
