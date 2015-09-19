class Celda{
	char sprite;
	int tipo; // 0 = impasable, 1 = terreno para A, 2 = terreno para B, 3 = terreno para ambos
	int jugador; // -1 = nadie, 0 = jugador A, 1 = jugador B
	int especial; // 0 = no 1 = solo activado 2 = ambos activado 3 = fin de nivel
					//-1 = solo desactivado -2 = ambos desactivado
	int indiceEspecial;
	
	public char getSprite(){
		return sprite;
	}
	public Celda(char sprite, int tipo){
		
		this.sprite = sprite;
		this.tipo = tipo;
		this.jugador = -1;
		this.especial = 0;
		
	}
	public Celda copy(){
		Celda c = new Celda(sprite,tipo);
		c.setJugador(jugador);
		c.setEspecial(especial);
		c.setIndiceEspecial(indiceEspecial);
		return c;
	}
	public void setJugador(int jugador){this.jugador = jugador;}
	public void setTipo(int tipo){this.tipo = tipo;}
	public void setSprite(char sprite){this.sprite = sprite;}
	public int getTipo(){return tipo;}
	public int getIndiceEspecial(){return indiceEspecial;}
	public int getEspecial(){return especial;}
	
	public void setEspecial(int especial){this.especial = especial;}
	public void setIndiceEspecial(int indiceEspecial){this.indiceEspecial = indiceEspecial;}
	
	public boolean verificarMovimientoPosible(int tipo){
		if( this.tipo==3 ) return true;
		if( this.tipo == tipo+1 ) return true;
		else return false;
	}
}