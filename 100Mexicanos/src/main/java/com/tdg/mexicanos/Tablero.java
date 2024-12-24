/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tdg.mexicanos;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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
    ImageIcon rPuntos, tacheGris, tacheRojo;
    JLabel[] tachesEquipo1 = new JLabel[3];
    JLabel[] tachesEquipo2 = new JLabel[3];
    private JTable tablaRespuestas;
    private DefaultTableModel modeloTabla;
    public static boolean pausa = true;///////cambiar a false para que funja :p

    /**
     * Creates new form Tablero
     */
    public Tablero() {
        initComponents();
        iniciarCronometro();

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
        // tiempo.setBounds(width / 2, 20, 250, 120);
        // tiempo.setFont(new java.awt.Font("Tahoma", 0, 150));
        // tiempo.setForeground(new java.awt.Color(224, 118, 47));
        // this.add(tiempo);

        puntos1.setBounds(20, 20, 250, 120);
        puntos1.setFont(new java.awt.Font("Tahoma", 0, 80));
        puntos1.setForeground(new java.awt.Color(255, 255, 255));
        puntos1.setText("000");
        puntos1.setIcon(rPuntos);
        puntos1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        puntos1.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
        this.add(puntos1);

        for (int i = 0; i < tachesEquipo1.length; i++) {
            ImageIcon resizedTacheGris = new ImageIcon(
            tacheGris.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH));
            tachesEquipo1[i] = new JLabel(resizedTacheGris);
            tachesEquipo1[i].setBounds(20, 90 + (i * 260), 250, 250);
            this.add(tachesEquipo1[i]);
        }

        puntos2.setBounds(width - 290, 20, 250, 120);
        puntos2.setFont(new java.awt.Font("Tahoma", 0, 80));
        puntos2.setIcon(rPuntos);
        puntos2.setForeground(new java.awt.Color(255, 255, 255));
        puntos2.setText("000");
        puntos2.setIcon(rPuntos);
        puntos2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        puntos2.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
        this.add(puntos2);

        for (int i = 0; i < tachesEquipo2.length; i++) {
            ImageIcon resizedTacheGris = new ImageIcon(
            tacheGris.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH));
            tachesEquipo2[i] = new JLabel(resizedTacheGris);
            tachesEquipo2[i].setBounds(width - 290, 90 + (i * 260), 250, 250);
            this.add(tachesEquipo2[i]);
        }

        puntosT.setBounds(width / 2 - 100, 20, 250, 120);
        puntosT.setFont(new java.awt.Font("Tahoma", 0, 150));
        puntosT.setForeground(new java.awt.Color(247, 245, 134));
        puntosT.setText("100");
        this.add(puntosT);

        modeloTabla = new DefaultTableModel(new Object[]{"RESPUESTAS", "PUNTOS"}, 0);
        tablaRespuestas = new JTable(modeloTabla);
        tablaRespuestas.setFont(new java.awt.Font("Tahoma", 0, 20));
        tablaRespuestas.setRowHeight(30);
        tablaRespuestas.setShowGrid(true);

        // ScrollPane para la tabla
        JScrollPane scrollPane = new JScrollPane(tablaRespuestas);
        int tableWidth = 700;
        scrollPane.setBounds((getWidth() - tableWidth) / 2, 300, tableWidth, 270);
        add(scrollPane);

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

    public void iniciarCronometro() {
        Thread cronometro = new Thread(() -> {
            int segundos = 11;
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

    //public void 

    public void marcarError(int equipo, int numeroError) {
        if (equipo == 1) {
            tachesEquipo1[numeroError-1].setIcon(tacheRojo);
        } else if (equipo == 2) {
            tachesEquipo2[numeroError-1].setIcon(tacheRojo);
        }
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
