package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Unit test for simple App.
 */
public class AppTest {
    Evaluator1 evaluator = new Evaluator1();

    /**
     * Rigorous Test :-)
     */
    @Test
    public void testQuoteExpression() {
        Evaluator1 evaluator = new Evaluator1();
        String expression = "(quote (+ 2 3))";
        Lexer1 lexer = new Lexer1(expression);
        List<Object> tokens = lexer.tokenize();
        Parser1 parser = new Parser1(tokens);
        Node1 rootNode = parser.parse();
        Object expectedResult = "(+ 2 3 )";
        Object result = evaluator.evaluate(rootNode);
        testExpression(evaluator, expression, expectedResult);
    }
    
    private void testExpression(Evaluator1 evaluator, String expression, Object expectedResult) {
        System.out.println("Expresión: " + expression);

        // Paso 1: Tokenizar la expresión
        Lexer1 lexer = new Lexer1(expression);
        List<Object> tokens = lexer.tokenize();

        // Paso 2: Analizar los tokens para construir un árbol de sintaxis abstracta (AST)
        Parser1 parser = new Parser1(tokens);
        Node1 rootNode = parser.parse();

        // Paso 3: Evaluar la expresión utilizando el evaluador
        Object result0 = evaluator.evaluate(rootNode);
        String result = result0.toString();

        // Comprobar si el resultado es igual al resultado esperado
        assertEquals(expectedResult, result);

        System.out.println("Resultado de la evaluación: " + result);
        System.out.println();
    }
}
