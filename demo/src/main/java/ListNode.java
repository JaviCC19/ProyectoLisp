import java.util.List;

// Clase que representa un nodo que contiene una lista de nodos hijos
public class ListNode implements Node {
    private List<Node> children; // Lista de nodos hijos

    // Constructor que inicializa la lista de nodos hijos
    public ListNode(List<Node> children) {
        this.children = children;
    }

    
    /** 
     * @return List<Node>
     */
    // Método para obtener la lista de nodos hijos
    public List<Node> getChildren() {
        return children;
    }

    
    /** 
     * @param evaluator
     * @return Object
     */
    // Método para evaluar el nodo utilizando un evaluador dado
    @Override
    public Object evaluate(Evaluator evaluator) {
        // Este método podría implementarse para evaluar la lista de nodos hijos
        // utilizando el evaluador proporcionado y devolver el resultado
        // La lógica específica de evaluación para listas puede implementarse aquí
        // Por ejemplo, evaluar cada subnodo y devolver el resultado combinado
        return null; // En este caso, se devuelve null ya que la implementación real depende del evaluador utilizado
    }

    
    /** 
     * @return String
     */
    // Método para representar el nodo como una cadena de texto
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("("); // Agregar el inicio de la lista
        for (Node child : children) {
            sb.append(child.toString()).append(" "); // Representar cada nodo hijo como una cadena y agregarlo a la representación de la lista
        }
        sb.append(")"); // Agregar el final de la lista
        return sb.toString(); // Devolver la representación de la lista como una cadena
    }
}
