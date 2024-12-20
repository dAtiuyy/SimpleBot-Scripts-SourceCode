package Gauntlet;

import simple.hooks.filters.SimplePrayers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GauntletGui {
    final private JButton startStopButton;
    private boolean scriptStarted;
    final private JFrame frame;
    private final JComboBox<SimplePrayers.Prayers> comboBox1;
    private final JComboBox<SimplePrayers.Prayers> comboBox2;
    private final JComboBox<SimplePrayers.Prayers> comboBox6;
    private final JComboBox<String> comboBox3;
    private final JComboBox<String> rangeOverheadBox;
    private final JComboBox<String> comboBox5;
    private final JComboBox<Integer> healthBox;
    private final JComboBox<Integer> prayerBox;

    public GauntletGui() {
        frame = new JFrame("Gauntlet Helper");
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
        JLabel label6 = new JLabel("Prayer for Melee Boost:");
        label6.setBounds(20, 80, 180, 30);
        frame.add(label6);
        JLabel label1 = new JLabel("Prayer for Range Boost:");
        label1.setBounds(20, 120, 180, 30);
        frame.add(label1);
        JLabel label2 = new JLabel("Prayer for Mage Boost:");
        label2.setBounds(20, 160, 180, 30);
        frame.add(label2);


        JLabel label3 = new JLabel("Weapon for Melee Overhead:");
        label3.setBounds(20, 220, 180, 30);
        frame.add(label3);
        JLabel rangeOverhead = new JLabel("Weapon for Range Overhead:");
        rangeOverhead.setBounds(20, 260, 180, 30);
        frame.add(rangeOverhead);
        JLabel label5 = new JLabel("Weapon for Mage Overhead:");
        label5.setBounds(20, 300, 180, 30);
        frame.add(label5);


        JLabel healthLabel = new JLabel("Eat food at:");
        healthLabel.setBounds(20, 360, 180, 30);
        frame.add(healthLabel);
        JLabel prayerLabel = new JLabel("Drink potion at:");
        prayerLabel.setBounds(20, 400, 180, 30);
        frame.add(prayerLabel);

        SimplePrayers.Prayers[] rangePrayers = {SimplePrayers.Prayers.EAGLE_EYE, SimplePrayers.Prayers.RIGOUR};
        SimplePrayers.Prayers[] magePrayers = {SimplePrayers.Prayers.MYSTIC_MIGHT, SimplePrayers.Prayers.AUGURY};
        SimplePrayers.Prayers[] meleePrayers = {SimplePrayers.Prayers.CHIVALRY, SimplePrayers.Prayers.PIETY};

        String[] weapons = {"Halberd", "Bow", "Staff"};

        Integer[] numbers = {10, 20, 30, 40, 50, 60, 70, 80, 90};

        comboBox6 = new JComboBox<>(meleePrayers);
        comboBox6.setBounds(200, 80, 140, 30);
        frame.add(comboBox6);
        comboBox1 = new JComboBox<>(rangePrayers);
        comboBox1.setBounds(200, 120, 140, 30);
        frame.add(comboBox1);
        comboBox2 = new JComboBox<>(magePrayers);
        comboBox2.setBounds(200, 160, 140, 30);
        frame.add(comboBox2);


        comboBox3 = new JComboBox<>(weapons);
        comboBox3.setBounds(200, 220, 140, 30);
        frame.add(comboBox3);
        rangeOverheadBox = new JComboBox<>(weapons);
        rangeOverheadBox.setBounds(200, 260, 140, 30);
        frame.add(rangeOverheadBox);
        comboBox5 = new JComboBox<>(weapons);
        comboBox5.setBounds(200, 300, 140, 30);
        frame.add(comboBox5);


        healthBox = new JComboBox<>(numbers);
        healthBox.setBounds(200, 360, 140, 30);
        frame.add(healthBox);
        prayerBox = new JComboBox<>(numbers);
        prayerBox.setBounds(200, 400, 140, 30);
        frame.add(prayerBox);
    }


    public void show() {
        frame.setVisible(true);
    }

    public void close() {
        if (frame != null) {
            frame.dispose();
        }
    }

    public boolean isScriptStarted() {
        return scriptStarted;
    }

    public SimplePrayers.Prayers FromComboBox1() {
        return (SimplePrayers.Prayers) comboBox1.getSelectedItem();
    }

    public SimplePrayers.Prayers FromComboBox2() {
        return (SimplePrayers.Prayers) comboBox2.getSelectedItem();
    }

    public SimplePrayers.Prayers FromComboBox6() {
        return (SimplePrayers.Prayers) comboBox6.getSelectedItem();
    }

    public String FromComboBox3() {
        return (String) comboBox3.getSelectedItem();
    }

    public String fromRangeOverhead() {
        return (String) rangeOverheadBox.getSelectedItem();
    }
    public String FromComboBox5() {
        return (String) comboBox5.getSelectedItem();
    }

    public Integer FromHealthBox() {
        return (Integer) healthBox.getSelectedItem();
    }

    public Integer FromPrayerBox() {
        return  (Integer) prayerBox.getSelectedItem();
    }
}
