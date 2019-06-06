package com.tpb.bot.util;

public class LogFileDownload {
	public String datefiledownload;
	public String type;
	public String name;
	public String status;
	public String filepathfolder;
	public String note;

	public LogFileDownload(String datefiledownload, String type, String name,
			 String status,String filepathfolder, String note) {
		super();
		this.datefiledownload = datefiledownload;
		this.type = type;
		this.name = name;
		this.status = status;
		this.filepathfolder = filepathfolder;
		this.note = note;
	}
	
	

	public String getDatefiledownload() {
		return datefiledownload;
	}



	public void setDatefiledownload(String datefiledownload) {
		this.datefiledownload = datefiledownload;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getFilepathfolder() {
		return filepathfolder;
	}



	public void setFilepathfolder(String filepathfolder) {
		this.filepathfolder = filepathfolder;
	}



	public String getNote() {
		return note;
	}



	public void setNote(String note) {
		this.note = note;
	}



	public LogFileDownload() {
		super();
	}



	@Override
	public String toString() {
		return datefiledownload + "@" + type + "@" + name + "@"
				+ status	+ "@" + filepathfolder  + "@" + note + "\n";
	}
	
}
