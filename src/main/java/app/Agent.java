package app;

import java.util.HashMap;
import java.util.Random;

import app.enums.*;

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

    public void try_exit() {
        while(this.getCharInMap(current_x, current_y) != Obstaculo.SAIDA.getValue()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException err) {
                err.printStackTrace();
                break;
            }

            Direcao direcao = this.getNextDirection();
            this.setDirection(direcao);
            
            this.showInMap();
            System.out.println(direcao);
            System.out.println("----------------");
            this.move();
        }
        this.showInMap();
        System.out.println("Congratulation!!! happy happy happy!!!");
    }

    private Direcao getNextDirection() {
        Direcao[] disponiveis = new Direcao[4];
        int size = 0;
        int i = 0;
        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 3; x++) {
                if(this.vision[y][x] != Obstaculo.PAREDE.getValue()) {
                    if(y == 0 && x == 1) {
                        disponiveis[i] = Direcao.NORTE;
                        size++;
                        i++;
                    } else if (y == 1 && x == 2) {
                        disponiveis[i] = Direcao.LESTE;
                        size++;
                        i++;
                    } else if (y == 2 && x == 1) {
                        disponiveis[i] = Direcao.SUL;
                        size++;
                        i++;
                    } else if (y == 1 && x == 0) {
                        disponiveis[i] = Direcao.OESTE;
                        size++;
                        i++;
                    }
                }
            }
        }
        
        int new_random = new Random().nextInt(0, size);
        while(disponiveis[new_random] == null) {
            new_random = new Random().nextInt(0, size);
        }
        return disponiveis[new_random];
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
                String saida = "";
                if(current == Obstaculo.PAREDE.getValue()) {
                    saida = "\033[34m" + current + "\033[0m";
                } else if (current == Obstaculo.COMIDA.getValue()) {
                    saida = "\033[33m" + current + "\033[0m";
                } else if (current == Obstaculo.ENTRADA.getValue()) {
                    saida = "\033[31m" + current + "\033[0m";
                } else if (current == Obstaculo.SAIDA.getValue()) {
                    saida = "\033[32m" + current + "\033[0m";
                } else if (current == this.agent_char) {
                    saida = "\033[35m" + current + "\033[0m";
                } else {
                    saida = String.valueOf(current);
                }
                System.out.print(saida);
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
        return this.getCharInMap(x, y) == Obstaculo.PAREDE.getValue();
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
