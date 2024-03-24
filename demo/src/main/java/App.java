import java.util.*;

public class App {
    public static void main(String[] args) {

        Evaluator evaluator = new Evaluator();
        boolean systemON = true;
        Scanner sc = new Scanner(System.in);


        System.out.println("***********************************************************");
        System.out.println("*  __        _______ _     ____ ___  __  __ _____ _ _ _   *");
        System.out.println("*  \\ \\      / / ____| |   / ___/ _ \\|  \\/  | ____| | | |  *");
        System.out.println("*   \\ \\ /\\ / /|  _| | |  | |  | | | | |\\/| |  _| | | | |  *");
        System.out.println("*    \\ V  V / | |___| |__| |__| |_| | |  | | |___|_|_|_|  *");
        System.out.println("*     \\_/\\_/  |_____|_____\\____\\___/|_|  |_|_____(_|_|_)  *");
        System.out.println("***********************************************************");
        System.out.println();
        System.out.println("-----------------TE PRESENTAMOS NUESTRO INTERPRETE EN LISP!!!--------------------");
        System.out.println();
        
        while (systemON) {
            System.out.println("¿Qué deseas hacer?");
            System.out.println("------------------------------");
            System.out.println("1. Ingresar código en LISP");
            System.out.println("2. Salir del programa");

            String decisionInicial = sc.nextLine();
            switch (decisionInicial) {
                case "1":
                    System.out.println("--------------------------------------");
                    System.out.println("Por favor, ingresa el código que quieres que sea evaluado.");
                    System.out.println("Para que el interprete funcione adecuadamente, tendrás que ingresar línea por línea lo que quieres evaluar.");
                    System.out.println("Por ejemplo, se espera algo como: (setq x 20) o algo como (* x 10)");
                    System.out.println("Dicho esto, ¿qué quieres evaluar?");
                    System.out.println();

                    String expression = sc.nextLine();
                    System.out.println("Gracias!! Ahora probaremos evaluarla :)\n");

                    Object resultado = testExpression(evaluator, expression);

                    System.out.println();
                    System.out.println("El resultado ha sido: " + resultado);
                    System.out.println("--------------------------------------\n");
                    break;

                case "2":
                    System.out.println("Que tenga un buen día!!!!");
                    systemON = false;
                    break;

                default:
                    System.out.println("Por favor, verifica que estés ingresando una opción válida.");
                    break;
            }

        }

        
    }

    private static Object testExpression(Evaluator evaluator, String expression) {
        // Paso 1: Tokenizar la expresión
        Lexer lexer = new Lexer(expression);
        List<Object> tokens = lexer.tokenize();

        // Paso 2: Analizar los tokens para construir un árbol de sintaxis abstracta (AST)
        Parser parser = new Parser(tokens);
        Node rootNode = parser.parse();

        // Paso 3: Evaluar la expresión utilizando el evaluador
        Object result = evaluator.evaluate(rootNode);

        // Devolver resultado
        return result;
        
    }
}
