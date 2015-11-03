package vista;

public class HiloAcciones extends Thread {
	Ventana v;
	Object bandera2;

	public HiloAcciones(Ventana ventana, Object b) {
		v = ventana;
		bandera2 = b;
	}

	public void run() {
		v.pnlTexto.removeTexto();
		v.pnlTexto.removeTexto();
		while (true) {
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