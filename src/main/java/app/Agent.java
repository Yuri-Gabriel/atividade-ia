package app;

import java.util.HashMap;

public class Agent {
    private char[][] map;
    private char[][] vision;
    private int current_x;
    private int current_y;

    private final char agent_char = 'A';

    public int pontos;

    private HashMap<Direcao, Runnable> moveFunctions;

    private Direcao direcao;

    public Agent(char[][] map) {
        this.map = map;
        this.vision = new char[3][3];
        this.direcao = Direcao.LESTE;

        this.pontos = 0;

        this.setStartPos();
        this.setVision();

        this.moveFunctions = new HashMap<Direcao, Runnable>();
        this.moveFunctions.put(Direcao.NORTE, () -> this.moverNorte());
        this.moveFunctions.put(Direcao.LESTE, () -> this.moverLeste());
        this.moveFunctions.put(Direcao.SUL, () -> this.moverSul());
        this.moveFunctions.put(Direcao.OESTE, () -> this.moverOeste());
    }

    public void setDirection(Direcao dir) {
        this.direcao = dir;
    }

    public void move() {
        this.moveFunctions.get(this.direcao).run();
        this.setVision();
        this.atualizarPontos();
    }

    private void setStartPos() {
        for(int i = 0; i < this.map.length; i++) {
            for(int j = 0; j < this.map[i].length; j++) {
                if(this.map[i][j] == Obstaculo.ENTRADA.getValue()) {
                    this.current_y = i;
                    this.current_x = j;
                    return;
                }
            }
        }
    }

    public void showInMap() {
        System.out.println("Pontuação: " + this.pontos);
        for(int i = 0; i < this.map.length; i++) {
            for(int j = 0; j < this.map[i].length; j++) {
                char current = (i == this.current_y && j == this.current_x) ? 
                                        this.agent_char : this.map[i][j];
                System.out.print(current);
            }
            System.out.println();
        }
    } 

    private void setVision() {
        int x = this.current_x - 1;
        int y = this.current_y - 1;
        int vision_x = 0;
        int vision_y = 0;
        for(int i = y; i <= y + 2; i++) {
            for(int j = x; j <= x + 2; j++) {
                this.vision[vision_y][vision_x] = this.map[i][j];
                vision_x++;
            }
            vision_y++;
            vision_x = 0;
        }

    }

    public void showVision() {
        System.out.println("Pontuação: " + this.pontos);
        for(int i = 0; i < this.vision.length; i++) {
            for(int j = 0; j < this.vision[i].length; j++) {
                char current = (i == this.current_y && j == this.current_x) ? 
                                        this.agent_char : this.vision[i][j];
                System.out.print(current);
            }
            System.out.println();
        }
    }

    private void moverNorte() {
        if(this.current_y > 0 && !this.eParede(current_x, current_y - 1)) {
            this.current_y--;
        }
    }

    private void moverLeste() {
        if(this.current_x < this.map[0].length && !this.eParede(current_x + 1, current_y)) {
            this.current_x++;
        }
    }

    private void moverSul() {
        if(this.current_y < this.map.length && !this.eParede(current_x, current_y + 1)) {
            this.current_y++;
        }
    }

    private void moverOeste() {
        if(this.current_x > 0 && !this.eParede(current_x - 1, current_y)) {
            this.current_x--;
        }
    }

    private boolean eParede(int x, int y) {
        return this.getCharInMap(current_x - 1, current_y) == Obstaculo.PAREDE.getValue();
    }

    private char getCharInMap(int x, int y) {
        return this.map[y][x];
    }

    private void atualizarPontos() {
        char current_pos = this.map[this.current_y][this.current_x];
        if(current_pos == Obstaculo.COMIDA.getValue()) {
            this.pontos += 10;
            this.map[this.current_y][this.current_x] = Obstaculo.CORREDOR.getValue();
        }
        if(current_pos == Obstaculo.CORREDOR.getValue()) this.pontos--;
    }
}
