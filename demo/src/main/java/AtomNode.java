import java.util.List;

// Implementación de la interfaz Node para representar un nodo átomo en una estructura de árbol
public class AtomNode implements Node {
    // Valor almacenado en el nodo átomo
    private Object value;

    // Constructor que recibe el valor del átomo
    public AtomNode(Object value) {
        this.value = value;
    }

    // Método para obtener el valor del átomo
    // @return El valor del átomo
    public Object getValue() {
        return value;
    }

    // Método para evaluar el nodo átomo utilizando un evaluador específico
    // @param evaluator El evaluador a utilizar para la evaluación
    // @return El valor del átomo
    @Override
    public Object evaluate(Evaluator evaluator) {
        // Devolver el valor del átomo
        return value;
    }

    // Método para representar el nodo átomo como una cadena de texto
    // @return Representación en cadena de texto del valor del átomo
    @Override
    public String toString() {
        return value.toString();
    }
}
