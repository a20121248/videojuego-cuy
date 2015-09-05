import java.util.ArrayList;
import java.util.List;

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