package com.example;

import java.util.List;
import java.util.ArrayList;

// Clase para analizar una lista de tokens y generar un árbol de sintaxis abstracta (AST)
public class Parser1 {
    private List<Object> tokens; // Lista de tokens a ser analizados
    private int currentPosition; // Posición actual en la lista de tokens

    // Constructor que inicializa la lista de tokens y la posición actual
    public Parser1(List<Object> tokens) {
        this.tokens = tokens;
        this.currentPosition = 0;
    }

    
    /** 
     * @return Node
     */
    // Método principal que inicia el análisis sintáctico y devuelve el nodo raíz del AST
    public Node1 parse() {
        return parseExpression();
    }

    
    /** 
     * @return Node
     */
    // Método recursivo para analizar una expresión y construir el AST
    private Node1 parseExpression() {
        // Comprobar si se ha alcanzado el final de la lista de tokens
        if (currentPosition >= tokens.size()) {
            return null; // Si es así, no hay nada más que analizar, devolver null
        }
        
        Object token = tokens.get(currentPosition); // Obtener el token actual
        currentPosition++; // Avanzar a la siguiente posición en la lista de tokens
        
        if ("(".equals(token)) { // Si el token es un paréntesis de apertura
            List<Node1> children = new ArrayList<>(); // Crear una lista para almacenar los nodos hijos
            while (!")".equals(tokens.get(currentPosition))) { // Mientras no se encuentre un paréntesis de cierre
                Node1 child = parseExpression(); // Analizar la expresión recursivamente para obtener el nodo hijo
                if (child != null) { // Si se obtiene un nodo hijo válido
                    children.add(child); // Agregarlo a la lista de nodos hijos
                }
            }
            currentPosition++; // Consumir el paréntesis de cierre
            return new ListNode1(children); // Devolver un nodo de lista con los nodos hijos
        } else if (token instanceof String || token instanceof Double || token instanceof Integer) {
            // Si el token es una cadena, un número entero o un número decimal
            return new AtomNode1(token); // Crear un nodo átomo con el token como valor
        } else {
            // Manejar otros tipos de token o errores
            return null; // En este caso, devolvemos null indicando que no se puede construir un nodo válido
        }
    }
}

