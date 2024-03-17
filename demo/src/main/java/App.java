

import java.util.*;

public class App {
    public static void main(String[] args) {
        // Crear un nuevo evaluador
        Evaluator evaluator = new Evaluator();

        // Ejemplo de uso de SETQ: (setq x 10)
        String setqExpression = "(setq x 10)";
        testExpression(evaluator, setqExpression, 10);

        // Ejemplo de uso de QUOTE: (quote (+ 2 3))
        String quoteExpression = "(quote (+ 2 3))";
        testExpression(evaluator, quoteExpression, "(+ 2 3)");

        // Ejemplo de uso de ': (' (+ 2 3))
        String quoteSymbolExpression = "(' (+ 2 3))";
        testExpression(evaluator, quoteSymbolExpression, "(+ 2 3)");

        // Ejemplo de uso de operaciones aritméticas con SETQ: (setq y (* x 5))
        String setqArithmeticExpression = "(setq y (* x 5))";
        testExpression(evaluator, setqArithmeticExpression, 50);

        // Ejemplo de uso de operaciones aritméticas con variables: (+ x y)
        String variableArithmeticExpression = "(+ x y)";
        testExpression(evaluator, variableArithmeticExpression, 60);

        // Expresión aritmética más compleja para probar
        String complexArithmeticExpression = "(/ (* (+ x y) (- 20 x)) (+ x y 2))";
        testExpression(evaluator, complexArithmeticExpression, 1.0416666666666667);

        String prueba1 = "(setq y \"Juan\")";
        testExpression(evaluator, prueba1, "Juan");

        String prueba2 = "(atom 5)";
        testExpression(evaluator, prueba2, prueba2);

        String prueba3 = "(atom \"Juan\")";
        testExpression(evaluator, prueba3, prueba2);

        String prueba4 = "(atom '(a b c))";
        testExpression(evaluator, prueba4, prueba2);
        
        String prueba5 = "(list \"Juan\")";
        testExpression(evaluator, prueba5, prueba2);

        String prueba6 = "(list '(a b c))";
        testExpression(evaluator, prueba6, prueba2);

        String prueba7 = "(list 'x)";
        testExpression(evaluator, prueba7, prueba2);

        String prueba8 = "(equal '(a b c) '(a b))";
        testExpression(evaluator, prueba8, prueba8);

        String prueba9 = "(equal '(a b c) '(a b c))";
        testExpression(evaluator, prueba9, prueba8);

        String prueba10 = "(equal 'a 'a)";
        testExpression(evaluator, prueba10, prueba10);

        String prueba11 = "(equal 'b 'a)";
        testExpression(evaluator, prueba11, prueba10);

        String prueba12 = "(< 5 3)";
        testExpression(evaluator, prueba12, prueba10);

        String prueba13 = "(< 2 10)";
        testExpression(evaluator, prueba13, prueba10);

        String prueba14 = "(> 5 3)";
        testExpression(evaluator, prueba14, prueba10);

        String prueba15 = "(> 2 10)";
        testExpression(evaluator, prueba15, prueba10);


    }

    private static void testExpression(Evaluator evaluator, String expression, Object expectedResult) {
        System.out.println("Expresión: " + expression);

        // Paso 1: Tokenizar la expresión
        Lexer lexer = new Lexer(expression);
        List<Object> tokens = lexer.tokenize();

        // Paso 2: Analizar los tokens para construir un árbol de sintaxis abstracta (AST)
        Parser parser = new Parser(tokens);
        Node rootNode = parser.parse();

        // Paso 3: Evaluar la expresión utilizando el evaluador
        Object result = evaluator.evaluate(rootNode);

        // Mostrar el resultado
        System.out.println("Resultado de la evaluación: " + result);

        System.out.println();
    }
}


