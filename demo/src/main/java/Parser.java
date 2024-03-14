import java.util.ArrayList;
import java.util.List;

public class Parser {
    private List<String> tokens;
    private int currentTokenIndex;

    public Parser(List<String> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }

    public void parse() {
        while (currentTokenIndex < tokens.size()) {
            parseExpression();
        }
    }

    private List<String> parseExpression() {
        List<String> expressionTokens = new ArrayList<>();
        
        String token = tokens.get(currentTokenIndex);
        currentTokenIndex++;

        if (token.equals("(")) {
            // Inicio de una expresión
            while (!tokens.get(currentTokenIndex).equals(")")) {
                expressionTokens.addAll(parseExpression());
            }
            currentTokenIndex++; // Saltar el paréntesis de cierre ")"
        } else if (!token.equals(")")) {
            // No es un paréntesis de cierre
            expressionTokens.add(token);
        }
        
        return expressionTokens;
    }
}

