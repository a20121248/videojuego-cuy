package vista;

import controlador.*;
//import modelo.*;

import java.io.IOException;
import java.util.*;

import javax.swing.SwingUtilities;

import java.lang.String;

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
