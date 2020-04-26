package Network.Server;


import Controller.GameController;
import Enumerations.Color;
import Network.Client.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class instantiates a new server and wait for connections with clients.
 */
public class Server extends Observable {
    /**
     * The socket's port to connect to from the client.
     */
    public final static int SOCKET_PORT = 7777;
    private ArrayList<ClientHandler> connections = new ArrayList<>();
    private HashMap<String, Color> playerUsernameColorHashMap = new HashMap<>();
    private HashMap<String, VirtualView> playerUsernameVirtualViewHashMap = new HashMap<>();  //TODO da riempire in client handler
    private static Server server;
    private int maxNumberOfPlayers;
    public ReentrantLock firstConnectionLock;
    
    /**
     * This method creates a connection to be caught by clients. As the server catches a connection, it
     * instantiates a {@link ClientHandler} as a thread. It will handle the connection between that client and the
     * server itself.
     *
     * @param args they are not used.
     */
    public static void main(String[] args)
    {
        server = new Server();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("cannot open server socket");
            System.exit(1);
            return;
        }
        
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, server);
                Thread thread = new Thread(clientHandler, "server_" + clientSocket.getInetAddress());
                thread.start();
                System.out.println("I've received a request");
            } catch (IOException e) {
                System.out.println("connection dropped");
            }
        }
    }
    
    public ArrayList<ClientHandler> getNumberOfPlayers(){
        return connections; //TODO rendere safe questa chiamata
    }
    
    public int getMaxNumberOfPlayers() {
        return maxNumberOfPlayers;
    }
    
    public void setMaxNumberOfPlayers(int maxNumberOfPlayers) {
        this.maxNumberOfPlayers = maxNumberOfPlayers;
    }
    
    public void addPlayer(ClientHandler clientHandler){
        connections.add(clientHandler);
    }
    
    public void addPlayerUsernameColorHashMap(String username, Color color){
        playerUsernameColorHashMap.put(username, color);
    }
    
    public void addPlayerUsernameVirtualViewHashMap(String username, VirtualView virtualView){
        playerUsernameVirtualViewHashMap.put(username, virtualView);
    }
    
    /**
     * This methods makes the game start, instantiating the controller and setting it into the virtualView.
     */
    public void initGame() {
        new GameController(maxNumberOfPlayers, playerUsernameColorHashMap, playerUsernameVirtualViewHashMap);
    }
    
}

