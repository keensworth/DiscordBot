package bot.message;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
    private MessageParser messageParser;

    public MessageListener(MessageParser messageParser){
        this.messageParser = messageParser;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor() == event.getJDA().getSelfUser())
            return;

        Message msg = event.getMessage();
        String msgData = msg.getContentRaw().toLowerCase();

        if (msgData.length() <= 0)
            return;

        messageParser.parseMessage(msg, msgData);
    }

}