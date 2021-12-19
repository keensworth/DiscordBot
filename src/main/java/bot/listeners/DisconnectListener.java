package bot.listeners;

import bot.audio.AudioStateManager;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DisconnectListener extends ListenerAdapter {
    private AudioStateManager audioStateManager;

    public DisconnectListener(AudioStateManager audioStateManager){
        this.audioStateManager = audioStateManager;
    }
    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event){
        if (event.getMember().getIdLong() == event.getJDA().getSelfUser().getIdLong()){
            if (!event.getMember().getVoiceState().inVoiceChannel())
                audioStateManager.disconnect();
        }
    }
}
