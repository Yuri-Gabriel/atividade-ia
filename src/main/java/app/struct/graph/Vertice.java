package app.struct.graph;

import java.util.ArrayList;

public class Vertice<T> {
	
	private T valor;
	private ArrayList<Aresta<T>> arestasEntrada;
	private ArrayList<Aresta<T>> arestasSaida;
	
	public Vertice(T dado) {
		this.valor = dado;
		this.arestasEntrada = new ArrayList<Aresta<T>>();
		this.arestasSaida = new ArrayList<Aresta<T>>();
	}

	public ArrayList<Aresta<T>> getArestasEntrada() {
		return arestasEntrada;
	}

	public void setArestasEntrada(ArrayList<Aresta<T>> arestasEntrada) {
		this.arestasEntrada = arestasEntrada;
	}

	public ArrayList<Aresta<T>> getArestasSaida() {
		return arestasSaida;
	}

	public void setArestasSaida(ArrayList<Aresta<T>> arestasSaida) {
		this.arestasSaida = arestasSaida;
	}

	public T getValor() {
		return valor;
	}

	public void setValor(T valor) {
		this.valor = valor;
	}
	
	public void addArestaEntrada(Aresta<T> aresta) {
		this.arestasEntrada.add(aresta);
	}
	
	public void addArestaSaida(Aresta<T> aresta) {
		this.arestasSaida.add(aresta);
	}
	
}
