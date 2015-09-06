import java.util.ArrayList;
import java.util.*;
import java.util.List;

public class GestorMapas{
	List<Mapa> mapas;
	Renderizador rend;
	Mapa mapaActual;
	PersonajePrincipal jugador1,jugador2;
	boolean fin1,fin2;
	int indMapaActual;
	int cant;
	String comandoActual;
	Celda celdaDual;
	int dual;
	int tipoDual; // 0 = jugador1, 1= jugador2
	private void añadirMapas(){
		//16 de ancho, 12 de altura
		Mapa m;
		Objeto b;
		List<Objeto> obstaculos;
		List<Integer> direccion,liberaX,liberaY,valorLiberacion;
		List<CeldaEspecial> listaCeldaEsp;
		Celda Esp = new Celda(' ',0);
		Celda S = new Celda('S',1);
		Celda N = new Celda('N',2);
		Celda a = new Celda('a',0);
		Celda o = new Celda('o',3);
		
		m = new Mapa(16,12,2,11,0,0);
		obstaculos = new ArrayList<Objeto>();
		b = new Objeto(3,3,2,2,'m');
		obstaculos.add(b);
		b = new Objeto(8,4,2,2,'m');
		obstaculos.add(b);
		b = new Objeto(4,6,2,2,'j');
		obstaculos.add(b);
		b = new Objeto(10,8,2,2,'j');
		obstaculos.add(b);
		b = new Objeto(10,11,2,2,'j');
		obstaculos.add(b);
		b = new Objeto(2,9,2,2,'d');
		obstaculos.add(b);
		b = new Objeto(6,0,16,2,'a');
		obstaculos.add(b);
		m.setObstaculos(obstaculos);
		for(int i=0;i<2;i++){
			List<Celda> l = new ArrayList<Celda>();
			for(int j=0;j<16;j++){
				Celda c = Esp.copy();
				l.add(c);
			}
			m.addFila(l);
		}
		for(int i=2;i<7;i++){
			List<Celda> l = new ArrayList<Celda>();
			for(int j=0;j<16;j++){
				Celda c = S.copy();
				l.add(c);
			}
			m.addFila(l);
		}
		/*for(int i=6;i<8;i++){
			List<Celda> l = new ArrayList<Celda>();
			for(int j=0;j<16;j++){
				Celda c = a.copy();
				l.add(c);
			}
			m.addFila(l);
		}*/
		for(int i=7;i<12;i++){
			List<Celda> l = new ArrayList<Celda>();
			for(int j=0;j<16;j++){
				Celda c = N.copy();
				l.add(c);
			}
			m.addFila(l);
		}
		for(int i=5;i<9;i++){
			Celda aux = m.getCelda(i,15);
			aux.setTipo(3);
			aux.setEspecial(3);
			aux.setSprite('o');
		}
		for(int i=0;i<obstaculos.size();i++){
			Objeto bb = obstaculos.get(i);
			int altura = bb.getAltura();
			int ancho = bb.getAncho();
			int posX = bb.getPosX();
			int posY = bb.getPosY();
			char sprite = bb.getSprite();
			for(int j=0;j<altura;j++){
				for(int k=0;k<ancho;k++){
					Celda aux = m.getCelda(posX+j,posY+k);
					aux.setTipo(0);
					aux.setSprite(sprite);
				}
			}
		}
		listaCeldaEsp = new ArrayList<CeldaEspecial>();
		
		Celda aux = m.getCelda(2,8);
		aux.setEspecial(1);
		aux.setIndiceEspecial(0);
		aux.setSprite('C');
		CeldaEspecial celdaEsp = new CeldaEspecial();
		celdaEsp.setComandoEspecial("WDEQ");
		direccion = new ArrayList<Integer>();
		direccion.add(2);
		direccion.add(2);
		direccion.add(2);
		celdaEsp.setDireccionX(direccion);
		direccion = new ArrayList<Integer>();
		direccion.add(9);
		direccion.add(10);
		direccion.add(11);
		celdaEsp.setDireccionY(direccion);
		liberaX = new ArrayList<Integer>();
		liberaY = new ArrayList<Integer>();
		valorLiberacion = new ArrayList<Integer>();
		liberaX.add(5);
		liberaX.add(8);
		liberaY.add(13);
		liberaY.add(13);
		valorLiberacion.add(2);
		valorLiberacion.add(2);
		celdaEsp.setLiberaX(liberaX);
		celdaEsp.setLiberaY(liberaY);
		celdaEsp.setValorLiberacion(valorLiberacion);
		listaCeldaEsp.add(celdaEsp);
		
		aux = m.getCelda(5,13);
		aux.setEspecial(-2);
		aux.setIndiceEspecial(1);
		aux.setSprite('D');
		celdaEsp = new CeldaEspecial();
		celdaEsp.setComandoEspecial("SDKIQEUO");
		celdaEsp.setDualOpuesto(2);
		direccion = new ArrayList<Integer>();
		direccion.add(6);
		direccion.add(6);
		direccion.add(6);
		celdaEsp.setDireccionX(direccion);
		direccion = new ArrayList<Integer>();
		direccion.add(13);
		direccion.add(14);
		direccion.add(15);
		celdaEsp.setDireccionY(direccion);
		listaCeldaEsp.add(celdaEsp);
		
		aux = m.getCelda(8,13);
		aux.setEspecial(-2);
		aux.setIndiceEspecial(2);
		aux.setSprite('D');
		celdaEsp = new CeldaEspecial();
		celdaEsp.setComandoEspecial("SDKIQEUO");
		celdaEsp.setDualOpuesto(1);
		direccion = new ArrayList<Integer>();
		direccion.add(7);
		direccion.add(7);
		direccion.add(7);
		celdaEsp.setDireccionX(direccion);
		direccion = new ArrayList<Integer>();
		direccion.add(13);
		direccion.add(14);
		direccion.add(15);
		celdaEsp.setDireccionY(direccion);
		listaCeldaEsp.add(celdaEsp);
		
		m.setListaEspecial(listaCeldaEsp);
		/*aux = m.getCelda(11,0);
		aux.setEspecial(2);
		aux.setSprite('B');*/
		mapas.add(m);
		
		// Nivel 1
		
		m = new Mapa(16,12,2,11,0,0);
		obstaculos = new ArrayList<Objeto>();
		b = new Objeto(8,9,2,2,'i');
		obstaculos.add(b);
		b = new Objeto(10,11,2,2,'g');
		obstaculos.add(b);
		b = new Objeto(8,3,1,4,'L');
		obstaculos.add(b);
		b = new Objeto(7,0,16,1,'p');
		obstaculos.add(b);
		m.setObstaculos(obstaculos);
		for(int i=0;i<3;i++){
			List<Celda> l = new ArrayList<Celda>();
			for(int j=0;j<16;j++){
				Celda c = Esp.copy();
				l.add(c);
			}
			m.addFila(l);
		}
		for(int i=3;i<8;i++){
			List<Celda> l = new ArrayList<Celda>();
			for(int j=0;j<16;j++){
				Celda c = S.copy();
				l.add(c);
			}
			m.addFila(l);
		}
		/*for(int i=6;i<8;i++){
			List<Celda> l = new ArrayList<Celda>();
			for(int j=0;j<16;j++){
				Celda c = a.copy();
				l.add(c);
			}
			m.addFila(l);
		}*/
		for(int i=8;i<12;i++){
			List<Celda> l = new ArrayList<Celda>();
			for(int j=0;j<16;j++){
				Celda c = N.copy();
				l.add(c);
			}
			m.addFila(l);
		}
		for(int i=3;i<7;i++){
			Celda auxi = m.getCelda(i,0);
			auxi.setTipo(1);
			auxi.setEspecial(3);
		}
		for(int i=8;i<12;i++){
			Celda auxi = m.getCelda(i,0);
			auxi.setTipo(2);
			auxi.setEspecial(3);
		}		
		
		
		for(int i=0;i<obstaculos.size();i++){
			Objeto bb = obstaculos.get(i);
			int altura = bb.getAltura();
			int ancho = bb.getAncho();
			int posX = bb.getPosX();
			int posY = bb.getPosY();
			char sprite = bb.getSprite();
			for(int j=0;j<altura;j++){
				for(int k=0;k<ancho;k++){
					Celda auxi = m.getCelda(posX+j,posY+k);
					auxi.setTipo(0);
					auxi.setSprite(sprite);
				}
			}
		}
		listaCeldaEsp = new ArrayList<CeldaEspecial>();
		
		aux = m.getCelda(4,10);
		aux.setEspecial(1);
		aux.setIndiceEspecial(0);
		aux.setSprite('C');
		celdaEsp = new CeldaEspecial();
		celdaEsp.setComandoEspecial("SDQEQE");
		direccion = new ArrayList<Integer>();
		direccion.add(5);
		direccion.add(6);
		direccion.add(8);
		celdaEsp.setDireccionX(direccion);
		direccion = new ArrayList<Integer>();
		direccion.add(10);
		direccion.add(10);
		direccion.add(10);
		celdaEsp.setDireccionY(direccion);
		liberaX = new ArrayList<Integer>();
		liberaY = new ArrayList<Integer>();
		valorLiberacion = new ArrayList<Integer>();
		liberaX.add(9);
		liberaY.add(4);
		valorLiberacion.add(2);
		valorLiberacion.add(2);
		celdaEsp.setLiberaX(liberaX);
		celdaEsp.setLiberaY(liberaY);
		celdaEsp.setValorLiberacion(valorLiberacion);
		listaCeldaEsp.add(celdaEsp);
		
		aux = m.getCelda(9,4);
		aux.setEspecial(-1);
		aux.setIndiceEspecial(1);
		aux.setSprite('C');
		celdaEsp = new CeldaEspecial();
		celdaEsp.setComandoEspecial("JJUOJ");
		direccion = new ArrayList<Integer>();
		direccion.add(9);
		direccion.add(9);
		celdaEsp.setDireccionX(direccion);
		direccion = new ArrayList<Integer>();
		direccion.add(3);
		direccion.add(2);
		celdaEsp.setDireccionY(direccion);
		listaCeldaEsp.add(celdaEsp);
				
		m.setListaEspecial(listaCeldaEsp);
		/*aux = m.getCelda(11,0);
		aux.setEspecial(2);
		aux.setSprite('B');*/
		mapas.add(m);
			
	}
	public GestorMapas(){
		mapas = new ArrayList<Mapa>();
		rend = new Renderizador();
		añadirMapas();
		cant = 2;
		fin1 = false;
		fin2 = false;
	}
	
