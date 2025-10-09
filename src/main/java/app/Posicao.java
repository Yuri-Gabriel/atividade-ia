package app;

public class Posicao {
    public int y;
    public int x;
    public boolean comida;

    public Posicao(int y, int x) {
        this.y = y;
        this.x = x;
        this.comida = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Posicao)) return false;
        Posicao p = (Posicao) o;
        return this.y == p.y && this.x == p.x;
    }

    @Override
    public int hashCode() {
        return 31 * y + x;
    }
}
