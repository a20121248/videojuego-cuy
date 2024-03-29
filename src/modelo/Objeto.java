package modelo;

import java.awt.image.BufferedImage;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class Objeto implements Dibujable{
	private char sprite;
	private int posX,posY; //esquina superior izquierda
	private int ancho;
	private int altura;
	@XStreamOmitField
	BufferedImage img;
	
	public Objeto(int posX,int posY,int ancho,int altura,char sprite){
		this.posX = posX;
		this.posY = posY;
		this.ancho = ancho;
		this.altura = altura;
		this.sprite = sprite;
	}
	public Objeto copy(){
		Objeto c = new Objeto(posX,posY,ancho,altura,sprite);
		c.setImg(img);
		return c;
	}
	public int getPosX(){return posX;}
	public int getPosY(){return posY;}
	public int getAncho(){return ancho;}
	public int getAltura(){return altura;}
	public char getSprite(){return sprite;}
	public BufferedImage getImg(){return img;}
	public void setImg(BufferedImage img){this.img=img;}
}