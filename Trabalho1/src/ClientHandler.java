
import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private int id;
    private GameServer server;

    private String lastGuess = null;
    private boolean played = false;

    public ClientHandler(Socket socket, int id, GameServer server) {
        this.socket = socket;
        this.id = id;
        this.server = server;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {}
    }

    public int getPlayerId() {
        return id;
    }

    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("GUESS")) {
                    synchronized (this) {
                        lastGuess = line.substring(6).trim();
                        played = true;
                    }
                }
            }
        } catch (IOException e) {}
    }

    public synchronized boolean hasPlayed() {
        return played;
    }

    public synchronized String consumeGuess() {
        String g = lastGuess;
        lastGuess = null;
        played = false;
        return g;
    }

    public void send(String msg) {
        out.println(msg);
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {}
    }
}
