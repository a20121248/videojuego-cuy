package vista;
import javax.swing.SwingUtilities;

public class Juego {
    public static void main(String[] args) {
    	//Ahora se va a poder correr la ventan en el thread Event Dispatch
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Ventana v = new Ventana();
                v.setVisible(true);
                v.dibujarExtra();
            }
        });
    }
}
