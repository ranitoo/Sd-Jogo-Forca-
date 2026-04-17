
import java.net.Socket;
import java.util.*;

public class GameServer {
    private final List<ClientHandler> players = new ArrayList<>();
    private final int MAX_PLAYERS = 4;

    private boolean started = false;
    private GameState gameState;

    public synchronized boolean isFull() {
        return players.size() >= MAX_PLAYERS;
    }

    public synchronized boolean isStarted() {
        return started;
    }

    public synchronized void addPlayer(Socket socket) {
        int id = players.size() + 1;
        ClientHandler handler = new ClientHandler(socket, id, this);
        players.add(handler);
        handler.start();

        broadcast("WELCOME " + id + " " + players.size());

        if (players.size() == 2 && !started) {
            new Thread(this::startGame).start();
        }
    }

    private void startGame() {
        try {
            Thread.sleep(20000); // lobby timeout
        } catch (InterruptedException e) {}

        synchronized (this) {
            if (started) return;
            started = true;
        }

        gameState = new GameState();

        broadcast("START " + gameState.getMask() + " " + gameState.getAttempts() + " 60000");

        runGameLoop();
    }

    private void runGameLoop() {
        int round = 1;

        while (gameState.getAttempts() > 0 && !gameState.isWon()) {

            broadcast("ROUND " + round + " " + gameState.getMask() + " "
                    + gameState.getAttempts() + " " + gameState.getUsedLetters());

            waitForGuesses();

            processGuesses();

            broadcast("STATE " + gameState.getMask() + " "
                    + gameState.getAttempts() + " " + gameState.getUsedLetters());

            round++;
        }

        endGame();
    }

    private void waitForGuesses() {
        long timeout = System.currentTimeMillis() + 60000;

        while (System.currentTimeMillis() < timeout) {
            boolean allPlayed = true;
            for (ClientHandler p : players) {
                if (!p.hasPlayed()) {
                    allPlayed = false;
                    break;
                }
            }
            if (allPlayed) return;

            try { Thread.sleep(100); } catch (InterruptedException e) {}
        }
    }

    private void processGuesses() {
        List<Integer> winners = new ArrayList<>();

        for (ClientHandler p : players) {
            String guess = p.consumeGuess();

            if (guess == null || guess.isEmpty()) {
                gameState.decrementAttempts();
                continue;
            }

            guess = guess.toLowerCase();

            if (guess.length() == 1) {
                if (!gameState.applyLetter(guess.charAt(0))) {
                    gameState.decrementAttempts();
                }
            } else {
                if (gameState.checkWord(guess)) {
                    winners.add(p.getPlayerId());
                } else {
                    gameState.decrementAttempts();
                }
            }
        }

        if (!winners.isEmpty()) {
            gameState.setWon(true);
            broadcast("END WIN " + join(winners) + " " + gameState.getWord());
            closeAll();
        }
    }

    private void endGame() {
        if (!gameState.isWon()) {
            broadcast("END LOSE " + gameState.getWord());
            closeAll();
        }
    }

    private void broadcast(String msg) {
        for (ClientHandler p : players) {
            p.send(msg);
        }
    }

    private void closeAll() {
        for (ClientHandler p : players) {
            p.close();
        }
    }

    private String join(List<Integer> list) {
        return list.toString().replaceAll("[\\[\\] ]", "");
    }
}
