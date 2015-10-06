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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.awt.*;
import javax.swing.*;
public class Ventana extends JFrame {

	private JPanel contentPane;
	private JPanel panel,panel2;
	int flag = 0;
	Mapa m=null;
	BufferedImage b;
	InterpreteComandos interp;
	GestorMapas gestor;
	Renderizador rend;
	String comando;
	String nombre="Kek";
	int mapaActual = 0;
	int cantMapas = 3;
	int indiceComando;
	int indiceDialogo = 0;
	int valor;
	boolean perdioVida = false;
	List<String> textos;
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
	

	public void addTexto(String texto){
		textos.add(texto);
	}
	public void removeTexto(){
		textos.remove(textos.size()-1);
	}
	public void inicializarTexto(){
		textos.clear();
		textos.add("Nombre: ");
		textos.add(nombre);
		textos.add("Nivel: Tutorial");
		textos.add("Vida: 10");
		textos.add("Comando actual:");
		textos.add("");
	}
	public void cambiarNivel(int index){
		textos.set(2,"Nivel: "+index);
	}
	public void quitarVida(int val){
		int vidaActual = Integer.parseInt(textos.get(3).substring(6));
		vidaActual -= val;
		textos.set(3, "Vida: "+vidaActual);
	}
	public void anadirUltimo(char c){
		String aux = textos.get(textos.size()-1);
		aux += c;
		textos.set(textos.size()-1, aux);
	}
	public void eliminarUltimo(){
		String aux = textos.get(textos.size()-1);
		aux = aux.substring(0, aux.length()-1);
		textos.set(textos.size()-1, aux);
	}
	class PanelGraficos extends JPanel{
		
		
		public void paint(Graphics g){
			super.paint(g);
			//System.out.println("topkek");
			int aux = 10;			
			for(int i=0;i<textos.size();i++){
				//System.out.println(textos.get(i));
				String[] s = textos.get(i).split(" ");
				String out = "";
				int l = 0;
				for(int j=0;j<s.length;j++){
					if(l + s[j].length() >= 25){
						g.drawString(out, 10, aux);
						out = "";
						aux+= 30;
						l = 0;
					}
					l += s[j].length();
					out += " ";
					out += s[j];
					if(out.charAt(out.length()-1) == '.'){
						g.drawString(out, 10, aux);
						out = "";
						aux+=30;
						l=0;
					}
				}
				g.drawString(out, 10, aux);
				aux += 30;
			}
		}
		
	}
	class PanelTexto extends JPanel implements KeyListener{
		public void paint(Graphics g){
			super.paint(g);
			if(m!=null){
				for(int i=0;i<m.getAltura();i++){
					for(int j=0;j<m.getAncho();j++){
						b = m.getCelda(i, j).getImg();
						g.drawImage(b,j*64,i*64,null);
					}
				}
				for(int i=0;i<m.cantidadObstaculos();i++){
					b = m.getObstaculo(i).getImg();
					g.drawImage(b,m.getObstaculo(i).getPosY()*64,m.getObstaculo(i).getPosX()*64,null);
				}
				for(int i=0;i<m.getAltura();i++){
					for(int j=0;j<m.getAncho();j++){
						if(m.getCelda(i, j).getSprite() == 'A'){
							try {
								b = (ImageIO.read(getClass().getResource("/imagenes/sprite_cuy.png")));
								g.drawImage(b,j*64,i*64,null);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}else if(m.getCelda(i, j).getSprite() == 'B'){
							try {
								b = (ImageIO.read(getClass().getResource("/imagenes/sprite_perro.png")));
								g.drawImage(b,j*64,i*64,null);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
			}
		}
		
		public PanelTexto(){
			addKeyListener(this);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
				Object[] options = {"Si","No"};
				
				int n = JOptionPane.showOptionDialog(this,
				    "¿Seguro que desea salir?",
				    "A Silly Question",
				    JOptionPane.YES_OPTION,
				    JOptionPane.NO_OPTION,
				    null,
				    options,
				    options[0]);
				if(n==JOptionPane.YES_OPTION) System.exit(0);
				
			}
			if(flag == -6){ //ingresar nombre
				if(e.getKeyCode() == KeyEvent.VK_ENTER) nuevoNivel(mapaActual);
				else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					eliminarUltimo();
					if(nombre.length()!=0) nombre= nombre.substring(0,nombre.length()-1);
					dibujarExtra();
				}
				else{
					char c = e.getKeyChar();
					if((c>='a' && c<='z')||(c>='A' && c<='Z')){
						anadirUltimo(c);
						nombre += c;
						dibujarExtra();
					}
				}
			}else if(flag == -5){ //ingresar datos y aceptar juego i.e. bucle lectura
				if(e.getKeyChar() == 'a' || e.getKeyChar()=='A'){
					flag = -6;
					textos.clear();
					addTexto("Ingrese su nombre");
					addTexto("");
					dibujarExtra();
					gestor.reiniciarVida();
					nombre = "";
				}else if(e.getKeyChar() == 'b' || e.getKeyChar()=='B'){
					textos.clear();
					addTexto("Saliendo del juego");
					addTexto("Presione enter para terminar");
					dibujarExtra();
					flag = -3;
				}else if(e.getKeyChar() == 'c' || e.getKeyChar()=='C'){
					textos.clear();
					addTexto("Cargando juego, espere un momento");
					dibujarExtra();
					try {XStream xs = new XStream(new DomDriver());
					
						gestor = (GestorMapas)xs.fromXML(new FileInputStream("Savestate.xml"));
						nombre=gestor.Get_nombre();
						gestor.recargarImagenes();
			           dibujar(gestor.getMapaActual());
			           mapaActual = gestor.getIndMapaActual();
						inicializarTexto();
						if(mapaActual>0) cambiarNivel(mapaActual);
						int aux = gestor.getVida();
						while(aux!=10){
							quitarVida(2);
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
				textos.clear();
				addTexto("Kiru es un cuy mascota");
				addTexto("a. Iniciar juego");
				addTexto("b. Salir del juego");
				addTexto("c. Cargar partida");
				dibujarExtra();
			}else if(flag == -3 && e.getKeyCode() == KeyEvent.VK_ENTER){ // juego terminado
				System.exit(0);
			}else if(flag == -2 && e.getKeyCode() == KeyEvent.VK_ENTER){ // dialogo despues de fin de nivel
				removeTexto();
				removeTexto();
				mapaActual++;
				if(mapaActual == cantMapas){
					addTexto(cadenaJuegoCompletado);
					dibujarExtra();
					flag = -3;
				}else{
					nuevoNivel(mapaActual);
				}
			}else if(flag==-1 && e.getKeyCode() == KeyEvent.VK_ENTER){ // dialogo en inicio de nivel
				removeTexto();
				removeTexto();
				addTexto(dialogo[0]);
				addTexto("Presione enter para continuar");
				dibujarExtra();
				flag = 0;
			}else if(flag == 0 && e.getKeyCode() == KeyEvent.VK_ENTER){ // historias por nivel
				removeTexto();
				removeTexto();
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
						addTexto(cadenaJuegoCompletado);
						dibujarExtra();
					}else{
						nuevoNivel(mapaActual);
					}
				}else if(!aux.equals("")){
					comando = aux;
					indiceComando = 0;
					flag = 2;
					perdioVida = false;
					addTexto(comando);
					addTexto("");
					dibujarExtra();
				}
				
			}else if(flag==2){ // comando
				String str = "";
				str += e.getKeyChar();
				str = str.toUpperCase();
				if(perdioVida) removeTexto();
				if(interp.interpretarEspecial(str,comando.charAt(indiceComando))){
					perdioVida = false;
					anadirUltimo(str.charAt(0));
					indiceComando++;
					if(indiceComando == comando.length()){
						flag = 3;
						addTexto("Presione enter para continuar");
					}
					dibujarExtra();
				}else{
					quitarVida(2);
					perdioVida = true;
					if(gestor.perderVida(2)){
						
						addTexto("Kiru y Milo perdieron 2 puntos de vida.");
						dibujarExtra();
					}else{
						addTexto(cadenaGameOver);
						addTexto("Vuelvalo a intentar");
						addTexto("Presione enter para continuar");
						dibujarExtra();
						flag = -4;
					}
				}
			}else if(flag == 3 && e.getKeyCode() == KeyEvent.VK_ENTER){ // realizar comando especial
				String aux = gestor.ejecutarComando(valor);
				dibujar(gestor.getMapaActual());
				
				
				if (aux.equals("F") && mapaActual == 0) {
					removeTexto();
					removeTexto();
					removeTexto();
					addTexto(dialogo[1]);
					addTexto("Presione enter para continuar");
					dibujarExtra();
					flag = -2;
				}
				if (aux.equals("Done")){
					removeTexto();
					removeTexto();
					removeTexto();
					dibujarExtra();
					flag = 1;
				}
				if(aux.equals("F") && flag != -2){
					removeTexto();
					removeTexto();
					removeTexto();
					mapaActual++;
					if(mapaActual == cantMapas){
						flag = -3;
						addTexto(cadenaJuegoCompletado);
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
	public void nuevoNivel(int nivel){
		gestor.cargarMapa(nivel,nombre);
		dibujar(gestor.getMapaActual());
		if(nivel==0){
			inicializarTexto();
			flag = -1;
		}else{
			cambiarNivel(nivel);
			flag = 0;
		}
		addTexto(historias[nivel]);
		addTexto("Presione enter para continuar");
		dibujarExtra();
		
	}
	public void dibujar(Mapa m){
		this.m = m;
		panel.repaint();
	}
	public void dibujarExtra(){
		//System.out.println("kya");
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
		panel = new PanelTexto();
		panel.setBounds(0, 0, 1024, 768);
		panel.setFocusable(true);
		contentPane.add(panel);
		panel2 = new PanelGraficos();
		panel2.setBounds(1024,0,200,768);
		contentPane.add(panel2);
		this.addFocusListener(new FocusAdapter() {
	        @Override
	        public void focusGained(FocusEvent aE) {
	            panel.requestFocusInWindow();
	        }
	    });
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				Object[] options = {"Si","No"};
				
				int n = JOptionPane.showOptionDialog(Ventana.this,
				    "¿Seguro que desea salir?",
				    "A Silly Question",
				    JOptionPane.YES_OPTION,
				    JOptionPane.NO_OPTION,
				    null,
				    options,
				    options[0]);
				if(n==JOptionPane.YES_OPTION) System.exit(0);
			}
		});
		interp = new InterpreteComandos();
		gestor = new GestorMapas();
		rend = new Renderizador();
		textos = new ArrayList<String>();
		addTexto("Kiru es un cuy mascota");
		addTexto("a. Iniciar juego");
		addTexto("b. Salir del juego");
		addTexto("c. Cargar partida");
	}
}
