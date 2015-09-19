public class PersonajePrincipal extends Personaje {
	private static int vida = 10;

	public static boolean perderVida(int v) {
		vida -= v;
		return (vida > 0);
	}
	
	public static void reiniciarVida() {
		vida = 10;
	}

	public PersonajePrincipal(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}

	boolean verificarPosX(int dir, int bound1, int bound2, Mapa mapaActual, int tipo) {
		int aux = posX;
		if ( dir == 0 ) aux++;
		else aux--;
		if ( aux < bound1 || aux >= bound2 ) return false;
		Celda c = mapaActual.getCelda(aux, posY);
		if ( !c.verificarMovimientoPosible(tipo) ) return false;
		posX = aux;
		return true;
	}

	boolean verificarPosY(int dir, int bound1, int bound2, Mapa mapaActual, int tipo) {
		int aux = posY;
		if ( dir == 0 ) aux++;
		else aux--;
		if ( aux < bound1 || aux >= bound2 ) return false;
		Celda c = mapaActual.getCelda(posX, aux);
		if ( !c.verificarMovimientoPosible(tipo) ) return false;
		posY = aux;
		return true;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	int getPosX() {
		return posX;
	}

	int getPosY() {
		return posY;
	}
}
