package proyecto;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class RegistroDonantesPanel extends JPanel {

    private JTextField nombreField;
    private JTextField apellidoField;
    private JTextField emailField;
    private JTextField telefonoField;
    private final JButton registrarButton;
    private final InterfazMinisterio interfaz;
    private BufferedImage fondoImage; // La imagen de fondo

    public RegistroDonantesPanel(InterfazMinisterio interfaz) {
        this.interfaz = interfaz;
        setLayout(null);
        setBackground(Color.gray);
        // Cargar la imagen de fondo
        try {
            fondoImage = ImageIO.read(new File("fondo.jpg")); // Reemplaza con la ubicación de tu imagen de fondo
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreField = new JTextField(20);
        JLabel apellidoLabel = new JLabel("Apellido:");
        apellidoField = new JTextField(20);
        JLabel emailLabel = new JLabel("Correo Electrónico:");
        emailField = new JTextField(20);
        JLabel telefonoLabel = new JLabel("Telefono:");
        telefonoField = new JTextField(20);

        registrarButton = new JButton("Registrar");

        // Establece las posiciones y tamaños de los componentes
        nombreLabel.setBounds(130, 100, 50, 30);
        nombreField.setBounds(300, 100, 200, 30);
        apellidoLabel.setBounds(130, 150, 50, 30);
        apellidoField.setBounds(300, 150, 200, 30);
        emailLabel.setBounds(130, 200, 200, 30);
        emailField.setBounds(300, 200, 200, 30);
        telefonoLabel.setBounds(130, 250, 200, 30);
        telefonoField.setBounds(300, 250, 200, 30);
        registrarButton.setBounds(200, 350, 200, 30);

        // Agrega los componentes al panel
        add(nombreLabel);
        add(nombreField);
        add(apellidoLabel);
        add(apellidoField);
        add(emailLabel);
        add(emailField);
        add(telefonoLabel);
        add(telefonoField);
        add(registrarButton);
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
        registrarButton.addActionListener((ActionEvent e) -> {
            // Procesa el registro de donantes
            String telefono = telefonoField.getText();
            try {
                int number = Integer.parseInt(telefono);
                String nombre = nombreField.getText();
                String apellido = apellidoField.getText();
                String email = emailField.getText();

                guardarDatosEnArchivo(nombre, apellido, email, telefono);
                JOptionPane.showMessageDialog(RegistroDonantesPanel.this, "Donante registrado con éxito", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                // Limpia los campos del formulario
                nombreField.setText("");
                apellidoField.setText("");
                emailField.setText("");
                telefonoField.setText("");
                // Después de registrar, cambia al siguiente panel (RegistroDonaciones)
                interfaz.cambiarPanel("RegistroDonaciones");
            } catch (NumberFormatException nfe) {
                // Manejo de error si el número de teléfono no es válido
                JOptionPane.showMessageDialog(RegistroDonantesPanel.this, "Ingrese un número telefónico válido", "", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void guardarDatosEnArchivo(String nombre, String apellido, String email, String telefono) {
        try {
            // Crea un BufferedWriter para escribir datos de manera eficiente
            try (FileWriter fileWriter = new FileWriter("donantes.txt", true);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                // Escribe los datos en el archivo
                bufferedWriter.write(nombre + "," + apellido + "," + email + "," + telefono);
                bufferedWriter.newLine(); // Agrega un salto de línea para separar los registros
            }
        } catch (IOException ex) {
            // Manejo de errores en caso de problemas al escribir en el archivo
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibuja la imagen de fondo
        if (fondoImage != null) {
            g.drawImage(fondoImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
