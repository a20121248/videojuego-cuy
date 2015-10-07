package vista;
import javax.swing.SwingUtilities;

public class Juego {
    public static void main(String[] args) {
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
