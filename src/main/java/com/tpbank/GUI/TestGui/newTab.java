package com.tpbank.GUI.TestGui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

import javax.swing.*;
import javax.swing.border.Border;

import com.github.lgooddatepicker.components.DateTimePicker;

public class newTab {

    public static void main(String[] args) {
        createWindow();
    }

    private static void createWindow() {
        JFrame frame = new JFrame("Swing Tester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createUI(frame);
        // frame.setSize(560, 200);
        // frame.setLocationRelativeTo(null);
        // frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setDefaultLookAndFeelDecorated(true);
        frame.setBackground(Color.white);
    }

    private static void createUI(JFrame frame) {
        // Create a border

        JPanel panelEstablish = new JPanel();
        GridBagConstraints cEstablish = new GridBagConstraints();
        GridBagLayout layoutEstablish = new GridBagLayout();
        panelEstablish.setLayout(layoutEstablish);
        panelEstablish.setBackground(Color.white);

        // ======== panel Setting=========================
        JPanel panelSetting = new JPanel();
        GridBagConstraints cSetting = new GridBagConstraints();
        GridBagLayout layoutSetting = new GridBagLayout();
        panelSetting.setLayout(layoutSetting);
        Border blacklineSetting = BorderFactory.createTitledBorder("Setting");
        panelSetting.setBorder(blacklineSetting);
//		panelSetting.setBackground(Color.white);

        // ======== panel panelIntervalSimple=========================
        JPanel panelIntervalSimple = new JPanel();
        GridBagConstraints cIntervalSimple = new GridBagConstraints();
        GridBagLayout layoutIntervalSimple = new GridBagLayout();
        panelIntervalSimple.setLayout(layoutIntervalSimple);
        Border blacklineIntervalSimple = BorderFactory.createTitledBorder("");
        panelIntervalSimple.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panelIntervalSimple.setBackground(Color.white);

        JRadioButton r1 = new JRadioButton("One time");
        JRadioButton r2 = new JRadioButton("Daily");
        JRadioButton r3 = new JRadioButton("Weekly");
        JRadioButton r4 = new JRadioButton("Monthly");
        r1.setBounds(10, 50, 100, 30);
        r2.setBounds(10, 75, 100, 30);
        r2.setBounds(10, 100, 100, 30);
        r2.setBounds(10, 125, 100, 30);
        ButtonGroup bg = new ButtonGroup();
        bg.add(r1);
        bg.add(r2);
        bg.add(r3);
        bg.add(r4);
        // ======== panelIntervalSimple=========================
        // RadioButton "One time"
        cIntervalSimple.fill = GridBagConstraints.HORIZONTAL;
        cIntervalSimple.gridx = 0;
        cIntervalSimple.gridy = 0;
        cIntervalSimple.anchor = GridBagConstraints.CENTER; // bottom of
        // space
        panelIntervalSimple.add(r1, cIntervalSimple);

        // RadioButton "Daily"
        cIntervalSimple.fill = GridBagConstraints.HORIZONTAL;
        cIntervalSimple.gridx = 0;
        cIntervalSimple.gridy = 1;
        cIntervalSimple.anchor = GridBagConstraints.CENTER; // bottom of
        // space
        panelIntervalSimple.add(r2, cIntervalSimple);

        // RadioButton "Weekly"
        cIntervalSimple.fill = GridBagConstraints.HORIZONTAL;
        cIntervalSimple.gridx = 0;
        cIntervalSimple.gridy = 2;
        cIntervalSimple.anchor = GridBagConstraints.CENTER; // bottom of
        // space
        panelIntervalSimple.add(r3, cIntervalSimple);

        // RadioButton "Monthly"
        cIntervalSimple.fill = GridBagConstraints.HORIZONTAL;
        cIntervalSimple.gridx = 0;
        cIntervalSimple.gridy = 3;
        cIntervalSimple.anchor = GridBagConstraints.CENTER; // bottom of
        // space
        panelIntervalSimple.add(r4, cIntervalSimple);

        // =======Start Time=====================
        // Panel StartTime
        JPanel panelStartTime = new JPanel();
        GridBagConstraints cStartTime = new GridBagConstraints();
        GridBagLayout layoutStartTime = new GridBagLayout();
        panelStartTime.setLayout(layoutStartTime);
        panelStartTime.setBackground(Color.white);

        // add label Start to Panel Setting:
        JLabel startLb = new JLabel("Start");
        cStartTime.fill = GridBagConstraints.HORIZONTAL;
        cStartTime.ipadx = 0; // reset to default
        cStartTime.ipady = 0; // reset to default
        cStartTime.gridx = 0;
        cStartTime.gridy = 0;
        cStartTime.anchor = GridBagConstraints.CENTER; // bottom of
        // space
        cStartTime.insets = new Insets(10, 10, 0, 0);
        panelStartTime.add(startLb, cStartTime);

        // add label Start to Panel StartTime:
        DateTimePicker dateTimePickerStar = new DateTimePicker();
        cStartTime.fill = GridBagConstraints.WEST;
        cStartTime.ipadx = 0; // reset to default
        cStartTime.ipady = 0; // reset to default
        cStartTime.gridy = 0;
        cStartTime.gridx = 1;
        cStartTime.anchor = GridBagConstraints.CENTER; // bottom of
        // space
        cStartTime.insets = new Insets(10, 10, 0, 0);
        panelStartTime.add(dateTimePickerStar, cSetting);

        // add label Start to Panel Setting:
        JLabel stoptLb = new JLabel("Stop");
        cStartTime.fill = GridBagConstraints.HORIZONTAL;
        cStartTime.ipadx = 0; // reset to default
        cStartTime.ipady = 0; // reset to default
        cStartTime.gridy = 1;
        cStartTime.gridx = 0;
        cStartTime.anchor = GridBagConstraints.CENTER; // bottom of
        // space
        cStartTime.insets = new Insets(10, 10, 0, 0);
        panelStartTime.add(stoptLb, cStartTime);

        // DateTime expire
        DateTimePicker dateTimePickerStop = new DateTimePicker();
        cStartTime.fill = GridBagConstraints.WEST;
        cStartTime.ipadx = 0; // reset to default
        cStartTime.ipady = 0; // reset to default
        cStartTime.gridy = 1;
        cStartTime.gridx = 1;
        cStartTime.anchor = GridBagConstraints.CENTER; // bottom of
        // space
        cStartTime.insets = new Insets(0, 10, 0, 0);
        panelStartTime.add(dateTimePickerStop, cStartTime);


        // =======Start Time=====================
        // Panel Recur
        JPanel panelRecur = new JPanel();
        GridBagConstraints cRecur = new GridBagConstraints();
        GridBagLayout layoutRecur = new GridBagLayout();
        panelRecur.setLayout(layoutRecur);
        Border blacklineRecur = BorderFactory
                .createTitledBorder("");
        panelRecur.setBorder(blacklineRecur);
        panelRecur.setBackground(Color.white);
        // Label Recur everyday
        JLabel labelRecur = new JLabel("Recur every: ");
        cRecur.ipadx = 0; // reset to default
        cRecur.ipady = 0; // reset to default
        cRecur.fill = GridBagConstraints.LINE_START;
        cRecur.gridx = 0;
        cRecur.gridy = 0;
        cRecur.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cStartTime.insets = new Insets(0, 0, 0, 0);
        panelRecur.add(labelRecur, cRecur);

        // Label Recur everyday
        JTextField textFieldRecur = new JTextField();
        cRecur.ipadx = 0; // reset to default
        cRecur.ipady = 0; // reset to default
        cRecur.fill = GridBagConstraints.LINE_START;
        cRecur.gridx = 1;
        cRecur.gridy = 0;
        cRecur.ipady = 0;
        cRecur.ipadx = 20;
        cRecur.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cStartTime.insets = new Insets(0, 0, 0, 0);
        panelRecur.add(textFieldRecur, cRecur);

        // Label Recur everyday
        JLabel labelWeekon = new JLabel("week on");
        cRecur.fill = GridBagConstraints.LINE_START;
        cRecur.ipadx = 0; // reset to default
        cRecur.ipady = 0; // reset to default
        cRecur.gridx = 2;
        cRecur.gridy = 0;
        cRecur.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cStartTime.insets = new Insets(0, 0, 0, 0);
        panelRecur.add(labelWeekon, cRecur);

        // Checkbox Sunday
        JCheckBox cbSunday = new JCheckBox("Sunday");
        cRecur.ipadx = 0; // reset to default
        cRecur.ipady = 0; // reset to default
        cRecur.fill = GridBagConstraints.LINE_START;
        cRecur.gridy = 1;
        cRecur.gridx = 0;
        cRecur.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cStartTime.insets = new Insets(0, 10, 0, 0);
        panelRecur.add(cbSunday, cRecur);

        // Checkbox Monday
        JCheckBox cbMonday = new JCheckBox("Monday");
        cRecur.ipadx = 0; // reset to default
        cRecur.ipady = 0; // reset to default
        cRecur.fill = GridBagConstraints.LINE_START;
        cRecur.gridy = 1;
        cRecur.gridx = 1;
        cRecur.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cStartTime.insets = new Insets(0, 0, 0, 0);
        panelRecur.add(cbMonday, cRecur);

        // Checkbox Tuesday
        JCheckBox cbTuesday = new JCheckBox("Tuesday");
        cRecur.ipadx = 0; // reset to default
        cRecur.ipady = 0; // reset to default
        cRecur.fill = GridBagConstraints.LINE_START;
        cRecur.gridy = 1;
        cRecur.gridx = 2;
        cRecur.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cStartTime.insets = new Insets(0, 0, 0, 0);
        panelRecur.add(cbTuesday, cRecur);

        // Checkbox Wednesday
        JCheckBox cbWednesday = new JCheckBox("Wednesday");
        cRecur.ipadx = 0; // reset to default
        cRecur.ipady = 0; // reset to default
        cRecur.fill = GridBagConstraints.LINE_START;
        cRecur.gridy = 1;
        cRecur.gridx = 3;
        cRecur.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cStartTime.insets = new Insets(0, 0, 0, 0);
        panelRecur.add(cbWednesday, cRecur);

        // Checkbox Thursday
        JCheckBox cbThursday = new JCheckBox("Thursday");
        cRecur.ipadx = 0; // reset to default
        cRecur.ipady = 0; // reset to default
        cRecur.fill = GridBagConstraints.LINE_START;
        cRecur.gridy = 2;
        cRecur.gridx = 0;
        cRecur.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cStartTime.insets = new Insets(10, 10, 0, 0);
        panelRecur.add(cbThursday, cRecur);

        // Checkbox Friday
        JCheckBox cbFriday = new JCheckBox("Friday");
        cRecur.ipadx = 0; // reset to default
        cRecur.ipady = 0; // reset to default
        cRecur.fill = GridBagConstraints.LINE_START;
        cRecur.gridy = 2;
        cRecur.gridx = 1;
        cRecur.gridwidth = 1;
        cRecur.ipady = 1;
        cRecur.ipadx = 4;
        cRecur.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cStartTime.insets = new Insets(0, 0, 0, 0);
        panelRecur.add(cbFriday, cRecur);

        // Checkbox Saturday
        JCheckBox cbSaturday = new JCheckBox("Saturday");
        cRecur.ipadx = 0; // reset to default
        cRecur.ipady = 0; // reset to default
        cRecur.fill = GridBagConstraints.LINE_START;
        cRecur.gridy = 2;
        cRecur.gridx = 2;
        cRecur.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cStartTime.insets = new Insets(0, 0, 0, 0);
        panelRecur.add(cbSaturday, cRecur);

        // ============ panel StartTime to add Panel Setting===================
        // add Panel Setting to panel StartTime
        cSetting.fill = GridBagConstraints.HORIZONTAL;
        cSetting.gridx = 0;
        cSetting.gridy = 0;
        cSetting.ipadx = 1; // reset to default
        cSetting.ipady = 4; // reset to default
        cSetting.gridwidth = 1;
        cSetting.gridheight = 4;
        cSetting.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cSetting.insets = new Insets(0, 0, 0, 0);
        panelSetting.add(panelIntervalSimple, cSetting);
        panelSetting.setBackground(Color.white);

        // add panel StartTime to panel Setting
        cSetting.fill = GridBagConstraints.LINE_START;
        cSetting.gridx = 1;
        cSetting.gridy = 0;
        cSetting.gridwidth = 1;
        cSetting.gridheight = 1;
        cSetting.ipadx = 1; // reset to default
        cSetting.ipady = 1; // reset to default
        cSetting.insets = new Insets(0, 0, 0, 0);
//		cSetting.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        panelSetting.add(panelStartTime, cSetting);

        // add panel Recur to panel Setting
        cSetting.fill = GridBagConstraints.HORIZONTAL;
        cSetting.gridx = 1;
        cSetting.gridy = 1;
        cSetting.ipadx = 1; // reset to default
        cSetting.ipady = 3; // reset to default
        cSetting.gridwidth = 1;
        cSetting.gridheight = 3;
        cSetting.insets = new Insets(0, 0, 0, 0);
//		cSetting.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        panelSetting.add(panelRecur, cSetting);

        // ======== panel Advance Setting=========================
        JPanel panelAdvancedSetting = new JPanel();
        GridBagConstraints cAdvancedSetting = new GridBagConstraints();
        GridBagLayout layoutAdvancedSetting = new GridBagLayout();
        panelAdvancedSetting.setLayout(layoutAdvancedSetting);
        Border blacklineAdvancedSetting = BorderFactory
                .createTitledBorder("Advanced Setting");
        panelAdvancedSetting.setBorder(blacklineAdvancedSetting);
        panelAdvancedSetting.setBackground(Color.white);

//		// ======== panel cbDelayTask=========================
//		JPanel panelDelayTask = new JPanel();
//		GridBagConstraints cDelayTask = new GridBagConstraints();
//		GridBagLayout layoutDelayTask = new GridBagLayout();
//		panelDelayTask.setLayout(layoutSetting);
//
//		// Checkbox cbDelayTask
//		JCheckBox cbDelayTask = new JCheckBox(
//				"Delay Task for up (random delay): ");
//		cDelayTask.fill = GridBagConstraints.LINE_START;
//		cDelayTask.ipadx = 0; // reset to default
//		cDelayTask.ipady = 0; // reset to default
//		cDelayTask.gridy = 0;
//		cDelayTask.gridx = 0;
//		cDelayTask.anchor = GridBagConstraints.LINE_START; // bottom of
//		// space
//		cDelayTask.insets = new Insets(0, 0, 0, 0);
//		panelDelayTask.add(cbDelayTask, cDelayTask);
//
//		// Menu choose time
//		JMenuItem i1, i2, i3, i4;
//		JMenu menuChooseTime = new JMenu("1 hour");
//		JMenuBar menuBarChooseTime = new JMenuBar();
//		cDelayTask.fill = GridBagConstraints.CENTER;
//		i1 = new JMenuItem("1 hour");
//		i2 = new JMenuItem("2 hour");
//		i3 = new JMenuItem("6 hour");
//		i4 = new JMenuItem("12 hour");
//		menuChooseTime.add(i1);
//		menuChooseTime.add(i2);
//		menuChooseTime.add(i3);
//		menuChooseTime.add(i4);
//		cDelayTask.anchor = GridBagConstraints.LINE_START; // bottom of
//		// space
//		menuBarChooseTime.add(menuChooseTime);
//		menuBarChooseTime.setBorder(BorderFactory.createLineBorder(
//				Color.LIGHT_GRAY, 1));
//		cDelayTask.fill = GridBagConstraints.HORIZONTAL;
//		cDelayTask.ipadx = 0; // reset to default
//		cDelayTask.ipady = 4; // reset to default
//		cDelayTask.gridy = 0;
//		cDelayTask.gridx = 1;
//		cDelayTask.anchor = GridBagConstraints.CENTER; // bottom of
//		// space
//		cDelayTask.insets = new Insets(0, 30, 0, 0);
//		panelDelayTask.add(menuBarChooseTime, cDelayTask);
//
//		// ======== panel cbRepeatTask=========================
//		JPanel panelRepeatTask = new JPanel();
//		GridBagConstraints cRepeatTask = new GridBagConstraints();
//		GridBagLayout layoutRepeatTask = new GridBagLayout();
//		panelRepeatTask.setLayout(layoutRepeatTask);
//
//		// Checkbox cbRepeatTask
//		JCheckBox cbRepeatTask = new JCheckBox("Repeat task every: ");
//		cRepeatTask.fill = GridBagConstraints.HORIZONTAL;
//		cRepeatTask.ipadx = 0; // reset to default
//		cRepeatTask.ipady = 0; // reset to default
//		cRepeatTask.gridy = 0;
//		cRepeatTask.gridx = 0;
//		cRepeatTask.anchor = GridBagConstraints.LINE_START; // bottom of
//		// space
//		cRepeatTask.insets = new Insets(0, 0, 0, 0);
//		panelRepeatTask.add(cbRepeatTask, cRepeatTask);
//
//		// Menu choose time
//		JMenuItem iRp1, iRp2, iRp3, iRp4;
//		JMenu menuChooseTimeRepeat = new JMenu("1 hour");
//		JMenuBar menuBarChooseTimeRepeat = new JMenuBar();
//		cRepeatTask.fill = GridBagConstraints.HORIZONTAL;
//		iRp1 = new JMenuItem("1 hour");
//		iRp2 = new JMenuItem("2 hour");
//		iRp3 = new JMenuItem("6 hour");
//		iRp4 = new JMenuItem("12 hour");
//		menuChooseTimeRepeat.add(iRp1);
//		menuChooseTimeRepeat.add(iRp2);
//		menuChooseTimeRepeat.add(iRp3);
//		menuChooseTimeRepeat.add(iRp4);
//		menuBarChooseTimeRepeat.add(menuChooseTimeRepeat);
//		menuBarChooseTimeRepeat.setBorder(BorderFactory.createLineBorder(
//				Color.LIGHT_GRAY, 1));
//		cRepeatTask.fill = GridBagConstraints.HORIZONTAL;
//		cRepeatTask.ipadx = 0; // reset to default
//		cRepeatTask.ipady = 0; // reset to default
//		cRepeatTask.gridy = 0;
//		cRepeatTask.gridx = 1;
//		cRepeatTask.anchor = GridBagConstraints.CENTER; // bottom of
//		// space
//		cRepeatTask.insets = new Insets(0, 10, 0, 0);
//		panelRepeatTask.add(menuBarChooseTimeRepeat, cRepeatTask);
//
//		// label Duration
//		JLabel lbDuration = new JLabel("for duration of: ");
//		cRepeatTask.fill = GridBagConstraints.HORIZONTAL;
//		cRepeatTask.ipadx = 0; // reset to default
//		cRepeatTask.ipady = 0; // reset to default
//		cRepeatTask.gridy = 0;
//		cRepeatTask.gridx = 2;
//		cRepeatTask.anchor = GridBagConstraints.CENTER; // bottom of
//		// space
//		cRepeatTask.insets = new Insets(0, 100, 0, 0);
//		panelRepeatTask.add(lbDuration, cRepeatTask);
//
//		// Menu choose time
//		JMenuItem iDr1, iDr2, iDr3;
//		JMenu menuChooseTimeDuration = new JMenu("1 day");
//		JMenuBar menuBarChooseTimeDuration = new JMenuBar();
//		cRepeatTask.fill = GridBagConstraints.HORIZONTAL;
//		iDr1 = new JMenuItem("1 day");
//		iDr2 = new JMenuItem("1 week");
//		iDr3 = new JMenuItem("1 month");
//		menuChooseTimeDuration.add(iDr1);
//		menuChooseTimeDuration.add(iDr2);
//		menuChooseTimeDuration.add(iDr3);
//		menuBarChooseTimeDuration.add(menuChooseTimeDuration);
//		menuBarChooseTimeDuration.setBorder(BorderFactory.createLineBorder(
//				Color.LIGHT_GRAY, 1));
//		cRepeatTask.fill = GridBagConstraints.HORIZONTAL;
//		cRepeatTask.ipadx = 0; // reset to default
//		cRepeatTask.ipady = 0; // reset to default
//		cRepeatTask.gridy = 0;
//		cRepeatTask.gridx = 3;
//		cRepeatTask.anchor = GridBagConstraints.CENTER; // bottom of
//		// space
//		cRepeatTask.insets = new Insets(0, 0, 0, 0);
//		panelRepeatTask.add(menuBarChooseTimeDuration, cRepeatTask);
//
//		// ======== panel StopAll=========================
//		JPanel panelStopAll = new JPanel();
//		GridBagConstraints cStopAll = new GridBagConstraints();
//		GridBagLayout layoutStopAll = new GridBagLayout();
//		panelStopAll.setLayout(layoutStopAll);
//
//		// Checkbox cbStopAll
//		JCheckBox cbStopAll = new JCheckBox(
//				"Stop all running tasks at end of receptition duration ");
//		cStopAll.fill = GridBagConstraints.HORIZONTAL;
//		cStopAll.ipadx = 0; // reset to default
//		cStopAll.ipady = 0; // reset to default
//		cStopAll.gridy = 0;
//		cStopAll.gridx = 0;
//		cStopAll.anchor = GridBagConstraints.LINE_START; // bottom of
//		// space
//		cStopAll.insets = new Insets(0, 0, 0, 0);
//		panelStopAll.add(cbStopAll, cStopAll);
//
//		// ======== panel cbStopif=========================
//		JPanel panelStopTaskIf = new JPanel();
//		GridBagConstraints cStopTaskIf = new GridBagConstraints();
//		GridBagLayout layoutStopTaskIf = new GridBagLayout();
//		panelStopTaskIf.setLayout(layoutStopTaskIf);
//
//		// Checkbox cbStopif
//		JCheckBox cbStopTaskIf = new JCheckBox("Stop if runs longer than: ");
//		cStopTaskIf.fill = GridBagConstraints.HORIZONTAL;
//		cStopTaskIf.ipadx = 0; // reset to default
//		cStopTaskIf.ipady = 0; // reset to default
//		cStopTaskIf.gridy = 0;
//		cStopTaskIf.gridx = 0;
//		cStopAll.anchor = GridBagConstraints.LINE_START; // bottom of
//		// space
//		cStopTaskIf.insets = new Insets(0, 0, 0, 0);
//		panelStopTaskIf.add(cbStopTaskIf, cStopTaskIf);
//
//		// Menu choose day running
//		JMenuItem iDrt1, iDrt2, iDrt3, iDrt4;
//		JMenu menuChooseTimeRunning = new JMenu("3 days");
//		JMenuBar menuBarChooseTimeRunning = new JMenuBar();
//		cStopTaskIf.fill = GridBagConstraints.HORIZONTAL;
//		iDrt1 = new JMenuItem("3 days");
//		iDrt2 = new JMenuItem("7 days");
//		iDrt3 = new JMenuItem("15 days");
//		iDrt4 = new JMenuItem("30 days");
//		menuChooseTimeRunning.add(iDrt1);
//		menuChooseTimeRunning.add(iDrt2);
//		menuChooseTimeRunning.add(iDrt3);
//		menuChooseTimeRunning.add(iDrt4);
//		menuBarChooseTimeRunning.add(menuChooseTimeRunning);
//		menuBarChooseTimeRunning.setBorder(BorderFactory.createLineBorder(
//				Color.LIGHT_GRAY, 1));
//		cStopTaskIf.fill = GridBagConstraints.HORIZONTAL;
//		cStopTaskIf.ipadx = 0; // reset to default
//		cStopTaskIf.ipady = 0; // reset to default
//		cStopTaskIf.gridy = 0;
//		cStopTaskIf.gridx = 1;
//		cStopAll.anchor = GridBagConstraints.CENTER; // bottom of
//		// space
//		cStopTaskIf.insets = new Insets(0, 10, 0, 0);
//		panelStopTaskIf.add(menuBarChooseTimeRunning, cStopTaskIf);
//
//		// ======== panel Expire=========================
        JPanel panelExpire = new JPanel();
        GridBagConstraints cExpire = new GridBagConstraints();
        GridBagLayout layoutExpire = new GridBagLayout();
        panelExpire.setLayout(layoutExpire);
//
//		// Checkbox synchronize Stop
//		JCheckBox cbSynchronizeStop = new JCheckBox(
//				"Synchronize across time zones");
//		cExpire.fill = GridBagConstraints.HORIZONTAL;
//		cExpire.ipadx = 0; // reset to default
//		cExpire.ipady = 0; // reset to default
//		cExpire.gridy = 0;
//		cExpire.gridx = 2;
//		cExpire.anchor = GridBagConstraints.LINE_START; // bottom of
//		// space
//		cExpire.insets = new Insets(0, 0, 0, 0);
//		panelExpire.add(cbSynchronizeStop, cExpire);
//
//		// Checkbox Enable
//		JCheckBox cbEnable = new JCheckBox("Enable");
//		cExpire.fill = GridBagConstraints.HORIZONTAL;
//		cExpire.ipadx = 0; // reset to default
//		cExpire.ipady = 0; // reset to default
//		cExpire.gridy = 1;
//		cExpire.gridx = 0;
//		cExpire.anchor = GridBagConstraints.LINE_START; // bottom of
//		// space
//		cExpire.insets = new Insets(0, 0, 0, 0);
//		panelExpire.add(cbEnable, cExpire);
//
        // Label Save textField
        JLabel lbSaveFolder = new JLabel("Save folder");
        cExpire.fill = GridBagConstraints.HORIZONTAL;
        cExpire.ipadx = 0; // reset to default
        cExpire.ipady = 0; // reset to default
        cExpire.gridy = 2;
        cExpire.gridx = 0;
        cExpire.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cExpire.insets = new Insets(0, 10, 0, 0);
        panelExpire.add(lbSaveFolder, cExpire);

        // TextField Save map
        JTextField txtSaveFolder = new JTextField();
        cExpire.fill = GridBagConstraints.HORIZONTAL;
        cExpire.ipadx = 0; // reset to default
        cExpire.ipady = 0; // reset to default
        cExpire.gridy = 2;
        cExpire.gridx = 1;
        cExpire.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cExpire.insets = new Insets(0, 10, 0, 0);
        panelExpire.add(txtSaveFolder, cExpire);

        // TextField Button
        JButton btSaveFolder = new JButton("Save folder ");
        cExpire.fill = GridBagConstraints.CENTER;
        cExpire.ipadx = 0; // reset to default
        cExpire.ipady = 0; // reset to default
        cExpire.gridy = 2;
        cExpire.gridx = 2;
        cExpire.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cExpire.insets = new Insets(0, 10, 0, 0);
        panelExpire.add(btSaveFolder, cExpire);

        // Start Button
        JButton btStart = new JButton("START");
        cExpire.fill = GridBagConstraints.CENTER;
        cExpire.ipadx = 1; // reset to default
        cExpire.ipady = 0; // reset to default
        cExpire.gridy = 3;
        cExpire.gridx = 1;
        cExpire.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cExpire.insets = new Insets(10, 10, 0, 0);
        panelExpire.add(btStart, cExpire);

        // Start Button
        JButton btStOP = new JButton("STOP");
        cExpire.fill = GridBagConstraints.CENTER;
        cExpire.ipadx = 1; // reset to default
        cExpire.ipady = 0; // reset to default
        cExpire.gridy = 3;
        cExpire.gridx = 2;
        cExpire.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cExpire.insets = new Insets(10, 10, 0, 0);
        panelExpire.add(btStOP, cExpire);


//		// ========== add Panels to panel Advanced Setting=========
//		cAdvancedSetting.gridx = 0;
//		cAdvancedSetting.gridy = 0;
//		cAdvancedSetting.ipadx = 0; // reset to default
//		cAdvancedSetting.ipady = 0; // reset to default
//		cAdvancedSetting.anchor = GridBagConstraints.LINE_START; // bottom of
//																	// space
//		cAdvancedSetting.insets = new Insets(0, 0, 0, 0);
//		panelAdvancedSetting.add(panelDelayTask, cAdvancedSetting);
//
//		cAdvancedSetting.gridx = 0;
//		cAdvancedSetting.gridy = 1;
//		cAdvancedSetting.ipadx = 0; // reset to default
//		cAdvancedSetting.ipady = 0; // reset to default
//		cAdvancedSetting.anchor = GridBagConstraints.LINE_START; // bottom of
//																	// space
//		cAdvancedSetting.insets = new Insets(0, 0, 0, 0);
//		panelAdvancedSetting.add(panelRepeatTask, cAdvancedSetting);
//
//		cAdvancedSetting.gridx = 0;
//		cAdvancedSetting.gridy = 2;
//		cAdvancedSetting.ipadx = 0; // reset to default
//		cAdvancedSetting.ipady = 0; // reset to default
//		cAdvancedSetting.anchor = GridBagConstraints.LINE_START; // bottom of
//																	// space
//		cAdvancedSetting.insets = new Insets(0, 0, 0, 0);
//		panelAdvancedSetting.add(panelStopAll, cAdvancedSetting);
//
//		cAdvancedSetting.gridx = 0;
//		cAdvancedSetting.gridy = 3;
//		cAdvancedSetting.ipadx = 0; // reset to default
//		cAdvancedSetting.ipady = 0; // reset to default
//		cAdvancedSetting.anchor = GridBagConstraints.LINE_START; // bottom of
//																	// space
//		cAdvancedSetting.insets = new Insets(0, 0, 0, 0);
//		panelAdvancedSetting.add(panelStopTaskIf, cAdvancedSetting);
        cAdvancedSetting.fill = GridBagConstraints.HORIZONTAL;
        cAdvancedSetting.gridx = 0;
        cAdvancedSetting.gridy = 4;
        cAdvancedSetting.ipadx = 0; // reset to default
        cAdvancedSetting.ipady = 0; // reset to default
        cAdvancedSetting.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cAdvancedSetting.insets = new Insets(0, 0, 0, 0);
        panelAdvancedSetting.add(panelExpire, cAdvancedSetting);

        // ========== add Panel Setting to panel
        // Establish=======================
        // add Panel Setting to panel
        cEstablish.fill = GridBagConstraints.HORIZONTAL;
        cEstablish.gridx = 0;
        cEstablish.gridy = 0;
        cEstablish.ipadx = 1; // reset to default
        cEstablish.ipady = 0; // reset to default
        cEstablish.insets = new Insets(0, 0, 0, 0);
        panelEstablish.add(panelSetting, cEstablish);

        // Establish=======================
        // add Panel Advance Setting to panel
        cEstablish.fill = GridBagConstraints.HORIZONTAL;
        cEstablish.gridx = 0;
        cEstablish.gridy = 1;
        cEstablish.ipadx = 1; // reset to default
        cEstablish.ipady = 0; // reset to default
        cEstablish.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cEstablish.insets = new Insets(0, 0, 0, 0);
        panelEstablish.add(panelAdvancedSetting, cEstablish);

        frame.getContentPane().add(panelEstablish, BorderLayout.CENTER);
    }
}