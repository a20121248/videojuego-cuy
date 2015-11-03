package vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import modelo.*;
import controlador.*;

import javax.swing.border.EmptyBorder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import javax.swing.*;

public class Ventana extends JFrame implements KeyListener {

	Object bandera = new Object();
	Object bandera2 = new Object();

	private static final long serialVersionUID = -8472914249245291228L;

	public static final int INGRESARNOMBRE = -6;
	public static final int MENUINICIAL = -5;
	public static final int JUEGOPERDIDO = -4;
	public static final int JUEGOTERMINADO = -3;
	public static final int MOSTRARDIALOGOFINNIVEL = -2;
	public static final int MOSTRARDIALOGOININIVEL = -1;
	public static final int MOSTRARHISTORIA = 0;
	public static final int REALIZARMOVIMIENTO = 1;
	public static final int REALIZARCOMANDO = 2;
	public static final int COMANDOCOMPLETADO = 3;

	public static boolean cargoCorrectamente = true;
	private JPanel contentPane;
	private PanelGraficos pnlGrafico;
	public static PanelTexto pnlTexto;
	public static int eventFlag = 0;
	Mapa m = null;
	BufferedImage b;
	InterpreteComandos interp;
	public static GestorMapas gestor;
	String comando;
	public static String nombre = "";
	public static int mapaActual = 0;
	int cantMapas = 3;
	int indiceComando;
	int indiceDialogo = 0;
	int valor;
	boolean perdioVida = false;
	HiloVida hiloVida;
	final String[] historias = { "Kiru y Milo conversan. Le nace la pregunta a Kiru y deciden viajar.",
			"Kiru y Milo viajan a Paracas en un auto. Llegan a la playa y empiezan a jugar.",
			"Kiru y Milo se encuentran con Peli el Pelicano. Peli el pelicano no sabe de donde viene Kiru. Kiru y Milo deciden viajar a la sierra.",
			"Kiru y Milo conversan con Dana la Llama. Dana responde la pregunta de Kiru. Kiru se contenta y decide, con Milo, viajar por todos los Andes." };
	final String cadenaGameOver = "Has perdido.";
	final String cadenaJuegoCompletado = "Felicitaciones, has terminado el juego.";
	final String[] dialogo = { "o	Usa WASD para mover a Kiru y JKLI para mover a Milo. "
			+ "o	Si ves un lugar para la accion o el duo, parate sobre el! Podras realizar acciones especiales. "
			+ "o	Solo podra pasar los niveles con la ayuda de las acciones especiales. Para esto, tendras que presionar comandos que se mostraran en un cuadro de dialogo como este. "
			+ "o	Los comandos deben ser ejecutados en la secuencia correcta sino perderas puntos de vida. "
			+ "o	Puedes ver los puntos de vida en la parte superior de la pantalla. "
			+ "o	Para activar los terrenos con acciones especiales duo, tienen que estar sobre ellos Kiru y Milo al mismo tiempo, en los de acciones especiales solo con uno basta. ",

			"o	En tu aventura, a veces te toparas con animales malos. "
					+ "o	Estos enemigos te bajaran 2 puntos de vida, si tus puntos de vida llegan a 0, se acabara el juego. "
					+ "o	Si un enemigo afecta a un personaje, este no se podra mover, tendra que usar a su amigo para ayudarlo. " };

