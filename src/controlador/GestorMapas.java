package controlador;

import java.util.ArrayList;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.imageio.ImageIO;

import modelo.*;

public class GestorMapas {
	private List<Mapa> mapas;
	private Mapa mapaActual;
	private PersonajePrincipal jugador1, jugador2;
	private boolean fin1, fin2;
	private int indMapaActual;
	private int cant;
	private String jugador_nombre;
	private String comandoActual;
	private Celda celdaDual, celdaOriginal0, celdaOriginal1;
	private int dual;
	private int tipoDual; // 0 = jugador1, 1= jugador2
	private int indMovimientoEspecial;
	private int vida;
	private CeldaEspecial espAux;
	
	//Constantes
	public static final int MAXY = 12;
	public static final int MAXX = 16;
	public static final int VIDAINICIAL = 10;
	
	//Constructor
	public GestorMapas() {
		mapas = new ArrayList<Mapa>();
		vida = VIDAINICIAL;
		
		try {
			anadirMapas();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		cant = mapas.size();
		fin1 = false;
		fin2 = false;
	}
	
	public String Get_nombre(){
		return jugador_nombre;
	}
	
	private Mapa Leer_Mapa(List<Objeto> obstaculos, int i1, int j1, int i2,int j2, BufferedReader inputStream, ArrayList<String> lineasMapaAux, String nombre) {
	
      //Se va a crear una matriz auxiliar para verificar donde estan los obstaculos
		boolean[][] visit = new boolean[20][20];
		for (int i = 0; i < MAXY; i++) {
			for (int j = 0; j < MAXX; j++) {
				visit[i][j] = false;
			}
		}
		
		// Lectura de archivos de texto
		InputStream in = getClass().getResourceAsStream(nombre);
		inputStream = new BufferedReader(new InputStreamReader(in));

		String linea;
		try {
			while ((linea = inputStream.readLine()) != null) {
				if (linea.equals("")) break;
				lineasMapaAux.add(linea);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		int tamX = lineasMapaAux.get(0).length();
		int tamY = lineasMapaAux.size();
		
		Mapa nuevoMapa = new Mapa(tamX, tamY, i1, j1, i2, j2);

		for (int i = 0; i < tamY; i++) {
			ArrayList<Celda> fila = new ArrayList<Celda>();
			for (int j = 0; j < tamX; j++) {
				Celda celda;
				char charAux = lineasMapaAux.get(i).charAt(j);
				if (charAux == 'S') celda = new Celda(charAux, TipoCelda.TERRENO_A);
				else if (charAux == 'N') celda = new Celda(charAux, TipoCelda.TERRENO_B);
				else if (charAux == 'A') celda = new Celda('S', TipoCelda.TERRENO_A);
				else if (charAux == 'B') celda = new Celda('N', TipoCelda.TERRENO_B);
				else if (charAux == 'C' || charAux == 'D') celda = new Celda(charAux,TipoCelda.TERRENO_AMBOS);
				else celda = new Celda(charAux,TipoCelda.IMPASABLE);
				fila.add(celda);
			}
			nuevoMapa.addFila(fila);
		}
		
		//Lectura de Obstaculos
		Celda celdaAux;
		Objeto objAux;
		for (int i = 0; i < MAXY; i++) {
			for (int j = 0; j < MAXX; j++) {
				celdaAux = nuevoMapa.getCelda(i, j);
				char c = celdaAux.getSprite();
				boolean esObstaculo = c != ' ' && c != 'A' && c != 'B' && c != 'S' && c != 'N' && c != 'D' && c != 'C';
				if (visit[i][j] == false && esObstaculo) {
					int contF = 1, contC = 1; // contador filas y contador columnas
					while (i + contF < MAXY && nuevoMapa.getCelda(i + contF, j).getSprite() == c) contF++;
					while (j + contC < MAXX && nuevoMapa.getCelda(i, contC + j).getSprite() == c) contC++;
					for (int k = 0; k < contF; k++) {
						for (int l = 0; l < contC; l++) {
							visit[i + k][j + l] = true;
						}
					}
					objAux = new Objeto(i, j, contC, contF, c);
					obstaculos.add(objAux);
				}
			}
		}
		
		//Lectura de imagenes
		nombre = nombre.replace(".txt", "");
		for (int i = 0; i < MAXY; i++) {
			int j;
			for (j = 0; j < MAXX; j++) {
				Celda c = nuevoMapa.getCelda(i, j);
				if (c.getSprite() == 'S' || c.getSprite() == 'A') break;
			}
			if (j != 16) {
				for (j = 0; j < 16; j++) {
					Celda c = nuevoMapa.getCelda(i, j);
					try {
						c.setImg(ImageIO.read(getClass().getResource("/imagenes" + nombre + "/piso1.gif")));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			for (j = 0; j < 16; j++) {
				Celda c = nuevoMapa.getCelda(i, j);
				if (c.getSprite() == 'N' || c.getSprite() == 'B')
					break;
			}
			if (j != 16) {
				for (j = 0; j < 16; j++) {
					Celda c = nuevoMapa.getCelda(i, j);
					try {
						c.setImg(ImageIO.read(getClass().getResource(
								"/imagenes" + nombre + "/piso2.gif")));
					} catch (IOException e) {

						e.printStackTrace();
					}
				}
			}

		}
		nuevoMapa.setObstaculos(obstaculos);
		for (int i = 0; i < obstaculos.size(); i++) {
			Objeto bb = obstaculos.get(i);
			int altura = bb.getAltura();
			int ancho = bb.getAncho();
			int posX = bb.getPosX();
			int posY = bb.getPosY();
			char sprite = bb.getSprite();
			if (sprite != 'a' && sprite != 'p' && sprite != 'o'
					&& sprite != 'L' && sprite != 'g' & sprite != 't'
					&& sprite != 'd')
				try {
					bb.setImg(ImageIO.read(getClass().getResource(
							"/imagenes" + nombre + "/" + sprite + ".gif")));
				} catch (IOException e1) {

					e1.printStackTrace();
				}
			for (int j = 0; j < altura; j++) {
				for (int k = 0; k < ancho; k++) {
					celdaAux = nuevoMapa.getCelda(posX + j, posY + k);
					celdaAux.setTipo(TipoCelda.IMPASABLE);
					celdaAux.setSprite(sprite);
					if (sprite == 'a' || sprite == 'p' || sprite == 'o'
							|| sprite == 'L' || sprite == 'g' || sprite == 't'
							|| sprite == 'd')
						try {
							// System.out.println("/imagenes"+nombre+"/"+sprite+".gif");
							celdaAux.setImg(ImageIO.read(getClass().getResource(
									"/imagenes" + nombre + "/" + sprite
											+ ".gif")));
						} catch (IOException e) {

							e.printStackTrace();
						}
				}
			}
		}
		int cantCeldasEspeciales = 0;
		try {
			while ((linea = inputStream.readLine()) != null) {
				if (linea.equals(""))
					break;
				cantCeldasEspeciales = Integer.parseInt(linea);

			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		List<CeldaEspecial> listaCeldaEsp = new ArrayList<CeldaEspecial>();
		for (int i = 0; i < cantCeldasEspeciales; i++) {
			int x, y, esp;
			List<Integer> direccion;

			try {
				CeldaEspecial celdaEsp;
				x = Integer.parseInt(inputStream.readLine());
				y = Integer.parseInt(inputStream.readLine());
				celdaAux = nuevoMapa.getCelda(x, y);
				esp = Integer.parseInt(inputStream.readLine());
				celdaAux.setEspecial(esp);
				if (esp == 1 || esp == -1)
					celdaEsp = new AccionSimple();
				else
					celdaEsp = new AccionDual();
				if (esp == 1 || esp == -1)
					try {
						celdaAux.setImg(ImageIO.read(getClass().getResource(
								"/imagenes/sprite_azul.png")));
					} catch (IOException e) {

						e.printStackTrace();
					}
				else
					try {
						celdaAux.setImg(ImageIO.read(getClass().getResource(
								"/imagenes/sprite_rojo.png")));
					} catch (IOException e) {

						e.printStackTrace();
					}

				if (esp == 1 || esp == -1)
					celdaAux.setSprite('C');
				else
					celdaAux.setSprite('D');

				celdaAux.setIndiceEspecial(Integer.parseInt(inputStream.readLine()));
				celdaEsp.setComandoEspecial(inputStream.readLine());
				direccion = new ArrayList<Integer>();
				int cantMov = Integer.parseInt(inputStream.readLine());
				for (int j = 0; j < cantMov; j++)
					direccion.add(Integer.parseInt(inputStream.readLine()));
				celdaEsp.setDireccionX(direccion);
				direccion = new ArrayList<Integer>();
				for (int j = 0; j < cantMov; j++)
					direccion.add(Integer.parseInt(inputStream.readLine()));
				celdaEsp.setDireccionY(direccion);

				List<Integer> liberaX = new ArrayList<Integer>();
				List<Integer> liberaY = new ArrayList<Integer>();
				List<Integer> valorLiberacion = new ArrayList<Integer>();

				int cantLibera = Integer.parseInt(inputStream.readLine());

				for (int j = 0; j < cantLibera; j++)
					liberaX.add(Integer.parseInt(inputStream.readLine()));
				for (int j = 0; j < cantLibera; j++)
					liberaY.add(Integer.parseInt(inputStream.readLine()));
				for (int j = 0; j < cantLibera; j++)
					valorLiberacion
							.add(Integer.parseInt(inputStream.readLine()));

				celdaEsp.setLiberaX(liberaX);
				celdaEsp.setLiberaY(liberaY);
				celdaEsp.setValorLiberacion(valorLiberacion);

				int cantDestruye = Integer.parseInt(inputStream.readLine());

				if (cantDestruye == 1) {
					List<Integer> indObstaculo = new ArrayList<Integer>();
					List<Celda> reemplazarObstaculo = new ArrayList<Celda>();
					indObstaculo.add(-1);
					indObstaculo.add(-1);
					indObstaculo.add(-1);
					indObstaculo.add(2);
					reemplazarObstaculo.add(null);
					reemplazarObstaculo.add(null);
					reemplazarObstaculo.add(null);
					reemplazarObstaculo
							.add(new Celda('N', TipoCelda.TERRENO_B));
					celdaEsp.setIndObstaculo(indObstaculo);
					celdaEsp.setReemplazarObstaculo(reemplazarObstaculo);
				}

				if (esp == 2 || esp == -2) {
					celdaEsp.setDualOpuesto(Integer.parseInt(inputStream
							.readLine()));
				}

				listaCeldaEsp.add(celdaEsp);
				// System.out.println(cantDestruye);
				inputStream.readLine();

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		nuevoMapa.setListaEspecial(listaCeldaEsp);
		try {
			try {
				while ((linea = inputStream.readLine()) != null) {
					lineasMapaAux.add(linea);
					if (linea.equals(""))
						break;
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
		}
		return nuevoMapa;
	}

	private void anadirMapas() throws IOException {
		Mapa nuevoMapa;
		List<Objeto> obstaculos;
		ArrayList<String> lineasMapaAux = new ArrayList<String>();
		BufferedReader inputStream = null;
		obstaculos = new ArrayList<Objeto>();
		//TAMBIEN TIENE QUE LEER LAS POSICIONES
		nuevoMapa = Leer_Mapa(obstaculos, 2, 11, 0, 0, inputStream, lineasMapaAux,"/MapaTutorial.txt");
		for (int i = 5; i < 9; i++) {
			Celda aux = nuevoMapa.getCelda(i, 15);
			aux.setTipo(TipoCelda.TERRENO_A);
			aux.setEspecial(3);
			aux.setSprite('o');
		}
		for (int i = 0; i < 16; i++) {
			try {
				nuevoMapa.getCelda(0, i).setImg(
						ImageIO.read(getClass().getResource(
								"/imagenes/MapaTutorial/pared.gif")));
			} catch (IOException e) {

				e.printStackTrace();
			}
			try {
				nuevoMapa.getCelda(1, i).setImg(
						ImageIO.read(getClass().getResource(
								"/imagenes/MapaTutorial/piso madera.gif")));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		mapas.add(nuevoMapa);

		// Nivel 1
		lineasMapaAux = new ArrayList<String>();
		inputStream = null;
		obstaculos = new ArrayList<Objeto>();
		nuevoMapa = Leer_Mapa(obstaculos, 5, 9, 15, 15, inputStream, lineasMapaAux,
				"/MapaNivel1.txt");

		for (int i = 3; i < 7; i++) {
			Celda auxi = nuevoMapa.getCelda(i, 0);
			auxi.setTipo(TipoCelda.TERRENO_A);
			auxi.setEspecial(3);
		}
		for (int i = 8; i < 12; i++) {
			Celda auxi = nuevoMapa.getCelda(i, 0);
			auxi.setTipo(TipoCelda.TERRENO_B);
			auxi.setEspecial(3);
		}
		for (int i = 0; i < 16; i++) {
			try {
				nuevoMapa.getCelda(0, i).setImg(
						ImageIO.read(getClass().getResource(
								"/imagenes/MapaNivel1/1.gif")));
			} catch (IOException e) {

				e.printStackTrace();
			}
			try {
				nuevoMapa.getCelda(1, i).setImg(
						ImageIO.read(getClass().getResource(
								"/imagenes/MapaNivel1/1.gif")));
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
		mapas.add(nuevoMapa);
		// Nivel 2
		lineasMapaAux = new ArrayList<String>();
		inputStream = null;
		obstaculos = new ArrayList<Objeto>();
		nuevoMapa = Leer_Mapa(obstaculos, 5, 10, 0, 0, inputStream, lineasMapaAux,
				"/MapaNivel2.txt");

		for (int j = 12; j < 16; j++) {
			Celda aux2 = nuevoMapa.getCelda(6, j);
			aux2.setTipo(TipoCelda.TERRENO_A);
			aux2.setEspecial(0);
			aux2.setSprite('o');
			aux2 = nuevoMapa.getCelda(7, j);
			aux2.setTipo(TipoCelda.TERRENO_B);
			aux2.setEspecial(0);
			aux2.setSprite('o');
		}

		Celda aux2 = nuevoMapa.getCelda(6, 15);
		aux2.setEspecial(3);
		aux2 = nuevoMapa.getCelda(7, 15);
		aux2.setEspecial(3);

		mapas.add(nuevoMapa);

	}
	
	public void cargarMapa(int index,String nombre_j) {

		if (index > cant)
			return;
		mapaActual = mapas.get(index).copy();
		indMapaActual = index;
		jugador_nombre= nombre_j;
		jugador1 = new PersonajePrincipal(mapaActual.posX1, mapaActual.posY1);
		Celda c = mapaActual.getCelda(jugador1.getPosX(), jugador1.getPosY());
		celdaOriginal0 = c.copy();
		c.setJugador(0);
		c.setSprite('A');
		jugador2 = new PersonajePrincipal(mapaActual.posX2, mapaActual.posY2);
		c = mapaActual.getCelda(jugador2.getPosX(), jugador2.getPosY());
		celdaOriginal1 = c.copy();
		c.setJugador(1);
		c.setSprite('B');
	}

	public void realizarMovimiento(int mov) {

		Celda c, c2 = null;
		int posXAux, posYAux;
		if (mov == -1)
			return;

		if (mov < 4) {
			c = celdaOriginal0;
			posXAux = jugador1.getPosX();
			posYAux = jugador1.getPosY();
		} else {
			c = celdaOriginal1;
			posXAux = jugador2.getPosX();
			posYAux = jugador2.getPosY();
		}
		if (mov == 0)
			c2 = mapaActual
					.getCelda(jugador1.getPosX() - 1, jugador1.getPosY());
		else if (mov == 1)
			c2 = mapaActual
					.getCelda(jugador1.getPosX(), jugador1.getPosY() - 1);
		else if (mov == 2)
			c2 = mapaActual
					.getCelda(jugador1.getPosX() + 1, jugador1.getPosY());
		else if (mov == 3)
			c2 = mapaActual
					.getCelda(jugador1.getPosX(), jugador1.getPosY() + 1);
		else if (mov == 4)
			c2 = mapaActual
					.getCelda(jugador2.getPosX() - 1, jugador2.getPosY());
		else if (mov == 5)
			c2 = mapaActual
					.getCelda(jugador2.getPosX(), jugador2.getPosY() - 1);
		else if (mov == 6)
			c2 = mapaActual
					.getCelda(jugador2.getPosX() + 1, jugador2.getPosY());
		else if (mov == 7)
			c2 = mapaActual
					.getCelda(jugador2.getPosX(), jugador2.getPosY() + 1);
		if (c2 != null)
			c2 = c2.copy();
		boolean aux = true;
		if (mov == 0)
			aux = jugador1.verificarPosX(1, 0, mapaActual.getAltura(),
					mapaActual, TipoCelda.TERRENO_A);
		else if (mov == 1)
			aux = jugador1.verificarPosY(1, 0, mapaActual.getAncho(),
					mapaActual, TipoCelda.TERRENO_A);
		else if (mov == 2)
			aux = jugador1.verificarPosX(0, 0, mapaActual.getAltura(),
					mapaActual, TipoCelda.TERRENO_A);
		else if (mov == 3)
			aux = jugador1.verificarPosY(0, 0, mapaActual.getAncho(),
					mapaActual, TipoCelda.TERRENO_A);
		else if (mov == 4)
			aux = jugador2.verificarPosX(1, 0, mapaActual.getAltura(),
					mapaActual, TipoCelda.TERRENO_B);
		else if (mov == 5)
			aux = jugador2.verificarPosY(1, 0, mapaActual.getAncho(),
					mapaActual, TipoCelda.TERRENO_B);
		else if (mov == 6)
			aux = jugador2.verificarPosX(0, 0, mapaActual.getAltura(),
					mapaActual, TipoCelda.TERRENO_B);
		else if (mov == 7)
			aux = jugador2.verificarPosY(0, 0, mapaActual.getAncho(),
					mapaActual, TipoCelda.TERRENO_B);
		if (!aux)
			return;

		mapaActual.retornarCelda(posXAux, posYAux, c);

		if (mov < 4) {
			celdaOriginal0 = c2.copy();
			fin1 = false;
			if (tipoDual == 0)
				dual = -1;
			mapaActual.modificarPosJugador(0, jugador1.getPosX(),
					jugador1.getPosY());
		} else {
			celdaOriginal1 = c2.copy();
			fin2 = false;
			if (tipoDual == 1)
				dual = -1;
			mapaActual.modificarPosJugador(1, jugador2.getPosX(),
					jugador2.getPosY());
		}
	}

	public String realizarMovimientoEspecial(int mov) {
		Celda c;
		if (mov == -1)
			return "";
		if (mov < 4)
			c = mapaActual.getCelda(jugador1.getPosX(), jugador1.getPosY());
		else
			c = mapaActual.getCelda(jugador2.getPosX(), jugador2.getPosY());
		int especial = c.getEspecial();
		int ind = c.getIndiceEspecial();
		if (especial == 0)
			return "";
		else if (especial == 1) { // comando solo
			indMovimientoEspecial = 0;
			CeldaEspecial celdaEsp = mapaActual.getEspecial(ind);
			comandoActual = celdaEsp.getComandoEspecial();
			List<Integer> liberaX = celdaEsp.getLiberaX();
			List<Integer> liberaY = celdaEsp.getLiberaY();
			List<Integer> valorLiberacion = celdaEsp.getvalorLiberacion();

			for (int i = 0; i < liberaX.size(); i++) {
				Celda c2 = mapaActual.getCelda(liberaX.get(i), liberaY.get(i));
				c2.setEspecial(valorLiberacion.get(i));
			}
			if (mov < 4)
				realizarMovimientoEspecial(4); // se verifica si el otro
												// personaje ya esta en un dual
			else
				realizarMovimientoEspecial(0); // que se activo ahora
			c.setEspecial(0);
			if (mov < 4)
				celdaOriginal0.setEspecial(0);
			else
				celdaOriginal1.setEspecial(0);

			return comandoActual;
		} else if (especial == 2) { // comando dual

			CeldaEspecial celdaEsp = mapaActual.getEspecial(ind);
			int dualOpuesto = celdaEsp.getDualOpuesto();
			if (dual == dualOpuesto) {
				comandoActual = celdaEsp.getComandoEspecial();
				celdaOriginal0.setEspecial(0);
				celdaOriginal1.setEspecial(0);
				c.setEspecial(0);
				celdaDual.setEspecial(0);
			} else {
				indMovimientoEspecial = 0;
				dual = ind;
				celdaDual = c;
				if (mov < 4)
					tipoDual = 0;
				else
					tipoDual = 1;
				return "";
			}
			return comandoActual;
		} else if (especial == 3) { // llego a fin de mapa
			if (mov < 4) {
				if (fin2)
					return "F";
				else
					fin1 = true;
			} else {
				if (fin1)
					return "F";
				else
					fin2 = true;
			}
		}
		return "";
	}

	public String ejecutarComando(int mov) {
		Celda c;
		// Scanner scanner = new Scanner(System.in);
		if (mov < 4)
			c = mapas.get(indMapaActual).getCelda(jugador1.getPosX(),
					jugador1.getPosY());
		else
			c = mapas.get(indMapaActual).getCelda(jugador2.getPosX(),
					jugador2.getPosY());
		CeldaEspecial celdaEspp;
		if (indMovimientoEspecial == 0) {
			// int especial = c.getEspecial();
			int ind = c.getIndiceEspecial();
			celdaEspp = mapaActual.getEspecial(ind);
			espAux = celdaEspp;
		} else {
			celdaEspp = espAux;
		}
		celdaEspp.ejecutarEspecial(mov, mapaActual, jugador1, jugador2,
				celdaOriginal0, celdaOriginal1, indMovimientoEspecial);
		indMovimientoEspecial += 1;
		// List<Integer> aux = celdaEspp.getDireccionX();
		// List<Integer> auy = celdaEspp.getDireccionY();
		if (celdaEspp.getDireccionX().size() == indMovimientoEspecial) {
			if (realizarMovimientoEspecial(0).equals("F"))
				return "F";
			if (realizarMovimientoEspecial(4).equals("F"))
				return "F";
			return "Done";
		}
		return "";
	}

	public void reiniciarVida() {
		vida = 10;
	}

	public boolean perderVida(int v) {
		vida -= v;
		return (vida > 0);
	}

	public Mapa getMapaActual() {
		return mapaActual;
	}

	public int getIndMapaActual() {
		return indMapaActual;
	}

	public int getVida() {
		return vida;
	}

	public void recargarImagenes() {
		Celda aux;
		mapas = new ArrayList<Mapa>();
		try {
			anadirMapas();
		} catch (IOException e2) {

			e2.printStackTrace();
		}
		String nombre;
		if (indMapaActual == 0)
			nombre = "/MapaTutorial";
		else
			nombre = "/MapaNivel" + indMapaActual;

		celdaOriginal0.setImg(mapas.get(indMapaActual)
				.getCelda(jugador1.getPosX(), jugador1.getPosY()).getImg());
		celdaOriginal1.setImg(mapas.get(indMapaActual)
				.getCelda(jugador2.getPosX(), jugador2.getPosY()).getImg());

		for (int i = 0; i < 12; i++) {
			int j;
			for (j = 0; j < 16; j++) {
				Celda c = mapas.get(indMapaActual).getCelda(i, j);
				if (c.getSprite() == 'S' || c.getSprite() == 'A')
					break;
			}
			if (j != 16) {
				for (j = 0; j < 16; j++) {
					Celda c = mapaActual.getCelda(i, j);
					try {
						c.setImg(ImageIO.read(getClass().getResource(
								"/imagenes" + nombre + "/piso1.gif")));
					} catch (IOException e) {

						e.printStackTrace();
					}
				}
			}

			for (j = 0; j < 16; j++) {
				Celda c = mapas.get(indMapaActual).getCelda(i, j);
				if (c.getSprite() == 'N' || c.getSprite() == 'B')
					break;
			}
			if (j != 16) {
				for (j = 0; j < 16; j++) {
					Celda c = mapaActual.getCelda(i, j);
					try {
						c.setImg(ImageIO.read(getClass().getResource(
								"/imagenes" + nombre + "/piso2.gif")));
					} catch (IOException e) {

						e.printStackTrace();
					}
				}
			}
		}
		for (int i = 0; i < mapaActual.cantidadObstaculos(); i++) {
			Objeto bb = mapaActual.getObstaculo(i);
			int altura = bb.getAltura();
			int ancho = bb.getAncho();
			int posX = bb.getPosX();
			int posY = bb.getPosY();
			char sprite = bb.getSprite();
			if (sprite != 'a' && sprite != 'p' && sprite != 'o'
					&& sprite != 'L' && sprite != 'g' & sprite != 't'
					&& sprite != 'd')
				try {
					bb.setImg(ImageIO.read(getClass().getResource(
							"/imagenes" + nombre + "/" + sprite + ".gif")));
				} catch (IOException e1) {

					e1.printStackTrace();
				}
			for (int j = 0; j < altura; j++) {
				for (int k = 0; k < ancho; k++) {
					aux = mapaActual.getCelda(posX + j, posY + k);
					aux.setTipo(TipoCelda.IMPASABLE);
					aux.setSprite(sprite);
					if (sprite == 'a' || sprite == 'p' || sprite == 'o'
							|| sprite == 'L' || sprite == 'g' || sprite == 't'
							|| sprite == 'd')
						try {
							aux.setImg(ImageIO.read(getClass().getResource("/imagenes" + nombre + "/" + sprite+ ".gif")));
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}
		}
		if (indMapaActual == 0) {
			for (int i = 0; i < 16; i++) {
				try {
					mapaActual.getCelda(0, i).setImg(
							ImageIO.read(getClass().getResource(
									"/imagenes/MapaTutorial/pared.gif")));
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					mapaActual.getCelda(1, i).setImg(
							ImageIO.read(getClass().getResource(
									"/imagenes/MapaTutorial/piso madera.gif")));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (indMapaActual == 1) {
			for (int i = 0; i < 16; i++) {
				try {
					mapaActual.getCelda(0, i).setImg(
							ImageIO.read(getClass().getResource(
									"/imagenes/MapaNivel1/1.gif")));
				} catch (IOException e) {

					e.printStackTrace();
				}
				try {
					mapaActual.getCelda(1, i).setImg(
							ImageIO.read(getClass().getResource(
									"/imagenes/MapaNivel1/1.gif")));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		int cant = mapaActual.cantidadCeldasEsp();
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 16; j++) {
				aux = mapas.get(indMapaActual).getCelda(i, j);
				if (aux.getEspecial() == 1 || aux.getEspecial() == -1) {
					try {
						mapaActual.getCelda(i, j).setImg(
								ImageIO.read(getClass().getResource(
										"/imagenes/sprite_azul.png")));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (aux.getEspecial() == 2 || aux.getEspecial() == -2) {
					try {
						mapaActual.getCelda(i, j).setImg(
								ImageIO.read(getClass().getResource(
										"/imagenes/sprite_rojo.png")));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}