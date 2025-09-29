package app.struct.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Grafo<T> {
	
	private ArrayList<Vertice<T>> vertices;
	private ArrayList<Aresta<T>> arestas;
	private FuncBoolean<T> areEqualsFunc;
	
	public Grafo() {
		this.vertices = new ArrayList<Vertice<T>>();
		this.arestas = new ArrayList<Aresta<T>>();
		this.setAreEquals((Vertice<T> v) -> false);
	}
	
	public void addVertice(T valor) throws GrafoException {
		if(this.valorJaInserido(valor)) throw new GrafoException(
			String.format("O valor '%s' já está no grafo", valor.toString())
		);

		Vertice<T> novoVertice = new Vertice<T>(valor);
		this.vertices.add(novoVertice);
	}
	
	public void addAresta(Double peso, T dadoInicio, T dadoFim) throws GrafoException {
		if(!this.valorJaInserido(dadoInicio)) throw new GrafoException(
			String.format("O valor '%s' não está no grafo.", dadoInicio.toString())
		);
		if(!this.valorJaInserido(dadoFim)) throw new GrafoException(
			String.format("O valor '%s' não está no grafo.", dadoFim.toString())
		);
		if(peso == null) throw new GrafoException(
			String.format(
				"Informe um peso à aresta: '%s' ---> '%s'",
				dadoInicio.toString(),
				dadoFim.toString()
			)
		);

		Vertice<T> inicio = this.getVertice(dadoInicio);
		Vertice<T> fim = this.getVertice(dadoFim);
		Aresta<T> aresta = new Aresta<T>(peso, inicio, fim);
		inicio.addArestaSaida(aresta);
		fim.addArestaEntrada(aresta);
		this.arestas.add(aresta);
	}
	
	public Vertice<T> getVertice(T dado) throws GrafoException {
        if(dado == null) throw new GrafoException("Nenhum valor passado");
        
		Vertice<T> vertice = null;
		for(int i=0; i < this.vertices.size();i++) {
			if(this.vertices.get(i).getValor().equals(dado) || this.areEqualsFunc.run(this.vertices.get(i))) {
				vertice = this.vertices.get(i);
				break;
			}
		}
		return vertice;
	}

	public boolean valorJaInserido(T valor) throws GrafoException {
		if(valor == null) throw new GrafoException("Nenhum valor passado");
		Iterator<Vertice<T>> iterator = this.vertices.iterator();
		while(iterator.hasNext()) {
			Vertice<T> next = iterator.next();
			if(next.getValor().equals(valor) || this.areEqualsFunc.run(next)) return true;
		}
		return false;
	}

	public void setAreEquals(FuncBoolean<T> func) {
		this.areEqualsFunc = func;
	}

	private int pegarIndice(T valor) throws GrafoException {
        if(valor == null) throw new GrafoException("Nenhum valor passado");
        
		for(int i = 0; i < this.vertices.size(); i++) {
			if(this.vertices.get(i).getValor().equals(valor) || this.areEqualsFunc.run(this.vertices.get(i))) return i;
		}

		return -1;
	}
	
	public LinkedList<Vertice<T>> buscaEmLarguraCaminho(T origem, T destino) throws GrafoException {
		if (!this.valorJaInserido(origem) || !this.valorJaInserido(destino)) {
			throw new GrafoException("Origem ou destino não estão no grafo.");
		}

		ArrayList<Vertice<T>> marcados = new ArrayList<>();
		Queue<Vertice<T>> fila = new LinkedList<>();
		HashMap<Vertice<T>, Vertice<T>> pais = new HashMap<>();

		Vertice<T> vOrigem = this.getVertice(origem);
		Vertice<T> vDestino = this.getVertice(destino);

		marcados.add(vOrigem);
		fila.add(vOrigem);

		boolean achou = false;

		while (!fila.isEmpty() && !achou) {
			Vertice<T> atual = fila.poll();
			for (Aresta<T> aresta : atual.getArestasSaida()) {
				Vertice<T> prox = aresta.getFim();
				if (!marcados.contains(prox)) {
					marcados.add(prox);
					pais.put(prox, atual);
					fila.add(prox);

					if (prox.equals(vDestino)) {
						achou = true;
						break;
					}
				}
			}
		}

		LinkedList<Vertice<T>> caminho = new LinkedList<>();
		if (achou) {
			Vertice<T> atual = vDestino;
			while (atual != null) {
				caminho.addFirst(atual);
				atual = pais.get(atual);
			}
		}
		return caminho;
	}
	
}