	public Ventana() {

		eventFlag = MENUINICIAL;

		inicializarCanvas();
		inicializarPaneles();

		// Cuando se trate de cerrar el juego se va a preguntar si en verdad se
		// quiere salir
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ventanaConfirmacionSalir();
			}
		});

		// Se inicializa sus valores
		addKeyListener(this);
		interp = new InterpreteComandos();
		gestor = new GestorMapas();

		// Se da las opciones por default
		pnlTexto.addTexto("Kiru es un cuy mascota");
		pnlTexto.addTexto("a. Iniciar juego");
		pnlTexto.addTexto("b. Salir del juego");
		pnlTexto.addTexto("c. Cargar partida");
	}

	private void inicializarCanvas() {
		setBounds(100, 100, 1324, 806);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}

	private void inicializarPaneles() {
		pnlGrafico = new PanelGraficos();
		pnlGrafico.setBounds(0, 0, 1024, 806);
		pnlGrafico.setFocusable(false);
		contentPane.add(pnlGrafico);
		pnlTexto = new PanelTexto();
		pnlTexto.setBounds(1024, 0, 200, 806);
		pnlTexto.setFocusable(false);
		contentPane.add(pnlTexto);
	}

	@Override
	public void keyPressed(KeyEvent e) {

		// Permite salir en Todo Momento
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.ventanaConfirmacionSalir();
		}

		// Manejo de los diferentes tipos de eventos
		if (eventFlag == INGRESARNOMBRE) {
			eventoIngresarNombre(e);
		} else if (eventFlag == MENUINICIAL) {
			eventoMenuInicial(e);
		} else if (eventFlag == JUEGOPERDIDO && e.getKeyCode() == KeyEvent.VK_ENTER) {
			eventoJuegoPerdido();
		} else if (eventFlag == JUEGOTERMINADO && e.getKeyCode() == KeyEvent.VK_ENTER) {
			System.exit(0);
		} else if (eventFlag == MOSTRARDIALOGOFINNIVEL && e.getKeyCode() == KeyEvent.VK_ENTER) {
			eventoDialogoFinNivel();
		} else if (eventFlag == MOSTRARDIALOGOININIVEL && e.getKeyCode() == KeyEvent.VK_ENTER) {
			eventoDialogoInicioNivel();
		} else if (eventFlag == MOSTRARHISTORIA && e.getKeyCode() == KeyEvent.VK_ENTER) {
			eventoMostrarHistoria();
		} else if (eventFlag == REALIZARMOVIMIENTO) {
			eventoRealizarMovimiento(e);
		} else if (eventFlag == REALIZARCOMANDO) {
			eventoRealizarComandoEspecial(e);
		}
	}

	private void ventanaConfirmacionSalir() {
		Object[] options = { "Si", "No" };

		int n = JOptionPane.showOptionDialog(Ventana.this, "¿Seguro que desea salir?", "Mensaje de confirmacion",
				JOptionPane.YES_OPTION, JOptionPane.NO_OPTION, null, options, options[0]);
		if (n == JOptionPane.YES_OPTION)
			System.exit(0);
	}

	private void eventoIngresarNombre(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			nuevoNivel(mapaActual);
		} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			pnlTexto.eliminarUltimo();
			if (!nombre.isEmpty()) {
				nombre = nombre.substring(0, nombre.length() - 1);
			}
			dibujarExtra();
		} else {
			char c = e.getKeyChar();
			if (Character.isAlphabetic(c)) {
				pnlTexto.anadirUltimo(c);
				nombre += c;
				dibujarExtra();
			}
		}
	}

	private void eventoMenuInicial(KeyEvent e) {
		if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A') {
			opcionComenzarPartida();
		} else if (e.getKeyChar() == 'b' || e.getKeyChar() == 'B') {
			opcionSalirDelJuego();
		} else if (e.getKeyChar() == 'c' || e.getKeyChar() == 'C') {
			opcionCargarJuego();
		}
	}

	private void opcionComenzarPartida() {
		eventFlag = INGRESARNOMBRE;
		pnlTexto.textos.clear();
		pnlTexto.addTexto("Ingrese su nombre");
		pnlTexto.addTexto("");
		dibujarExtra();
		gestor.reiniciarVida();
		nombre = "";
	}

	private void opcionSalirDelJuego() {
		pnlTexto.textos.clear();
		pnlTexto.addTexto("Saliendo del juego");
		pnlTexto.addTexto("Presione enter para terminar");
		dibujarExtra();
		eventFlag = JUEGOTERMINADO;
	}

	private void opcionCargarJuego() {
		pnlTexto.textos.clear();
		pnlTexto.addTexto("Cargando juego, espere un momento");
		dibujarExtra();
		VentanaGuardado dialogo = new VentanaGuardado(Ventana.this, true, gestor, 'c');
		dialogo.setVisible(true);
		if (Ventana.cargoCorrectamente) {
			nombre = gestor.getNombre();
			gestor.recargarImagenes();
			dibujar(gestor.getMapaActual());
			mapaActual = gestor.getIndMapaActual();
			pnlTexto.inicializarTexto(nombre);
			if (mapaActual > 0)
				pnlTexto.cambiarNivel(mapaActual);
			int aux = gestor.getVida();
			while (aux != 10) {
				pnlTexto.quitarVida(2);
				aux += 2;
			}
			dibujarExtra();
			eventFlag = REALIZARMOVIMIENTO;
		} else {
			pnlTexto.textos.clear();
			pnlTexto.addTexto("Kiru es un cuy mascota");
			pnlTexto.addTexto("a. Iniciar juego");
			pnlTexto.addTexto("b. Salir del juego");
			pnlTexto.addTexto("c. Cargar partida");
			dibujarExtra();
			Ventana.cargoCorrectamente = true;
		}
	}

	private void eventoJuegoPerdido() {
		eventFlag = MENUINICIAL;
		mapaActual = 0;
		pnlTexto.textos.clear();
		pnlTexto.addTexto("Kiru es un cuy mascota");
		pnlTexto.addTexto("a. Iniciar juego");
		pnlTexto.addTexto("b. Salir del juego");
		pnlTexto.addTexto("c. Cargar partida");
		dibujarExtra();
	}

	private void eventoDialogoFinNivel() {
		pnlTexto.removeTexto();
		pnlTexto.removeTexto();
		mapaActual++;
		if (mapaActual == cantMapas) {
			pnlTexto.addTexto(cadenaJuegoCompletado);
			dibujarExtra();
			eventFlag = JUEGOTERMINADO;
		} else {
			nuevoNivel(mapaActual);
		}
	}

	private void eventoDialogoInicioNivel() {
		pnlTexto.removeTexto();
		pnlTexto.removeTexto();
		pnlTexto.addTexto(dialogo[0]);
		pnlTexto.addTexto("Presione enter para continuar");
		dibujarExtra();
		eventFlag = MOSTRARHISTORIA;
	}

	private void eventoMostrarHistoria() {
		pnlTexto.removeTexto();
		pnlTexto.removeTexto();
		dibujarExtra();
		eventFlag++;
	}

	private void eventoRealizarMovimiento(KeyEvent e) {
		String str = "";
		str += e.getKeyChar();

		if (str.charAt(0) == 'G' || str.charAt(0) == 'g') {
			guardarJuego();
		}

		valor = interp.interpretarMovimiento(str);
		gestor.realizarMovimiento(valor);
		dibujar(gestor.getMapaActual());
		String aux = gestor.realizarMovimientoEspecial(valor);

		if (aux.equals("F")) {
			opcionFinNivel();
		} else if (!aux.equals("")) {
			opcionRealizarComando(aux);
		}

	}

	private void guardarJuego() {
		VentanaGuardado dialogo = new VentanaGuardado(Ventana.this, true, gestor, 'G');
		dialogo.setVisible(true);
		nombre = gestor.getNombre();
		gestor.recargarImagenes();
		dibujar(gestor.getMapaActual());
		mapaActual = gestor.getIndMapaActual();
		pnlTexto.inicializarTexto(nombre);
		if (mapaActual > 0)
			pnlTexto.cambiarNivel(mapaActual);
		int aux = gestor.getVida();
		while (aux != 10) {
			pnlTexto.quitarVida(2);
			aux += 2;
		}
		dibujarExtra();
		eventFlag = REALIZARMOVIMIENTO;
	}

	private void opcionFinNivel() {
		mapaActual++;
		if (mapaActual == cantMapas) {
			eventFlag = JUEGOTERMINADO;
			pnlTexto.addTexto(cadenaJuegoCompletado);
			dibujarExtra();
		} else {
			nuevoNivel(mapaActual);
		}
	}

	private void opcionRealizarComando(String aux) {
		comando = aux;
		indiceComando = 0;
		eventFlag = REALIZARCOMANDO;
		perdioVida = false;
		pnlTexto.addTexto(comando);
		pnlTexto.addTexto("");
		dibujarExtra();
		hiloVida = new HiloVida(bandera, this, gestor.getTiempo(valor));
		hiloVida.start();
	}

	private void eventoRealizarComandoEspecial(KeyEvent e) {
		String str = "";
		str += e.getKeyChar();
		str = str.toUpperCase();
		synchronized (bandera) {
			if (perdioVida)
				pnlTexto.removeTexto();
			if (interp.interpretarEspecial(str, comando.charAt(indiceComando))) {
				perdioVida = false;
				pnlTexto.anadirUltimo(str.charAt(0));
				indiceComando++;
				if (indiceComando == comando.length()) {
					hiloVida.stop2();
					eventFlag = COMANDOCOMPLETADO;
					HiloAcciones hilo = new HiloAcciones(this, bandera2);
					hilo.start();
				}
				dibujarExtra();
			} else {
				pnlTexto.quitarVida(2);
				perdioVida = true;
				if (gestor.perderVida(2)) {
					pnlTexto.addTexto("Kiru y Milo perdieron 2 puntos de vida.");
					dibujarExtra();
				} else {
					hiloVida.stop2();
					pnlTexto.addTexto(cadenaGameOver);
					pnlTexto.addTexto("Vuelvalo a intentar");
					pnlTexto.addTexto("Presione enter para continuar");
					dibujarExtra();
					eventFlag = JUEGOPERDIDO;

				}
			}
		}
	}

	public void nuevoNivel(int nivel) {
		gestor.cargarMapa(nivel, nombre);
		dibujar(gestor.getMapaActual());
		if (nivel == 0) {
			pnlTexto.inicializarTexto(nombre);
			eventFlag = MOSTRARDIALOGOININIVEL;
		} else {
			pnlTexto.cambiarNivel(nivel);
			eventFlag = MOSTRARHISTORIA;
		}
		pnlTexto.addTexto(historias[nivel]);
		pnlTexto.addTexto("Presione enter para continuar");
		dibujarExtra();

	}

	public void dibujar(Mapa m) {
		pnlGrafico.m = m;
		pnlGrafico.repaint();
	}

	public void dibujarExtra() {
		pnlTexto.repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
