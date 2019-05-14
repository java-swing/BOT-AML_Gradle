package com.tpbank.GUI.TestGui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

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
        LayoutManager layout = new FlowLayout();
        panel.setLayout(layout);

        JPanel panel1 = new JPanel();
        String spaces = "                   ";
//
//        panel1.add(new JLabel(spaces + "Title border to JPanel" + spaces));
//        panel1.setBorder(blackline);

        JRadioButton r1=new JRadioButton("One time");
        JRadioButton r2=new JRadioButton("Daily");
        JRadioButton r3=new JRadioButton("Weekly");
        JRadioButton r4=new JRadioButton("Monthly");
        r1.setBounds(75,50,100,30);
        r2.setBounds(100,50,100,30);
        r2.setBounds(125,50,100,30);
        r2.setBounds(150,50,100,30);
        ButtonGroup bg=new ButtonGroup();
        bg.add(r1);bg.add(r2);
        bg.add(r3);bg.add(r4);
        panel1.add(r1);
        panel1.add(r2);
        panel1.add(r3);
        panel1.add(r4);


        panel.add(panel1);
        frame.getContentPane().add(panel, BorderLayout.EAST);
    }
}