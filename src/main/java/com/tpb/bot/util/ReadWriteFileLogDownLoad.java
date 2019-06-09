package com.tpb.bot.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

 
public class ReadWriteFileLogDownLoad { 
	private static LogFileDownload logaml;
	
	private static final String FILENAME = "log/downloadfile.txt";
	
	public static void main(String[] args){
		createLogFileDownload("1","2","3","4","5","6");
	}

	public static void createLogFileDownload(String date,String type,String name, String status, String folder, String note){
		String content="";	
		logaml =new LogFileDownload(date, type, name, status, folder, note);
		content = logaml.toString();
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(FILENAME, true);
			bw = new BufferedWriter(fw);	
			bw.write(content);
			//System.out.println("Ghi them noi dung file xong!");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}