package pl.edu.uksw;

import marytts.signalproc.display.CursorDisplayer;
import marytts.signalproc.display.SignalGraph;
import marytts.signalproc.display.Spectrogram;
import marytts.signalproc.window.Window;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class DisplayPanel extends JPanel {

    private static List<String> windowTypes = Arrays.asList("RectWindow", "HammingWindow", "BlackmanWindow", "HanningWindow", "GaussWindow", "BartlettWindow", "FlattopWindow");
    private int selectedWindowType = 0;


    private JFormattedTextField windowLengthTextField;
    private JFormattedTextField windowPrescaleTextField;
    private File selectedFile;
    private JFormattedTextField windowShiftTextField;
    private JFormattedTextField fftSizeTextField;
    private JFormattedTextField heightTextField;
    private JFormattedTextField widthTextField;
    private JButton chooseFileButton;

    private MainPanel mainPanel;

    public DisplayPanel() {
        setLayout(new BorderLayout());
        add(createOptionsPanel(), BorderLayout.EAST);
        add(createButtonsPanel(), BorderLayout.SOUTH);


    }


    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel();
        JButton refreshButton = new JButton("Refresh");
        panel.add(refreshButton);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (mainPanel != null) {
                   remove(mainPanel);
                }

                if (selectedFile != null) {
                    try {
                        SwingUtilities.getWindowAncestor(DisplayPanel.this).setSize(Integer.parseInt(widthTextField.getValue().toString()),
                                Integer.parseInt(heightTextField.getValue().toString()));
                        mainPanel = new MainPanel((JFrame) SwingUtilities.getWindowAncestor(DisplayPanel.this),
                                AudioSystem.getAudioInputStream(selectedFile), Window.get(selectedWindowType,
                                Integer.parseInt(windowLengthTextField.getValue().toString()), Double.parseDouble(windowPrescaleTextField.getValue().toString())),
                                Integer.parseInt(windowShiftTextField.getValue().toString()),
                                Integer.parseInt(fftSizeTextField.getValue().toString()),
                                Integer.parseInt(widthTextField.getValue().toString()),
                                Integer.parseInt(heightTextField.getValue().toString()));
                        add(mainPanel);
                        revalidate();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Some error occured\n" + ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No file selected");
                }


            }
        });


        JButton playButton = new JButton("Play whole audio");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFile != null) {
                    try {
                        AudioInputStream audioIn = AudioSystem.getAudioInputStream(selectedFile);
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioIn);
                        clip.start();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Some error occured\n" + ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No file selected");
                }


            }
        });
        panel.add(playButton);

        JButton recordButton = new JButton("Record");
        recordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RecordingFrame();

            }
        });
        panel.add(recordButton);


        chooseFileButton = new JButton("Change file");
        panel.add(chooseFileButton);
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setMultiSelectionEnabled(false);
                fc.addChoosableFileFilter(new FileNameExtensionFilter("Sound clips", AudioUtils.getAudioFileTypes()));
                int result = fc.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fc.getSelectedFile();
                    chooseFileButton.setText("Choose file, current file path = " + selectedFile.getAbsolutePath());
                }
            }
        });


        return panel;
    }


    private JPanel createOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(10, 2));
        final JComboBox<String> windowsCombo = new JComboBox<>(new Vector<String>(windowTypes));
        windowsCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedWindow = windowsCombo.getSelectedItem().toString();
                selectedWindowType = windowTypes.indexOf(selectedWindow);
            }
        });
        panel.add(new JLabel("Window type"));
        panel.add(windowsCombo);

        windowLengthTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        windowLengthTextField.setValue(65);
        panel.add(new JLabel("Window length"));
        panel.add(windowLengthTextField);

        windowPrescaleTextField = new JFormattedTextField(NumberFormat.getNumberInstance());
        windowPrescaleTextField.setValue(1.0);
        panel.add(new JLabel("Window prescale"));
        panel.add(windowPrescaleTextField);

        windowShiftTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        windowShiftTextField.setValue(32);
        panel.add(new JLabel("Window shift"));
        panel.add(windowShiftTextField);

        fftSizeTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        fftSizeTextField.setValue(256);
        panel.add(new JLabel("FFT size"));
        panel.add(fftSizeTextField);

        widthTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        widthTextField.setValue(800);
        panel.add(new JLabel("Width"));
        panel.add(widthTextField);

        heightTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        heightTextField.setValue(800);
        panel.add(new JLabel("Height"));
        panel.add(heightTextField);

        return panel;
    }


}
