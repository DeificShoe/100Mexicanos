/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tdg.mexicanos;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.tdg.mexicanos.sonidos.Sonidos;
/**
 *
 * @author emanu
 */
public class Control extends javax.swing.JPanel {
    String equipoJugador = "1";
    Button tachar;
    private JTable tablaControl;
    private DefaultTableModel modeloTablaControl;
    private Tablero tablero;
    private int tacheCount = 0;
    Sonidos sonidoController = new Sonidos();

    /**
     * Creates new form Control
     */
    public Control(Tablero tablero) {
        initComponents();
        this.tablero = tablero;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        this.setSize(width, height);
        this.setBackground(new java.awt.Color(61, 61, 61));

        // Crear la tabla editable con columnas "Respuesta" y "Puntos"
        modeloTablaControl = new DefaultTableModel(new Object[] { "RESPUESTAS", "PUNTOS" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) {
                    return Integer.class;
                }
                return String.class;
            }

            @Override
            public void addRow(Object[] rowData) {
                if (getRowCount() < 8) {
                    super.addRow(rowData);
                }
            }
        };

        // Add empty rows to fill the table up to 8 rows
        for (int i = 0; i < 8; i++) {
            modeloTablaControl.addRow(new Object[] { "", 0 });
        }

        tablaControl = new JTable(modeloTablaControl);
        tablaControl.setFont(new java.awt.Font("Tahoma", 0, 28));
        tablaControl.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(tablaControl);
        int tableWidth = 700;
        scrollPane.setBounds((getWidth() - tableWidth) / 2, 50, tableWidth, 270);
        add(scrollPane);

        // Add "Asignar" buttons next to each row
        for (int i = 0; i < modeloTablaControl.getRowCount(); i++) {
            JButton btnAsignarFila = new JButton("Asignar");
            int row = i; // Capture the current row index
            btnAsignarFila.setBounds((getWidth() - tableWidth) / 2 + tableWidth + 10, 70 + (i * 30), 100, 30);
            btnAsignarFila.addActionListener(e -> {
                enviarPuntos(row);
                sonidoController.reproducirRCorrect();
            });
            this.add(btnAsignarFila);
        }

        // Botón para guardar datos
        JButton btnGuardar = new JButton("Guardar Datos");
        btnGuardar.setBounds(width / 2 - 100, 400, 200, 50);
        btnGuardar.addActionListener(e -> guardarDatos());
        this.add(btnGuardar);

        // Botón para cargar datos
        JButton btnCargar = new JButton("Cargar Datos");
        btnCargar.setBounds(width / 2 - 100, 470, 200, 50);
        btnCargar.addActionListener(e -> cargarDatos());
        this.add(btnCargar);

        // Botón para marcar taches
        JButton btnMarcarTache = new JButton("Marcar Tache");
        btnMarcarTache.setBounds(width / 2 - 100, 540, 200, 50);
        btnMarcarTache.addActionListener(e -> marcarTache());
        this.add(btnMarcarTache);

        // Botones para asignar puntos a cada equipo
        JButton asignar1 = new JButton("dar puntos eq.1");
        asignar1.setBounds(width / 2 - 350, 400, 200, 50);
        asignar1.addActionListener(e -> darPuntos(1));
        this.add(asignar1);

        JButton asignar2 = new JButton("dar puntos eq.2");
        asignar2.setBounds(width / 2 + 150, 400, 200, 50);
        asignar2.addActionListener(e -> darPuntos(2));
        this.add(asignar2);

        this.setVisible(true);
    }

    private void darPuntos(int equipo) {
        if (equipo == 1) {
            tablero.puntos1.setText(tablero.puntosT.getText());
        } else if (equipo == 2) {
            tablero.puntos2.setText(tablero.puntosT.getText());
        }
        tablero.reiniciarPuntos();
        sonidoController.reproducirWinRonda();
    }


    private void guardarDatos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (int i = 0; i < modeloTablaControl.getRowCount(); i++) {
                    writer.write(modeloTablaControl.getValueAt(i, 0) + "," + modeloTablaControl.getValueAt(i, 1));
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void cargarDatos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                modeloTablaControl.setRowCount(0); // Clear existing data
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    modeloTablaControl.addRow(new Object[] { data[0], Integer.parseInt(data[1]) });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void enviarPuntos(int row) {
        Object respuesta = modeloTablaControl.getValueAt(row, 0);
        Object puntos = modeloTablaControl.getValueAt(row, 1);
        if (respuesta != null && puntos != null) {
            int puntosInt = (int) puntos;
            tablero.actualizarPuntos(puntosInt);
            tablero.agregarFilaOrdenada(new Object[] { respuesta, puntos });
        }
    }

    private void marcarTache() {
        if (tacheCount < 3) {
            tablero.marcarError(1, tacheCount + 1);
            tacheCount++;
        }
        sonidoController.reproducirError();
    }

    public ActionListener obtenerDatos() {
        return e -> {
            innitTabla();
        };
    }

    public void innitTabla() {
        int rowCount = modeloTablaControl.getRowCount();
        Object[][] datos = new Object[rowCount][2];
        for (int i = 0; i < rowCount; i++) {
            datos[i][0] = modeloTablaControl.getValueAt(i, 0);
            datos[i][1] = modeloTablaControl.getValueAt(i, 1);
        }
        // Enviar datos al Tablero
        tablero.actualizarTabla(datos);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
