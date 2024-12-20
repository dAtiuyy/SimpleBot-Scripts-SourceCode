package ZulrahHelper;

import simple.hooks.filters.SimplePrayers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ZulrahHelperGui {
    final private JButton startStopButton;
    private boolean scriptStarted;
    final private JFrame frame;
    private final JComboBox<SimplePrayers.Prayers> comboBox1;
    private final JComboBox<SimplePrayers.Prayers> comboBox2;
    private final JComboBox<Integer> healthBox;
    private final JComboBox<Integer> prayerBox;
    private final JTextArea rangeGearInput;
    private final JTextArea mageGearInput;

    public ZulrahHelperGui() {
        frame = new JFrame("Zulrah Helper");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);
        frame.setBounds(100, 100, 400, 500);

        startStopButton = new JButton("Start Script");
        startStopButton.setBounds(150, 30, 100, 30);
        frame.getContentPane().add(startStopButton);

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

        JLabel label1 = new JLabel("Prayer for Range Boost:");
        label1.setBounds(20, 100, 180, 30);
        frame.add(label1);

        JLabel label2 = new JLabel("Prayer for Mage Boost:");
        label2.setBounds(20, 140, 180, 30);
        frame.add(label2);

        JLabel healthLabel = new JLabel("Eat food at:");
        healthLabel.setBounds(20, 180, 180, 30);
        frame.add(healthLabel);

        JLabel prayerLabel = new JLabel("Drink potion at:");
        prayerLabel.setBounds(20, 220, 180, 30);
        frame.add(prayerLabel);

        JLabel rangeGearLabel = new JLabel("Range Gear List:");
        rangeGearLabel.setBounds(20, 260, 180, 30);
        frame.add(rangeGearLabel);

        JLabel mageGearLabel = new JLabel("Mage Gear List:");
        mageGearLabel.setBounds(200, 260, 180, 30);
        frame.add(mageGearLabel);

        SimplePrayers.Prayers[] rangePrayers = {SimplePrayers.Prayers.EAGLE_EYE, SimplePrayers.Prayers.RIGOUR};
        SimplePrayers.Prayers[] magePrayers = {SimplePrayers.Prayers.MYSTIC_MIGHT, SimplePrayers.Prayers.AUGURY};

        Integer[] numbers = {10, 20, 30, 40, 50, 60, 70, 80, 90};

        comboBox1 = new JComboBox<>(rangePrayers);
        comboBox1.setBounds(200, 100, 140, 30);
        frame.add(comboBox1);

        comboBox2 = new JComboBox<>(magePrayers);
        comboBox2.setBounds(200, 140, 140, 30);
        frame.add(comboBox2);

        healthBox = new JComboBox<>(numbers);
        healthBox.setBounds(200, 180, 140, 30);
        frame.add(healthBox);

        prayerBox = new JComboBox<>(numbers);
        prayerBox.setBounds(200, 220, 140, 30);
        frame.add(prayerBox);

        rangeGearInput = new JTextArea();
        rangeGearInput.setBounds(20, 290, 170, 120);
        rangeGearInput.setLineWrap(true);
        rangeGearInput.setWrapStyleWord(true);
        frame.add(rangeGearInput);

        mageGearInput = new JTextArea();
        mageGearInput.setBounds(200, 290, 170, 120);
        mageGearInput.setLineWrap(true);
        mageGearInput.setWrapStyleWord(true);
        frame.add(mageGearInput);
    }

    public void show() {frame.setVisible(true);}

    public void close() {
        if (frame != null) {
            frame.dispose();
        }
    }

    public boolean isScriptStarted() {return scriptStarted;}
    public SimplePrayers.Prayers FromComboBox1() {return (SimplePrayers.Prayers) comboBox1.getSelectedItem();}
    public SimplePrayers.Prayers FromComboBox2() {return (SimplePrayers.Prayers) comboBox2.getSelectedItem();}
    public Integer FromHealthBox() {return (Integer) healthBox.getSelectedItem();}
    public Integer FromPrayerBox() {return (Integer) prayerBox.getSelectedItem();}
    public String[] getRangeGearInput() {
        String input = rangeGearInput.getText();
        return input.split(",\\s*");
    }
    public String[] getMageGearInput() {
        String input = mageGearInput.getText();
        return input.split(",\\s*");
    }
}
