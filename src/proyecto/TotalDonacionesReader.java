package proyecto;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;

public class TotalDonacionesReader extends JPanel {

    private final JButton cerrarButton;
    private final JButton totalButton;
    private final JButton historialButton;
    private final JButton informeButton;
    private final JTextArea textoInforme;
    private final JScrollPane scrollPane;
    private BufferedImage fondoImage; // La imagen de fondo

    TotalDonacionesReader(InterfazMinisterio interfaz) {
        setLayout(null);
        setBackground(Color.gray);
// Cargar la imagen de fondo
        try {
            fondoImage = ImageIO.read(new File("fondo.jpg")); // Reemplaza con la ubicación de tu imagen de fondo
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Crear un área de texto para mostrar el mensaje de agradecimiento
        cerrarButton = new JButton("Cerrar");
        cerrarButton.setBounds(200, 350, 200, 30);

        add(cerrarButton);

        // ActionListener para el botón de Cerrar
        cerrarButton.addActionListener(e -> {

            System.exit(0);
        });

        textoInforme = new JTextArea();
        scrollPane = new JScrollPane(textoInforme); // Agrega JTextArea al JScrollPane
        totalButton = new JButton("total");
        historialButton = new JButton("historial");
        informeButton = new JButton("informe");
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        scrollPane.setBounds(50, 100, 500, 200);
        totalButton.setBounds(45, 50, 170, 30);
        historialButton.setBounds(215, 50, 170, 30);
        informeButton.setBounds(385, 50, 170, 30);
        totalButton.addActionListener(e -> {
            String archivo = "donacion.txt";
            String texto = "El monto total de las donaciones es: ";
            double totalDonaciones = leerTotalDonaciones(archivo);

            if (totalDonaciones >= 0) {
                texto += " " + totalDonaciones;
            } else {
                texto = "No se pudo leer el archivo de donaciones.";
            }
            textoInforme.setText(texto);
        });
        historialButton.addActionListener(e -> {
            String registro1 = "donantes.txt";
            String registro2 = "donacion.txt";

            String texto = "El historial total de las donaciones es: \n";
            String totalDonaciones = leerHistorialDonaciones(registro1, registro2);

            textoInforme.setText(texto + totalDonaciones);
        });
        informeButton.addActionListener(e -> {
            String archivo = "donacion.txt";
            int[] contador = contarDonacionesPorEstado(archivo);
            double totalDonaciones = leerTotalDonaciones(archivo);
            String texto = "Informe de Donaciones\n\n"
                    + "Resumen de Donaciones\n"
                    + "Total Donado: " + totalDonaciones
                    + "\n\n Estado de Donaciones"
                    + "\nDonaciones Pendientes: " + contador[0]
                    + "\nDonaciones Recibidas: " + contador[1]
                    + "\nDonaciones Procesadas: " + contador[2]
                    + "\nDonaciones Rechazadas: " + contador[3];
            textoInforme.setText(texto);
        });
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
        add(totalButton);
        add(historialButton);
        add(scrollPane);
        add(informeButton);
    }

    public static double leerTotalDonaciones(String archivo) {

        // nombre+ " dono " +monto + " y se como "+ estado"
        double total = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");

                if (partes.length == 3) {
                    try {
                        double monto = Double.parseDouble(partes[0].trim());
                        total += monto;
                    } catch (NumberFormatException e) {
                        System.err.println("Error al leer el monto en la línea: " + linea);
                    }
                } else {
                    System.err.println("Formato incorrecto en la línea: " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de donaciones: " + e.getMessage());
            return -1; // Valor negativo para indicar un error
        }

        return total;
    }

    public static String leerHistorialDonaciones(String archivo1, String archivo2) {
        StringBuilder result = new StringBuilder();

        try (BufferedReader reader1 = new BufferedReader(new FileReader(archivo1));
                BufferedReader reader2 = new BufferedReader(new FileReader(archivo2))) {

            String linea1;
            String linea2;

            while ((linea1 = reader1.readLine()) != null && (linea2 = reader2.readLine()) != null) {
                String[] partes1 = linea1.split(",");
                String[] partes2 = linea2.split(",");

                if (partes1.length == 4 && partes2.length == 3) {
                    String nombre = partes1[0].trim();
                    String monto = partes2[0].trim();
                    result.append(nombre).append(" donó ").append(monto).append("\n");
                } else {
                    System.err.println("Formato incorrecto en una de las líneas.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer los archivos: " + e.getMessage());
        }

        return result.toString();
    }

    public static int[] contarDonacionesPorEstado(String archivo) {
        int[] contador = new int[4]; // Un arreglo para contar donaciones por estado: 0=pending, 1=received, 2=processed, 3=rejected

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 3) {
                    String estado = partes[2].trim();
                    switch (estado) {
                        case "pendiente":
                            contador[0]++;
                            break;
                        case "recibida":
                            contador[1]++;
                            break;
                        case "procesada":
                            contador[2]++;
                            break;
                        case "rechazada":
                            contador[3]++;
                            break;
                        default:
                            System.err.println("Estado no válido en la línea: " + linea);
                    }
                } else {
                    System.err.println("Formato incorrecto en la línea: " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de donaciones: " + e.getMessage());
        }

        return contador;
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
