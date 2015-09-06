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
        
        
        int teclaPresionada = 0;
        //bucle de lectura, aún no es el juego
        //TODO: solo leer dato y pasárselo a InterpreteComandos
        //Pueden usar System.exit(0) para salir del programa si no están en el main.
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


        System.out.println("Ingrese su nombre");
        String nombre = scanner.next();
        System.out.println("Tu nombre es " + nombre);
        //Renderizador rend = new Renderizador(gestor.getMapa(0));
        //TODO: renderizar primer nivel
        
        
    }
}
