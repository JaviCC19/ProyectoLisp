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
    //Prueba del quote
    @Test
    public void testQuoteExpression() {
        String expression = "(quote (+ 2 3))";
        Object expectedResult = "(+ 2 3 )";
        testExpression(evaluator, expression, expectedResult);
    }

    //Prueba de SetQ
    @Test
    public void testSetQ() {
        String expression = "(setq x 10)";
        Object expectedResult = "10";
        testExpression(evaluator, expression, expectedResult);
    }

    //Prueba de '
    @Test
    public void testQuote2() {
        String expression = "(' (+ 2 3))";
        Object expectedResult = "(+ 2 3 )";
        testExpression(evaluator, expression, expectedResult);
    }

    //Prueba de SETQ COMBINADO
    @Test
    public void testSetqAritmetica() {
        String expression0 = "(setq x 10)";
        Object expectedResult0 = "10";
        testExpression(evaluator, expression0, expectedResult0);
        String expression = "(setq y (* x 5))";
        Object expectedResult = "50.0";
        testExpression(evaluator, expression, expectedResult);
        String expression2 = "(/ (* (+ x y) (- 20 x)) (+ x y 2))";
        Object expectedResult2 = "9.67741935483871";
        testExpression(evaluator, expression2, expectedResult2);
    }

    //Prueba Atom
    @Test
    public void testATOM() {
        String expression = "(atom 5))";
        Object expectedResult = "true";
        testExpression(evaluator, expression, expectedResult);
        String expression1 = "(atom '(a b c)')";
        Object expectedResult1 = "false";
        testExpression(evaluator, expression1, expectedResult1);
    }

    //Prueba list
    @Test
    public void testLIST() {
        String expression = "(list '(a b c)'))";
        Object expectedResult = "true";
        testExpression(evaluator, expression, expectedResult);
        String expression1 = "(list 5)";
        Object expectedResult1 = "false";
        testExpression(evaluator, expression1, expectedResult1);
    }

    //Prueba de equal, <, >
    @Test
    public void testEqualMayorMenor() {
        String expression0 = "(equal '(a b c) '(a b c))";
        Object expectedResult0 = "true";
        testExpression(evaluator, expression0, expectedResult0);
        String expression = "(< 3 4)";
        Object expectedResult = "true";
        testExpression(evaluator, expression, expectedResult);
        String expression2 = "(> 5 2)";
        Object expectedResult2 = "true";
        testExpression(evaluator, expression2, expectedResult2);
    }

    //Prueba de COND
    @Test
    public void testCOND() {
        String expression0 = "(setq x 10)";
        Object expectedResult0 = "10";
        testExpression(evaluator, expression0, expectedResult0);
        String expression2 = "(cond ((> x 10) (setq x 1)) ((= x 10) (setq x 2)))";
        Object expectedResult2 = "2";
        testExpression(evaluator, expression2, expectedResult2);
    }

    //Prueba de función RECURSIVA
    @Test
    public void testFuncionRecursiva() {
        String expression0 = "(defun factorial (n) (cond ((= n 0.0) 1) (t (* n (factorial (- n 1))))))";
        Object expectedResult0 = "factorial";
        testExpression(evaluator, expression0, expectedResult0);
        String expression2 = "(factorial (5))";
        Object expectedResult2 = "120.0";
        testExpression(evaluator, expression2, expectedResult2);
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
