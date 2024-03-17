import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private String input;
    private int currentPosition;

    public Lexer(String input) {
        this.input = input;
        this.currentPosition = 0;
    }

    public List<Object> tokenize() {
        List<Object> tokens = new ArrayList<>();
        while (currentPosition < input.length()) {
            char currentChar = input.charAt(currentPosition);
            if (currentChar == '(' || currentChar == ')') {
                tokens.add(Character.toString(currentChar));
                currentPosition++;
            } else if (Character.isWhitespace(currentChar)) {
                currentPosition++;
            } else if (Character.isDigit(currentChar) || currentChar == '.') {
                StringBuilder tokenBuilder = new StringBuilder();
                while (currentPosition < input.length() && (Character.isDigit(input.charAt(currentPosition)) || input.charAt(currentPosition) == '.')) {
                    tokenBuilder.append(input.charAt(currentPosition));
                    currentPosition++;
                }
                // Convertir el token a un Double si contiene un punto, de lo contrario, tratarlo como un número entero
                String token = tokenBuilder.toString();
                if (token.contains(".")) {
                    tokens.add(Double.parseDouble(token));
                } else {
                    tokens.add(Integer.parseInt(token));
                }
            } else {
                // Si no es un paréntesis, un espacio en blanco o un número, tratarlo como una cadena de texto
                StringBuilder tokenBuilder = new StringBuilder();
                while (currentPosition < input.length() && !Character.isWhitespace(input.charAt(currentPosition)) &&
                        input.charAt(currentPosition) != '(' && input.charAt(currentPosition) != ')' && !Character.isDigit(input.charAt(currentPosition))) {
                    tokenBuilder.append(input.charAt(currentPosition));
                    currentPosition++;
                }
                tokens.add(tokenBuilder.toString());
            }
        }
        return tokens;
    }
}
