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
				//nueva línea
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
		/* Director's Cut
		 * 
		 * final String[] historias = {
			"Había una vez un cuy que jugaba chowdown y se fue de viaje por el Perú buscando chowdonitas.",
			"Conoció a Andrea Guardia y la retó a un chowdown. Fue ggeado.",
			"Fue a la mundial de Pokémon y conoció a Cybertron.",
			"Venció al jefe final Mascapo y se volvió el mejor chowdownita de la historia."
		};
		
		final String cadenaGameOver = "Has dejado en visto a Héctor.";
		final String cadenaJuegoCompletado = "El juego terminó, pero el chowdown sigue.";*/
		final String[] historias = {
			"Kiru y Milo conversan. Le nace la pregunta a Kiru y deciden viajar.",
			"Kiru y Milo viajan a Paracas en un auto. Llegan a la playa y empiezan a jugar.",
			"Kiru y Milo se encuentran con Peli el Pelícano. Peli el pelícano no sabe de dónde viene Kiru. Kiru y Milo deciden viajar a la sierra.",
			"Kiru y Milo conversan con Dana la Llama. Dana responde la pregunta de Kiru. Kiru se contenta y decide, con Milo, viajar por todos los Andes."
		};
		
		final String cadenaGameOver = "Has perdido.";
		final String cadenaJuegoCompletado = "Felicitaciones, has terminado el juego.";
		final String[] dialogo = {
			"o	Usa WASD para mover a Kiru y JKLI para mover a Milo"+
			"o	Si ves un lugar para la acción o el dúo ¡Párate sobre él! Podrás realizar acciones especiales.\n"+
			"o	Sólo podrás pasar los niveles con la ayuda de las acciones especiales. Para esto, tendrás que presionar comandos que se mostrarán en un cuadro de diálogo como éste.\n"+
			"o	Los comandos deben ser ejecutados en la secuencia correcta sino perderás puntos de vida.\n"+
			"o	Puedes ver los puntos de vida en la parte superior de la pantalla.\n"+
			"o	Para activar los terrenos con acciones especiales dúo, tienen que estar sobre ellos Kiru y Milo al mismo tiempo, en los de acciones especiales sólo con uno basta.\n",
			
			"o	En tu aventura, a veces te toparás con animales malos.\n"+
			"o	Estos enemigos te bajarán puntos de vida. Si tus puntos de vida llegan a 0, se acabará el juego.\n"+
			"o	Si un enemigo afecta a un personaje, este no se podrá mover. Tendrás que usar a su amigo para ayudarlo.\n"


		};
		
		
		boolean gameOver = false;
		do {
			if (gameOver) {
				//Se ha perdido por lo menos una vez
				gameOver = false;
				System.out.println("Vuevelo a intentar..");
				PersonajePrincipal.reiniciarVida();
			}
			InterpreteComandos interp = new InterpreteComandos();
			GestorMapas gestor = new GestorMapas();
			//INICIO: Pantalla de inicio
			System.out.println("Kiru es un cuy mascota");
			System.out.println("a. Iniciar juego");
			System.out.println("b. Salir del juego");
			
			//¿Juego nuevo o salir?
			bucleLectura();
			scanner.nextLine();
			
			//Solicitud de datos para juego nuevo
			System.out.println("Ingrese su nombre");
			String nombre = scanner.nextLine();
			int cantMapas = 3;
			for (int i = 0; i < cantMapas; i++) {
				if (i == 0) {
					System.out.println("Tutorial");
				}
				else {
					System.out.println("Nivel " + i);
				}
				//Pantalla de historia "i"
				System.out.println(historias[i]);
				System.out.print("Presione enter para continuar.");
				scanner.nextLine();
				if(i==0){
					System.out.println(dialogo[0]);
					System.out.print("Presione enter para continuar.");
					scanner.nextLine();
				}
				
				//Tutorial, Nivel 1, Nivel 2
				gestor.cargarMapa(i);
				while (true) {
					System.out.println("Ingresar comando");
					String comando = scanner.nextLine();
					int valor = interp.interpretarMovimiento(comando);
					gestor.realizarMovimiento(valor);
					String aux = gestor.realizarMovimientoEspecial(valor);
					if (aux.equals("F")) {
						//System.out.println("Felicidades, terminó el nivel");
						break;
					} 
					else if (!aux.isEmpty()) {
						while (true) {
							comando = scanner.nextLine();
							if (interp.interpretarEspecial(comando, aux)) {
								// Slsvcn estuvo aquí 08/09/2015
								// acertó el comando, salir del bucle
								break;
							} 
							else {
								// se equivocó, bajarles vida
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
							if(i==0){
								System.out.println(dialogo[1]);
								System.out.print("Presione enter para continuar.");
								scanner.nextLine();
							}
							//System.out.println("Felicidades, terminó el nivel");
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
