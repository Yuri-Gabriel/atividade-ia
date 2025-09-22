package app;

public enum Direcao {
    NORTE('N'),
    LESTE('L'),
    SUL('S'),
    OESTE('O');

    private char value;

    private Direcao(char c) {
        this.value = c;
    }

    public char getValue() {
        return this.value;
    }
}
