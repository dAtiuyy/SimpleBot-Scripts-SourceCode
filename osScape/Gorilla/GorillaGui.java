package Gorilla;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class GorillaGui {
    final private JCheckBox enableReturnVariable; // Added checkbox
    private boolean returnVariable;
    final private JFrame frame;
    public GorillaGui() {
        frame = new JFrame("Gorilla");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel label = new JLabel("Gorilla Stuff", SwingConstants.CENTER);
        frame.add(label);
        frame.setLocationRelativeTo(null);
        enableReturnVariable = new JCheckBox("Use Veng");
        enableReturnVariable.setBounds(100, 120, 150, 20);
        frame.getContentPane().add(enableReturnVariable);
        enableReturnVariable.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    returnVariable = true;
                } else {
                    returnVariable = false;
                }
            }
        });
    }
    public void show() {
        frame.setVisible(true);
    }
    public void close() {
        if (frame != null) {
            frame.dispose();
        }
    }
    public boolean isReturnVariable() {
        return returnVariable;
    }
}
