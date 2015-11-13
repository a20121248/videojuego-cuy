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
			fServerSocket = new ServerSocket(9000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				fServerSocket = new ServerSocket(9001);
				v.sendMessage("127.0.0.1", 9000, "Done");
				v.portOpuesto = 9000;
				v.nuevoNivel(v.mapaActual);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
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
								v.doRobot(txtLine.charAt(0));
								/*Robot robot = new Robot();
								if(txtLine.charAt(0) == '+') robot.keyPress(KeyEvent.VK_ENTER);
								else robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(txtLine.charAt(0)));*/
							}else{
								v.portOpuesto = 9001;
								v.nuevoNivel(v.mapaActual);
								v.dibujar();
							}
							//txtMessages.append(address.toString() + ": " + 
							//					txtLine.trim() + "\n");
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
}
