/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tdg.mexicanos;

import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

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
    public int tacheCount = 0;
    private List<List<String[]>> grupos; // Lista para almacenar los grupos
    private int grupoActual = 0;
    private Label pregunta = new Label();
    private JButton finTiempo = new JButton();
    Sonidos sonidoController = new Sonidos();
    private JTable tablaTablero = Tablero.getTablaRespuestas();
    private int[] highlightedRow = new int[8]; // Usar un arreglo para que sea mutable

    DefaultTableCellRenderer renderer;

    /**
     * Creates new form Control
     */
    public Control(Tablero tablero) {
        initComponents();
        initHigligthRow();
        this.tablero = tablero;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        this.setSize(width, height);
        this.setBackground(new java.awt.Color(61, 61, 61));

        // configuracion label de la pregunta
        pregunta.setBounds(width / 2 - 200, 10, 400, 30);
        pregunta.setAlignment(Label.CENTER);
        pregunta.setBackground(new Color(0, 118, 181));
        pregunta.setForeground(Color.WHITE);
        pregunta.setFont(new java.awt.Font("Tahoma", 0, 20));
        this.add(pregunta);

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
            JButton btnAsignarFila = new BotonEstilizado("Asignar");
            int row = i; // Capture the current row index
            btnAsignarFila.setBounds((getWidth() - tableWidth) / 2 + tableWidth + 10, 70 + (i * 30), 100, 30);
            btnAsignarFila.addActionListener(e -> {
                enviarPuntos(row);
            });
            this.add(btnAsignarFila);
        }

        // Botón para guardar datos
        JButton btnGuardar = new BotonEstilizado("Guardar Datos");
        btnGuardar.setBounds(width / 2 - 100, 400, 200, 50);
        btnGuardar.addActionListener(e -> guardarDatos());
        this.add(btnGuardar);

        // Botón para cargar datos
        JButton btnCargar = new BotonEstilizado("Cargar Datos");
        btnCargar.setBounds(width / 2 - 100, 470, 200, 50);
        btnCargar.addActionListener(e -> cargarDatos());
        this.add(btnCargar);

        // boton para retrocedeer pregunta
        JButton btnRetroceder = new BotonEstilizado("pregunta ant.");
        btnRetroceder.setBounds(width / 2 - 350, 470, 200, 50);
        btnRetroceder.addActionListener(e -> cargarGrupoAnterior());
        this.add(btnRetroceder);

        // Botón para avanzar pregunta
        JButton btnAvanzar = new BotonEstilizado("sig. pregunta");
        btnAvanzar.setBounds(width / 2 + 150, 470, 200, 50);
        btnAvanzar.addActionListener(e -> cargarSiguienteGrupo());
        this.add(btnAvanzar);

        // Botón para marcar taches
        JButton btnMarcarTache = new BotonEstilizado("Marcar Tache");
        btnMarcarTache.setBounds(width / 2 - 100, 540, 200, 50);
        btnMarcarTache.addActionListener(e -> {
            tablero.iniciarCronometroTaches();
        });
        this.add(btnMarcarTache);

        // Botón para sonar la campana
        JButton btnFinTiempo = new BotonEstilizado("poco tiempo");
        btnFinTiempo.setBounds(width / 2 - 350, 540, 200, 50);
        btnFinTiempo.addActionListener(e -> tablero.iniciarCronometro());
        this.add(btnFinTiempo);

        // Botones para asignar puntos a cada equipo
        JButton asignar1 = new BotonEstilizado("dar puntos eq.1");
        asignar1.setBounds(width / 2 - 350, 400, 200, 50);
        asignar1.addActionListener(e -> darPuntos(1));
        this.add(asignar1);

        JButton asignar2 = new BotonEstilizado("dar puntos eq.2");
        asignar2.setBounds(width / 2 + 150, 400, 200, 50);
        asignar2.addActionListener(e -> darPuntos(2));
        this.add(asignar2);

        renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Cambiar color de texto dinámicamente
                if (row == highlightedRow[row]) {
                    c.setForeground(new Color(250, 130, 32));
                } else {
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        };

        this.setVisible(true);
    }

    private void initHigligthRow() {
        for (int i = 0; i < highlightedRow.length; i++) {
            highlightedRow[i] = -1;
        }
    }

    public class BotonEstilizado extends JButton {

        private boolean isHovered = false; // Para manejar el estado de hover

        public BotonEstilizado(String texto) {
            super(texto);
            setForeground(Color.WHITE); // Color del texto
            setFont(new Font("Arial", Font.ITALIC, 20)); // Fuente del texto
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Borde negro
            setFocusPainted(false); // Quitar el enfoque
            setContentAreaFilled(false); // Permitir personalizar el fondo

            // Listener para detectar el hover
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint(); // Actualizar la pintura
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint(); // Actualizar la pintura
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Colores del degradado
            Color colorInicio = isHovered ? new Color(34, 133, 199) : new Color(94, 94, 94); // Gris claro en hover
            Color colorFin = isHovered ? new Color(94, 94, 94) : new Color(34, 133, 199); // Azul claro en hover

            // Crear el degradado vertical
            GradientPaint gradient = new GradientPaint(0, 0, colorInicio, 0, getHeight(), colorFin);

            // Dibujar el fondo con el degradado
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());

            super.paintComponent(g); // Pintar el contenido (texto)
        }
    }

    private void darPuntos(int equipo) {

        if (equipo == 1) {
            int puntos1Valor = Integer.parseInt(tablero.puntos1.getText());
            int puntosTValor = Integer.parseInt(tablero.puntosT.getText());

            int sumaPuntos = puntos1Valor + puntosTValor;

            tablero.puntos1.setText(String.valueOf(sumaPuntos));
            // tablero.puntos1.setText(tablero.puntosT.getText());
        } else if (equipo == 2) {
            int puntos2Valor = Integer.parseInt(tablero.puntos2.getText());
            int puntosTValor = Integer.parseInt(tablero.puntosT.getText());

            int sumaPuntos = puntos2Valor + puntosTValor;

            tablero.puntos2.setText(String.valueOf(sumaPuntos));
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

    /*
     * private void cargarDatos() {
     * JFileChooser fileChooser = new JFileChooser();
     * fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
     * int option = fileChooser.showOpenDialog(this);
     * if (option == JFileChooser.APPROVE_OPTION) {
     * File file = fileChooser.getSelectedFile();
     * try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
     * String line;
     * modeloTablaControl.setRowCount(0); // Clear existing data
     * while ((line = reader.readLine()) != null) {
     * String[] data = line.split(",");
     * modeloTablaControl.addRow(new Object[] { data[0], Integer.parseInt(data[1])
     * });
     * }
     * } catch (IOException e) {
     * e.printStackTrace();
     * }
     * }
     * }
     */

    private void cargarDatos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            grupos = new ArrayList<>(); // Reiniciar la lista de grupos
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                List<String[]> grupoTemporal = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("(")) {
                        // Nuevo grupo detectado o comentario
                        if (!grupoTemporal.isEmpty()) {
                            grupos.add(grupoTemporal);
                            grupoTemporal = new ArrayList<>();
                        }
                    } else {
                        // Agregar datos al grupo actual
                        String[] data = line.split(",");
                        grupoTemporal.add(new String[] { data[0], data[1] });
                    }
                }
                // Agregar el último grupo si hay datos pendientes
                if (!grupoTemporal.isEmpty()) {
                    grupos.add(grupoTemporal);
                }
                grupoActual = 0; // Reiniciar el índice

                // Cargar automáticamente el primer grupo y actualizar la etiqueta
                if (!grupos.isEmpty()) {
                    cargarGrupoActual();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ordenarTablaPorPuntos();
        premarcarRespuestas(Color.black);
    }

    // Método para cargar el siguiente grupo
    private void cargarSiguienteGrupo() {

        if (grupoActual < grupos.size()) {
            tablero.reiniciarTaches();
            tablero.actualizarTabla(new Object[][] {});
            tablero.reiniciarPuntos();
            cargarGrupoActual(); // Cargar los datos del grupo actual
            grupoActual++; // Pasar al siguiente grupo
            ordenarTablaPorPuntos();
            initHigligthRow();
            premarcarRespuestas(Color.black);
        } else {
            JOptionPane.showMessageDialog(this, "Ya no hay más preguntas.");
            return;
        }
    }

    // Método para cargar el grupo anterior
    private void cargarGrupoAnterior() {

        if (grupoActual > 0) {
            tablero.reiniciarTaches();
            tablero.actualizarTabla(new Object[][] {});
            tablero.reiniciarPuntos();
            cargarGrupoActual(); // Cargar los datos del grupo actual
            grupoActual--; // Retrocede al grupo anterior
            ordenarTablaPorPuntos();
            initHigligthRow();
            premarcarRespuestas(Color.black);
        } else {
            JOptionPane.showMessageDialog(this, "Ya estás en la pregunta 1..");
            return;
        }
    }

    private void cargarGrupoActual() {
        ordenarTablaPorPuntos();
        if (grupoActual >= 0 && grupoActual < grupos.size()) {
            List<String[]> grupo = grupos.get(grupoActual); // Obtener el grupo actual
            modeloTablaControl.setRowCount(0); // Limpia la tabla

            if (!grupo.isEmpty()) {
                // Actualizar la etiqueta con la primera fila del grupo
                String textoPregunta = grupo.get(0)[0]; // Primera columna de la primera fila
                pregunta.setText(textoPregunta);

                // Cargar el resto de las filas en la tabla (omitimos la primera fila)
                for (int i = 1; i < grupo.size(); i++) {
                    String[] fila = grupo.get(i);
                    modeloTablaControl.addRow(new Object[] { fila[0], Integer.parseInt(fila[1]) });
                }
            }
            premarcarRespuestas(Color.black);
        }
    }

    private void ordenarTablaPorPuntos() {
        tablero.ordenarTablaPorPuntos();
        // Verifica si la tabla y su modelo no son nulos
        if (tablaControl != null && modeloTablaControl != null) {
            // Crear un TableRowSorter basado en el modelo de la tabla
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTablaControl);

            // Configurar el sorter a la tabla
            tablaControl.setRowSorter(sorter);

            // Crear una lista con el criterio de orden: columna "PUNTOS" (índice 1)
            // descendente
            List<RowSorter.SortKey> sortKeys = new ArrayList<>();
            sortKeys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));

            // Asignar las claves de orden al sorter
            sorter.setSortKeys(sortKeys);
            sorter.sort(); // Ejecutar la ordenación
        }
    }

    private void premarcarRespuestas(Color color) {

        tablero.ordenarTablaPorPuntos();
        tablero.colorTabla(color);
        int rowCount = modeloTablaControl.getRowCount();
        Object[][] datos = new Object[rowCount][2];
        for (int i = 0; i < rowCount; i++) {
            datos[i][0] = modeloTablaControl.getValueAt(i, 0);
            datos[i][1] = modeloTablaControl.getValueAt(i, 1);
        }
        // Enviar datos al Tablero
        tablero.actualizarTabla(datos);
    }

    private void actualizarColor(int row) {
        highlightedRow[row] = row;
        System.out.println("Row: " + row);
        System.out.println("Highlighted: " + highlightedRow.length);

        // Asignar renderer a todas las columnas
        for (int i = 0; i < tablaTablero.getColumnCount(); i++) {
            tablaTablero.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    // Metodo para sumar los puntos de las respuestas correctas a puntosT
    private void enviarPuntos(int row) {
        if(row>=tablaControl.getRowCount()){
            return;
        }
        System.out.println(row);
        int modelRow = tablaControl.convertRowIndexToModel(row);
        Object respuesta = modeloTablaControl.getValueAt(modelRow, 0);
        Object puntos = modeloTablaControl.getValueAt(modelRow, 1);
        // premarcarRespuestas(new Color(250, 130, 32));
        if (respuesta != null && puntos != null) {
            int puntosInt = (int) puntos;
            if (puntosInt == 0)
                return;
            
            

            if(highlightedRow[row] != -1){
                return;
            }

            tablero.actualizarPuntos(puntosInt);
            sonidoController.reproducirRCorrect();
            tablero.agregarFilaOrdenada(new Object[] { respuesta, puntos });
            // System.out.println("actualizar color row");
            actualizarColor(row);
        }
    }

    void marcarTache() {
        if (tacheCount < 3) {
            tablero.marcarError(tacheCount + 1);
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
