package vista;

import controlador.*;
//import modelo.*;

import java.io.IOException;
import java.util.*;
import java.lang.String;

public class Juego {
	public static void bucleLectura() {
		int teclaPresionada = 0;
		// System.exit(0) finaliza el programa.
		while (true) {
			try {
				teclaPresionada = System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (teclaPresionada == -1)
				System.exit(0);
			if (teclaPresionada == '\n')
				continue; // nueva l匤ea
			if (teclaPresionada == 'a')
				return; // ok, salimos del bucle de lectura
			else if (teclaPresionada == 'b') {
				System.out.println("El juego termina");
				System.exit(0);
			} else
				System.out.println("Presione a o b");
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		final String[] historias = { 
			"Kiru y Milo conversan. Le nace la pregunta a Kiru y deciden viajar.",
			"Kiru y Milo viajan a Paracas en un auto. Llegan a la playa y empiezan a jugar.",
			"Kiru y Milo se encuentran con Peli el Pel兤ano. Peli el pel兤ano no sabe de dde viene Kiru. Kiru y Milo deciden viajar a la sierra.",
			"Kiru y Milo conversan con Dana la Llama. Dana responde la pregunta de Kiru. Kiru se contenta y decide, con Milo, viajar por todos los Andes."
		};

		final String cadenaGameOver = "Has perdido.";
		final String cadenaJuegoCompletado = "Felicitaciones, has terminado el juego.";
		final String[] dialogo = { 
			"o	Usa WASD para mover a Kiru y JKLI para mover a Milo" +
			"o	Si ves un lugar para la acci o el d侒 ｡P疵ate sobre 駘! Podr疽 realizar acciones especiales.\n" +
			"o	So podr疽 pasar los niveles con la ayuda de las acciones especiales. Para esto, tendr疽 que presionar comandos que se mostrar疣 en un cuadro de di疝ogo como 駸te.\n" +
			"o	Los comandos deben ser ejecutados en la secuencia correcta sino perder疽 puntos de vida.\n" +
			"o	Puedes ver los puntos de vida en la parte superior de la pantalla.\n" +
			"o	Para activar los terrenos con acciones especiales d侒, tienen que estar sobre ellos Kiru y Milo al mismo tiempo, en los de acciones especiales so con uno basta.\n",

			"o	En tu aventura, a veces te topar疽 con animales malos.\n" +
			"o	Estos enemigos te bajar疣 puntos de vida. Si tus puntos de vida llegan a 0, se acabar� el juego.\n" +
			"o	Si un enemigo afecta a un personaje, este no se podr� mover. Tendr疽 que usar a su amigo para ayudarlo.\n" 
		};

		
		boolean gameOver = false;
		do {
			InterpreteComandos interp = new InterpreteComandos();
			GestorMapas gestor = new GestorMapas();
			Renderizador rend = new Renderizador();
			Ventana v = new Ventana();
			v.setVisible(true);
			if (gameOver) {
				// Se ha perdido por lo menos una vez
				gameOver = false;
				System.out.println("VueLvelo a intentar..");
				gestor.reiniciarVida();
			}

			// INICIO: Pantalla de inicio
			System.out.println("Kiru es un cuy mascota");
			System.out.println("a. Iniciar juego");
			System.out.println("b. Salir del juego");

			// ｿJuego nuevo o salir?
			bucleLectura();
			scanner.nextLine();

			// Solicitud de datos para juego nuevo
			System.out.println("Ingrese su nombre");
			String nombre = scanner.nextLine();
			System.out.println("Bienvenido " + nombre + ".");
			int cantMapas = 3;
			for (int i = 0; i < cantMapas; i++) {

				if (i == 0)
					System.out.println("Tutorial");
				else
					System.out.println("Nivel " + i);

				// Pantalla de historia "i"
				System.out.println(historias[i]);
				System.out.print("Presione enter para continuar.");
				scanner.nextLine();
				if (i == 0) {
					System.out.println(dialogo[0]);
					System.out.print("Presione enter para continuar.");
					scanner.nextLine();
				}

				// Tutorial, Nivel 1, Nivel 2
				gestor.cargarMapa(i);
				System.out.println("kya");
				v.dibujar(gestor.getMapaActual());
				rend.dibujarMapa(gestor.getMapaActual());
				while (true) {
					System.out.println("Ingresar comando");
					String comando = scanner.nextLine();
					int valor = interp.interpretarMovimiento(comando);
					gestor.realizarMovimiento(valor);
					v.dibujar(gestor.getMapaActual());
					rend.dibujarMapa(gestor.getMapaActual());
					
					String aux = gestor.realizarMovimientoEspecial(valor);
					if (aux.equals("F")) {
						break;
					} 
					else if (!aux.equals("")) {
						while (true) {
							rend.mostrarComandos(aux);
							comando = scanner.nextLine();
							if (interp.interpretarEspecial(comando, aux))
								break; // Slsvcn estuvo aqu� 08/09/2015; acert�
										// el comando, salir del bucle
							else {
								// se equivoc�, bajarles vida
								if (gestor.perderVida(2))
									System.out.println("Kiru y Milo perdieron 2 puntos de vida."); // siguen vivos
								else {
									// gg
									System.out.println(cadenaGameOver);
									gameOver = true;
									break;
								}
							}
						}
						if (gameOver)
							break;

						while (true) {
							aux = gestor.ejecutarComando(valor);
							v.dibujar(gestor.getMapaActual());
							rend.dibujarMapa(gestor.getMapaActual());
							scanner.nextLine();
							if (aux.equals("F") && i == 0) {
								System.out.println(dialogo[1]);
								System.out.print("Presione enter para continuar.");
								scanner.nextLine();
								break;
							}
							if (aux.equals("Done"))
								break;
						}
					}
					if (aux.equals("F"))
						break;
				}
				if (gameOver)
					break;
			}
		} 
		while (gameOver);

		// Pantalla de historia 4
		System.out.println(historias[3]);

		// Pantalla de juego completado
		System.out.println(cadenaJuegoCompletado);

		scanner.close();
	}
}
