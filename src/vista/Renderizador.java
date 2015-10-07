package vista;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import controlador.GestorMapas;
import modelo.*;

class PanelTexto extends JPanel{
	
    private static final long serialVersionUID = 1065949227120247586L;
    List<String> textos;

    public void addTexto(String texto){
		textos.add(texto);
	}
	public void removeTexto(){
		textos.remove(textos.size()-1);
	}
	public void inicializarTexto(String nombre){
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
    
    public PanelTexto(){
    	textos = new ArrayList<String>();
    }
	
}
class PanelGraficos extends JPanel{

    private static final long serialVersionUID = -4712396594532659965L;

    Mapa m;
    BufferedImage imgCuy,imgPerro;
    public void paint(Graphics g){
		super.paint(g);
		if(m!=null){
			BufferedImage b;
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
						b = imgCuy;
						g.drawImage(b,j*64,i*64,null);
					}else if(m.getCelda(i, j).getSprite() == 'B'){
						b = imgPerro;
						g.drawImage(b,j*64,i*64,null);
					}
				}
			}
		}
	}
    public PanelGraficos(){
    	m = null;
    	try {
			imgCuy = (ImageIO.read(getClass().getResource("/imagenes/sprite_cuy.gif")));
			imgPerro = (ImageIO.read(getClass().getResource("/imagenes/sprite_perro.gif")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
}