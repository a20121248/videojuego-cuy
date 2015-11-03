package vista;

public class HiloVida extends Thread {
	Object bandera;
	Ventana ventana;
	int tiempo;
	boolean flag = true;

	public HiloVida(Object bandera, Ventana ventana, int tiempo) {
		this.bandera = bandera;
		this.ventana = ventana;
		this.tiempo = tiempo;
	}

	public void stop2() {
		flag = false;
		interrupt();
	}

	public void run() {
		while (flag) {
			try {
				sleep(1000 * tiempo);
				if (!flag)
					break;
				synchronized (bandera) {
					if (ventana.perdioVida)
						ventana.pnlTexto.removeTexto();
					ventana.pnlTexto.quitarVida(2);
					ventana.perdioVida = true;
					if (ventana.gestor.perderVida(2)) {
						ventana.pnlTexto.addTexto("Kiru y Milo perdieron 2 puntos de vida.");
						ventana.dibujarExtra();
					} else {
						ventana.pnlTexto.addTexto(ventana.cadenaGameOver);
						ventana.pnlTexto.addTexto("Vuelvalo a intentar");
						ventana.pnlTexto.addTexto("Presione enter para continuar");
						ventana.dibujarExtra();
						ventana.eventFlag = -4;
						break;
					}
				}
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
