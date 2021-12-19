package bot.message;

import bot.response.CLICommandManager;
import console.Console;
import net.dv8tion.jda.api.entities.Message;
import bot.response.CommandManager;
import bot.response.PhraseManager;

public class MessageParser {
    private final CommandManager commandManager;
    private final PhraseManager phraseManager;
    private final CLICommandManager cliCommandManager;

    public MessageParser(CommandManager commandManager, PhraseManager phraseManager, CLICommandManager cliCommandManager){
        this.commandManager = commandManager;
        this.phraseManager = phraseManager;
        this.cliCommandManager = cliCommandManager;
    }

    public void parseMessage(Message msg, String msgData){
        String[] params = getParameters(msgData);
        int command = getCommand(params);
        if (command != -1){
            Console.print(msg.getAuthor().getName() + ": " + msgData);
            commandManager.executeCommand(command, params, msg);
            return;
        }

        String filteredMsgData = msgData.replaceAll("[^a-zA-Z .!?]", "");
        int phrase = getPhrase(filteredMsgData);
        if (phrase != -1) {
            Console.print(msg.getAuthor().getName() + ": " + msgData);
            phraseManager.respondPhrase(phrase, msg);
            return;
        }
    }

    public void parseCommandLineMessage(String msgData){
        if (msgData.length() <= 0)
            return;

        String[] params = getParameters(msgData);
        int command = getCliCommand(params);
        if (command != -1){
            Console.print("console: " + msgData);
            cliCommandManager.executeCommand(command, params);
            return;
        }
    }

    private int getCommand(String[] params){
        return commandManager.validateInput(params);
    }

    private int getCliCommand(String[] params){
        return cliCommandManager.validateInput(params);
    }

    private int getPhrase(String filteredMsgData){
        return phraseManager.validateInput(filteredMsgData);
    }

    private String[] getParameters(String msgData){
        return msgData.split(" ");
    }
}
