
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static void printState(String mask, String attempts, String letters) {
        System.out.println("\n==============================");
        System.out.println(" Palavra:   " + formatMask(mask));
        System.out.println(" Tentativas:" + attempts);
        System.out.println(" Letras:    " + (letters.isEmpty() ? "-" : letters));
        System.out.println("==============================");
    }

    private static String formatMask(String mask) {
        StringBuilder sb = new StringBuilder();
        for (char c : mask.toCharArray()) {
            sb.append(c).append(' ');
        }
        return sb.toString();
    }

    private static void printMenu() {
        System.out.println("\n Escolhe uma opção:");
        System.out.println("1 - Jogar letra");
        System.out.println("2 - Tentar palavra");
        System.out.print("Opção: ");
    }

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 12345);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);

        // Thread para receber mensagens do servidor
        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {

                    if (line.startsWith("WELCOME")) {
                        System.out.println(" Ligado ao servidor: " + line);
                    }

                    else if (line.startsWith("START")) {
                        String[] parts = line.split(" ");
                        System.out.println("\n Jogo iniciado!");
                        printState(parts[1], parts[2], "");
                    }

                    else if (line.startsWith("ROUND")) {
                        String[] parts = line.split(" ");
                        System.out.println("\n Ronda " + parts[1]);
                        printState(parts[2], parts[3], parts.length > 4 ? parts[4] : "");

                        printMenu();
                    }

                    else if (line.startsWith("STATE")) {
                        String[] parts = line.split(" ");
                        printState(parts[1], parts[2], parts.length > 3 ? parts[3] : "");
                    }

                    else if (line.startsWith("END")) {
                        String[] parts = line.split(" ");

                        if (parts[1].equals("WIN")) {
                            System.out.println("\n VITÓRIA!");
                            System.out.println("Vencedores: " + parts[2]);
                            System.out.println("Palavra: " + parts[3]);
                        } else {
                            System.out.println("\n DERROTA!");
                            System.out.println("Palavra correta: " + parts[2]);
                        }

                        System.exit(0);
                    }

                    else if (line.startsWith("FULL")) {
                        System.out.println(" Servidor cheio!");
                        System.exit(0);
                    }

                    else {
                        System.out.println("SERVER: " + line);
                    }
                }
            } catch (IOException e) {
                System.out.println("Ligação perdida.");
            }
        }).start();

        // Input do jogador
        while (true) {
            String option = scanner.nextLine();

            if (option.equals("1")) {
                System.out.print(" Letra: ");
                String letter = scanner.nextLine();
                out.println("GUESS " + letter);
            }

            else if (option.equals("2")) {
                System.out.print(" Palavra: ");
                String word = scanner.nextLine();
                out.println("GUESS " + word);
            }

            else {
                // fallback (permite escrever direto)
                out.println("GUESS " + option);
            }
        }
    }
}