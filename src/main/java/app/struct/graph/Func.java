package app.struct.graph;

@FunctionalInterface
public interface Func<T> {
    void run(Vertice<T> current);
}

