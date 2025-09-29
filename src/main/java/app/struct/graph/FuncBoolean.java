package app.struct.graph;

@FunctionalInterface
public interface FuncBoolean<T> {
    boolean run(Vertice<T> current);
}

