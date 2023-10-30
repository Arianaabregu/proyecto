package proyecto;

import java.awt.CardLayout;
import javax.swing.*;

public class InterfazMinisterio {

    private final JFrame ventana;
    private final RegistroDonantesPanel registroDonantesPanel;
    private final RegistroDonacionesPanel registroDonacionesPanel;
    private final TotalDonacionesReader totalDonacionesReader;
    private final CardLayout cardLayout;

    public InterfazMinisterio() {
        // Inicializa la ventana principal
        ventana = new JFrame("Ministerio de la Mujer");
        cardLayout = new CardLayout();
        ventana.getContentPane().setLayout(cardLayout);
        ventana.setResizable(false);

        // Crea instancias de los paneles
        registroDonantesPanel = new RegistroDonantesPanel(this);
        registroDonacionesPanel = new RegistroDonacionesPanel(this);
        
        totalDonacionesReader = new TotalDonacionesReader(this);

        // Agrega los paneles a la ventana con identificadores
        ventana.getContentPane().add(registroDonantesPanel, "RegistroDonantes");
        ventana.getContentPane().add(registroDonacionesPanel, "RegistroDonaciones");
        ventana.getContentPane().add(totalDonacionesReader, "TotalDonaciones");
    }

    // Método para mostrar la interfaz
    public void mostrarInterfaz() {
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setBounds(100, 100, 600, 500);
        ventana.setVisible(true);
        
    }

    // Método para cambiar al panel especificado
    void cambiarPanel(String panel) {
        cardLayout.show(ventana.getContentPane(), panel);
    }
}
