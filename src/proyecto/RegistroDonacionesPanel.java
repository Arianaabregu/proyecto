package proyecto;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;
import javax.imageio.ImageIO;


public class RegistroDonacionesPanel extends JPanel {

    private JTextField montoField;
    private JTextField descripcionField;
    private final JButton registrarButton;
    private final InterfazMinisterio interfaz;
    private BufferedImage fondoImage; // La imagen de fondo

    public RegistroDonacionesPanel(InterfazMinisterio interfaz) {
        this.interfaz = interfaz;
        setLayout(null);
        setBackground(Color.gray);

        // Cargar la imagen de fondo
        try {
            fondoImage = ImageIO.read(new File("fondo.jpg")); // Reemplaza con la ubicación de tu imagen de fondo
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel montoLabel = new JLabel("Monto de la donación:");
        montoField = new JTextField(20);
        JLabel descripcionLabel = new JLabel("Descripción:");
        descripcionField = new JTextField(20);
        registrarButton = new JButton("Registrar Donación");

        montoLabel.setBounds(130, 100, 200, 30);
        montoField.setBounds(300, 100, 200, 30);
        descripcionLabel.setBounds(130, 150, 150, 30);
        descripcionField.setBounds(300, 150, 200, 30);
        registrarButton.setBounds(200, 350, 200, 30);
        
        try {
            Image imagenOriginal = ImageIO.read(new File("logo.png")); // Reemplaza con la ubicación de tu imagen

            // Escalar la imagen al tamaño deseado (en este caso, 20x20)
            Image imagenEscalada = imagenOriginal.getScaledInstance(190, 50, Image.SCALE_SMOOTH);

            ImageIcon icono = new ImageIcon(imagenEscalada);
            JLabel imagenLabel = new JLabel(icono);
            imagenLabel.setBounds(400, 5, 190, 50);
            add(imagenLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        add(montoLabel);
        add(montoField);
        add(descripcionLabel);
        add(descripcionField);
        add(registrarButton);

        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String monto = montoField.getText();
                try {
                    double number = Double.parseDouble(monto);
                    String descripcion = descripcionField.getText();

                    JOptionPane.showMessageDialog(RegistroDonacionesPanel.this, "Donación registrada con éxito", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                    montoField.setText("");
                    descripcionField.setText("");

                    guardarDatosEnArchivo(monto, descripcion);

                    // Realizar alguna acción, como guardar los datos de la donación en una base de datos o mostrar un mensaje de confirmación.
                    interfaz.cambiarPanel("TotalDonaciones");
                    JOptionPane.showMessageDialog(RegistroDonacionesPanel.this, "Gracias por la donación", "", JOptionPane.INFORMATION_MESSAGE);

                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(RegistroDonacionesPanel.this, "Ingrese un monto numérico válido", "", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private void guardarDatosEnArchivo(String monto, String descripcion) {
        try {
            FileWriter fileWriter = new FileWriter("donacion.txt", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Crear una instancia de Random
            Random rand = new Random();

            // Generar un número aleatorio entre 1 y 100 para representar las probabilidades
            int probabilidad = rand.nextInt(100) + 1;
            String estado;

            if (probabilidad <= 50) {
                estado = "recibida";
            } else if (probabilidad <= 80) {
                estado = "procesada";
            } else if (probabilidad <= 95) {
                estado = "pendiente";
            } else {
                estado = "rechazada";
            }

            bufferedWriter.write(monto + "," + descripcion + "," + estado);
            bufferedWriter.newLine();

            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException ex) {
            // Manejo de errores en caso de problemas al escribir en el archivo
            // Puedes agregar un mensaje de error aquí si es necesario
        }
    }
    // Agregar la imagen en una esquina

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibuja la imagen de fondo
        if (fondoImage != null) {
            g.drawImage(fondoImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
