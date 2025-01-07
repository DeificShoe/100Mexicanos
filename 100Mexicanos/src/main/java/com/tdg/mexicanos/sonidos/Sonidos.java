package com.tdg.mexicanos.sonidos;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sonidos {

    private Clip cargarSonido(String nombreArchivo) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                getClass().getResource("/assets/sounds/" + nombreArchivo)
            );
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            return clip;
        } catch (Exception e) {
            System.err.println("No se pudo cargar el sonido: " + nombreArchivo);
            e.printStackTrace();
            return null;
        }
    }

    public void reproducirTicTac() {
        Clip clip = cargarSonido("tictac.wav");
        if (clip != null) {
            clip.start();
        }
    }

    public void detenerTicTac() {
        Clip clip = cargarSonido("tictac.wav");
        if (clip != null) {
            clip.stop();
        }
    }

    public void reproducirFinTiempo() {
        Clip clip = cargarSonido("finTiempo.wav");
        if (clip != null) {
            clip.start();
        }
    }

    public void reproducirWinRonda() {
        Clip clip = cargarSonido("winRonda.wav");
        if (clip != null) {
            clip.start();
        }
    }

    public void reproducirRCorrect() {
        Clip clip = cargarSonido("rCorrect.wav");
        if (clip != null) {
            clip.start();
        }
    }

    public void reproducirError() {
        Clip clip = cargarSonido("error.wav");
        if (clip != null) {
            clip.start();
        }
    }

    
}
