package pl.edu.uksw;

import marytts.signalproc.display.*;
import marytts.util.data.audio.MaryAudioUtils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {

        JFrame frame = new JFrame();
        frame.setTitle("Edyta Frąszczak - Spectogram");
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.add(new DisplayPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
        Files.list(Paths.get("C:\\Users\\Damian\\Documents")).forEach(x -> {
            if (x.toFile().getAbsolutePath().endsWith("wav")) {
                try {
                    MaryAudioUtils.getSamplesAsDoubleArray(AudioSystem.getAudioInputStream(x.toFile()));
                    System.out.println(x.toFile());
                } catch (Exception ex) {

                }
            }
        });


    }
}
