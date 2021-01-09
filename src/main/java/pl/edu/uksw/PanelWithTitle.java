package pl.edu.uksw;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class PanelWithTitle extends JPanel {

    protected JPanel northPanel;
    protected JLabel label;


    public PanelWithTitle(String title, JComponent content) {
        setLayout(new BorderLayout());
        TitledBorder border = new TitledBorder(title);
        border.setTitleJustification(TitledBorder.CENTER);
        border.setTitlePosition(TitledBorder.TOP);
        setBorder(border);
        add(content);
    }


}







