package vista;

import java.awt.BorderLayout;
import java.util.List;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.io.IOException;
import java.awt.EventQueue;
import java.awt.Graphics;

import modelo.*;
import controlador.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Ventana extends JFrame {

	private JPanel contentPane;
	private JPanel panel,panel2;
	Mapa m=null;
	BufferedImage b;

	/**
	 * Launch the application.
	 */
	class Panel extends JPanel{
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
	}
	public void dibujar(Mapa m){
		this.m = m;
		panel.repaint();
	}

	/**
	 * Create the frame.
	 */
	public Ventana() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1224, 768);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new Panel();
		panel.setBounds(0, 0, 1024, 768);
		contentPane.add(panel);
		panel2 = new JPanel();
		panel2.setBounds(1024, 768, 200, 768);
		contentPane.add(panel2);
	}
}
