import java.io.IOException;
import java.util.*;

class InterpreteComandos{
	public void interpretar(String comando){
		
	}
}
class Renderizador{
	Mapa mapaActual;
	public void dibujarMapa(){
		
	}
	public Renderizador(Mapa mapa){
		mapaActual = mapa;
	}
	
}
class Mapa{
	int ancho;
	int altura;
	List<List<Celda> > map;
	public void modificarCelda(int x,int y){
		
	}
	
}
class GestorMapas{
	List<Mapa> mapas;
	int cant;
	public GestorMapas(){
		mapas = new ArrayList<Mapa>();
		cant = 0;
	}
	public Mapa getMapa(int index){
		return mapas.get(index);
	}
	public void addMapa(Mapa mapa){
		cant++;
		mapas.add(mapa);
	}
}
class Celda{
	char valor;
}
class Personaje{
	String nombre;
	
}
class Objeto{
	String nombre;
	int ancho;
	int altura;
}
class Juego {
	public static void main(String[] args){
		InterpreteComandos interp = new InterpreteComandos();
		GestorMapas gestor = new GestorMapas();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Kiru es un cuy mascota");
		System.out.println("a. Iniciar juego");
		System.out.println("b. Salir del juego");
		
		
		int c='k';
		while(true){
			try {
				c = System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(c==10 || c==13) continue;
			if(c=='a'){
				break;
			}else if(c=='b'){
				System.out.println("El juego termina");
				break;
			}else{
				System.out.println("Presione a o b");
			}
		}
		if(c=='b') return; 
		Renderizador rend = new Renderizador(gestor.getMapa(0));
		System.out.println("Ingrese su nombre");
		String nombre = scanner.nextLine();
	}
}
