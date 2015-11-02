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

class HiloVida extends Thread {
	Object bandera;
	Ventana v;
	int tiempo;
	boolean flag = true;
	
	public HiloVida(Object bandera2, Ventana ventana,int tiempo) {
		bandera = bandera2;
		this.v = ventana;
		this.tiempo = tiempo;
	}
	public void stop2(){
		flag = false;
		interrupt();
	}
	public void run() {
		while(flag){
			
			try {
				sleep(1000*tiempo);
				if(!flag) break;
				synchronized(bandera){					
					if (v.perdioVida)
						v.pnlTexto.removeTexto();
				
					v.pnlTexto.quitarVida(2);
					v.perdioVida = true;
					if (v.gestor.perderVida(2)) {
						v.pnlTexto.addTexto("Kiru y Milo perdieron 2 puntos de vida.");
						v.dibujarExtra();
					} else {
						v.pnlTexto.addTexto(v.cadenaGameOver);
						v.pnlTexto.addTexto("Vuelvalo a intentar");
						v.pnlTexto.addTexto("Presione enter para continuar");
						v.dibujarExtra();
						v.eventFlag = -4;
						break;
					}
				}
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}

class HiloAcciones extends Thread{
	Ventana v;
	Object bandera2;
	public HiloAcciones(Ventana ventana,Object b){
		v = ventana;
		bandera2 = b;
	}
	public void run(){
		v.pnlTexto.removeTexto();
		v.pnlTexto.removeTexto();
		while(true){
			String aux = v.gestor.ejecutarComando(v.valor);
			v.dibujar(v.gestor.getMapaActual());
	
			if (aux.equals("F") && v.mapaActual == 0) {
				v.pnlTexto.addTexto(v.dialogo[1]);
				v.dibujarExtra();
				v.eventFlag = Ventana.MOSTRARDIALOGOFINNIVEL;
			}
			if (aux.equals("Done")) {
				v.dibujarExtra();
				v.eventFlag = Ventana.REALIZARMOVIMIENTO;
				break;
			}
			if (aux.equals("F") && v.eventFlag != Ventana.MOSTRARDIALOGOFINNIVEL) {
				v.mapaActual++;
				if (v.mapaActual == v.cantMapas) {
					v.eventFlag = Ventana.JUEGOTERMINADO;
					v.pnlTexto.addTexto(v.cadenaJuegoCompletado);
					v.dibujarExtra();
				} else {
					v.nuevoNivel(v.mapaActual);
				}
				break;
			}
			try {
				sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

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
	
	public static boolean cargo=true;
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
	HiloVida h;
	final String[] historias = { 
		"Kiru y Milo conversan. Le nace la pregunta a Kiru y deciden viajar.",
		"Kiru y Milo viajan a Paracas en un auto. Llegan a la playa y empiezan a jugar.",
		"Kiru y Milo se encuentran con Peli el Pelicano. Peli el pelicano no sabe de donde viene Kiru. Kiru y Milo deciden viajar a la sierra.",
		"Kiru y Milo conversan con Dana la Llama. Dana responde la pregunta de Kiru. Kiru se contenta y decide, con Milo, viajar por todos los Andes."
	};
	final String cadenaGameOver = "Has perdido.";
	final String cadenaJuegoCompletado = "Felicitaciones, has terminado el juego.";
	final String[] dialogo = { 
		"o	Usa WASD para mover a Kiru y JKLI para mover a Milo. " +
		"o	Si ves un lugar para la accion o el duo, parate sobre el! Podras realizar acciones especiales. " +
		"o	Solo podra pasar los niveles con la ayuda de las acciones especiales. Para esto, tendras que presionar comandos que se mostraran en un cuadro de dialogo como este. " +
		"o	Los comandos deben ser ejecutados en la secuencia correcta sino perderas puntos de vida. " +
		"o	Puedes ver los puntos de vida en la parte superior de la pantalla. " +
		"o	Para activar los terrenos con acciones especiales duo, tienen que estar sobre ellos Kiru y Milo al mismo tiempo, en los de acciones especiales solo con uno basta. ",

		"o	En tu aventura, a veces te toparas con animales malos. " +
		"o	Estos enemigos te bajaran 2 puntos de vida, si tus puntos de vida llegan a 0, se acabara el juego. " +
		"o	Si un enemigo afecta a un personaje, este no se podra mover, tendra que usar a su amigo para ayudarlo. " 
	};
	public Ventana() {

		eventFlag = MENUINICIAL;

		// Para el canvas
		setBounds(100, 100, 1324, 806);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Para los Paneles
		pnlGrafico = new PanelGraficos();
		pnlGrafico.setBounds(0, 0, 1024, 806);
		pnlGrafico.setFocusable(false);
		contentPane.add(pnlGrafico);
		pnlTexto = new PanelTexto();
		pnlTexto.setBounds(1024, 0, 200, 806);
		pnlTexto.setFocusable(false);
		contentPane.add(pnlTexto);

		// La ventana que sale cuando se intenta cerrar
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Object[] options = { "Si", "No" };

				int n = JOptionPane.showOptionDialog(Ventana.this, "¿Seguro que desea salir?",
						"Mensaje de confirmacion", JOptionPane.YES_OPTION, JOptionPane.NO_OPTION, null, options,
						options[0]);
				if (n == JOptionPane.YES_OPTION)
					System.exit(0);
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
	public void keyPressed(KeyEvent e) {

		// Permite salir en Todo Momento
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Object[] options = { "Si", "No" };
			int n = JOptionPane.showOptionDialog(this, "¿Seguro que desea salir?", "Mensaje de confirmacion",
					JOptionPane.YES_OPTION, JOptionPane.NO_OPTION, null, options, options[0]);
			if (n == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}

		// Manejo del Ingreso de nombre
		if (eventFlag == INGRESARNOMBRE) {
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

		// Manejo del Menu Inicial
		else if (eventFlag == MENUINICIAL) { // ingresar datos y aceptar juego i.e. bucle lectura

			// Comenzar Partida
			if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A') {
				eventFlag = INGRESARNOMBRE;
				pnlTexto.textos.clear();
				pnlTexto.addTexto("Ingrese su nombre");
				pnlTexto.addTexto("");
				dibujarExtra();
				gestor.reiniciarVida();
				nombre = "";
			}

			// Salir del Juego
			else if (e.getKeyChar() == 'b' || e.getKeyChar() == 'B') {
				pnlTexto.textos.clear();
				pnlTexto.addTexto("Saliendo del juego");
				pnlTexto.addTexto("Presione enter para terminar");
				dibujarExtra();
				eventFlag = JUEGOTERMINADO;
			}

			// Cargar Juego
			else if (e.getKeyChar() == 'c' || e.getKeyChar() == 'C') {
				pnlTexto.textos.clear();
				pnlTexto.addTexto("Cargando juego, espere un momento");
				dibujarExtra();
				VentanaGuardado dialogo= new VentanaGuardado(Ventana.this,true,gestor,'c');				
				// System.exit(0);
				dialogo.setVisible(true);								
				if(Ventana.cargo) {
					nombre = gestor.Get_nombre();
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
				}else{
					pnlTexto.textos.clear();
					pnlTexto.addTexto("Kiru es un cuy mascota");
					pnlTexto.addTexto("a. Iniciar juego");
					pnlTexto.addTexto("b. Salir del juego");
					pnlTexto.addTexto("c. Cargar partida");
					dibujarExtra();
					Ventana.cargo=true;
				}
				
				
			}
		}

		// Manejo del Game Over
		else if (eventFlag == JUEGOPERDIDO && e.getKeyCode() == KeyEvent.VK_ENTER) { // game over
			eventFlag = MENUINICIAL;
			mapaActual = 0;
			pnlTexto.textos.clear();
			pnlTexto.addTexto("Kiru es un cuy mascota");
			pnlTexto.addTexto("a. Iniciar juego");
			pnlTexto.addTexto("b. Salir del juego");
			pnlTexto.addTexto("c. Cargar partida");
			dibujarExtra();
		}

		// Manejo del Fin del Juego
		else if (eventFlag == JUEGOTERMINADO && e.getKeyCode() == KeyEvent.VK_ENTER) { // juego terminado
			System.exit(0);
		}

		// Manejo del Dialogo del fin de nivel
		else if (eventFlag == MOSTRARDIALOGOFINNIVEL && e.getKeyCode() == KeyEvent.VK_ENTER) { // dialogo despues de fin de nivel
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

		// Manejo del Dialogo del inicio de nivel
		else if (eventFlag == MOSTRARDIALOGOININIVEL && e.getKeyCode() == KeyEvent.VK_ENTER) { // dialogo en inicio de nivel
			pnlTexto.removeTexto();
			pnlTexto.removeTexto();
			pnlTexto.addTexto(dialogo[0]);
			pnlTexto.addTexto("Presione enter para continuar");
			dibujarExtra();
			eventFlag = MOSTRARHISTORIA;
		}

		// Maneo de la Historia
		else if (eventFlag == MOSTRARHISTORIA && e.getKeyCode() == KeyEvent.VK_ENTER) { // historias por nivel
			pnlTexto.removeTexto();
			pnlTexto.removeTexto();
			dibujarExtra();
			eventFlag++;
		}

		// Manejo de los movimientos
		else if (eventFlag == REALIZARMOVIMIENTO) { // movimiento
			String str = "";
			str += e.getKeyChar();

			// Guardar Juego
			if (str.charAt(0) == 'G' || str.charAt(0) == 'g') {
				VentanaGuardado dialogo= new VentanaGuardado(Ventana.this,true,gestor,'G');				
					// System.exit(0);
				dialogo.setVisible(true);
				nombre = gestor.Get_nombre();
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

			valor = interp.interpretarMovimiento(str);
			gestor.realizarMovimiento(valor);
			dibujar(gestor.getMapaActual());
			String aux = gestor.realizarMovimientoEspecial(valor);

			if (aux.equals("F")) {
				mapaActual++;
				if (mapaActual == cantMapas) {
					eventFlag = JUEGOTERMINADO;
					pnlTexto.addTexto(cadenaJuegoCompletado);
					dibujarExtra();
				} else {
					nuevoNivel(mapaActual);
				}
			} else if (!aux.equals("")) {
				comando = aux;
				indiceComando = 0;
				eventFlag = REALIZARCOMANDO;
				perdioVida = false;
				pnlTexto.addTexto(comando);
				pnlTexto.addTexto("");
				dibujarExtra();
				h = new HiloVida(bandera,this,gestor.getTiempo(valor));
				h.start();
			}

		}

		// Manejo de los comandos especiales
		else if (eventFlag == REALIZARCOMANDO) { // comando
			String str = "";
			str += e.getKeyChar();
			str = str.toUpperCase();
			synchronized(bandera){
				if (perdioVida)
					pnlTexto.removeTexto();
				if (interp.interpretarEspecial(str, comando.charAt(indiceComando))) {
					perdioVida = false;
					pnlTexto.anadirUltimo(str.charAt(0));
					indiceComando++;
					if (indiceComando == comando.length()) {
						h.stop2();
						eventFlag = COMANDOCOMPLETADO;
						HiloAcciones hilo = new HiloAcciones(this,bandera2);
						hilo.start();
						//pnlTexto.addTexto("Presione enter para continuar");
					}
					dibujarExtra();
				} else {
					pnlTexto.quitarVida(2);
					perdioVida = true;
					if (gestor.perderVida(2)) {
						pnlTexto.addTexto("Kiru y Milo perdieron 2 puntos de vida.");
						dibujarExtra();
					} else {
						h.stop2();
						pnlTexto.addTexto(cadenaGameOver);
						pnlTexto.addTexto("Vuelvalo a intentar");
						pnlTexto.addTexto("Presione enter para continuar");
						dibujarExtra();
						eventFlag = JUEGOPERDIDO;
						
					}
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}

