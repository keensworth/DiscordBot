package bot.response;

import net.dv8tion.jda.api.entities.Message;

import java.util.regex.Pattern;

public class PhraseManager {
    Pattern[] phrasePatterns = new Pattern[]{
            Pattern.compile("daevon|deion|noied|kalar|deoin|(trog[a-z ]*pog)|(lieutenant[a-z ]*troglodyte)|(lunchbox[a-z ]*(larry|larold))|(neon[a-z ]*peon)"),
            Pattern.compile("\\bdp\\b|(doctor[a-z ]*pepper)|(dr[a-z .]*pepper)"),
            Pattern.compile("what[a-z ]*up"),
    };

    Phrase[] phrases = new Phrase[]{
            new DeionPhrase(),
            new DoctorPepperPhrase(),
            new WhatsUpPhrase()
    };

    public PhraseManager(){ }

    public void respondPhrase(int phrase, Message msg){
        phrases[phrase].respond(msg);
    }

    public int validateInput(String filteredMsgData){
        for (int i = 0; i < phrasePatterns.length; i++){
            Pattern pattern = phrasePatterns[i];
            if (pattern.matcher(filteredMsgData).find())
                return i;
        }

        return -1;
    }
}
