package vista;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class HiloConexion extends Thread{
	
	private ServerSocket fServerSocket;
	private BufferedInputStream input;
	private Ventana v;
	
	public HiloConexion(Ventana v){
		super();
		try {
			this.v = v;
			fServerSocket = new ServerSocket(9010);
		} catch (IOException e) {
			try {
				fServerSocket = new ServerSocket(9011);
				v.sendMessage("127.0.0.1", 9010, "Done");
				v.portOpuesto = 9010;
				v.nuevoNivel(v.mapaActual);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	public void run(){
		Socket theClient;
		while (true){
			if (fServerSocket == null) return;
			try {
				theClient = fServerSocket.accept();
				InetAddress address = theClient.getInetAddress();
				
				input = new BufferedInputStream(theClient.getInputStream());
				
				byte[] buffer = new byte[1024];
				int len = input.read(buffer);
				
				if (len>0)
					try {
						{
							String txtLine = new String(buffer);
							System.out.print(v.portOpuesto);
							System.out.print(":");
							System.out.println(txtLine);
							if(!txtLine.contains("Done")){
								v.keyPressedMultiplayer(txtLine.charAt(0));
							}else{
								v.portOpuesto = 9011;
								v.nuevoNivel(v.mapaActual);
								v.dibujar();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
}
