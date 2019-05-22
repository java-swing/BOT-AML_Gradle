package com.tpbank.writeToPdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveEstablishInTxtFile {
	private String saveString;
	
	public String getSaveString() {
		return saveString;
	}

	public void setSaveString(String saveString) {
		this.saveString = saveString;
	}

	
	public SaveEstablishInTxtFile(String saveString) {
		super();
		this.saveString = saveString;
	}

	public void saveTextEstablish(String saveString) {
		 
		FileOutputStream fop = null;
		File file;
		String content = saveString;
 
		try {
 
			file = new File("SaveEstablishText\\saveEstablish.txt");
			fop = new FileOutputStream(file);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
 
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
