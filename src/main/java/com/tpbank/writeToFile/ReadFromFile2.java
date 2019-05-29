package com.tpbank.writeToFile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

public class ReadFromFile2 {

	String logs = null;
	public void ReadFileAndSplit () throws Exception {
		// We need to provide file path as the parameter:
		// double backquote is to avoid compiler interpret words
		// like \test as \t (ie. as a escape sequence)
		File file = new File("SaveEstablishText\\saveEstablish.txt");

		BufferedReader br = new BufferedReader(new FileReader(file));

		String st;
		System.out.println("=====================================================");
		System.out.println("String has split by \"/\" ");
		while ((st = br.readLine()) != null) {
			String s1 = st;
			String[] words = s1.split("/");
			for (String w : words) {
				System.out.println(w);
			}
		}
	}
	
	public String ReadLog() {
		 File file = new File("log\\testlog.log");
		 String logs = "";
	      try
	      {
	          if (file.exists()) {
	        	  BufferedReader br = new BufferedReader(new FileReader(file));
	        	  String st;
	        	  while ((st = br.readLine()) != null) {
	        		  String log = st;
	        		  logs += log + "\n";
	        	  }
	          }
	         
	      } catch (IOException e)
	      {
	         e.printStackTrace();
	      }
	      return logs;
	}
	
	/*public String autoLoad() {
		Timer auto = new Timer(0, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				logs = ReadLog();
			}
		});
		auto.setDelay(1000);
		auto.start();
		return logs;
	}*/
}