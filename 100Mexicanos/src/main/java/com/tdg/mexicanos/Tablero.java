/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tdg.mexicanos;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.tdg.mexicanos.sonidos.Sonidos;

/**
 *
 * @author emanu
 */
public class Tablero extends javax.swing.JPanel {
    JLabel puntos1 = new JLabel();
    JLabel puntos2 = new javax.swing.JLabel();
    JLabel puntosT = new javax.swing.JLabel();
    JLabel tiempo = new javax.swing.JLabel();
    Sonidos sonidoController = new Sonidos();
    Control control = new Control(this);
    ImageIcon rPuntos, tacheGris, tacheRojo;
    JLabel[] tachesEquipo1 = new JLabel[3];
    private JTable tablaRespuestas;
    private DefaultTableModel modeloTabla;
    public static boolean pausa = false;/////// cambiar a false para que funja :p
    public static boolean pausaTaches = false;/////// cambiar a false para que funja :p
    private int totalPuntos = 0;

    /**
     * Creates new form Tablero
     */
    public Tablero() {
        initComponents();
        // iniciarCronometro();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        this.setOpaque(true);
        this.setBackground(new java.awt.Color(0, 118, 181));
        this.setSize(width, height);

        rPuntos = new ImageIcon(getClass().getResource("/assets/images/rPuntos.png"));

        Image scaledImage = rPuntos.getImage().getScaledInstance(250, 120, Image.SCALE_SMOOTH);
        rPuntos = new ImageIcon(scaledImage);

        tacheGris = new ImageIcon(getClass().getResource("/assets/images/tacheGris.png"));
        tacheRojo = new ImageIcon(getClass().getResource("/assets/images/tacheRojo.png"));

        puntos1.setBounds(20, 20, 250, 120);
        puntos1.setFont(new java.awt.Font("Tahoma", 0, 80));
        puntos1.setForeground(new java.awt.Color(255, 255, 255));
        puntos1.setText("000");
        puntos1.setIcon(rPuntos);
        puntos1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        puntos1.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
        this.add(puntos1);

        puntos2.setBounds(width - 290, 20, 250, 120);
        puntos2.setFont(new java.awt.Font("Tahoma", 0, 80));
        puntos2.setIcon(rPuntos);
        puntos2.setForeground(new java.awt.Color(255, 255, 255));
        puntos2.setText("000");
        puntos2.setIcon(rPuntos);
        puntos2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        puntos2.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
        this.add(puntos2);

        puntosT.setBounds(width / 2 - 100, 20, 250, 120);
        puntosT.setFont(new java.awt.Font("Tahoma", 0, 150));
        puntosT.setForeground(new java.awt.Color(247, 245, 134));
        puntosT.setText("000");
        this.add(puntosT);
        modeloTabla = new DefaultTableModel(new Object[] { "RESPUESTAS", "PUNTOS" }, 0) {
            @Override
            public void addRow(Object[] rowData) {
                if (getRowCount() < 8) {
                    super.addRow(rowData);
                }
            }
        };

        tablaRespuestas = new JTable(modeloTabla) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            /*
             * @Override
             * public Component prepareRenderer(TableCellRenderer renderer, int row, int
             * column) {
             * Component component = super.prepareRenderer(renderer, row, column);
             * if (component instanceof JComponent) {
             * ((JComponent) component).setOpaque(false); // Hacer las celdas transparentes
             * }
             * return component;
             * }
             */
        };
        tablaRespuestas.setFont(new java.awt.Font("Tahoma", 1, 40));
        tablaRespuestas.setOpaque(false);
        tablaRespuestas.setBackground(new Color(0, 0, 0, 250));
        tablaRespuestas.setForeground(new Color(250, 130, 32));
        tablaRespuestas.setGridColor(Color.white);
        tablaRespuestas.setRowHeight(60);
        tablaRespuestas.setShowGrid(true);

