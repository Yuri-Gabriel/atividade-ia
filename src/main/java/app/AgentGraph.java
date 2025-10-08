package app;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import app.enums.Direcao;
import app.enums.Obstaculo;
import app.struct.graph.Grafo;
import app.struct.graph.GrafoException;
import app.struct.graph.Vertice;

public class AgentGraph {
    private char[][] map;
    private char[][] vision;
    private int current_x;
    private int current_y;

    private Grafo<Posicao> grafo;
    private LinkedList<Posicao> caminho;

    private final char agent_char = 'A';

    public int pontos;

    public AgentGraph(char[][] map) throws GrafoException {
        this.map = map;
        this.vision = new char[3][3];

        this.pontos = 0;

        this.setStartPos();
        this.setVision();

        this.montarGrafo();
    }

    public void montarGrafo() throws GrafoException {
        this.grafo = new Grafo<>();

        Posicao entrada = null;
        Posicao saida = null;

        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                if (!this.eParede(x, y)) {
                    Posicao pos = new Posicao(y, x);
                    this.grafo.addVertice(pos);

                    if (this.map[y][x] == Obstaculo.ENTRADA.getValue()) {
                        entrada = pos;
                    } else if (this.map[y][x] == Obstaculo.SAIDA.getValue()) {
                        saida = pos;
                    }
                }
            }
        }

        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                if (this.eParede(x, y)) continue;

                Posicao origem = new Posicao(y, x);

                for (int[] d : dirs) {
                    int ny = y + d[0];
                    int nx = x + d[1];
                    if (ny >= 0 && ny < this.map.length && nx >= 0 && nx < this.map[0].length) {
                        if (!this.eParede(nx, ny)) {
                            Posicao destino = new Posicao(ny, nx);
                            if (this.grafo.valorJaInserido(origem) && this.grafo.valorJaInserido(destino)) {
                                this.grafo.addAresta(1.0, origem, destino);
                            }
                        }
                    }
                }
            }
        }

        LinkedList<Vertice<Posicao>> verticesCaminho = this.grafo.buscaEmLarguraCaminho(entrada, saida);

        this.caminho = new LinkedList<>();
        for (Vertice<Posicao> v : verticesCaminho) {
            this.caminho.add(v.getValor());
        }
    }

    public void try_exit() {
        this.caminho.forEach((Posicao pos) -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException err) {
                err.printStackTrace();
            }


            this.setPosition(pos.x, pos.y);
            this.atualizarPontos();
            this.showInMap();
        });
        System.out.println("Congratulation!!! happy happy happy!!!");
    }

    private void setPosition(int x, int y) {
        this.current_x = x;
        this.current_y = y;
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
        x = x < 0 ? 0 : x;
        y = y < 0 ? 0 : y;
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
