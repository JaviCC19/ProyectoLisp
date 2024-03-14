import java.util.List;

public class Interpreter {
    public void interpretAST(List<Object> ast) {
        for (Object expression : ast) {
            if (expression instanceof List) {
                interpretListExpression((List<Object>) expression);
            } else {
                interpretToken(expression.toString());
            }
        }
    }

    private void interpretListExpression(List<Object> expression) {
        String operator = expression.get(0).toString();
        switch (operator) {
            case "defun":
                // Interpreta una definición de función
                interpretDefun(expression);
                break;
            case "let":
                // Interpreta una expresión let
                interpretLet(expression);
                break;
            case "if":
                // Interpreta una expresión if
                interpretIf(expression);
                break;
            // faltan el resto de lisp
            default:
                System.out.println("Operador desconocido: " + operator);
        }
    }

    private void interpretDefun(List<Object> expression) {
        // Lógica para interpretar una definición de función
        System.out.println();
        // Por ejemplo, puedes almacenar la definición en algún lugar para su posterior uso
    }

    private void interpretLet(List<Object> expression) {
        // Lógica para interpretar una expresión let
        System.out.println();
    }

    private void interpretIf(List<Object> expression) {
        // Lógica para interpretar una expresión if
        System.out.println();
    }

    private void interpretToken(String token) {
        // Lógica para interpretar un token (por ejemplo, identificador, constante)
        System.out.println();
    }
}
