package app.struct;

public class Timer {
    public static void withTimer(Runnable func) {
        long startTime = System.nanoTime();
        func.run();

        long endTime = System.nanoTime();
        double duration = (endTime - startTime)  / 1000 / 1000 / 1000;
        System.out.println(String.format("Tempo de execução: %.2f s", duration));
    }
}