        // Centrar texto en las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tablaRespuestas.getColumnCount(); i++) {
            tablaRespuestas.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tablaRespuestas);
        int tableWidth = 700;
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBounds((getWidth() - tableWidth) / 2, 200, tableWidth, 510);
        add(scrollPane);
        // Add taches at the bottom, centered horizontally
        int tacheSize = 250;
        int totalTachesWidth = tacheSize * 3;
        int startX = (width - totalTachesWidth) / 2;

        for (int i = 0; i < 3; i++) {
            ImageIcon resizedTacheGris = new ImageIcon(
                    tacheGris.getImage().getScaledInstance(tacheSize, tacheSize, Image.SCALE_SMOOTH));
            tachesEquipo1[i] = new JLabel(resizedTacheGris);
            tachesEquipo1[i].setBounds(startX + (i * tacheSize), height / 2 - tacheSize / 2, tacheSize, tacheSize);
            tachesEquipo1[i].setVisible(false);
            this.add(tachesEquipo1[i]);
        }

        setVisible(true);

    }

    public void actualizarTabla(Object[][] datos) {
        // Limpiar tabla actual
        modeloTabla.setRowCount(0);
        // Agregar nuevas filas
        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }
    }

    public void agregarFila(Object[] fila) {
        modeloTabla.addRow(fila);
    }

    public void agregarFilaOrdenada(Object[] fila) {
        modeloTabla.addRow(fila);
        ordenarTablaPorPuntos();
    }

    private void ordenarTablaPorPuntos() {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTabla);
        tablaRespuestas.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();
    }

    public void iniciarCronometro() {
        System.out.println("Iniciando cronometro");
        Thread cronometro = new Thread(() -> {
            int segundos = 6;
            while (!Thread.currentThread().isInterrupted()) {
                while (!pausa) {
                    try {
                        Thread.sleep(1000);
                        while (segundos >= 0) {
                            segundos--;
                            break;
                        }
                        String s = Integer.toString(segundos);
                        tiempo.setText(s);
                        sonidoController.reproducirTicTac();
                        if (segundos == 0) {
                            pausarCronometro();
                            sonidoController.reproducirFinTiempo();
                            sonidoController.detenerTicTac();
                        }
                    } catch (InterruptedException ex) {
                        System.out.println("Excepcion en hilo " + ex.getMessage());
                    }
                }
            }
        });
        cronometro.start();
    }

    public void pausarCronometro() {
        pausa = true;
    }

    public void reanudarCronometro() {
        pausa = false;
    }

    public void iniciarCronometroTaches() {
        System.out.println("Iniciando cronometro taches");
        Thread cronometroTache = new Thread(() -> {
            control.marcarTache();
            reanudarCronometroTaches();
            int segundos = 3;
            while (!Thread.currentThread().isInterrupted()) {
                while (!pausaTaches) {
                    try {
                        for (int i = 0; i < 3; i++) {
                            tachesEquipo1[i].setVisible(true);
                        }
                        while (segundos >= 0) {
                            segundos--;
                            break;
                        }
                        if (segundos == 0) {
                            for (int i = 0; i < 3; i++) {
                                tachesEquipo1[i].setVisible(false);
                            }
                            segundos = 3;
                            pausarCronometroTaches();
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        System.out.println("Excepcion en hilo " + ex.getMessage());
                    }
                }
            }
        });
        cronometroTache.start();
    }

    public void pausarCronometroTaches() {
        pausaTaches = true;
    }

    public void reanudarCronometroTaches() {
        pausaTaches = false;
    }
    // public void

    public void marcarError(int numeroError) {
        if (numeroError <= 3) {
            tachesEquipo1[numeroError - 1].setIcon(tacheRojo);
        }
    }

    public void actualizarPuntos(int puntos) {
        totalPuntos += puntos;
        puntosT.setText(String.valueOf(totalPuntos));
    }

    public void reiniciarPuntos() {
        totalPuntos = 0;
        puntosT.setText("100");
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
