import java.util.List;

public class Interpreter {
    private List<String> tokens;
    private int currentTokenIndex;

    public Interpreter(List<String> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }

    public void interpret() {
        while (currentTokenIndex < tokens.size()) {
            interpretExpression();
        }
    }

    private void interpretExpression() {
        String token = tokens.get(currentTokenIndex);
        currentTokenIndex++;

        // Casos base
        if (token.equals("(")) {
            // Inicio de una expresión
            interpretExpression();
        } else if (token.equals(")")) {
            // Fin de una expresión
            return;
        } else if (token.equals("defun")) {
            // Definición de función
            interpretDefun();
        } else if (token.equals("cond")) {
            // Expresión condicional
            interpretCond();
        } else {
            // Otros casos (identificadores, constantes, etc.)
            interpretToken(token);
        }
    }

    private void interpretDefun() {
        // Lógica para interpretar una definición de función
        System.out.println("Interpretando definición de función...");
    }

    private void interpretCond() {
        // Lógica para interpretar una expresión condicional
        System.out.println("Interpretando expresión condicional...");
    }

    private void interpretToken(String token) {
        // Lógica para interpretar un token (por ejemplo, identificador, constante)
        System.out.println("Interpretando token: " + token);
    }
}
