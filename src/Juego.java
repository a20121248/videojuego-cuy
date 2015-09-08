import java.io.IOException;
import java.util.*;

public class Juego {
	public static void bucleLectura() {
		int teclaPresionada = 0;
		//System.exit(0) finaliza el programa.
		while (true) {
			try {
				teclaPresionada = System.in.read();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			if (teclaPresionada == -1) {
				// fin de archivo
				System.exit(0);
			}
			if (teclaPresionada == '\n') {
				//nueva l�nea
				continue;
			}
			if (teclaPresionada == 'a') {
				// ok, salimos del bucle de lectura
				return;
			} 
			else if (teclaPresionada == 'b') {
				System.out.println("El juego termina");
				System.exit(0);
			} 
			else {
				System.out.println("Presione a o b");
			}
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		final String[] historias = {
			"Hab�a una vez un cuy que jugaba chowdown y se fue de viaje por el Per� buscando chowdonitas.",
			"Conoci� a Andrea Guardia y la ret� a un chowdown. Fue ggeado.",
			"Fue a la mundial de Pok�mon y conoci� a Cybertron.",
			"Venci� al jefe final Mascapo y se volvi� el mejor chowdownita de la historia."
		};
		
		final String cadenaGameOver = "Has dejado en visto a H�ctor.";
		final String cadenaJuegoCompletado = "El juego termin�, pero el chowdown sigue.";
		
		boolean gameOver = false;
		do {
			if (gameOver) {
				//Se ha perdido por lo menos una vez
				gameOver = false;
				System.out.println("As� que quieres volver a intentarlo :v.");
				PersonajePrincipal.reiniciarVida();
			}
			InterpreteComandos interp = new InterpreteComandos();
			GestorMapas gestor = new GestorMapas();
			//INICIO: Pantalla de inicio
			System.out.println("Kiru es un cuy mascota");
			System.out.println("a. Iniciar juego");
			System.out.println("b. Salir del juego");
			
			//�Juego nuevo o salir?
			bucleLectura();
			
			//Solicitud de datos para juego nuevo
			System.out.println("Ingrese su nombre");
			String nombre = scanner.next();
			int cantMapas = 3;
			for (int i = 0; i < cantMapas; i++) {
				System.out.println("Nivel " + (i + 1));
				
				//Pantalla de historia "i"
				System.out.println(historias[i]);
				System.out.print("Presione enter para continuar.");
				try {
					System.in.read();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				
				
				//Tutorial 1, Nivel 1, Nivel 2
				gestor.cargarMapa(i);
				while (true) {
					System.out.println("Ingresar comando");
					String comando = scanner.nextLine();
					int valor = interp.interpretarMovimiento(comando);
					gestor.realizarMovimiento(valor);
					String aux = gestor.realizarMovimientoEspecial(valor);
					if (aux.equals("F")) {
						System.out.println("Felicidades, termin� el nivel");
						break;
					} 
					else if (!aux.isEmpty()) {
						while (true) {
							comando = scanner.nextLine();
							if (interp.interpretarEspecial(comando, aux)) {
								// Slsvcn estuvo aqu� 08/09/2015
								// acert� el comando, salir del bucle
								break;
							} 
							else {
								// se equivoc�, bajarles vida
								if (PersonajePrincipal.perderVida(2)) {
									// siguen vivos
									System.out.println("Kiru y Milo perdieron 2 puntos de vida.");
								} 
								else {
									// gg
									System.out.println(cadenaGameOver);
									gameOver = true;
									break;
								}
							}
							gestor.mostrarComando();
						}
						if (gameOver) break;
						if (gestor.ejecutarComando(valor).equals("F")) {
							System.out.println("Felicidades, termin� el nivel");
							break;
						}
					}
				}
				if (gameOver) break;
			}
		} 
		while (gameOver);
		
		//Pantalla de historia 4
		System.out.println(historias[3]);
		
		//Pantalla de juego completado
		System.out.println(cadenaJuegoCompletado);
		
		
		scanner.close();
	}
}
