package TeakNMahoganyCutter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeakNMahoganyCutterGUI {
    private JComboBox<String> itemList;
    private JFrame frame;
    private JButton startStopButton;
    private boolean scriptStarted;
    public TeakNMahoganyCutterGUI() {
        initialize();
    }
    private void initialize() {
        frame = new JFrame("TeakNMahogany Cutter GUI");
        frame.setBounds(100, 100, 300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        startStopButton = new JButton("Start Script");
        startStopButton.setBounds(100, 80, 120, 30);
        frame.getContentPane().add(startStopButton);
        String[] options = {"Teak", "Mahogany"};
        itemList = new JComboBox<>(options);
        itemList.setBounds(10, 60, 120, 20);
        frame.getContentPane().add(itemList);
        startStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (scriptStarted) {
                    scriptStarted = false;
                    startStopButton.setText("Start Script");
                } else {
                    scriptStarted = true;
                    startStopButton.setText("Stop Script");
                }
            }
        });
        frame.setVisible(true);
    }
    public String getSelectedItem() {
        return (String) itemList.getSelectedItem();
    }
    public boolean isScriptStarted() {
        return scriptStarted;
    }
    public void onCloseGUI() {
        frame.dispose();
    }
}
