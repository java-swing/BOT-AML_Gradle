package com.tpbank.GUI.TestGui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;

public class newTab {

    public static void main(String[] args) {
        createWindow();
    }

    private static void createWindow() {
        JFrame frame = new JFrame("Swing Tester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createUI(frame);
        frame.setSize(560, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void createUI(JFrame frame){
        //Create a border
        Border blackline = BorderFactory.createTitledBorder("Setting");
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();

        JPanel panel1 = new JPanel();
        panel1.setBorder(blackline);

        JRadioButton r1=new JRadioButton("One time");
        JRadioButton r2=new JRadioButton("Daily");
        JRadioButton r3=new JRadioButton("Weekly");
        JRadioButton r4=new JRadioButton("Monthly");

        ButtonGroup bg=new ButtonGroup();
        bg.add(r1);bg.add(r2);
        bg.add(r3);bg.add(r4);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 0; // make this component tall
        c.weightx = 0.0;
        c.gridwidth = 0; // 2 columns wide
        panel.add(r1, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.ipady = 0; // make this component tall
        c.weightx = 0.0;
        c.gridwidth = 0; // 2 columns wide
        panel1.add(r1, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 2;
        c.ipady = 0; // make this component tall
        c.weightx = 0.0;
        c.gridwidth = 0; // 2 columns wide
        panel1.add(r2, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 3;
        c.ipady = 0; // make this component tall
        c.weightx = 0.0;
        c.gridwidth = 0; // 2 columns wide
        panel1.add(r3, c);


        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
        c.gridy = 4;
        c.ipady = 0; // make this component tall
        c.weightx = 0.0;
        c.gridwidth = 0; // 2 columns wide
        panel1.add(r4);


        panel.add(panel1);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
    }
}