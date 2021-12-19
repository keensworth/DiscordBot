package bot.response;

import console.Console;
import net.dv8tion.jda.api.entities.Message;

import java.io.File;
import java.util.Random;

public interface Phrase {
    void respond(Message msg);
}

class DeionPhrase implements Phrase {
    Random generator = new Random();

    public void respond(Message msg){
        File file = loadRandomImage();
        ResponseHandler.sendFile(msg, file);
    }

    private File loadRandomImage(){
        int image = generator.nextInt(13);
        File file = null;
        try{
            file = new File("src/main/resources/images/" + image + ".png");
        } catch (Exception e){
            Console.printError("Cannot load image");
        }
        return file;
    }
}

class DoctorPepperPhrase implements Phrase {
    public void respond(Message msg){
        ResponseHandler.sendMessage(msg, "*burp* Morty");
    }
}

class WhatsUpPhrase implements Phrase {
    public void respond(Message msg){
        ResponseHandler.sendMessage(msg, "The opposite of down");
    }
}