	public void cargarMapa(int index){
		if(index>cant) return;
		mapaActual = mapas.get(index).copy();
		
		jugador1 = new PersonajePrincipal(mapaActual.posX1,mapaActual.posY1);
		Celda c = mapaActual.getCelda(jugador1.posX,jugador1.posY);
		c.setJugador(0);
		c.setSprite('A');
		jugador2 = new PersonajePrincipal(mapaActual.posX2,mapaActual.posY2);
		c = mapaActual.getCelda(jugador2.posX,jugador2.posY);
		c.setJugador(1);
		c.setSprite('B');
		rend.dibujarMapa(mapaActual);
	}
	public void realizarMovimiento(int mov){
		
		Celda c;
		int posXAux,posYAux;
		if(mov==-1) return;
		
		if(mov<4){
			c = mapas.get(indMapaActual).getCelda(jugador1.getPosX(),jugador1.getPosY());
			posXAux = jugador1.getPosX(); posYAux = jugador1.getPosY();
		}else{
			c = mapas.get(indMapaActual).getCelda(jugador2.getPosX(),jugador2.getPosY());
			posXAux = jugador2.getPosX(); posYAux = jugador2.getPosY();
		}
		
		boolean aux=true;
		if(mov==0) aux = jugador1.verificarPosX(1,0,mapaActual.getAltura(),mapaActual,0);
		else if(mov==1) aux = jugador1.verificarPosY(1,0,mapaActual.getAncho(),mapaActual,0);
		else if(mov==2) aux = jugador1.verificarPosX(0,0,mapaActual.getAltura(),mapaActual,0);
		else if(mov==3) aux = jugador1.verificarPosY(0,0,mapaActual.getAncho(),mapaActual,0);
		else if(mov==4) aux = jugador2.verificarPosX(1,0,mapaActual.getAltura(),mapaActual,1);
		else if(mov==5) aux = jugador2.verificarPosY(1,0,mapaActual.getAncho(),mapaActual,1);
		else if(mov==6) aux = jugador2.verificarPosX(0,0,mapaActual.getAltura(),mapaActual,1);
		else if(mov==7) aux = jugador2.verificarPosY(0,0,mapaActual.getAncho(),mapaActual,1);
		if(!aux) return;
		
		mapaActual.retornarCelda(posXAux, posYAux, c);
		
		
		if(mov<4){
			fin1 = false;
			if(tipoDual == 0) dual=-1;
			mapaActual.modificarPosJugador(0,jugador1.getPosX(),jugador1.getPosY());
		}else{
			fin2 = false;
			if(tipoDual == 1) dual=-1;
			mapaActual.modificarPosJugador(1,jugador2.getPosX(),jugador2.getPosY());
		}
		
		rend.dibujarMapa(mapaActual);
	}
	public String realizarMovimientoEspecial(int mov){
		Celda c;
		if(mov==-1) return "";
		if(mov<4) c = mapaActual.getCelda(jugador1.getPosX(),jugador1.getPosY());
		else c = mapaActual.getCelda(jugador2.getPosX(),jugador2.getPosY());
		int especial = c.getEspecial();
		int ind = c.getIndiceEspecial();
		if(especial == 0) return "";
		else if(especial == 1){ //comando solo
			CeldaEspecial celdaEsp = mapaActual.getEspecial(ind);
			rend.mostrarComandos(celdaEsp.getComandoEspecial());
			comandoActual = celdaEsp.getComandoEspecial();
			List<Integer> liberaX = celdaEsp.getLiberaX();
			List<Integer> liberaY = celdaEsp.getLiberaY();
			List<Integer> valorLiberacion = celdaEsp.getvalorLiberacion();
			
			for(int i=0;i<liberaX.size();i++){
				Celda c2 = mapaActual.getCelda(liberaX.get(i),liberaY.get(i));
				c2.setEspecial(valorLiberacion.get(i));
			}
			if(mov<4) realizarMovimientoEspecial(4);  //se verifica si el otro personaje ya esta en un dual
			else realizarMovimientoEspecial(0);       //que se activo ahora
			c.setEspecial(0);
			
			List<Integer> indObstaculo = celdaEsp.getIndObstaculo();
			List<Celda> reemplazarObstaculo = celdaEsp.getReemplazarObstaculo();
			
			for(int i=0;i<indObstaculo.size();i++){
				Objeto bb = mapas.get(indMapaActual).getObstaculo(indObstaculo.get(i));
				int altura = bb.getAltura();
				int ancho = bb.getAncho();
				int posX = bb.getPosX();
				int posY = bb.getPosY();
				char sprite = bb.getSprite();
				Celda celdaReemplazar = reemplazarObstaculo.get(i);
				for(int j=0;j<altura;j++){
					for(int k=0;k<ancho;k++){
						Celda aux = mapaActual.getCelda(posX+j,posY+k);
						aux.setSprite(celdaReemplazar.getSprite());
						aux.setTipo(celdaReemplazar.getTipo());
					}
				}
			}
			return comandoActual;
		}else if(especial==2){ //comando dual
			CeldaEspecial celdaEsp = mapaActual.getEspecial(ind);
			int dualOpuesto = celdaEsp.getDualOpuesto();
			//System.out.println("Dual: "+dual+" Opuesto: "+dualOpuesto);
			if(dual == dualOpuesto){
				rend.mostrarComandos(celdaEsp.getComandoEspecial());
				comandoActual = celdaEsp.getComandoEspecial();
				c.setEspecial(0);
				celdaDual.setEspecial(0);
			}else{
				dual = ind;
				celdaDual = c;
				if(mov<4) tipoDual = 0;
				else tipoDual = 1;
				return "";
			}
			return comandoActual;
		}else if(especial==3){ //llego a fin de mapa
			if(mov<4){
				if(fin2) return "F";
				else fin1 = true;
			}else{
				if(fin1) return "F";
				else fin2= true;
			}
		}
		return "";
	}
	public void mostrarComando(){
		rend.mostrarComandos(comandoActual);
	}
	public String ejecutarComando(int mov){
		Celda c;
		Scanner scanner = new Scanner(System.in);
		if(mov<4) c = mapas.get(indMapaActual).getCelda(jugador1.getPosX(),jugador1.getPosY());
		else c = mapas.get(indMapaActual).getCelda(jugador2.getPosX(),jugador2.getPosY());
		int especial = c.getEspecial();
		int ind = c.getIndiceEspecial();
		if(especial == 1 || especial == -1){ //comando solo
			CeldaEspecial celdaEsp = mapaActual.getEspecial(ind);
			List<Integer> movX = celdaEsp.getDireccionX();
			List<Integer> movY = celdaEsp.getDireccionY();
			
			if(mov<4){
				for(int i=0;i<movX.size();i++){
					mapaActual.retornarCelda(jugador1.getPosX(), jugador1.getPosY(), 
							mapas.get(indMapaActual).getCelda(jugador1.getPosX(), jugador1.getPosY()));
					jugador1.setPosX(movX.get(i));
					jugador1.setPosY(movY.get(i));
					mapaActual.modificarPosJugador(0,jugador1.getPosX(),jugador1.getPosY());
					rend.dibujarMapa(mapaActual);
					scanner.nextLine();
				}
				realizarMovimientoEspecial(0);
			}else{
				for(int i=0;i<movX.size();i++){
					mapaActual.retornarCelda(jugador2.getPosX(), jugador2.getPosY(), 
							mapas.get(indMapaActual).getCelda(jugador2.getPosX(), jugador2.getPosY()));
					jugador2.setPosX(movX.get(i));
					jugador2.setPosY(movY.get(i));
					mapaActual.modificarPosJugador(1,jugador2.getPosX(),jugador2.getPosY());
					rend.dibujarMapa(mapaActual);
					scanner.nextLine();
				}
				realizarMovimientoEspecial(4);
			}
			c.setEspecial(0);
		}else if(especial==2 || especial==-2){ //comando dual
			CeldaEspecial celdaEsp = mapaActual.getEspecial(ind);
			int dualOpuesto = celdaEsp.getDualOpuesto();
			CeldaEspecial celdaEsp2 = mapaActual.getEspecial(dualOpuesto);
			List<Integer> movX1;
			List<Integer> movY1;
			List<Integer> movX2;
			List<Integer> movY2;
			if(mov<4){
				movX1 = celdaEsp.getDireccionX();
				movY1 = celdaEsp.getDireccionY();
				movX2 = celdaEsp2.getDireccionX();
				movY2 = celdaEsp2.getDireccionY();
			}else{
				movX1 = celdaEsp2.getDireccionX();
				movY1 = celdaEsp2.getDireccionY();
				movX2 = celdaEsp.getDireccionX();
				movY2 = celdaEsp.getDireccionY();
			}
			for(int i=0;i<movX1.size();i++){
				mapaActual.retornarCelda(jugador2.getPosX(), jugador2.getPosY(), 
						mapas.get(indMapaActual).getCelda(jugador2.getPosX(), jugador2.getPosY()));
				mapaActual.retornarCelda(jugador1.getPosX(), jugador1.getPosY(), 
						mapas.get(indMapaActual).getCelda(jugador1.getPosX(), jugador1.getPosY()));
				jugador1.setPosX(movX1.get(i));
				jugador1.setPosY(movY1.get(i));
				jugador2.setPosX(movX2.get(i));
				jugador2.setPosY(movY2.get(i));
				mapaActual.modificarPosJugador(0,jugador1.getPosX(),jugador1.getPosY());
				mapaActual.modificarPosJugador(1,jugador2.getPosX(),jugador2.getPosY());
				rend.dibujarMapa(mapaActual);
				scanner.nextLine();
			}
			realizarMovimientoEspecial(0);
			if(realizarMovimientoEspecial(4).equals("F")) return "F";
		}
		return "";
	}
}