// Interfaz que define un nodo en la estructura del árbol de análisis sintáctico
public interface Node {
    // Método para evaluar el nodo en el contexto del evaluador y devolver un resultado
    Object evaluate(Evaluator evaluator);
}
