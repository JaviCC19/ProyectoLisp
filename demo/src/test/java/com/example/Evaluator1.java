package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Clase que evalúa nodos en una estructura de árbol Lisp
public class Evaluator1 {

    // Mapas para almacenar variables, parámetros de función y definiciones de función
    private Map<String, Object> variables = new HashMap<>();
    private Map<String, List<String>> functionParameters = new HashMap<>();
    private Map<String, ListNode1> functionDefinitions = new HashMap<>();

    // Constructor por defecto
    public Evaluator1() {
        this.variables = new HashMap<>();
        this.functionParameters = new HashMap<>();
        this.functionDefinitions = new HashMap<>();
    }

    // Constructor con parámetros para inicializar los mapas
    public Evaluator1(Map<String, Object> variables, Map<String, List<String>> functionParameters,
                     Map<String, ListNode1> functionDefinitions) {
        this.variables = new HashMap<>(variables);
        this.functionParameters = new HashMap<>(functionParameters);
        this.functionDefinitions = new HashMap<>(functionDefinitions);
    }

    
    /** 
     * @param node
     * @return Object
     */
    // Método para evaluar un nodo en la estructura del árbol
    public Object evaluate(Node1 node) {
        if (node instanceof AtomNode1) {
            Object value = ((AtomNode1) node).getValue();
            if (value instanceof String) {
                String strValue = (String) value;
                if (strValue.startsWith("\"") && strValue.endsWith("\"")) {
                    // Si el valor es un String entre comillas, se devuelve tal cual
                    return strValue.substring(1, strValue.length() - 1);
                } else {
                    // Si es un String sin comillas, se trata como una variable
                    return variables.getOrDefault(strValue, null);
                }
            } else {
                // Si no es un String, se devuelve el valor tal cual
                return value;
            }

        }
        //Verifica si el nodo es una instancia de una lista
        else if (node instanceof ListNode1) { 
            ListNode1 listNode = (ListNode1) node;
            List<Node1> children = listNode.getChildren();

            if (children.isEmpty()) {
                return null;
            } else {
                Node1 operatorNode = children.get(0);
                if (!(operatorNode instanceof AtomNode1)) {
                    return null;
                }

                //Se extrae el operador, esto para saber qué camino hay que tomar
                String operator = (String) ((AtomNode1) operatorNode).getValue();


                //Camino a tomar si se está definiendo una función
                if (operator.equals("defun")) {
                    // Definición de una función
                    if (children.size() != 4 || !(children.get(1) instanceof AtomNode1)) {
                        // Error de sintaxis en la definición de la función
                        return null;
                    }
                    //Se extrae el nombre de la función y sus parámetros
                    String functionName = (String) ((AtomNode1) children.get(1)).getValue();
                    ListNode1 parametersListNode = (ListNode1) children.get(2);
                    List<String> parameters = new ArrayList<>();
                    for (Node1 paramNode : parametersListNode.getChildren()) {
                        if (paramNode instanceof AtomNode1) {
                            parameters.add((String) ((AtomNode1) paramNode).getValue());
                        } else {
                            // Error de sintaxis en la definición de los parámetros de la función
                            return null;
                        }
                    }

                    //Dejamos guardado en un mapa la información de la función
                    functionParameters.put(functionName, parameters);
                    ListNode1 bodyListNode = (ListNode1) children.get(3);
                    functionDefinitions.put(functionName, bodyListNode);
                    return functionName; // Devuelve el nombre de la función como confirmación
                }

                //Camino a tomar si se encuentra con alguna función ya definida
                else if (functionDefinitions.containsKey(operator)) {
                    //Llamamos a toda la información importante
                    Map<String, Object> localVariables = new HashMap<>(variables);
                    Map<String, List<String>> localFunctionParameters = new HashMap<>(functionParameters);
                    Map<String, ListNode1> localFunctionDefinitions = new HashMap<>(functionDefinitions);

                    // Llamada a una función definida
                    String functionBody = stringify(functionDefinitions.get(operator));
                    List<String> functionParametersList = functionParameters.get(operator);

                    //Se extrae lo que se evaluará
                    String inputFunction = nodeToString(children.get(1));

                    //Extraemos los parametros a usar
                    ArrayList<String> functionParametersEvaluate = extraerDatos(inputFunction);

                    //Se crea un objeto Evaluator con los mapas ya generados para tener recursividad
                    Evaluator1 recursiveEvaluator = new Evaluator1(localVariables, localFunctionParameters,
                            localFunctionDefinitions);

                    ArrayList<Object> parametrosAEntrar = new ArrayList<Object>();

                    //Se evalúan los parámetros
                    for (int i = 0; i < functionParametersEvaluate.size(); i++) {
                        Lexer1 lexer2 = new Lexer1(functionParametersEvaluate.get(i));
                        List<Object> tokens2 = lexer2.tokenize();
                        Parser1 parser2 = new Parser1(tokens2);
                        Node1 rootNode = parser2.parse();
                        parametrosAEntrar.add(recursiveEvaluator.evaluate(rootNode));
                    }

                    //Se reemplazan los parametros por los valores dados
                    String inputEvaluar = reemplazar(functionBody, functionParametersList, parametrosAEntrar);

                    // Aquí se analiza y evalúa nuevamente la expresión Lisp
                    Lexer1 lexer = new Lexer1(inputEvaluar);
                    List<Object> tokens = lexer.tokenize();
                    Parser1 parser = new Parser1(tokens);
                    Node1 rootNode = parser.parse();
                    Object result2 = recursiveEvaluator.evaluate(rootNode);

                    // Retornar el resultado de la evaluación de la nueva expresión Lisp
                    return result2;
                }

                //Verificación de si es un atomo
                else if (operator.equals("atom")) {
                    // Verificar si el nodo tiene solo un elemento
                    if (children.size() == 2) {
                        Node1 argumentNode = children.get(1);
                        Object result = evaluate(argumentNode);

                        // Si el resultado es un átomo (es decir, no es una lista), devolver true
                        if (result instanceof ListNode1) {
                            return false;
                        } else {
                            // Si el resultado es una lista, devolver false
                            return true;
                        }
                    } else {
                        // Error: 'atom' requiere un argumento
                        return false;
                    }
                }

                //Verificación de si algo es una lista
                else if (operator.equals("list")) {
                    if (children.size() > 2) {
                        return true;
                    } else {
                        return false;
                    }
                }

                //Comparación entre atomos y listas
                else if (operator.equals("equal")) {
                    // Verificar si la expresión contiene al menos dos elementos
                    if (children.size() >= 3) {
                        Node1 firstNode;
                        Node1 secondNode;
                        // Verificar si el tamaño de la expresión es igual a 3 o mayor
                        if (children.size() == 3) {
                            // Si es igual a 3, las posiciones 1 y 2 son los elementos a comparar
                            firstNode = children.get(1);
                            secondNode = children.get(2);
                        } else if (children.size() > 3) {
                            firstNode = children.get(2);
                            secondNode = children.get(4);
                        } else {
                            // Manejar error: cantidad incorrecta de argumentos
                            return null;
                        }

                        String primeraEntrada = nodeToString(firstNode);
                        String segundaEntrada = nodeToString(secondNode);
                        if (primeraEntrada.equals(segundaEntrada)) {
                            return true;
                        } else {
                            return false;
                        }

                    } else {
                        // Manejar error: cantidad incorrecta de argumentos
                        return null;
                    }
                }

                //Comparación de valores númericos
                else if (operator.equals("=")) {
                    // Verificar si la expresión contiene al menos dos elementos
                    if (children.size() >= 3) {
                        Object firstNode;
                        Object secondNode;
                        // Verificar si el tamaño de la expresión es igual a 3 o mayor
                        if (children.size() == 3) {
                            // Si es igual a 3, las posiciones 1 y 2 son los elementos a comparar
                            firstNode = evaluate(children.get(1));
                            secondNode = evaluate(children.get(2));
                        } else if (children.size() > 3) {
                            firstNode = evaluate(children.get(2));
                            secondNode = evaluate(children.get(4));
                        } else {
                            // Manejar error: cantidad incorrecta de argumentos
                            return null;
                        }

                        // Verificar si los nodos evaluados son iguales
                        boolean sonIguales = deepEquals(firstNode, secondNode);
                        return sonIguales;
                    } else {
                        // Manejar error: cantidad incorrecta de argumentos
                        return null;
                    }

                }
                
                //Si se encuentra una variable, se extrae su valor del mapa
                else if (variables.containsKey(operator)) {
                    return variables.get(operator);

                //Se compara dos valores númericos, si el primer es menor al segundo
            } else if (operator.equals("<")) {
                Object firstNode;
                Object secondNode;
                // Verificar si la expresión contiene al menos dos elementos
                if (children.size() >= 3) {
                    // Obtener los valores correspondientes a los nodos a comparar
                    if (children.size() == 3) {
                        // Si es igual a 3, las posiciones 1 y 2 son los elementos a comparar
                        firstNode = evaluate(children.get(1));
                        secondNode = evaluate(children.get(2));
                    } else if (children.size() > 3) {
                        firstNode = evaluate(children.get(2));
                        secondNode = evaluate(children.get(4));
                    } else {
                        // Manejar error: cantidad incorrecta de argumentos
                        return null;
                    }

                    // Verificar si los nodos evaluados son números y realizar la comparación
                    if (firstNode instanceof Number && secondNode instanceof Number) {
                        Double primerValor = ((Number) firstNode).doubleValue();
                        Double segundoValor = ((Number) secondNode).doubleValue();
                        return primerValor < segundoValor;
                    } else {
                        // Manejar error: los nodos no son números
                        return null;
                    }
                } else {
                    // Manejar error: cantidad incorrecta de argumentos
                    return null;
                }
            }

                //Se compara dos valores númericos, si el primer es mayor al segundo
                else if (operator.equals(">")) {
                    Object firstNode;
                    Object secondNode;
                    // Verificar si la expresión contiene al menos dos elementos
                    if (children.size() >= 3) {
                        // Obtener los valores correspondientes a los nodos a comparar
                        if (children.size() == 3) {
                            // Si es igual a 3, las posiciones 1 y 2 son los elementos a comparar
                            firstNode = evaluate(children.get(1));
                            secondNode = evaluate(children.get(2));
                        } else if (children.size() > 3) {
                            firstNode = evaluate(children.get(2));
                            secondNode = evaluate(children.get(4));
                        } else {
                            // Manejar error: cantidad incorrecta de argumentos
                            return null;
                        }

                        // Verificar si los nodos evaluados son números y realizar la comparación
                        if (firstNode instanceof Number && secondNode instanceof Number) {
                            Double primerValor = ((Number) firstNode).doubleValue();
                            Double segundoValor = ((Number) secondNode).doubleValue();
                            return primerValor > segundoValor;
                        } else {
                            // Manejar error: los nodos no son números
                            return null;
                        }
                    } else {
                        // Manejar error: cantidad incorrecta de argumentos
                        return null;
                    }
                }

                //Si se encuentra la condiciónal, se evalúan varias condiciones y se vuelve a evaluar lo necesario
                else if (operator.equals("cond")) {
                    for (int i = 1; i < children.size(); i++) {
                        Node1 clause = children.get(i);
                        if (clause instanceof ListNode1) {
                            ListNode1 condClause = (ListNode1) clause;
                            List<Node1> clauseChildren = condClause.getChildren();
                            if (clauseChildren.size() >= 2) {
                                Node1 condition = clauseChildren.get(0);
                                Node1 expression = clauseChildren.get(1);
                                Object result = evaluate(condition);
                                if (result != null && result instanceof Boolean && ((Boolean) result)) {
                                    return evaluate(expression);
                                } else if (result == null) {
                                    return evaluate(expression);
                                }
                            }
                        }
                    }
                    // Si ninguna cláusula se evalúa como verdadera, devolver la frase "murio el cond"
                    return "Niguna se cumplio";
                }

                //Implementación para quote y '
                else if (operator.equals("quote") || operator.equals("'")) {
                    if (children.size() > 1) {
                        return children.get(1);
                    } else {
                        return null;
                    }
                }

                //Setear valor de variables 
                else if (operator.equals("setq")) {
                    if (children.size() == 3 && children.get(1) instanceof AtomNode1) {
                        String variableName = (String) ((AtomNode1) children.get(1)).getValue();
                        Object value = evaluate(children.get(2));
                        variables.put(variableName, value);
                        return value;
                    } else {
                        return null;
                    }
                }
                //Si no se ha encontrado nada, se asume que es un signo de operación aritmética
                else {
                    List<Object> evaluatedOperands = evaluateOperands(children);
                    return performOperation(operator, evaluatedOperands);
                }
            }
        }
        return null;
    }


    
    /** 
     * @param variables
     * @param functionParameters
     * @param Map<String
     * @param functionDefinitions
     * @param evaluator
     */
    // Método para copiar información de variables, parámetros de función y definiciones de función
    public static void copyInformation(Map<String, Object> variables, Map<String, List<String>> functionParameters,
            Map<String, ListNode1> functionDefinitions, Evaluator1 evaluator) {
            
    }
    
    
    /** 
     * @param input
     * @return ArrayList<Object>
     */
    // Método para convertir una cadena de entrada en una lista de elementos
    public static ArrayList<Object> parseStringToList(String input) {
        ArrayList<Object> list = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        boolean inQuotes = false;

        for (char c : input.toCharArray()) {
            if (c == '(' || c == ')' || c == ' ') {
                if (currentToken.length() > 0) {
                    if (inQuotes) {
                        list.add(currentToken.toString());
                    } else {
                        try {
                            int intValue = Integer.parseInt(currentToken.toString());
                            list.add(intValue);
                        } catch (NumberFormatException e) {
                            list.add(currentToken.toString());
                        }
                    }
                    currentToken.setLength(0);
                }

                if (c == '(') {
                    continue;
                } else if (c == ')') {
                    break;
                } else if (c == ' ') {
                    continue;
                }
            }

            if (c == '\'') {
                inQuotes = !inQuotes;
            } else {
                currentToken.append(c);
            }
        }

        return list;
    }

    
    /** 
     * @param input
     * @return ArrayList<String>
     */
    // Método para extraer datos de una cadena de entrada
    public static ArrayList<String> extraerDatos(String input) {
        String modificadoString = input.substring(1, input.length() - 1);
        ArrayList<String> extractedData = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\([^()]*\\)|[^\\s()]+");
        Matcher matcher = pattern.matcher(modificadoString);

        if(input.charAt(1) == '+' || input.charAt(1) == '-' || input.charAt(1) == '/' || input.charAt(1) == '*'){
            modificadoString = input;
            matcher = pattern.matcher(modificadoString);
            while (matcher.find()) {
                String match = matcher.group().trim();
                extractedData.add(match);
            }  
        } else{
            while (matcher.find()) {
                String match = matcher.group().trim();
                extractedData.add(match);
            }
        }
        return extractedData;
    }

    
    /** 
     * @param nodes
     * @return String
     */
    // Método para convertir una lista de nodos en una cadena de texto  
    public String nodesToString(List<Node1> nodes) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (Node1 node : nodes) {
            sb.append(nodeToString(node)).append(" ");
        }
        if (!nodes.isEmpty()) {
            // Eliminamos el espacio extra al final
            sb.setLength(sb.length() - 1);
        }
        sb.append(")");
        return sb.toString();
    }

    
    /** 
     * @param children
     * @return List<Object>
     */
    // Método privado para evaluar los operandos de una expresión
    private List<Object> evaluateOperands(List<Node1> children) {
        List<Object> evaluatedOperands = new ArrayList<>();
        for (int i = 1; i < children.size(); i++) {
            Object operandValue = evaluate(children.get(i));
            evaluatedOperands.add(operandValue);
        }
        return evaluatedOperands;
    }

    
    /** 
     * @param texto
     * @param letras
     * @param numeros
     * @return String
     */
    // Método para reemplazar valores en una cadena de texto
    public static String reemplazar(String texto, List<String> letras, ArrayList<Object> numeros) {
        StringBuilder resultado = new StringBuilder(texto);
        for (int i = 0; i < letras.size(); i++) {
            String letra = letras.get(i);
            Object numero = numeros.get(i);
            // Construir la expresión regular para buscar la letra individualmente
            String regex = "\\b" + Pattern.quote(letra) + "\\b";
            // Reemplazar la letra por el número en el texto utilizando la expresión regular
            resultado = new StringBuilder(resultado.toString().replaceAll(regex, String.valueOf(numero)));
        }
        return resultado.toString();
    }
    

    
    /** 
     * @param functionBody
     * @return String
     */
    // Método privado para convertir el cuerpo de una función en una representación de cadena
    private String stringify(ListNode1 functionBody) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        for (Node1 node : functionBody.getChildren()) {
            if (node instanceof AtomNode1) {
                stringBuilder.append(((AtomNode1) node).getValue());
            } else if (node instanceof ListNode1) {
                stringBuilder.append(stringify((ListNode1) node));
            }
            stringBuilder.append(" ");
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    
    /** 
     * @param node
     * @return Double
     */
    // Método privado para convertir un nodo en un valor de tipo Double
    private Double nodeToDouble(Node1 node) {
        if (node instanceof AtomNode1) {
            Object value = ((AtomNode1) node).getValue();
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
        }
        return null; // Si el nodo no es un AtomNode o no contiene un valor numérico
    }
    
    
    
    /** 
     * @param node
     * @return String
     */
    // Método para convertir un nodo en una cadena de texto
    public String nodeToString(Node1 node) {
    if (node instanceof AtomNode1) {
        // Si es un nodo átomo, devolvemos su valor como cadena
        return ((AtomNode1) node).getValue().toString();
    } else if (node instanceof ListNode1) {
        // Si es un nodo lista, construimos la representación de cadena recursivamente
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        List<Node1> children = ((ListNode1) node).getChildren();
        for (Node1 child : children) {
            stringBuilder.append(nodeToString(child)).append(" ");
        }
        // Eliminamos el último espacio en blanco y cerramos paréntesis
        if (stringBuilder.length() > 1) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    } else {
        // Si es otro tipo de nodo, devolvemos una cadena vacía
        return "";
    }
}
  
    /** 
     * @param obj1
     * @param obj2
     * @return boolean
     */
    // Método privado para verificar la igualdad profunda entre dos objetos
    private boolean deepEquals(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null) {
            return true;
        } else if (obj1 == null || obj2 == null) {
            return false;
        } else if (obj1 instanceof List && obj2 instanceof List) {
            List<?> list1 = (List<?>) obj1;
            List<?> list2 = (List<?>) obj2;
            if (list1.size() != list2.size()) {
                return false;
            }
            for (int i = 0; i < list1.size(); i++) {
                Object value1 = list1.get(i);
                Object value2 = list2.get(i);
                if (!deepEquals(value1, value2)) {
                    return false;
                }
            }
            return true;
        } else {
            return obj1.equals(obj2);
        }
    }
    

    
    /** 
     * @param operator
     * @param operands
     * @return Object
     */
    // Método privado para realizar operaciones matemáticas
    private Object performOperation(String operator, List<Object> operands) {
        if (operator.equals("+")) {
            return sum(operands);
        } else if (operator.equals("-")) {
            return subtract(operands);
        } else if (operator.equals("*")) {
            return multiply(operands);
        } else if (operator.equals("/")) {
            return divide(operands);
        } else {
            // Manejar otros operadores o errores
            return null;
        }
    }

    
    /** 
     * @param operands
     * @return double
     */
    // Método privado para sumar una lista de operandos
    private double sum(List<Object> operands) {
        double result = 0;
        for (Object operand : operands) {
            if (operand instanceof Number) {
                result += ((Number) operand).doubleValue();
            } else {
                // Manejar errores, los operandos deben ser números
            }
        }
        return result;
    }

    
    /** 
     * @param operands
     * @return double
     */
    // Método privado para restar una lista de operandos
    private double subtract(List<Object> operands) {
    if (operands.size() < 2) {
        // Manejar errores, la resta requiere al menos dos operandos
        return 0;
    }
    double result = ((Number) operands.get(0)).doubleValue();
    for (int i = 1; i < operands.size(); i++) {
        result -= ((Number) operands.get(i)).doubleValue();
    }
    return result;
}


    /** 
     * @param operands
     * @return double
     */
    // Método privado para multiplicar una lista de operandos
    private double multiply(List<Object> operands) {
        double result = 1;
        for (Object operand : operands) {
            result *= ((Number) operand).doubleValue();
        }
        return result;
    }

    
    /** 
     * @param operands
     * @return double
     */
    // Método privado para dividir una lista de operandos
    private double divide(List<Object> operands) {
        if (operands.size() < 2) {
            // Manejar errores, la división requiere al menos dos operandos
            return 0;
        }
        double result = ((Number) operands.get(0)).doubleValue();
        for (int i = 1; i < operands.size(); i++) {
            double operandValue = ((Number) operands.get(i)).doubleValue();
            if (operandValue == 0) {
                // Manejar errores, división por cero
                return Double.NaN; // O cualquier otro valor o indicador de error que desees
            }
            result /= operandValue;
        }
        return result;
    }
    
}

