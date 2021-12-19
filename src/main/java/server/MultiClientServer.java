package server;

import bot.DiscordBot;
import console.Console;

import java.net.ServerSocket;
import java.util.ArrayList;

public class MultiClientServer extends Thread{
    private ServerSocket serverSocket;
    private ArrayList<ClientHandler> clients;
    int port;
    DiscordBot bot;
    private boolean running;

    public MultiClientServer(int port, DiscordBot bot){
        this.port = port;
        this.bot = bot;
        clients = new ArrayList<>();
    }

    @Override
    public void run(){
        running = true;
        startServer();
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            Console.print("Server started");
            while (running) {
                ClientHandler client = new ClientHandler(serverSocket.accept(), bot);
                clients.add(client);
                client.start();
            }
        } catch (Exception e) {
            Console.printError("Failed to start server");
        }
    }

    public void close(){
        running = false;
        try {
            serverSocket.close();
            for (ClientHandler client : clients) {
                client.close();
            }
            Console.print("Server closed");
        } catch (Exception e) {
            Console.printError("Failed to close server");
        }
    }
}
