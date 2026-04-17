
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 12345;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Servidor iniciado na porta " + port);

        GameServer gameServer = new GameServer();

        while (true) {
            Socket socket = serverSocket.accept();

            synchronized (gameServer) {
                if (gameServer.isFull() || gameServer.isStarted()) {
                    socket.getOutputStream().write("FULL\n".getBytes());
                    socket.close();
                    continue;
                }

                gameServer.addPlayer(socket);
            }
        }
    }
}
