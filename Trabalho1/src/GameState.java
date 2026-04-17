
import java.util.*;

public class GameState {
    private String word;
    private char[] mask;
    private Set<Character> usedLetters = new HashSet<>();
    private int attempts = 6;
    private boolean won = false;

    private static final String[] WORDS = {
    	    "banana","marty","computador","java","socket","programa","rede","cliente","servidor","thread",
    	    "janela","cadeira","teclado","rato","ecra","livro","caderno","caneta","lapis","borracha",
    	    "escola","professor","aluno","turma","exame","praia","mar","areia","onda","sol",
    	    "cidade","aldeia","estrada","carro","comboio","aviao","bicicleta","motociclo","parque","jardim",
    	    "comida","bebida","restaurante","cozinha","prato","garfo","faca","colher","mesa","cadeirao",
    	    "porta","chave","fechadura","janota","espelho","quarto","sala","tapete","sofa","almofada",
    	    "roupa","camisa","calcas","casaco","sapato","meia","chapeu","luvas","cachecol","mochila",
    	    "tempo","relogio","minuto","segundo","hora","ontem","hoje","amanha","semana","mes",
    	    "ano","primavera","verao","outono","inverno","chuva","vento","nuvem","tempestade","nevoeiro",
    	    "amigo","familia","pai","mae","irmao","irma","avo","prima","vizinho","colega","andamento","futebol",
    	    "basquetebol","andebol","ginastica","computador","janela","vidro"
    	};
    public GameState() {
        Random r = new Random();
        word = WORDS[r.nextInt(WORDS.length)];
        mask = new char[word.length()];
        Arrays.fill(mask, '_');
    }

    public boolean applyLetter(char c) {
        if (usedLetters.contains(c)) return false;

        usedLetters.add(c);
        boolean found = false;

        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == c) {
                mask[i] = c;
                found = true;
            }
        }

        if (String.valueOf(mask).equals(word)) {
            won = true;
        }

        return found;
    }

    public boolean checkWord(String guess) {
        return word.equalsIgnoreCase(guess);
    }

    public void decrementAttempts() {
        attempts--;
    }

    public String getMask() {
        return new String(mask);
    }

    public int getAttempts() {
        return attempts;
    }

    public String getUsedLetters() {
        return usedLetters.toString().replaceAll("[\\[\\] ]", "");
    }

    public String getWord() {
        return word;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean w) {
        won = w;
    }
}