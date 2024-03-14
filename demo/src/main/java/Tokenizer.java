import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Clase para el Tokenizer
class Tokenizer {
    private final String[] KEYWORDS = {"defun", "cond", "setq", "let", "if", "+", "-", "*", "/", "T", "t", "NIL"};

    public List<Token> tokenizeFile(String filePath) {
        List<Token> tokens = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                tokens.addAll(tokenizeLine(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tokens;
    }

    private List<Token> tokenizeLine(String line) {
        List<Token> tokens = new ArrayList<>();
        String[] words = line.split("\\s+");

        for (String word : words) {
            if (isKeyword(word)) {
                tokens.add(new Token(word));
            }
        }

        return tokens;
    }

    private boolean isKeyword(String word) {
        for (String keyword : KEYWORDS) {
            if (keyword.equals(word)) {
                return true;
            }
        }
        return false;
    }
}
