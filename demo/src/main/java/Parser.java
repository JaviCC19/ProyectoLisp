import java.util.List;
import java.util.ArrayList;

public class Parser {
    private List<Object> tokens;
    private int currentPosition;

    public Parser(List<Object> tokens) {
        this.tokens = tokens;
        this.currentPosition = 0;
    }

    public Node parse() {
        return parseExpression();
    }

    private Node parseExpression() {
        if (currentPosition >= tokens.size()) {
            return null;
        }
        
        Object token = tokens.get(currentPosition);
        currentPosition++;
        
        if ("(".equals(token)) {
            List<Node> children = new ArrayList<>();
            while (!")".equals(tokens.get(currentPosition))) {
                Node child = parseExpression();
                if (child != null) {
                    children.add(child);
                }
            }
            currentPosition++; // consume ")"
            return new ListNode(children);
        } else if (token instanceof String) {
            return new AtomNode(token); // Si el token es una cadena, lo tratamos como un átomo
        } else if (token instanceof Double || token instanceof Integer) {
            return new AtomNode(token); // Si el token es un número, lo tratamos como un átomo
        } else {
            // Manejar otros tipos de token o errores
            return null;
        }
    }
}
