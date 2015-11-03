package vista;

public class HiloMovimiento extends Thread {
	Object bandera;
	Ventana v;
	boolean flag = true;

	public HiloMovimiento(Object bandera,Ventana v) {
		this.bandera = bandera;
		this.v = v;
	}

	public void stop2() {
		flag = false;
		interrupt();
	}

	public void run() {
		while (flag) {
			try {
				sleep(500);
				
				if (!flag)
					break;
				synchronized (bandera) {
					v.flagDibujo = !v.flagDibujo;
				}
				v.dibujar();
				
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
