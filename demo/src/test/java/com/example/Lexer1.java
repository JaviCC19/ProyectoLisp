package com.example;

import java.util.ArrayList;
import java.util.List;

// Clase para dividir una cadena de entrada en tokens individuales
public class Lexer1 {
    private String input; // Cadena de entrada a tokenizar
    private int currentPosition; // Posición actual en la cadena de entrada

    // Constructor que inicializa la cadena de entrada y la posición actual
    public Lexer1(String input) {
        this.input = input;
        this.currentPosition = 0;
    }

    
    /** 
     * @return List<Object>
     */
    // Método para tokenizar la cadena de entrada y devolver una lista de tokens
    public List<Object> tokenize() {
        List<Object> tokens = new ArrayList<>(); // Lista para almacenar los tokens
        while (currentPosition < input.length()) {
            char currentChar = input.charAt(currentPosition);
            if (currentChar == '(' || currentChar == ')') {
                tokens.add(Character.toString(currentChar)); // Agregar paréntesis como tokens individuales
                currentPosition++;
            } else if (Character.isWhitespace(currentChar)) {
                currentPosition++; // Ignorar caracteres de espacio en blanco
            } else if (Character.isDigit(currentChar) || currentChar == '.') {
                // Si el carácter es un dígito o un punto, construir un token numérico
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
                tokens.add(tokenBuilder.toString()); // Agregar la cadena como un token individual
            }
        }
        return tokens; // Devolver la lista de tokens
    }
}
