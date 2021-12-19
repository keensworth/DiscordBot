package server;

import bot.DiscordBot;
import console.Console;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private DiscordBot bot;
    private boolean running;

    public ClientHandler(Socket socket, DiscordBot bot){
        this.clientSocket = socket;
        this.bot = bot;
    }

    public void run() {
        running = true;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Console.print("Client connected");

            String inputLine;
            while ((inputLine = in.readLine()) != null && running) {
                bot.messageParser.parseCommandLineMessage(inputLine);
            }
            close();
        } catch (Exception e){
            Console.printError("Failed to communicate with client");
        }
    }

    public void close(){
        running = false;
        try {
            clientSocket.close();
            in.close();
            out.close();
            Console.print("Client disconnected");
        } catch (Exception e){
            Console.printError("Failed to close connection to client");
        }
    }
}
