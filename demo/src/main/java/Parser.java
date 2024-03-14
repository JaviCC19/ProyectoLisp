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

    private void parseExpression() {
        String token = tokens.get(currentTokenIndex);
        currentTokenIndex++;

        // Casos base
        if (token.equals("(")) {
            // Inicio de una expresión
            parseExpression();
        } else if (token.equals(")")) {
            // Fin de una expresión
            return;
        } else {
            // Manejar otros tokens (por ejemplo, identificadores, constantes, etc.)
            // Seguir agregando logica
            System.out.println("Token encontrado: " + token);
        }
    }
}
