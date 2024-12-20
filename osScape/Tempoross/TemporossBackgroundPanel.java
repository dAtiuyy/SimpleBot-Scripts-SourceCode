package Tempoross;

import javax.swing.*;
import java.awt.*;

public class TemporossBackgroundPanel extends JPanel {
    final private Image backgroundImage;
    public TemporossBackgroundPanel(String imagePath) {
        try {
            backgroundImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("image.jpg"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
