package pl.edu.uksw;

import marytts.signalproc.display.*;
import marytts.signalproc.window.Window;
import marytts.util.data.audio.MaryAudioUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainPanel extends JPanel {

    protected SignalGraph signalGraph;
    protected Spectrogram spectrogram;
    protected EnergyGraph energyGraph;


    protected List<FunctionGraph> allGraphs = new ArrayList();


    public MainPanel(JFrame mainFrame, AudioInputStream ais, Window window, int windowShift, int fftSize, int width, int height) {
        setLayout(new BorderLayout());
        setSize(width, height);
        if (!ais.getFormat().getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) {
            ais = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, ais);
        }

        int samplingRate = (int) ais.getFormat().getSampleRate();
        double[] signal = MaryAudioUtils.getSamplesAsDoubleArray(ais);

        JPanel zoomPanel = new JPanel();
        zoomPanel.setLayout(new BoxLayout(zoomPanel, BoxLayout.Y_AXIS));

        JButton zoomIn = new JButton("+");
        zoomIn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                changeZoomX(2);
                signalGraph.requestFocus();
            }
        });
        zoomPanel.add(zoomIn);
        JButton zoomOut = new JButton("-");
        zoomOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                changeZoomX(0.5);
                signalGraph.requestFocus();
            }
        });
        zoomPanel.add(zoomOut);
        add(zoomPanel, BorderLayout.NORTH);
        int graphWidth = width - zoomPanel.getPreferredSize().width - 30;
        JPanel graphPanel = new JPanel();
        graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(graphPanel);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scroll, BorderLayout.CENTER);
        signalGraph = new SignalGraph(signal, samplingRate, graphWidth, height * 30 / 100);
        allGraphs.add(signalGraph);
        graphPanel.add(new PanelWithTitle("Sygnał", signalGraph));

        spectrogram = new Spectrogram(signal, samplingRate, window, windowShift, fftSize, graphWidth, height * 40 / 100);
        allGraphs.add(spectrogram);
        graphPanel.add(new PanelWithTitle("Spektogram", spectrogram));

        energyGraph = new EnergyGraph(signal, samplingRate, graphWidth, height * 30 / 100);
        allGraphs.add(energyGraph);
        graphPanel.add(new PanelWithTitle("Energia", energyGraph));



        final CursorDisplayer glass = new CursorDisplayer();
        mainFrame.setGlassPane(glass);
        glass.setVisible(true);
        for (FunctionGraph g : allGraphs) {
            for (FunctionGraph g2 : allGraphs) {
                if (g2 != g) {
                    g.addCursorListener(g2);
                }
            }
            glass.addCursorSource(g);
            g.addCursorListener(glass);
        }
        signalGraph.requestFocus();

    }

    protected void changeZoomX(double factor) {
        for (FunctionGraph g : allGraphs) {
            g.setZoomX(g.getZoomX() * factor);
        }
    }

}

