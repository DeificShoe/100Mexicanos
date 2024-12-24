/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.tdg.mexicanos;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 *
 * @author emanu
 */
public class App {

    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame ventanaTablero = new JFrame("100 Mexicanos Dijeron - Tablero");
        ventanaTablero.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Tablero tablero = new Tablero();
        ventanaTablero.setPreferredSize(screenSize);
        ventanaTablero.setContentPane(tablero);
        ventanaTablero.setResizable(false);
        ventanaTablero.pack();
        ventanaTablero.setLocationRelativeTo(null);
        ventanaTablero.setVisible(true);

        JFrame ventanaControl = new JFrame("100 Mexicanos Dijeron - Control");
        ventanaControl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Control control = new Control(tablero);
        ventanaControl.setPreferredSize(screenSize);
        ventanaControl.setMinimumSize(screenSize);
        ventanaControl.setContentPane(control);
        ventanaControl.setLocationRelativeTo(null);
        ventanaControl.setVisible(true);
        ventanaControl.setResizable(false);
    }
}
