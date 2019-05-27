package com.tpbank.writeToFile;

import java.io.*;

public class LoadingLastStatus {
	public void ReadFileAndSplit () throws Exception {
		// We need to provide file path as the parameter:
		// double backquote is to avoid compiler interpret words
		// like \test as \t (ie. as a escape sequence)
		File file = new File("/SaveEstablishText/saveEstablish.txt");

		BufferedReader br = new BufferedReader(new FileReader(file));

		String st;
		System.out.println("=====================================================");
		System.out.println("String has split by \"//br\" ");
		while ((st = br.readLine()) != null) {
			String s1 = st;
			String[] words = s1.split("//br");
			for (String w : words) {
				System.out.println(w);
			}
		}
	}
}