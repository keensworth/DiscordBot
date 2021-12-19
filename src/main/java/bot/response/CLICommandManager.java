package bot.response;

import bot.DiscordBot;

import java.util.regex.Pattern;

public class CLICommandManager {
    Pattern[] commandPatterns;
    CLICommand[] commands;
    DiscordBot bot;

    public CLICommandManager(){ }

    public void init(DiscordBot bot){
        commandPatterns = new Pattern[]{
                Pattern.compile("kick"),
                Pattern.compile("unmute"),
                Pattern.compile("undeafen"),
                Pattern.compile("mute"),
                Pattern.compile("deafen"),
                Pattern.compile("shutdown")
        };

        commands = new CLICommand[]{
                new KickCommand(),
                new UnmuteCommand(),
                new UndeafenCommand(),
                new MuteCommand(),
                new DeafenCommand(),
                new CLIShutdownCommand()
        };

        this.bot = bot;
    }

    public void executeCommand(int command, String[] params){
        commands[command].execute(params, bot);
    }

    public int validateInput(String[] params){
        String command = params[0].toLowerCase();

        if (command.charAt(0) != '.')
            return -1;

        for (int i = 0; i < commandPatterns.length; i++){
            Pattern pattern = commandPatterns[i];
            if (pattern.matcher(command.substring(1)).find())
                return i;
        }

        return -1;
    }
}
