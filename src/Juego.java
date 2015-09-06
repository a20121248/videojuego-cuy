import java.io.IOException;
import java.util.*;

public class Juego {
	public static void main(String[] args){
		InterpreteComandos interp = new InterpreteComandos();
		GestorMapas gestor = new GestorMapas();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Kiru es un cuy mascota");
		System.out.println("a. Iniciar juego");
		System.out.println("b. Salir del juego");
		
		
		int teclaPresionada=0;
		while(true){
            try {
                teclaPresionada = System.in.read();
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
            if (teclaPresionada == -1) return; //fin de archivo
            if(teclaPresionada == '\n' || teclaPresionada == '\r') continue; //Line feed o Carriage return (nueva línea)
            
            if(teclaPresionada == 'a'){
                //ok, salimos del bucle de lectura
                break;
            }
            else if(teclaPresionada == 'b'){
                System.out.println("El juego termina");
                return; //fin del programa
            }
            else{
                System.out.println("Presione a o b");
            }
        }
		if(teclaPresionada=='b') return; 
		System.out.println("Ingrese su nombre");
		String nombre = scanner.nextLine();
		int cantMapas = 1;
		
		for(int i=0;i<cantMapas;i++){
			gestor.cargarMapa(0);
			while(true){
				System.out.println("Ingresar comando");
				String comando = scanner.nextLine();
				int valor = interp.interpretarMovimiento(comando);
				gestor.realizarMovimiento(valor);
				String aux = gestor.realizarMovimientoEspecial(valor); 
				if(aux.equals("F")){
					System.out.println("Felicidades, terminó el nivel");
					break;
				}else if(!aux.equals("")){
					while(true){
						comando = scanner.nextLine();
						if(interp.interpretarEspecial(comando,aux)) break;
						gestor.mostrarComando();
					}
					if(gestor.ejecutarComando(valor).equals("F")){
						System.out.println("Felicidades, terminó el nivel");
						break;
					}
				}
			}
		}
		scanner.close();
	}
}
