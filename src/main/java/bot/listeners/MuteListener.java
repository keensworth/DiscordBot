package bot.listeners;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMuteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MuteListener extends ListenerAdapter {
    @Override
    public void onGuildVoiceMute( GuildVoiceMuteEvent event){
        if (event.getMember().getIdLong() == event.getJDA().getSelfUser().getIdLong()){
            if (event.getMember().getVoiceState().inVoiceChannel())
                event.getMember().mute(false).queue();
        }
    }
}