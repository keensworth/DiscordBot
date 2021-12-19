package bot.response;

import bot.audio.AudioStateManager;
import net.dv8tion.jda.api.entities.Message;

import java.util.regex.Pattern;

public class CommandManager {
    Pattern[] commandPatterns;
    Command[] commands;

    public CommandManager(){ }

    public void init(AudioStateManager audioStateManager, String youtubeKey){
        commandPatterns = new Pattern[]{
                Pattern.compile("play"),
                Pattern.compile("skip"),
                Pattern.compile("stop"),
                Pattern.compile("clearqueue"),
                Pattern.compile("rps"),
                Pattern.compile("shutdown")
        };

        commands = new Command[]{
                new PlayCommand(audioStateManager, youtubeKey),
                new SkipCommand(audioStateManager),
                new StopCommand(audioStateManager),
                new ClearQueueCommand(audioStateManager),
                new RPSCommand(),
                new ShutdownCommand(audioStateManager)
        };
    }

    public void executeCommand(int command, String[] params, Message msg){
        commands[command].execute(params, msg);
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
