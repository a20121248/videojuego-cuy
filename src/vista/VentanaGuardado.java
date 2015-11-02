package vista;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import controlador.*;
import modelo.*;
public class VentanaGuardado extends JDialog{
	JTextField textField;
	GestorMapas gest;
	String texto;
	public VentanaGuardado(JFrame padre,boolean esModal,GestorMapas gestor,char c){		
		super(padre,esModal);
		gest=gestor;
		setSize(new Dimension(600, 300));
		Container cp= getContentPane();
		setResizable(true);
		cp.setLayout(null);
		JLabel lblIngreseElNombre = new JLabel("Ingrese el nombre del archivo:");
		lblIngreseElNombre.setHorizontalAlignment(SwingConstants.CENTER);
		lblIngreseElNombre.setBounds(222, 99, 178, 14);
		cp.add(lblIngreseElNombre);		
		textField = new JTextField();
		textField.setBounds(222, 124, 195, 20);
		cp.add(textField);
		if(c=='G') texto="Guardar juego";
		else texto="Cargar juego";
		JButton btnGuardarArchivo = new JButton(texto);
		btnGuardarArchivo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(textField.getText().equals("")!=true ){
					String nombarchivo= textField.getText();
					if(c=='G'){ //Guardando						
						XStream xs = new XStream(new DomDriver());
						xs.omitField(Celda.class, "img");
						xs.omitField(GestorMapas.class, "mapas");
						xs.omitField(Objeto.class, "img");
						// 1. Escribir el archivoFileWriter
						FileWriter fw = null;
						try {
							fw = new FileWriter(nombarchivo+".xml");
							fw.write(xs.toXML(gest));
							fw.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}				
						JOptionPane.showMessageDialog(null, "Juego Guardado", 
								"Título del Mensaje", JOptionPane.INFORMATION_MESSAGE);
						
					}else{ //Caso de carga
						try {
							XStream xs = new XStream(new DomDriver());
							Ventana.gestor = (GestorMapas) xs.fromXML(new FileInputStream(nombarchivo+".xml"));							
							
						} catch (IOException i) {
							JOptionPane.showMessageDialog(null, "No se encontró el archivo", 
									"Título del Mensaje", JOptionPane.INFORMATION_MESSAGE);
							Ventana.cargo=false;
							
						}
					}
					dispose();
				}else{
					JOptionPane.showMessageDialog(null, "Ingrese todos los datos", 
							"Título del Mensaje", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnGuardarArchivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnGuardarArchivo.setBounds(247, 171, 134, 23);
		cp.add(btnGuardarArchivo);	
		
	}
	
}
