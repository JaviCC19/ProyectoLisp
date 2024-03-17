import java.util.List;

public class AtomNode implements Node {
    private Object value;

    public AtomNode(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public Object evaluate(Evaluator evaluator) {
        // Devolver el valor del átomo
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}