package console;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * Provides access to the console to present information to the user, including prompts,
 * tables, queries, warnings, and errors
 */
public class Console {
    static String[] banner = new String[]{
            "",
            "",
            "",
            "",
            "            _____     ______     ______     __   __   ______     __   __     ______     ______     ______     ",
            "           /\\  __-.  /\\  __ \\   /\\  ___\\   /\\ \\ / /  /\\  __ \\   /\\ '-.\\ \\   /\\  == \\   /\\  __ \\   /\\__  _\\ ",
            "           \\ \\ \\/\\ \\ \\ \\  __ \\  \\ \\  __\\   \\ \\ \\'/   \\ \\ \\/\\ \\  \\ \\ \\-.  \\  \\ \\  __<   \\ \\ \\/\\ \\  \\/_/\\ \\/   ",
            "            \\ \\____-  \\ \\_\\ \\_\\  \\ \\_____\\  \\ \\__|    \\ \\_____\\  \\ \\_\\\\'\\_\\  \\ \\_____\\  \\ \\_____\\    \\ \\_\\    ",
            "             \\/____/   \\/_/\\/_/   \\/_____/   \\/_/      \\/_____/   \\/_/ \\/_/   \\/_____/   \\/_____/     \\/_/     ",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",

    };

    public Console() {
        AnsiConsole.systemInstall();
    }

    public static String promptUser(String prompt){

        java.io.Console cnsl = null;
        String input = null;
        try {
            cnsl = System.console();

            input = cnsl.readLine(prompt);
            input = input.toLowerCase();
            return input;

        } catch(Exception ex) {
            System.out.println("You likely just attempted to run this inside of an IDE.");
            System.out.println("Please follow the setup in the README to run on the command line.");
            return "";
        }
    }

    /**
     * Prints the ZenTicket banner
     */
    public static void printBanner() {
        System.out.println(ansi().eraseScreen());
        System.out.println(ansi().fg(Ansi.Color.GREEN));
        for (String line : banner) {
            System.out.println(ansi().a(line));
        }
        System.out.println(ansi().reset());
    }

    /**
     * Prints a formatted message to the console
     *
     * @param text
     */
    public static void print(String text){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println(ansi().fg(0xffc0c0c0).a("["+sdf.format(new Date()) + "] ").fg(Ansi.Color.WHITE).a(text).reset());
    }

    /**
     * Prints a formatted error message to the console
     *
     * @param errorMsg
     */
    public static void printError(String errorMsg) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println(ansi().fg(0xffc0c0c0).a("["+sdf.format(new Date()) + "] ").fg(Ansi.Color.RED).a("ERROR: ").fg(Ansi.Color.WHITE).a(errorMsg).reset());
    }

    /**
     * Prints a formatted warning message to the console
     *
     * @param warnMsg
     */
    public static void printWarn(String warnMsg) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println(ansi().fg(0xffc0c0c0).a("["+sdf.format(new Date()) + "] ").fg(Ansi.Color.YELLOW).a(warnMsg).reset());
    }
}
