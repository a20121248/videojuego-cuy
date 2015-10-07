package vista;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.awt.EventQueue;
import java.awt.Graphics;

import modelo.*;
import controlador.*;

import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.awt.*;
import javax.swing.*;
public class Ventana extends JFrame implements KeyListener {

    private static final long serialVersionUID = -8472914249245291228L;
    
    private JPanel contentPane;
	private PanelGraficos panel;
	private PanelTexto panel2;
	int flag = 0;
	Mapa m=null;
	BufferedImage b;
	InterpreteComandos interp;
	GestorMapas gestor;
	String comando;
	String nombre="";
	int mapaActual = 0;
	int cantMapas = 3;
	int indiceComando;
	int indiceDialogo = 0;
	int valor;
	boolean perdioVida = false;
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
			"o	Estos enemigos te bajaran�ｿｽ�ｽｽ�ｽ｣ puntos de vida, si tus puntos de vida llegan a 0, se acabara el juego. " +
			"o	Si un enemigo afecta a un personaje, este no se podra mover, tendra que usar a su amigo para ayudarlo. " 
		};
	

	
	public void nuevoNivel(int nivel){
		gestor.cargarMapa(nivel,nombre);
		dibujar(gestor.getMapaActual());
		if(nivel==0){
			panel2.inicializarTexto(nombre);
			flag = -1;
		}else{
			panel2.cambiarNivel(nivel);
			flag = 0;
		}
		panel2.addTexto(historias[nivel]);
		panel2.addTexto("Presione enter para continuar");
		dibujarExtra();
		
	}
	public void dibujar(Mapa m){
		panel.m = m;
		panel.repaint();
	}
	public void dibujarExtra(){
		panel2.repaint();
	}

	/**
	 * Create the frame.
	 */
	public Ventana() {
		flag = -5;
		JLabel label;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1324, 768);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		/*label = new JLabel();
		label.setForeground(Color.GREEN);*/
		panel = new PanelGraficos();
		panel.setBounds(0, 0, 1024, 768);
		panel.setFocusable(false);
		contentPane.add(panel);
		panel2 = new PanelTexto();
		panel2.setBounds(1024,0,200,768);
		panel2.setFocusable(false);
		contentPane.add(panel2);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				Object[] options = {"Si","No"};
				
				int n = JOptionPane.showOptionDialog(Ventana.this,
				    "¿Seguro que desea salir?",
				    "Mensaje de confirmacion",
				    JOptionPane.YES_OPTION,
				    JOptionPane.NO_OPTION,
				    null,
				    options,
				    options[0]);
				if(n==JOptionPane.YES_OPTION) System.exit(0);
			}
		});
		addKeyListener(this);
		
		interp = new InterpreteComandos();
		gestor = new GestorMapas();
		panel2.addTexto("Kiru es un cuy mascota");
		panel2.addTexto("a. Iniciar juego");
		panel2.addTexto("b. Salir del juego");
		panel2.addTexto("c. Cargar partida");
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			Object[] options = {"Si","No"};
			
			int n = JOptionPane.showOptionDialog(this,
			    "¿Seguro que desea salir?",
			    "Mensaje de confirmacion",
			    JOptionPane.YES_OPTION,
			    JOptionPane.NO_OPTION,
			    null,
			    options,
			    options[0]);
			if(n == JOptionPane.YES_OPTION) {
			    System.exit(0);
			}
			
		}
		if(flag == -6){ //ingresar nombre
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			    nuevoNivel(mapaActual);
			}
			else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				panel2.eliminarUltimo();
				if(!nombre.isEmpty()) {
				    nombre = nombre.substring(0,nombre.length()-1);
				}
				dibujarExtra();
			}
			else{
				char c = e.getKeyChar();
				if (Character.isAlphabetic(c)) {
					panel2.anadirUltimo(c);
					nombre += c;
					dibujarExtra();
				}
			}
		}else if(flag == -5){ //ingresar datos y aceptar juego i.e. bucle lectura
			if(e.getKeyChar() == 'a' || e.getKeyChar()=='A'){
				flag = -6;
				panel2.textos.clear();
				panel2.addTexto("Ingrese su nombre");
				panel2.addTexto("");
				dibujarExtra();
				gestor.reiniciarVida();
				nombre = "";
			}else if(e.getKeyChar() == 'b' || e.getKeyChar()=='B'){
				panel2.textos.clear();
				panel2.addTexto("Saliendo del juego");
				panel2.addTexto("Presione enter para terminar");
				dibujarExtra();
				flag = -3;
			}else if(e.getKeyChar() == 'c' || e.getKeyChar()=='C'){
				panel2.textos.clear();
				panel2.addTexto("Cargando juego, espere un momento");
				dibujarExtra();
				try {XStream xs = new XStream(new DomDriver());
				
					gestor = (GestorMapas)xs.fromXML(new FileInputStream("Savestate.xml"));
					nombre=gestor.Get_nombre();
					gestor.recargarImagenes();
		           dibujar(gestor.getMapaActual());
		           mapaActual = gestor.getIndMapaActual();
		           panel2.inicializarTexto(nombre);
					if(mapaActual>0) panel2.cambiarNivel(mapaActual);
					int aux = gestor.getVida();
					while(aux!=10){
						panel2.quitarVida(2);
						aux+=2;
					}
					dibujarExtra();
					flag = 1;
		       } catch (IOException i) {
		          System.out.println(i.toString());}
		       
				
			}
			
		}else if(flag == -4 && e.getKeyCode() == KeyEvent.VK_ENTER){ // game over
			flag = -5;
			mapaActual = 0;
			panel2.textos.clear();
			panel2.addTexto("Kiru es un cuy mascota");
			panel2.addTexto("a. Iniciar juego");
			panel2.addTexto("b. Salir del juego");
			panel2.addTexto("c. Cargar partida");
			dibujarExtra();
		}else if(flag == -3 && e.getKeyCode() == KeyEvent.VK_ENTER){ // juego terminado
			System.exit(0);
		}else if(flag == -2 && e.getKeyCode() == KeyEvent.VK_ENTER){ // dialogo despues de fin de nivel
			panel2.removeTexto();
			panel2.removeTexto();
			mapaActual++;
			if(mapaActual == cantMapas){
				panel2.addTexto(cadenaJuegoCompletado);
				dibujarExtra();
				flag = -3;
			}else{
				nuevoNivel(mapaActual);
			}
		}else if(flag==-1 && e.getKeyCode() == KeyEvent.VK_ENTER){ // dialogo en inicio de nivel
			panel2.removeTexto();
			panel2.removeTexto();
			panel2.addTexto(dialogo[0]);
			panel2.addTexto("Presione enter para continuar");
			dibujarExtra();
			flag = 0;
		}else if(flag == 0 && e.getKeyCode() == KeyEvent.VK_ENTER){ // historias por nivel
			panel2.removeTexto();
			panel2.removeTexto();
			dibujarExtra();
			flag++;
		}else if(flag == 1){ // movimiento
			String str = "";
			str += e.getKeyChar();
			if(str.charAt(0) == 'G' || str.charAt(0)=='g'){
				try {XStream xs = new XStream(new DomDriver());
				xs.omitField(Celda.class, "img");
				xs.omitField(GestorMapas.class, "mapas");
				xs.omitField(Objeto.class, "img");
		           // 1. Escribir el archivoFileWriter 
		           FileWriter fw = new FileWriter("savestate.xml");
		           fw.write(xs.toXML(gestor));
		           fw.close();
		           //System.exit(0);
		       } catch (IOException i) {
		          System.out.println(i.toString());}
		       }
			
			valor = interp.interpretarMovimiento(str);
			gestor.realizarMovimiento(valor);
			dibujar(gestor.getMapaActual());
			String aux = gestor.realizarMovimientoEspecial(valor);
			
			if(aux.equals("F")){
				mapaActual++;
				if(mapaActual == cantMapas){
					flag = -3;
					panel2.addTexto(cadenaJuegoCompletado);
					dibujarExtra();
				}else{
					nuevoNivel(mapaActual);
				}
			}else if(!aux.equals("")){
				comando = aux;
				indiceComando = 0;
				flag = 2;
				perdioVida = false;
				panel2.addTexto(comando);
				panel2.addTexto("");
				dibujarExtra();
			}
			
		}else if(flag==2){ // comando
			String str = "";
			str += e.getKeyChar();
			str = str.toUpperCase();
			if(perdioVida) panel2.removeTexto();
			if(interp.interpretarEspecial(str,comando.charAt(indiceComando))){
				perdioVida = false;
				panel2.anadirUltimo(str.charAt(0));
				indiceComando++;
				if(indiceComando == comando.length()){
					flag = 3;
					panel2.addTexto("Presione enter para continuar");
				}
				dibujarExtra();
			}else{
				panel2.quitarVida(2);
				perdioVida = true;
				if(gestor.perderVida(2)){
					
					panel2.addTexto("Kiru y Milo perdieron 2 puntos de vida.");
					dibujarExtra();
				}else{
					panel2.addTexto(cadenaGameOver);
					panel2.addTexto("Vuelvalo a intentar");
					panel2.addTexto("Presione enter para continuar");
					dibujarExtra();
					flag = -4;
				}
			}
		}else if(flag == 3 && e.getKeyCode() == KeyEvent.VK_ENTER){ // realizar comando especial
			String aux = gestor.ejecutarComando(valor);
			dibujar(gestor.getMapaActual());
			
			
			if (aux.equals("F") && mapaActual == 0) {
				panel2.removeTexto();
				panel2.removeTexto();
				panel2.removeTexto();
				panel2.addTexto(dialogo[1]);
				panel2.addTexto("Presione enter para continuar");
				dibujarExtra();
				flag = -2;
			}
			if (aux.equals("Done")){
				panel2.removeTexto();
				panel2.removeTexto();
				panel2.removeTexto();
				dibujarExtra();
				flag = 1;
			}
			if(aux.equals("F") && flag != -2){
				panel2.removeTexto();
				panel2.removeTexto();
				panel2.removeTexto();
				mapaActual++;
				if(mapaActual == cantMapas){
					flag = -3;
					panel2.addTexto(cadenaJuegoCompletado);
					dibujarExtra();
				}else{
					nuevoNivel(mapaActual);
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
