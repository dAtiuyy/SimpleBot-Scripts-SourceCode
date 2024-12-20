package Tempoross;

import javax.swing.*;
import java.awt.*;

public class TemporossGui {
    final private JFrame frame;
    public TemporossGui() {
        frame = new JFrame("Test GUI");
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        TemporossBackgroundPanel backgroundPanel = new TemporossBackgroundPanel("image.jpg");
        frame.setContentPane(backgroundPanel);
        JLabel label = new JLabel("Hello World", SwingConstants.CENTER);
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(label, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
    }
    public void show() {
        frame.setVisible(true);
    }
    public void close() {
        if (frame != null) {
            frame.dispose();
        }
    }
}
