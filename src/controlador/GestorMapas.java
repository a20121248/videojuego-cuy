package controlador;

import java.util.ArrayList;
import java.io.BufferedReader;
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
	private int cantidadMapas;
	private String nombreJugador;
	private String comandoActual;
	private Celda celdaDual, celdaOriginal0, celdaOriginal1;
	private int dual;
	private int tipoDual; // 0 = jugador1, 1= jugador2
	private int indMovimientoEspecial;
	private int vida;
	private CeldaEspecial espAux;

	// Constantes
	public static final int MAXALTURA = 12;
	public static final int MAXANCHO = 16;
	public static final int VIDAINICIAL = 10;

	public static final int SINACCION = 0;
	public static final int SOLODESACTIVADO = -1;
	public static final int SOLOACTIVADO = 1;
	public static final int DUODESACTIVADO = -2;
	public static final int DUOACTIVADO = 2;
	public static final int FINNIVEL = 3;

	public static final int ARRIBAJ1 = 0;
	public static final int IZQUIERDAJ1 = 1;
	public static final int ABAJOJ1 = 2;
	public static final int DERECHAJ1 = 3;
	public static final int ARRIBAJ2 = 4;
	public static final int IZQUIERDAJ2 = 5;
	public static final int ABAJOJ2 = 6;
	public static final int DERECHAJ2 = 7;
	public static final int JUGADOR1 = 0;
	public static final int JUGADOR2 = 1;

	public static final int TUTORIAL = 0;
	public static final int NIVEL1 = 1;
	public static final int NIVEL2 = 2;

	// Constructor
	public GestorMapas() {
		mapas = new ArrayList<Mapa>();
		vida = VIDAINICIAL;

		try {
			anadirMapas();
		} catch (IOException e) {
			e.printStackTrace();
		}

		cantidadMapas = mapas.size();
		fin1 = false;
		fin2 = false;
	}

	private void anadirMapas() throws IOException {

		Mapa nuevoMapa;
		List<Objeto> obstaculos;
		ArrayList<String> lineasMapaAux = new ArrayList<String>();
		BufferedReader inputStream = null;
		obstaculos = new ArrayList<Objeto>();

		nuevoMapa = leerMapa(obstaculos, inputStream, lineasMapaAux, "/MapaTutorial.txt");
		for (int i = 5; i < 9; i++) {
			Celda aux = nuevoMapa.getCelda(i, 15);
			aux.setTipo(TipoCelda.TERRENO_A);
			aux.setTipoCeldaEsp(3);
			aux.setSprite('o');
		}

		for (int i = 0; i < 16; i++) {
			try {
				nuevoMapa.getCelda(0, i)
						.setImg(ImageIO.read(getClass().getResource("/imagenes/MapaTutorial/pared.gif")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				nuevoMapa.getCelda(1, i)
						.setImg(ImageIO.read(getClass().getResource("/imagenes/MapaTutorial/piso madera.gif")));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		mapas.add(nuevoMapa);

		// Nivel 1
		lineasMapaAux = new ArrayList<String>();
		inputStream = null;
		obstaculos = new ArrayList<Objeto>();
		nuevoMapa = leerMapa(obstaculos, inputStream, lineasMapaAux, "/MapaNivel1.txt");

		for (int i = 3; i < 7; i++) {
			Celda auxi = nuevoMapa.getCelda(i, 0);
			auxi.setTipo(TipoCelda.TERRENO_A);
			auxi.setTipoCeldaEsp(3);
		}
		for (int i = 8; i < 12; i++) {
			Celda auxi = nuevoMapa.getCelda(i, 0);
			auxi.setTipo(TipoCelda.TERRENO_B);
			auxi.setTipoCeldaEsp(3);
		}
		for (int i = 0; i < 16; i++) {
			try {
				nuevoMapa.getCelda(0, i).setImg(ImageIO.read(getClass().getResource("/imagenes/MapaNivel1/1.gif")));
			} catch (IOException e) {

				e.printStackTrace();
			}
			try {
				nuevoMapa.getCelda(1, i).setImg(ImageIO.read(getClass().getResource("/imagenes/MapaNivel1/1.gif")));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		mapas.add(nuevoMapa);
		// Nivel 2
		lineasMapaAux = new ArrayList<String>();
		inputStream = null;
		obstaculos = new ArrayList<Objeto>();
		nuevoMapa = leerMapa(obstaculos, inputStream, lineasMapaAux, "/MapaNivel2.txt");

		for (int j = 12; j < 16; j++) {
			Celda aux2 = nuevoMapa.getCelda(6, j);
			aux2.setTipo(TipoCelda.TERRENO_A);
			aux2.setTipoCeldaEsp(0);
			aux2.setSprite('o');
			try {
				aux2.setImg(ImageIO
						.read(getClass().getResource("/imagenes" + "/MapaNivel2" + "/" + aux2.getSprite() + ".gif")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			aux2 = nuevoMapa.getCelda(7, j);
			aux2.setTipo(TipoCelda.TERRENO_B);
			aux2.setTipoCeldaEsp(0);
			aux2.setSprite('o');
			try {
				aux2.setImg(ImageIO
						.read(getClass().getResource("/imagenes" + "/MapaNivel2" + "/" + aux2.getSprite() + ".gif")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Celda aux2 = nuevoMapa.getCelda(6, 15);
		aux2.setTipoCeldaEsp(3);
		aux2 = nuevoMapa.getCelda(7, 15);
		aux2.setTipoCeldaEsp(3);

		for (int i = 0; i < 8; i++) {
			try {
				nuevoMapa.getCelda(0, i * 2)
						.setImg(ImageIO.read(getClass().getResource("/imagenes/MapaNivel2/fondo.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				nuevoMapa.getCelda(1, i * 2)
						.setImg(ImageIO.read(getClass().getResource("/imagenes/MapaNivel2/fondo.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		mapas.add(nuevoMapa);
	}

	private Mapa leerMapa(List<Objeto> obstaculos, BufferedReader inputStream, ArrayList<String> lineasMapaAux,
			String nombre) {

		InputStream in = getClass().getResourceAsStream(nombre);
		inputStream = new BufferedReader(new InputStreamReader(in));
		
		Mapa nuevoMapa = leerDatosDeArchivoTxt(obstaculos, inputStream, lineasMapaAux);
		nombre = nombre.replace(".txt", "");
		asignarPisosAlMapa(nuevoMapa, nombre);
		asignarImagenesAlMapa(obstaculos, nuevoMapa, nombre);

		// Asignar Eventos
		int cantCeldasEspeciales = leerCantidadCeldasEspeciales(inputStream);
		List<CeldaEspecial> listaCeldaEsp = new ArrayList<CeldaEspecial>();
		
		for (int i = 0; i < cantCeldasEspeciales; i++) {
			int x, y, tipoCeldaEsp;

			Celda celdaAux;

			List<Integer> direccion;
			try {

				// Crear nueva celda Especial
				CeldaEspecial celdaEsp;
				x = Integer.parseInt(inputStream.readLine());
				y = Integer.parseInt(inputStream.readLine());
				celdaAux = nuevoMapa.getCelda(x, y);
				tipoCeldaEsp = Integer.parseInt(inputStream.readLine());
				celdaAux.setTipoCeldaEsp(tipoCeldaEsp);
				// En base al tipo de celda Especial se le va a asignar un tipo de accion
				boolean esAccionSolo = tipoCeldaEsp == SOLOACTIVADO || tipoCeldaEsp == SOLODESACTIVADO;
				if (esAccionSolo)
					celdaEsp = new AccionSimple();
				else
					celdaEsp = new AccionDual();
				// Y tambien un tipo de Imagen
				if (esAccionSolo)
					try {
						celdaAux.setImg(ImageIO.read(getClass().getResource("/imagenes/sprite_azul.png")));
					} catch (IOException e) {
						e.printStackTrace();
					}
				else
					try {
						celdaAux.setImg(ImageIO.read(getClass().getResource("/imagenes/sprite_rojo.png")));
					} catch (IOException e) {
						e.printStackTrace();
					}
				if (esAccionSolo)
					celdaAux.setSprite('C');
				else
					celdaAux.setSprite('D');

				// Se le va a asignar los movimientos que se ejecutarán en la
				// Acción
				celdaAux.setIndiceEspecial(Integer.parseInt(inputStream.readLine()));
				celdaEsp.setComandoEspecial(inputStream.readLine());
				celdaEsp.setTiempo(Integer.parseInt(inputStream.readLine()));
				direccion = new ArrayList<Integer>();
				int cantMov = Integer.parseInt(inputStream.readLine());
				for (int j = 0; j < cantMov; j++)
					direccion.add(Integer.parseInt(inputStream.readLine()));
				celdaEsp.setDireccionX(direccion);
				direccion = new ArrayList<Integer>();
				for (int j = 0; j < cantMov; j++)
					direccion.add(Integer.parseInt(inputStream.readLine()));
				celdaEsp.setDireccionY(direccion);

				// Se le va a asignar las nuevas acciones a liberarse una vez se
				// completa la Acción
				List<Integer> liberaX = new ArrayList<Integer>();
				List<Integer> liberaY = new ArrayList<Integer>();
				List<Integer> valorLiberacion = new ArrayList<Integer>();

				int cantLibera = Integer.parseInt(inputStream.readLine());

				for (int j = 0; j < cantLibera; j++)
					liberaX.add(Integer.parseInt(inputStream.readLine()));
				for (int j = 0; j < cantLibera; j++)
					liberaY.add(Integer.parseInt(inputStream.readLine()));
				for (int j = 0; j < cantLibera; j++)
					valorLiberacion.add(Integer.parseInt(inputStream.readLine()));

				celdaEsp.setLiberaX(liberaX);
				celdaEsp.setLiberaY(liberaY);
				celdaEsp.setValorLiberacion(valorLiberacion);

				// Para los casos en donde se necesite romper un objeto se va a
				// implementar una forma de reemplazarlo
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
					reemplazarObstaculo.add(new Celda('N', TipoCelda.TERRENO_B));
					celdaEsp.setIndObstaculo(indObstaculo);
					celdaEsp.setReemplazarObstaculo(reemplazarObstaculo);
				}

				if (tipoCeldaEsp == DUOACTIVADO || tipoCeldaEsp == DUODESACTIVADO)
					celdaEsp.setDualOpuesto(Integer.parseInt(inputStream.readLine()));

				listaCeldaEsp.add(celdaEsp);
				inputStream.readLine();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Se le asigna los eventos de las Celdas Especiales al Mapa
		nuevoMapa.setListaEspecial(listaCeldaEsp);
		try {
			try {
				String linea;
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

	private Mapa leerDatosDeArchivoTxt(List<Objeto> obstaculos, BufferedReader inputStream, ArrayList<String> lineasMapaAux) {
		String linea;
		try {
			while ((linea = inputStream.readLine()) != null) {
				if (linea.equals(""))
					break;
				lineasMapaAux.add(linea);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		int tamX = lineasMapaAux.get(0).length();
		int tamY = lineasMapaAux.size();
		Mapa nuevoMapa = new Mapa(tamX, tamY);
		asignarTipoTerrenoAlMapa(lineasMapaAux, nuevoMapa, tamX, tamY);
		lecturaDeObstaculosAlMapa(nuevoMapa, obstaculos);
		return nuevoMapa;
	}

	private void asignarPisosAlMapa(Mapa nuevoMapa, String nombre) {

		for (int i = 0; i < MAXALTURA; i++) {
			int j;

			// Lectura de piso1 para el personaje A
			for (j = 0; j < MAXANCHO; j++) {
				Celda celdaAux = nuevoMapa.getCelda(i, j);
				if (celdaAux.getSprite() == 'S' || celdaAux.getSprite() == 'A')
					break;
			}
			if (j != MAXANCHO) {
				for (j = 0; j < MAXANCHO; j++) {
					Celda celdaAux = nuevoMapa.getCelda(i, j);
					try {
						celdaAux.setImg(ImageIO.read(getClass().getResource("/imagenes" + nombre + "/piso1.gif")));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			// Lectura de piso2 para el personaje B
			for (j = 0; j < MAXANCHO; j++) {
				Celda celdaAux = nuevoMapa.getCelda(i, j);
				if (celdaAux.getSprite() == 'N' || celdaAux.getSprite() == 'B')
					break;
			}
			if (j != MAXANCHO) {
				for (j = 0; j < MAXANCHO; j++) {
					Celda c = nuevoMapa.getCelda(i, j);
					try {
						c.setImg(ImageIO.read(getClass().getResource("/imagenes" + nombre + "/piso2.gif")));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}
	
	private void asignarTipoTerrenoAlMapa(ArrayList<String> lineasMapaAux, Mapa nuevoMapa, int tamX, int tamY) {
		for (int i = 0; i < tamY; i++) {
			ArrayList<Celda> fila = new ArrayList<Celda>();
			for (int j = 0; j < tamX; j++) {
				Celda celda;
				char charAux = lineasMapaAux.get(i).charAt(j);
				if (charAux == 'S')
					celda = new Celda(charAux, TipoCelda.TERRENO_A);
				else if (charAux == 'N')
					celda = new Celda(charAux, TipoCelda.TERRENO_B);
				else if (charAux == 'A') {
					celda = new Celda('S', TipoCelda.TERRENO_A);
					nuevoMapa.setPos1(i, j);
				} else if (charAux == 'B') {
					celda = new Celda('N', TipoCelda.TERRENO_B);
					nuevoMapa.setPos2(i, j);
				} else if (charAux == 'C' || charAux == 'D')
					celda = new Celda(charAux, TipoCelda.TERRENO_AMBOS);
				else
					celda = new Celda(charAux, TipoCelda.IMPASABLE);
				fila.add(celda);
			}
			nuevoMapa.addFila(fila);
		}
	}

	private int leerCantidadCeldasEspeciales(BufferedReader inputStream){
		int cantCeldasEspeciales=0;
		String linea;
		
		try {
			while ((linea = inputStream.readLine()) != null) {
				if (linea.equals(""))
					break;
				cantCeldasEspeciales = Integer.parseInt(linea);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return cantCeldasEspeciales;
	}
	
	private void lecturaDeObstaculosAlMapa(Mapa nuevoMapa, List<Objeto> obstaculos) {

		// Se va a crear una matriz auxiliar para verificar donde estan los
		// obstaculos
		boolean[][] visit = new boolean[20][20];
		for (int i = 0; i < MAXALTURA; i++) {
			for (int j = 0; j < MAXANCHO; j++) {
				visit[i][j] = false;
			}
		}

		Celda celdaAux;
		Objeto objAux;
		for (int i = 0; i < MAXALTURA; i++) {
			for (int j = 0; j < MAXANCHO; j++) {
				celdaAux = nuevoMapa.getCelda(i, j);
				char c = celdaAux.getSprite();
				boolean esObstaculo = c != ' ' && c != 'A' && c != 'B' && c != 'S' && c != 'N' && c != 'D' && c != 'C';
				if (visit[i][j] == false && esObstaculo) {
					int contF = 1, contC = 1; // contador filas y contador
												// columnas
					while (i + contF < MAXALTURA && nuevoMapa.getCelda(i + contF, j).getSprite() == c)
						contF++;
					while (j + contC < MAXANCHO && nuevoMapa.getCelda(i, contC + j).getSprite() == c)
						contC++;
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
		nuevoMapa.setObstaculos(obstaculos);
	}

	private void asignarImagenesAlMapa(List<Objeto> obstaculos, Mapa nuevoMapa, String nombre) {
		Celda celdaAux;

		for (int i = 0; i < obstaculos.size(); i++) {
			Objeto objetoAux = obstaculos.get(i);
			int altura = objetoAux.getAltura();
			int ancho = objetoAux.getAncho();
			int posX = objetoAux.getPosX();
			int posY = objetoAux.getPosY();
			char sprite = objetoAux.getSprite();
			boolean esImpasable = sprite != 'a' && sprite != 'p' && sprite != 'o' && sprite != 'L'
					&& sprite != 'g' & sprite != 't' && sprite != 'd';

			// Primero los Impasables
			if (esImpasable)
				try {
					objetoAux
							.setImg(ImageIO.read(getClass().getResource("/imagenes" + nombre + "/" + sprite + ".gif")));
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			for (int j = 0; j < altura; j++) {
				for (int k = 0; k < ancho; k++) {
					celdaAux = nuevoMapa.getCelda(posX + j, posY + k);
					celdaAux.setTipo(TipoCelda.IMPASABLE);
					celdaAux.setSprite(sprite);
					// De ahi los pasables
					if (!esImpasable)
						try {
							celdaAux.setImg(
									ImageIO.read(getClass().getResource("/imagenes" + nombre + "/" + sprite + ".gif")));
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}
		}
	}

	public void cargarMapa(int index, String nuevoNombre) {

		if (index > cantidadMapas)
			return; // Existe el mapa

		// Se inicializan las Variables del Mapa
		mapaActual = mapas.get(index).copy();
		indMapaActual = index;
		nombreJugador = nuevoNombre;

		// Se Inicializan los dos Personajes
		// Jugador 1
		jugador1 = new PersonajePrincipal(mapaActual.posX1, mapaActual.posY1);
		Celda celdaAux = mapaActual.getCelda(jugador1.getPosX(), jugador1.getPosY());
		celdaOriginal0 = celdaAux.copy();
		celdaAux.setJugador(JUGADOR1);
		celdaAux.setSprite('A');

		// Jugador 2
		jugador2 = new PersonajePrincipal(mapaActual.posX2, mapaActual.posY2);
		celdaAux = mapaActual.getCelda(jugador2.getPosX(), jugador2.getPosY());
		celdaOriginal1 = celdaAux.copy();
		celdaAux.setJugador(JUGADOR2);
		celdaAux.setSprite('B');
	}

	public void realizarMovimiento(int mov) {

		Celda celdaAntes, celdaDespues = null;
		int posXAux, posYAux, posXNuevo, posYNuevo;

		boolean noEsComandoConocido = mov == -1;
		if (noEsComandoConocido)
			return;

		boolean esMovJug1 = mov < ARRIBAJ2;

		if (esMovJug1) {
			celdaAntes = celdaOriginal0;
			posXAux = jugador1.getPosX();
			posYAux = jugador1.getPosY();
		} else {
			celdaAntes = celdaOriginal1;
			posXAux = jugador2.getPosX();
			posYAux = jugador2.getPosY();
		}

		// NVerificar si existe la celda
		posXNuevo = posXAux;
		posYNuevo = posYAux;

		if (mov == ARRIBAJ1 || mov == ARRIBAJ2)
			posXNuevo--;
		else if (mov == ABAJOJ1 || mov == ABAJOJ2)
			posXNuevo++;
		if (mov == DERECHAJ1 || mov == DERECHAJ2)
			posYNuevo++;
		else if (mov == IZQUIERDAJ1 || mov == IZQUIERDAJ2)
			posYNuevo--;
		celdaDespues = mapaActual.getCelda(posXNuevo, posYNuevo);
		if (celdaDespues != null)
			celdaDespues = celdaDespues.copy();

		// Verificar si el movimiento es Valido
		boolean esMovimientoValido = true;

		if (mov == 0)
			esMovimientoValido = jugador1.verificarPosX(false, 0, mapaActual.getAltura(), mapaActual,
					TipoCelda.TERRENO_A);
		else if (mov == 1)
			esMovimientoValido = jugador1.verificarPosY(false, 0, mapaActual.getAncho(), mapaActual,
					TipoCelda.TERRENO_A);
		else if (mov == 2)
			esMovimientoValido = jugador1.verificarPosX(true, 0, mapaActual.getAltura(), mapaActual,
					TipoCelda.TERRENO_A);
		else if (mov == 3)
			esMovimientoValido = jugador1.verificarPosY(true, 0, mapaActual.getAncho(), mapaActual,
					TipoCelda.TERRENO_A);
		else if (mov == 4)
			esMovimientoValido = jugador2.verificarPosX(false, 0, mapaActual.getAltura(), mapaActual,
					TipoCelda.TERRENO_B);
		else if (mov == 5)
			esMovimientoValido = jugador2.verificarPosY(false, 0, mapaActual.getAncho(), mapaActual,
					TipoCelda.TERRENO_B);
		else if (mov == 6)
			esMovimientoValido = jugador2.verificarPosX(true, 0, mapaActual.getAltura(), mapaActual,
					TipoCelda.TERRENO_B);
		else if (mov == 7)
			esMovimientoValido = jugador2.verificarPosY(true, 0, mapaActual.getAncho(), mapaActual,
					TipoCelda.TERRENO_B);

		if (!esMovimientoValido)
			return;

		mapaActual.retornarCelda(posXAux, posYAux, celdaAntes);

		if (esMovJug1) {
			celdaOriginal0 = celdaDespues.copy();
			fin1 = false;
			if (tipoDual == 0)
				dual = -1;
			mapaActual.modificarPosJugador(0, jugador1.getPosX(), jugador1.getPosY());
		} else {
			celdaOriginal1 = celdaDespues.copy();
			fin2 = false;
			if (tipoDual == 1)
				dual = -1;
			mapaActual.modificarPosJugador(1, jugador2.getPosX(), jugador2.getPosY());
		}
	}

	public String realizarMovimientoEspecial(int mov) {
		Celda celdaActual;
		boolean noEsComandoConocido = mov == -1;
		if (noEsComandoConocido)
			return "";

		// Verificar si es movimiento de jugador 1 o de jugador 2
		boolean esMovJ1 = mov < ARRIBAJ2;
		if (esMovJ1)
			celdaActual = mapaActual.getCelda(jugador1.getPosX(), jugador1.getPosY());
		else
			celdaActual = mapaActual.getCelda(jugador2.getPosX(), jugador2.getPosY());

		// Verificar el tipo de accion y si se encuentra activado
		int tipoCeldaEsp = celdaActual.getTipoCeldaEsp();
		int indiceListCeldaEsp = celdaActual.getIndiceListCeldaEsp();
		if (tipoCeldaEsp == SINACCION)
			return "";
		else if (tipoCeldaEsp == SOLOACTIVADO) { // comando solo

			indMovimientoEspecial = 0;
			CeldaEspecial celdaEsp = mapaActual.getCeldaEspEnLista(indiceListCeldaEsp);
			comandoActual = celdaEsp.getComandoEspecial();
			List<Integer> liberaX = celdaEsp.getLiberaX();
			List<Integer> liberaY = celdaEsp.getLiberaY();
			List<Integer> valorLiberacion = celdaEsp.getvalorLiberacion();

			for (int i = 0; i < liberaX.size(); i++) {
				Celda c2 = mapaActual.getCelda(liberaX.get(i), liberaY.get(i));
				c2.setTipoCeldaEsp(valorLiberacion.get(i));
			}

			if (esMovJ1)
				realizarMovimientoEspecial(4); // se verifica si el otro
												// personaje ya esta en un dual
			else
				realizarMovimientoEspecial(0); // que se activo ahora

			celdaActual.setTipoCeldaEsp(0);
			if (esMovJ1)
				celdaOriginal0.setTipoCeldaEsp(0);
			else
				celdaOriginal1.setTipoCeldaEsp(0);

			return comandoActual;
		} else if (tipoCeldaEsp == DUOACTIVADO) { // comando dual

			CeldaEspecial celdaEsp = mapaActual.getCeldaEspEnLista(indiceListCeldaEsp);
			int dualOpuesto = celdaEsp.getDualOpuesto();
			if (dual == dualOpuesto) {
				comandoActual = celdaEsp.getComandoEspecial();
				celdaOriginal0.setTipoCeldaEsp(0);
				celdaOriginal1.setTipoCeldaEsp(0);
				celdaActual.setTipoCeldaEsp(0);
				celdaDual.setTipoCeldaEsp(0);
			} else {
				indMovimientoEspecial = 0;
				dual = indiceListCeldaEsp;
				celdaDual = celdaActual;
				if (esMovJ1)
					tipoDual = 0;
				else
					tipoDual = 1;
				return "";
			}
			return comandoActual;

		} else if (tipoCeldaEsp == FINNIVEL) { // llego a fin de mapa
			if (esMovJ1) {
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
		Celda celdaActual;
		boolean esMovJug1 = mov < ARRIBAJ2;

		if (esMovJug1)
			celdaActual = mapas.get(indMapaActual).getCelda(jugador1.getPosX(), jugador1.getPosY());
		else
			celdaActual = mapas.get(indMapaActual).getCelda(jugador2.getPosX(), jugador2.getPosY());

		CeldaEspecial celdaEsp;
		if (indMovimientoEspecial == 0) {
			int ind = celdaActual.getIndiceListCeldaEsp();
			celdaEsp = mapaActual.getCeldaEspEnLista(ind);
			espAux = celdaEsp;
		} else {
			celdaEsp = espAux;
		}
		celdaEsp.ejecutarEspecial(mov, mapaActual, jugador1, jugador2, celdaOriginal0, celdaOriginal1,
				indMovimientoEspecial);

		indMovimientoEspecial += 1;

		if (celdaEsp.getDireccionX().size() == indMovimientoEspecial) {
			if (realizarMovimientoEspecial(0).equals("F"))
				return "F";
			if (realizarMovimientoEspecial(4).equals("F"))
				return "F";
			return "Done";
		}
		return "";
	}
	
	public int getTiempo(int mov){
		Celda celdaActual;
		//boolean noEsComandoConocido = mov == -1;

		// Verificar si es movimiento de jugador 1 o de jugador 2
		boolean esMovJ1 = mov < ARRIBAJ2;
		if (esMovJ1)
			celdaActual = mapaActual.getCelda(jugador1.getPosX(), jugador1.getPosY());
		else
			celdaActual = mapaActual.getCelda(jugador2.getPosX(), jugador2.getPosY());
		
		int indiceListCeldaEsp = celdaActual.getIndiceListCeldaEsp();
		CeldaEspecial celdaEsp = mapaActual.getCeldaEspEnLista(indiceListCeldaEsp);
		return celdaEsp.getTiempo();
		
	}

	public void reiniciarVida() {
		vida = VIDAINICIAL;
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
		Celda celdaAux;
		mapas = new ArrayList<Mapa>();
		try {
			anadirMapas();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		String nombre;
		if (indMapaActual == TUTORIAL)
			nombre = "/MapaTutorial";
		else
			nombre = "/MapaNivel" + indMapaActual;

		celdaOriginal0.setImg(mapas.get(indMapaActual).getCelda(jugador1.getPosX(), jugador1.getPosY()).getImg());
		celdaOriginal1.setImg(mapas.get(indMapaActual).getCelda(jugador2.getPosX(), jugador2.getPosY()).getImg());

		for (int i = 0; i < MAXALTURA; i++) {
			int j;
			for (j = 0; j < MAXANCHO; j++) {
				Celda c = mapas.get(indMapaActual).getCelda(i, j);
				if (c.getSprite() == 'S' || c.getSprite() == 'A')
					break;
			}
			if (j != MAXANCHO) {
				for (j = 0; j < MAXANCHO; j++) {
					Celda c = mapaActual.getCelda(i, j);
					try {
						c.setImg(ImageIO.read(getClass().getResource("/imagenes" + nombre + "/piso1.gif")));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			for (j = 0; j < MAXANCHO; j++) {
				Celda c = mapas.get(indMapaActual).getCelda(i, j);
				if (c.getSprite() == 'N' || c.getSprite() == 'B')
					break;
			}
			if (j != MAXANCHO) {
				for (j = 0; j < MAXANCHO; j++) {
					Celda c = mapaActual.getCelda(i, j);
					try {
						c.setImg(ImageIO.read(getClass().getResource("/imagenes" + nombre + "/piso2.gif")));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		for (int i = 0; i < mapaActual.cantidadObstaculos(); i++) {
			Objeto objetoAux = mapaActual.getObstaculo(i);
			int altura = objetoAux.getAltura();
			int ancho = objetoAux.getAncho();
			int posX = objetoAux.getPosX();
			int posY = objetoAux.getPosY();
			char sprite = objetoAux.getSprite();
			if (sprite != 'a' && sprite != 'p' && sprite != 'o' && sprite != 'L' && sprite != 'g' & sprite != 't'
					&& sprite != 'd')
				try {
					objetoAux
							.setImg(ImageIO.read(getClass().getResource("/imagenes" + nombre + "/" + sprite + ".gif")));
				} catch (IOException e1) {

					e1.printStackTrace();
				}
			for (int j = 0; j < altura; j++) {
				for (int k = 0; k < ancho; k++) {
					celdaAux = mapaActual.getCelda(posX + j, posY + k);
					celdaAux.setTipo(TipoCelda.IMPASABLE);
					celdaAux.setSprite(sprite);
					if (sprite == 'a' || sprite == 'p' || sprite == 'o' || sprite == 'L' || sprite == 'g'
							|| sprite == 't' || sprite == 'd')
						try {
							celdaAux.setImg(
									ImageIO.read(getClass().getResource("/imagenes" + nombre + "/" + sprite + ".gif")));
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}
		}
		if (indMapaActual == 0) {
			for (int i = 0; i < MAXANCHO; i++) {
				try {
					mapaActual.getCelda(0, i)
							.setImg(ImageIO.read(getClass().getResource("/imagenes/MapaTutorial/pared.gif")));
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					mapaActual.getCelda(1, i)
							.setImg(ImageIO.read(getClass().getResource("/imagenes/MapaTutorial/piso madera.gif")));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (indMapaActual == 1) {
			for (int i = 0; i < MAXANCHO; i++) {
				try {
					mapaActual.getCelda(0, i)
							.setImg(ImageIO.read(getClass().getResource("/imagenes/MapaNivel1/1.gif")));
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					mapaActual.getCelda(1, i)
							.setImg(ImageIO.read(getClass().getResource("/imagenes/MapaNivel1/1.gif")));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (indMapaActual == 2) {
			for (int i = 0; i < 8; i++) {
				try {
					mapaActual.getCelda(0, i * 2)
							.setImg(ImageIO.read(getClass().getResource("/imagenes/MapaNivel2/fondo.png")));
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					mapaActual.getCelda(1, i * 2)
							.setImg(ImageIO.read(getClass().getResource("/imagenes/MapaNivel2/fondo.png")));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			for (int j = 12; j < 16; j++) {
				Celda aux2 = mapaActual.getCelda(6, j);
				aux2.setTipo(TipoCelda.TERRENO_A);
				aux2.setTipoCeldaEsp(0);
				aux2.setSprite('o');
				try {
					aux2.setImg(ImageIO
							.read(getClass().getResource("/imagenes" + nombre + "/" + aux2.getSprite() + ".gif")));
				} catch (IOException e) {
					e.printStackTrace();
				}

				aux2 = mapaActual.getCelda(7, j);
				aux2.setTipo(TipoCelda.TERRENO_B);
				aux2.setTipoCeldaEsp(0);
				aux2.setSprite('o');
				try {
					aux2.setImg(ImageIO
							.read(getClass().getResource("/imagenes" + nombre + "/" + aux2.getSprite() + ".gif")));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			Celda aux2 = mapaActual.getCelda(6, 15);
			aux2.setTipoCeldaEsp(3);
			aux2 = mapaActual.getCelda(7, 15);
			aux2.setTipoCeldaEsp(3);
		}
		for (int i = 0; i < MAXALTURA; i++) {
			for (int j = 0; j < MAXANCHO; j++) {
				celdaAux = mapas.get(indMapaActual).getCelda(i, j);
				if (celdaAux.getTipoCeldaEsp() == 1 || celdaAux.getTipoCeldaEsp() == -1) {
					try {
						mapaActual.getCelda(i, j)
								.setImg(ImageIO.read(getClass().getResource("/imagenes/sprite_azul.png")));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (celdaAux.getTipoCeldaEsp() == 2 || celdaAux.getTipoCeldaEsp() == -2) {
					try {
						mapaActual.getCelda(i, j)
								.setImg(ImageIO.read(getClass().getResource("/imagenes/sprite_rojo.png")));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		mapaActual.getCelda(jugador1.getPosX(), jugador1.getPosY()).setSprite('A');
		mapaActual.getCelda(jugador2.getPosX(), jugador2.getPosY()).setSprite('B');
	}

	public String getNombre() {
		return nombreJugador;
	}
}