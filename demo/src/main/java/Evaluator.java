import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Evaluator {

    private final Map<String, Object> variables = new HashMap<>();
    private final Map<String, List<String>> functionParameters = new HashMap<>();
    private final Map<String, ListNode> functionDefinitions = new HashMap<>();

    public Object evaluate(Node node) {
        if (node instanceof AtomNode) {
            Object value = ((AtomNode) node).getValue();
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
        } else if (node instanceof ListNode) {
            ListNode listNode = (ListNode) node;
            List<Node> children = listNode.getChildren();
    
            if (children.isEmpty()) {
                return null;
            } else {
                Node operatorNode = children.get(0);
                if (!(operatorNode instanceof AtomNode)) {
                    return null;
                }
                
                String operator = (String) ((AtomNode) operatorNode).getValue();

                if (operator.equals("defun")) {
                    // Definición de una función
                    if (children.size() != 4 || !(children.get(1) instanceof AtomNode)) {
                        // Error de sintaxis en la definición de la función
                        return null;
                    }
                    String functionName = (String) ((AtomNode) children.get(1)).getValue();
                    ListNode parametersListNode = (ListNode) children.get(2);
                    List<String> parameters = new ArrayList<>();
                    for (Node paramNode : parametersListNode.getChildren()) {
                        if (paramNode instanceof AtomNode) {
                            parameters.add((String) ((AtomNode) paramNode).getValue());
                        } else {
                            // Error de sintaxis en la definición de los parámetros de la función
                            return null;
                        }
                    }
                    functionParameters.put(functionName, parameters);
                    ListNode bodyListNode = (ListNode) children.get(3);
                    functionDefinitions.put(functionName, bodyListNode);
                    return functionName; // Devuelve el nombre de la función como confirmación
                }

                else if (functionDefinitions.containsKey(operator)) {
                    // Llamada a una función definida
                    ListNode functionBody = functionDefinitions.get(operator);
                    List<String> functionParametersList = functionParameters.get(operator);
                    
                    // Crear un mapa de enlaces de argumentos
                    Map<String, Object> argumentBindings = new HashMap<>();
                    
                    // Verificar si la cantidad de argumentos coincide con la cantidad de parámetros
                    if (children.size() - 1 != functionParametersList.size()) {
                        // Manejar error: Cantidad incorrecta de argumentos
                        return null;
                    }
                    
                    // Asignar los valores de los argumentos a los parámetros de la función
                    for (int i = 0; i < functionParametersList.size(); i++) {
                        String parameterName = functionParametersList.get(i);
                        Node argumentNode = children.get(i + 1); // +1 para omitir el operador de la llamada
                        Object argumentValue = evaluate(argumentNode);
                        argumentBindings.put(parameterName, argumentValue);
                        
                    }
                
                    ListNode metodoEjecutable = substituteParameters(functionBody, argumentBindings);
                    String lispConParametros = stringify(metodoEjecutable);
                
                    // Aquí se analiza y evalúa nuevamente la expresión Lisp
                    Lexer lexer = new Lexer(lispConParametros);
                    List<Object> tokens = lexer.tokenize();
                    Parser parser = new Parser(tokens);
                    Node rootNode = parser.parse();
                    Evaluator evaluator = new Evaluator();
                    Object result2 = evaluator.evaluate(rootNode);
                
                    // Retornar el resultado de la evaluación de la nueva expresión Lisp
                    return result2;
                }
                
                else if (operator.equals("atom")) {
                    // Verificar si el nodo tiene solo un elemento
                    if (children.size() == 2) {
                        Node argumentNode = children.get(1);
                        Object result = evaluate(argumentNode);
                        
                        // Si el resultado es un átomo (es decir, no es una lista), devolver true
                        if (result instanceof ListNode) {
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

                else if (operator.equals("list")){
                    if(children.size()>2){
                        return true;
                    } else{
                        return false;
                    }
                }
                
                else if (operator.equals("equal") || operator.equals("=")) {
                    // Verificar si la expresión contiene al menos dos elementos
                    if (children.size() >= 3) {
                        Node firstNode;
                        Node secondNode;
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
                        if(primeraEntrada.equals(segundaEntrada)){
                            return true;
                        } else{
                            return false;
                        }


                    } else {
                        // Manejar error: cantidad incorrecta de argumentos
                        return null;
                    }
                }

                else if (operator.equals("<")){
                    Double primerValor = nodeToDouble(children.get(1));
                    Double segundoValor = nodeToDouble(children.get(2));
                    if(primerValor < segundoValor){
                        return true;
                    } else{
                        return false;
                    }
                }

                else if (operator.equals(">")) {
                    Double primerValor = nodeToDouble(children.get(1));
                    Double segundoValor = nodeToDouble(children.get(2));
                    if (primerValor > segundoValor) {
                        return true;
                    } else {
                        return false;
                    }
                }
                
           

                else if (operator.equals("cond")) {
                    for (int i = 1; i < children.size(); i++) {
                        Node clause = children.get(i);
                        if (clause instanceof ListNode) {
                            ListNode condClause = (ListNode) clause;
                            List<Node> clauseChildren = condClause.getChildren();
                            if (clauseChildren.size() >= 2) {
                                Node condition = clauseChildren.get(0);
                                Node expression = clauseChildren.get(1);
                                Object result = evaluate(condition);
                                if (result != null && result instanceof Boolean && ((Boolean) result)) {
                                    return evaluate(expression);
                                }
                            }
                        }
                    }
                    // Si ninguna cláusula se evalúa como verdadera, devolver la frase "murio el cond"
                    return "Niguna se cumplio";
                }

                else if (operator.equals("quote") || operator.equals("'")) {
                    if (children.size() > 1) {
                        return children.get(1);
                    } else {
                        return null;
                    }
                } else if (operator.equals("setq")) {
                    if (children.size() == 3 && children.get(1) instanceof AtomNode) {
                        String variableName = (String) ((AtomNode) children.get(1)).getValue();
                        Object value = evaluate(children.get(2));
                        variables.put(variableName, value);
                        return value;
                    } else {
                        return null;
                    }
                }
                else {
                    List<Object> evaluatedOperands = evaluateOperands(children);
                    return performOperation(operator, evaluatedOperands);
                }
            }
        }
        return null;
    }

    public String nodesToString(List<Node> nodes) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (Node node : nodes) {
            sb.append(nodeToString(node)).append(" ");
        }
        if (!nodes.isEmpty()) {
            // Eliminamos el espacio extra al final
            sb.setLength(sb.length() - 1);
        }
        sb.append(")");
        return sb.toString();
    }
    

    private List<Object> evaluateOperands(List<Node> children) {
        List<Object> evaluatedOperands = new ArrayList<>();
        for (int i = 1; i < children.size(); i++) {
            Object operandValue = evaluate(children.get(i));
            evaluatedOperands.add(operandValue);
        }
        return evaluatedOperands;
    }

    private String stringify(ListNode functionBody) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        for (Node node : functionBody.getChildren()) {
            if (node instanceof AtomNode) {
                stringBuilder.append(((AtomNode) node).getValue());
            } else if (node instanceof ListNode) {
                stringBuilder.append(stringify((ListNode) node));
            }
            stringBuilder.append(" ");
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    private Double nodeToDouble(Node node) {
        if (node instanceof AtomNode) {
            Object value = ((AtomNode) node).getValue();
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
        }
        return null; // Si el nodo no es un AtomNode o no contiene un valor numérico
    }
    
    
    public String nodeToString(Node node) {
    if (node instanceof AtomNode) {
        // Si es un nodo átomo, devolvemos su valor como cadena
        return ((AtomNode) node).getValue().toString();
    } else if (node instanceof ListNode) {
        // Si es un nodo lista, construimos la representación de cadena recursivamente
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        List<Node> children = ((ListNode) node).getChildren();
        for (Node child : children) {
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


    private ListNode substituteParameters(ListNode functionBody, Map<String, Object> argumentBindings) {
        List<Node> substitutedNodes = new ArrayList<>();
        for (Node node : functionBody.getChildren()) {
            if (node instanceof AtomNode) {
                String atomValue = (String) ((AtomNode) node).getValue();
                if (argumentBindings.containsKey(atomValue)) {
                    // Si el átomo es un parámetro, sustituirlo por su valor correspondiente
                    Object substitutionValue = argumentBindings.get(atomValue);
                    substitutedNodes.add(new AtomNode(substitutionValue));
                } else {
                    // Si el átomo no es un parámetro, conservarlo sin cambios
                    substitutedNodes.add(node);
                }
            } else if (node instanceof ListNode) {
                // Si es una lista, recursivamente sustituir los parámetros en su interior
                ListNode substitutedListNode = substituteParameters((ListNode) node, argumentBindings);
                substitutedNodes.add(substitutedListNode);
            }
        }
        return new ListNode(substitutedNodes);
    }
    

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

private double multiply(List<Object> operands) {
    double result = 1;
    for (Object operand : operands) {
        result *= ((Number) operand).doubleValue();
    }
    return result;
}

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
