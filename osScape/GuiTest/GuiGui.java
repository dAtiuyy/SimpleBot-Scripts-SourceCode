package GuiTest;

import javax.swing.*;

public class GuiGui {
    final private JFrame frame;

    public GuiGui() {
        // Initialize the JFrame in the constructor
        frame = new JFrame("Test GUI");

        // Set the size of the JFrame
        frame.setSize(300, 200);

        // Set the default close operation (important for closing the GUI properly)
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a JLabel with the text "Hello World"
        JLabel label = new JLabel("Hello World", SwingConstants.CENTER);

        // Add the label to the JFrame
        frame.add(label);

        // Center the JFrame on the screen
        frame.setLocationRelativeTo(null);
    }

    public void show() {
        // Make the JFrame visible
        frame.setVisible(true);
    }

    public void close() {
        // Dispose of the JFrame when needed
        if (frame != null) {
            frame.dispose();
        }
    }
}
