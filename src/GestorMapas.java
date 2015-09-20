import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
	Celda celdaDual,celdaOriginal0,celdaOriginal1;
	int dual;
	int tipoDual; // 0 = jugador1, 1= jugador2
	private void anadirMapas() throws IOException{
		//16 de ancho, 12 de altura
		Mapa m;
		Objeto b;
		List<Objeto> obstaculos;
		List<Integer> direccion,liberaX,liberaY,valorLiberacion,indObstaculo;
		List<Celda> reemplazarObstaculo;
		List<CeldaEspecial> listaCeldaEsp;
		Celda Esp = new Celda(' ',TipoCelda.IMPASABLE);
		Celda S = new Celda('S',TipoCelda.TERRENO_A);
		Celda N = new Celda('N',TipoCelda.TERRENO_B);
		Celda a = new Celda('a',TipoCelda.IMPASABLE);
		Celda o = new Celda('o',TipoCelda.TERRENO_AMBOS);
		ArrayList<String> lineasMapa0 = new ArrayList<String>();
		BufferedReader inputStream = null;

		//Lectura de archivos de texto
		try {
			inputStream = new BufferedReader(new FileReader("src/MapaTutorial.txt"));
			String linea;
			linea = inputStream.readLine();
			while ((linea = inputStream.readLine()) != null) {
				lineasMapa0.add(linea);
				//System.out.println(linea);
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("El archivo no existe en la ruta especificada.");
		}
		finally {
			if (inputStream != null) inputStream.close();
		}
		//System.out.println(lineasMapa0.size());System.out.println(lineasMapa0.get(0).length());
		
		m = new Mapa(lineasMapa0.get(0).length(), lineasMapa0.size(), 2, 11, 0, 0);
		
		//Se le va agregando celdas en base a los caracteres y el tipo.
		for (int i = 0; i < lineasMapa0.size(); i++) {
			ArrayList<Celda> fila = new ArrayList<Celda>();
			for (int j = 0; j < lineasMapa0.get(0).length(); j++) {
				if(lineasMapa0.get(i).charAt(j)=='S'){
					Celda celda = new Celda(lineasMapa0.get(i).charAt(j),TipoCelda.TERRENO_A);
					//todos impasables en un inicio
					fila.add(celda);
				}else if(lineasMapa0.get(i).charAt(j)=='N') {
					Celda celda = new Celda(lineasMapa0.get(i).charAt(j),TipoCelda.TERRENO_B);
					//todos impasables en un inicio
					fila.add(celda);
				}else if(lineasMapa0.get(i).charAt(j)=='A'){
					Celda celda = new Celda('S',TipoCelda.TERRENO_A);
					//todos impasables en un inicio
					fila.add(celda);
				}else if(lineasMapa0.get(i).charAt(j)=='B'){
					Celda celda = new Celda('N',TipoCelda.TERRENO_B);
					//todos impasables en un inicio
					fila.add(celda);
				}else if(lineasMapa0.get(i).charAt(j)=='C' ||lineasMapa0.get(i).charAt(j)=='D'){
					Celda celda = new Celda(lineasMapa0.get(i).charAt(j),TipoCelda.TERRENO_AMBOS);
					fila.add(celda);
				}else{
					Celda celda = new Celda(lineasMapa0.get(i).charAt(j),TipoCelda.IMPASABLE);
					//todos impasables en un inicio
					fila.add(celda);
				}
			}
			m.addFila(fila);
		}
		
		obstaculos = new ArrayList<Objeto>();
		
		//Obstaculos visitados
		boolean[][] visit = new boolean [20][20];
		for(int i=0;i<12;i++){
			for(int j=0;j<16;j++){
				visit[i][j]=false;
			}
		}
		
		//Saca todos los obstaculos del nivel para tenerlos en el atributo obstaculos
		for(int i=0;i<12;i++){
			for(int j=0;j<16;j++){
				//System.out.println(i);System.out.println(j);
				Celda aux= m.getCelda(i,j);
				char c=aux.getSprite();
				//System.out.println(c);
				if(visit[i][j]==false && c>='a' && c<='z'){
					int contF=1,contC=1; //contador filas contador columnas
					while(i+contF<12 && m.getCelda(i+contF,j).getSprite()==c) contF++;
					while(j+contC<16 && m.getCelda(i,contC+j).getSprite()==c) contC++;
					for(int k=0;i<contF;k++){
						for(int l=0;l<contC;l++){
							visit[i+k][j+l]=true;
						}
					}
					b=new Objeto(i,j,contC,contF,c);
					obstaculos.add(b);
				}
			}
		}
		m.setObstaculos(obstaculos);
		
		
		for(int i=5;i<9;i++){
			Celda aux = m.getCelda(i,15);
			aux.setTipo(TipoCelda.TERRENO_A);
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
					aux.setTipo(TipoCelda.IMPASABLE);
					aux.setSprite(sprite);
				}
			}
		}
		listaCeldaEsp = new ArrayList<CeldaEspecial>();
		
		Celda aux = m.getCelda(2,8);
		aux.setEspecial(1);
		aux.setIndiceEspecial(0);
		aux.setSprite('C');
		CeldaEspecial celdaEsp = new AccionSimple();
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
		celdaEsp = new AccionDual();
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
		celdaEsp = new AccionDual();
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
		mapas.add(m);
		
		// Nivel 1
		lineasMapa0 = new ArrayList<String>();
		inputStream = null;
		try {
			inputStream = new BufferedReader(new FileReader("src/MapaNivel1.txt"));
			String linea;
			linea = inputStream.readLine();
			while ((linea = inputStream.readLine()) != null) {
				lineasMapa0.add(linea);
				//System.out.println(linea);
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("El archivo no existe en la ruta especificada.");
		}
		finally {
			if (inputStream != null) inputStream.close();
		}
		//System.out.println(lineasMapa0.size());System.out.println(lineasMapa0.get(0).length());
		m = new Mapa(lineasMapa0.get(0).length(), lineasMapa0.size(), 5, 9, 15, 15);		
		//m = new Mapa(16,12,5,9,15,15);
		
		for (int i = 0; i < lineasMapa0.size(); i++) {
			ArrayList<Celda> fila = new ArrayList<Celda>();
			for (int j = 0; j < lineasMapa0.get(0).length(); j++) {
				if(lineasMapa0.get(i).charAt(j)=='S'){
					Celda celda = new Celda(lineasMapa0.get(i).charAt(j),TipoCelda.TERRENO_A);
					//todos impasables en un inicio
					fila.add(celda);
				}else if(lineasMapa0.get(i).charAt(j)=='N') {
					Celda celda = new Celda(lineasMapa0.get(i).charAt(j),TipoCelda.TERRENO_B);
					//todos impasables en un inicio
					fila.add(celda);
				}else if(lineasMapa0.get(i).charAt(j)=='A'){
					Celda celda = new Celda('S',TipoCelda.TERRENO_A);
					//todos impasables en un inicio
					fila.add(celda);
				}else if(lineasMapa0.get(i).charAt(j)=='B'){
					Celda celda = new Celda('N',TipoCelda.TERRENO_B);
					//todos impasables en un inicio
					fila.add(celda);
				}else if(lineasMapa0.get(i).charAt(j)=='C' ||lineasMapa0.get(i).charAt(j)=='D'){
					Celda celda = new Celda(lineasMapa0.get(i).charAt(j),TipoCelda.TERRENO_AMBOS);
					fila.add(celda);
				}else{
					Celda celda = new Celda(lineasMapa0.get(i).charAt(j),TipoCelda.IMPASABLE);
					//todos impasables en un inicio
					fila.add(celda);
				}
			}
			m.addFila(fila);
		}
		
		obstaculos = new ArrayList<Objeto>();
		
		for(int i=0;i<12;i++){
			for(int j=0;j<16;j++){
				visit[i][j]=false;
			}
		}
		
		for(int i=0;i<12;i++){
			for(int j=0;j<16;j++){
				//System.out.println(i);System.out.println(j);
				aux= m.getCelda(i,j);
				char c=aux.getSprite();
				//System.out.println(c);
				if(visit[i][j]==false && c>='a' && c<='z'){
					int contF=1,contC=1; //contador filas contador columnas
					while(i+contF<11 && m.getCelda(i+contF,j).getSprite()==c) contF++;
					while(j+contC<16 && m.getCelda(i,contC+j).getSprite()==c) contC++;
					for(int k=0;i<contF;k++){
						for(int l=0;l<contC;l++){
							visit[i+k][j+l]=true;
						}
					}
					b=new Objeto(i,j,contC,contF,c);
					obstaculos.add(b);
				}
			}
		}
		m.setObstaculos(obstaculos);
		
		/*obstaculos = new ArrayList<Objeto>();
		b = new Objeto(8,9,2,2,'i');
		obstaculos.add(b);
		b = new Objeto(10,11,2,2,'g');
		obstaculos.add(b);
		b = new Objeto(8,3,1,4,'L');
		obstaculos.add(b);
		b = new Objeto(7,0,16,1,'p');
		obstaculos.add(b);
		m.setObstaculos(obstaculos);
		*/
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
			auxi.setTipo(TipoCelda.TERRENO_A);
			auxi.setEspecial(3);
		}
		for(int i=8;i<12;i++){
			Celda auxi = m.getCelda(i,0);
			auxi.setTipo(TipoCelda.TERRENO_B);
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
					auxi.setTipo(TipoCelda.IMPASABLE);
					auxi.setSprite(sprite);
				}
			}
		}
		listaCeldaEsp = new ArrayList<CeldaEspecial>();
		
		aux = m.getCelda(4,10);
		aux.setEspecial(1);
		aux.setIndiceEspecial(0);
		aux.setSprite('C');
		celdaEsp = new AccionSimple();
		celdaEsp.setComandoEspecial("SDQEQE");
		direccion = new ArrayList<Integer>();
		direccion.add(5);
		direccion.add(6);
		direccion.add(8);
		direccion.add(4);
		celdaEsp.setDireccionX(direccion);
		direccion = new ArrayList<Integer>();
		direccion.add(10);
		direccion.add(10);
		direccion.add(10);
		direccion.add(10);
		celdaEsp.setDireccionY(direccion);
		liberaX = new ArrayList<Integer>();
		liberaY = new ArrayList<Integer>();
		valorLiberacion = new ArrayList<Integer>();
		liberaX.add(9);
		liberaY.add(4);
		valorLiberacion.add(1);
		celdaEsp.setLiberaX(liberaX);
		celdaEsp.setLiberaY(liberaY);
		celdaEsp.setValorLiberacion(valorLiberacion);
		indObstaculo = new ArrayList<Integer>();
		reemplazarObstaculo = new ArrayList<Celda>();
		indObstaculo.add(-1);
		indObstaculo.add(-1);
		indObstaculo.add(-1);
		indObstaculo.add(0);
		reemplazarObstaculo.add(null);
		reemplazarObstaculo.add(null);
		reemplazarObstaculo.add(null);
		reemplazarObstaculo.add(N.copy());
		celdaEsp.setIndObstaculo(indObstaculo);
		celdaEsp.setReemplazarObstaculo(reemplazarObstaculo);
		
		listaCeldaEsp.add(celdaEsp);
		
		aux = m.getCelda(9,4);
		aux.setEspecial(-1);
		aux.setIndiceEspecial(1);
		aux.setSprite('C');
		celdaEsp = new AccionSimple();
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
		mapas.add(m);
		
		//Nivel 2
		
		m = new Mapa(16,12,5,10,0,0);
		obstaculos = new ArrayList<Objeto>();
		b = new Objeto(7,0,9,1,'g');
		obstaculos.add(b);
		b = new Objeto(10,5,2,2,'h');
		obstaculos.add(b);
		b = new Objeto(3,4,1,4,'t');
		obstaculos.add(b);
		b = new Objeto(8,3,1,3,'m');
		obstaculos.add(b);
		b = new Objeto(3,9,7,9,'L');
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
		for(int i=8;i<12;i++){
			List<Celda> l = new ArrayList<Celda>();
			for(int j=0;j<16;j++){
				Celda c = N.copy();
				l.add(c);
			}
			m.addFila(l);
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
					auxi.setTipo(TipoCelda.IMPASABLE);
					auxi.setSprite(sprite);
				}
			}
		}
		
		for(int j=12;j<16;j++){
			Celda aux2 = m.getCelda(6,j);
			aux2.setTipo(TipoCelda.TERRENO_A);
			aux2.setEspecial(0);
			aux2.setSprite('o');
			aux2 = m.getCelda(7,j);
			aux2.setTipo(TipoCelda.TERRENO_B);
			aux2.setEspecial(0);
			aux2.setSprite('o');
		}
		
		Celda aux2 = m.getCelda(6, 15);
		aux2.setEspecial(3);
		aux2 = m.getCelda(7,15);
		aux2.setEspecial(3);
		
		listaCeldaEsp = new ArrayList<CeldaEspecial>();
		
		aux = m.getCelda(4,2);
		aux.setEspecial(1);
		aux.setIndiceEspecial(0);
		aux.setSprite('C');
		celdaEsp = new AccionSimple();
		celdaEsp.setComandoEspecial("SDEWD");
		direccion = new ArrayList<Integer>();
		direccion.add(4);
		direccion.add(4);
		direccion.add(4);
		celdaEsp.setDireccionX(direccion);
		direccion = new ArrayList<Integer>();
		direccion.add(3);
		direccion.add(4);
		direccion.add(5);
		celdaEsp.setDireccionY(direccion);
		
		liberaX = new ArrayList<Integer>();
		liberaY = new ArrayList<Integer>();
		valorLiberacion = new ArrayList<Integer>();
		liberaX.add(6);
		liberaY.add(8);
		liberaX.add(8);
		liberaY.add(8);
		valorLiberacion.add(2);
		valorLiberacion.add(2);
		celdaEsp.setLiberaX(liberaX);
		celdaEsp.setLiberaY(liberaY);
		celdaEsp.setValorLiberacion(valorLiberacion);
		listaCeldaEsp.add(celdaEsp);
		
		aux = m.getCelda(6,8);
		aux.setEspecial(-2);
		aux.setIndiceEspecial(1);
		aux.setSprite('D');
		celdaEsp = new AccionDual();
		celdaEsp.setComandoEspecial("SIQEUOKLSD");
		celdaEsp.setDualOpuesto(2);
		direccion = new ArrayList<Integer>();
		direccion.add(6);
		direccion.add(5);
		direccion.add(6);
		celdaEsp.setDireccionX(direccion);
		direccion = new ArrayList<Integer>();
		direccion.add(9);
		direccion.add(12);
		direccion.add(12);
		celdaEsp.setDireccionY(direccion);
		listaCeldaEsp.add(celdaEsp);
		
		aux = m.getCelda(8,8);
		aux.setEspecial(-2);
		aux.setIndiceEspecial(2);
		aux.setSprite('D');
		celdaEsp = new AccionDual();
		celdaEsp.setComandoEspecial("SIQEUOKLSD");
		celdaEsp.setDualOpuesto(1);
		direccion = new ArrayList<Integer>();
		direccion.add(8);
		direccion.add(8);
		direccion.add(7);
		celdaEsp.setDireccionX(direccion);
		direccion = new ArrayList<Integer>();
		direccion.add(9);
		direccion.add(12);
		direccion.add(12);
		celdaEsp.setDireccionY(direccion);
		listaCeldaEsp.add(celdaEsp);
		
		m.setListaEspecial(listaCeldaEsp);
		mapas.add(m);
			
	}
	public GestorMapas(){
		mapas = new ArrayList<Mapa>();
		rend = new Renderizador();
		try {
			anadirMapas();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cant = mapas.size();
		fin1 = false;
		fin2 = false;
	}
	
	public void cargarMapa(int index){
		if ( index>cant ) return;
		mapaActual = mapas.get(index).copy();
		indMapaActual = index;
		
		jugador1 = new PersonajePrincipal(mapaActual.posX1,mapaActual.posY1);
		Celda c = mapaActual.getCelda(jugador1.posX,jugador1.posY);
		celdaOriginal0 = c.copy();
		c.setJugador(0);
		c.setSprite('A');
		jugador2 = new PersonajePrincipal(mapaActual.posX2,mapaActual.posY2);
		c = mapaActual.getCelda(jugador2.posX,jugador2.posY);
		celdaOriginal1 = c.copy();
		c.setJugador(1);
		c.setSprite('B');
		rend.dibujarMapa(mapaActual);	
	}
	public void realizarMovimiento(int mov){
		
		Celda c,c2=null;
		int posXAux,posYAux;
		if ( mov==-1 ) return;
		
		if ( mov<4 ){
			c = celdaOriginal0;
			posXAux = jugador1.getPosX(); posYAux = jugador1.getPosY();
		}else{
			c = celdaOriginal1;
			posXAux = jugador2.getPosX(); posYAux = jugador2.getPosY();
		}
		if ( mov==0 ) c2 = mapaActual.getCelda(jugador1.posX-1,jugador1.posY);
		else if ( mov==1 ) c2 = mapaActual.getCelda(jugador1.posX,jugador1.posY-1);
		else if ( mov==2 ) c2 = mapaActual.getCelda(jugador1.posX+1,jugador1.posY);
		else if ( mov==3 ) c2 = mapaActual.getCelda(jugador1.posX,jugador1.posY+1);
		else if ( mov==4 ) c2 = mapaActual.getCelda(jugador2.posX-1,jugador2.posY);
		else if ( mov==5 ) c2 = mapaActual.getCelda(jugador2.posX,jugador2.posY-1);
		else if ( mov==6 ) c2 = mapaActual.getCelda(jugador2.posX+1,jugador2.posY);
		else if ( mov==7 ) c2 = mapaActual.getCelda(jugador2.posX,jugador2.posY+1);
		if ( c2!=null ) c2 = c2.copy();
		boolean aux=true;
		if ( mov==0 ) aux = jugador1.verificarPosX(1,0,mapaActual.getAltura(),mapaActual,TipoCelda.TERRENO_A);
		else if ( mov==1 ) aux = jugador1.verificarPosY(1,0,mapaActual.getAncho(),mapaActual,TipoCelda.TERRENO_A);
		else if ( mov==2 ) aux = jugador1.verificarPosX(0,0,mapaActual.getAltura(),mapaActual,TipoCelda.TERRENO_A);
		else if ( mov==3 ) aux = jugador1.verificarPosY(0,0,mapaActual.getAncho(),mapaActual,TipoCelda.TERRENO_A);
		else if ( mov==4 ) aux = jugador2.verificarPosX(1,0,mapaActual.getAltura(),mapaActual,TipoCelda.TERRENO_B);
		else if ( mov==5 ) aux = jugador2.verificarPosY(1,0,mapaActual.getAncho(),mapaActual,TipoCelda.TERRENO_B);
		else if ( mov==6 ) aux = jugador2.verificarPosX(0,0,mapaActual.getAltura(),mapaActual,TipoCelda.TERRENO_B);
		else if ( mov==7 ) aux = jugador2.verificarPosY(0,0,mapaActual.getAncho(),mapaActual,TipoCelda.TERRENO_B);
		if ( !aux ) return;
		
		mapaActual.retornarCelda(posXAux, posYAux, c);
		
		if ( mov<4 ){
			celdaOriginal0 = c2.copy();
			fin1 = false;
			if ( tipoDual == 0 ) dual=-1;
			mapaActual.modificarPosJugador(0,jugador1.getPosX(),jugador1.getPosY());
		}else{
			celdaOriginal1 = c2.copy();
			fin2 = false;
			if ( tipoDual == 1 ) dual=-1;
			mapaActual.modificarPosJugador(1,jugador2.getPosX(),jugador2.getPosY());
		}
		
		rend.dibujarMapa(mapaActual);
	}
	public String realizarMovimientoEspecial(int mov){
		Celda c;
		if ( mov==-1 ) return "";
		if ( mov<4 ) c = mapaActual.getCelda(jugador1.getPosX(),jugador1.getPosY());
		else c = mapaActual.getCelda(jugador2.getPosX(),jugador2.getPosY());
		int especial = c.getEspecial();
		int ind = c.getIndiceEspecial();
		if ( especial == 0 ) return "";
		else if ( especial == 1 ){ //comando solo
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
			if ( mov<4 ) realizarMovimientoEspecial(4);  //se verifica si el otro personaje ya esta en un dual
			else realizarMovimientoEspecial(0);       //que se activo ahora
			c.setEspecial(0);
			if ( mov<4 ) celdaOriginal0.setEspecial(0);
			else celdaOriginal1.setEspecial(0);
			
			return comandoActual;
		}else if ( especial==2 ){ //comando dual
			CeldaEspecial celdaEsp = mapaActual.getEspecial(ind);
			int dualOpuesto = celdaEsp.getDualOpuesto();
			if ( dual == dualOpuesto ){
				rend.mostrarComandos(celdaEsp.getComandoEspecial());
				comandoActual = celdaEsp.getComandoEspecial();
				celdaOriginal0.setEspecial(0);
				celdaOriginal1.setEspecial(0);
				c.setEspecial(0);
				celdaDual.setEspecial(0);
			}else{
				dual = ind;
				celdaDual = c;
				if ( mov<4 ) tipoDual = 0;
				else tipoDual = 1;
				return "";
			}
			return comandoActual;
		}else if ( especial==3 ){ //llego a fin de mapa
			if ( mov<4 ){
				if ( fin2 ) return "F";
				else fin1 = true;
			}else{
				if ( fin1 ) return "F";
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
		if ( mov<4 ) c = mapas.get(indMapaActual).getCelda(jugador1.getPosX(),jugador1.getPosY());
		else c = mapas.get(indMapaActual).getCelda(jugador2.getPosX(),jugador2.getPosY());
		int especial = c.getEspecial();
		int ind = c.getIndiceEspecial();
		CeldaEspecial celdaEspp = mapaActual.getEspecial(ind);
		celdaEspp.ejecutarEspecial(mov, mapaActual, jugador1, jugador2, celdaOriginal0, celdaOriginal1, rend);
		
		if(realizarMovimientoEspecial(0).equals("F")) return "F";
		if(realizarMovimientoEspecial(4).equals("F")) return "F";
		
		return "";
	}
}