package app.enums;

public enum Obstaculo {
    PAREDE('X'),
    CORREDOR('_'),
    COMIDA('o'),
    SAIDA('S'),
    ENTRADA('E');

    private char value;

    private Obstaculo(char c) {
        this.value = c;
    }

    public char getValue() {
        return this.value;
    }
}
