package modelo;

import java.util.List;
import java.util.ArrayList;

public class Mapa {
	private int ancho;
	private int altura;
	private List<List<Celda>> map;
	public int posX1, posX2, posY1, posY2;
	private List<Objeto> obstaculos;
	private List<CeldaEspecial> celdasEspeciales;

	public Mapa(int ancho, int altura) {
		obstaculos = new ArrayList<Objeto>();
		map = new ArrayList<List<Celda>>();
		celdasEspeciales = new ArrayList<CeldaEspecial>();
		this.ancho = ancho;
		this.altura = altura;
	}
	
	public Mapa(int ancho, int altura, int posX1, int posX2, int posY1, int posY2) {
		obstaculos = new ArrayList<Objeto>();
		map = new ArrayList<List<Celda>>();
		celdasEspeciales = new ArrayList<CeldaEspecial>();
		this.ancho = ancho;
		this.altura = altura;
		this.posX1 = posX1;
		this.posX2 = posX2;
		this.posY1 = posY1;
		this.posY2 = posY2;
	}
	
	public void setPos1(int posX1,int posY1){
		this.posX1 = posX1;
		this.posY1 = posY1;
	}
	public void setPos2(int posX2,int posY2){
		this.posX2 = posX2;
		this.posY2 = posY2;
	}

	public void addFila(List<Celda> l) {
		map.add(l);
	}

	public void modificarPosJugador(int tipo, int x, int y) {
		List<Celda> aux = map.get(x);
		Celda c = aux.get(y).copy();
		c.setJugador(tipo);
		if (tipo == 0)
			c.setSprite('A');
		else if (tipo == 1)
			c.setSprite('B');
		aux.set(y, c);
	}

	public void retornarCelda(int x, int y, Celda c) {
		List<Celda> aux = map.get(x);
		aux.set(y, c.copy());
	}

	public Celda getCelda(int x, int y) {
		if (x < 0 || x >= altura)
			return null;
		if (y < 0 || y >= ancho)
			return null;
		List<Celda> aux = map.get(x);
		return aux.get(y);
	}

	public Objeto getObstaculo(int i) {
		return obstaculos.get(i);
	}

	public int getAncho() {
		return ancho;
	}

	public int getAltura() {
		return altura;
	}

	public void setObstaculos(List<Objeto> obstaculos) {
		this.obstaculos = obstaculos;
	}

	public void setListaEspecial(List<CeldaEspecial> celdasEspeciales) {
		this.celdasEspeciales = celdasEspeciales;
	}

	public CeldaEspecial getCeldaEspEnLista(int index) {
		return celdasEspeciales.get(index);
	}

	public Mapa copy() {
		Mapa m = new Mapa(ancho, altura, posX1, posX2, posY1, posY2);

		m.setListaEspecial(celdasEspeciales);
		for (int i = 0; i < altura; i++) {
			List<Celda> l = new ArrayList<Celda>();
			for (int j = 0; j < ancho; j++) {
				Celda c = this.getCelda(i, j).copy();
				l.add(c);
			}
			m.addFila(l);
		}
		List<Objeto> obstaculos2 = new ArrayList<Objeto>();
		for(int i=0;i<obstaculos.size();i++){
			obstaculos2.add(obstaculos.get(i).copy());
		}
		m.setObstaculos(obstaculos2);
		return m;
	}
	public void eliminarObstaculo(int i){
		obstaculos.remove(i);
	}
	public int cantidadObstaculos(){return obstaculos.size();}
	public int cantidadCeldasEsp(){return celdasEspeciales.size();}
}