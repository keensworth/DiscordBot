package bot.response;

import bot.DiscordBot;
import bot.audio.AudioStateManager;
import bot.audio.VideoFetcher;
import console.Console;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.ChannelManager;
import net.dv8tion.jda.api.managers.GuildManager;
import net.dv8tion.jda.internal.handle.GuildSetupController;
import org.json.JSONObject;

import org.json.JSONArray;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

public interface CLICommand {
    int minParams = 0;
    int maxParams = 0;
    int execute(String[] params, DiscordBot bot);
}

class KickCommand implements CLICommand {
    public int execute(String[] params, DiscordBot bot){
        Guild guild = bot.jda.getGuildById(362773353183838208L);
        Member member = guild.getMemberById(params[1]);
        if (member.getVoiceState().inVoiceChannel())
            guild.kickVoiceMember(member).queue();
        return 0;
    }
}

class MuteCommand implements CLICommand {
    public int execute(String[] params, DiscordBot bot){
        Guild guild = bot.jda.getGuildById(362773353183838208L);
        Member member = guild.getMemberById(params[1]);
        if (member.getVoiceState().inVoiceChannel())
            guild.mute(member, true).queue();
        return 0;
    }
}

class DeafenCommand implements CLICommand {
    public int execute(String[] params, DiscordBot bot){
        Guild guild = bot.jda.getGuildById(362773353183838208L);
        Member member = guild.getMemberById(params[1]);
        if (member.getVoiceState().inVoiceChannel())
            guild.deafen(member, true).queue();
        return 0;
    }
}

class UnmuteCommand implements CLICommand {
    public int execute(String[] params, DiscordBot bot){
        Guild guild = bot.jda.getGuildById(362773353183838208L);
        Member member = guild.getMemberById(params[1]);
        if (member.getVoiceState().inVoiceChannel())
            guild.mute(member, false).queue();

        return 0;
    }
}

class UndeafenCommand implements CLICommand {
    public int execute(String[] params, DiscordBot bot){
        Guild guild = bot.jda.getGuildById(362773353183838208L);
        Member member = guild.getMemberById(params[1]);
        if (member.getVoiceState().inVoiceChannel())
            guild.deafen(member, false).queue();
        return 0;
    }
}

class CLIShutdownCommand implements CLICommand {
    public int execute(String[] params, DiscordBot bot){
        bot.server.close();
        bot.audioStateManager.shutdown();
        bot.shutdown();
        bot.jda.shutdown();
        return 0;
    }
}