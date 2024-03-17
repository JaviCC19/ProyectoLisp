import java.util.List;

public class ListNode implements Node {
    private List<Node> children;

    public ListNode(List<Node> children) {
        this.children = children;
    }

    public List<Node> getChildren() {
        return children;
    }

    @Override
    public Object evaluate(Evaluator evaluator) {
        // Evaluar cada hijo del nodo y devolver el resultado
        // Se puede implementar la lógica de evaluación específica para listas aquí
        // Por ejemplo, evaluar cada subnodo y devolver el resultado
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (Node child : children) {
            sb.append(child.toString()).append(" ");
        }
        sb.append(")");
        return sb.toString();
    }
}