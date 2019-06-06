package com.tpb.bot.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextArea;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.tpb.bot.constant.Constant;
import com.tpb.bot.job.ScreeningJob;
import com.tpb.bot.ui.BotUI;

public class LogFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		write("sbi_upload.txt", "test_123.txt");
		write("sbi_upload.txt", "test_456.txt");

		List<String> list = read("sbi_upload.txt");
		for (String s : list) {
			System.out.println(s);
		}
	}

	public synchronized static void write(String fileName, String line) {
		// SimpleDateFormat simpleDateFormat = new
		// SimpleDateFormat(Constant.DATE_FORMAT);
		// String today = simpleDateFormat.format(new Date());
		// String folderPath = Config.getParam("result.log.path") + "/" + today;
		//
		// if (!Util.checkFolderExists(folderPath)) {
		// Util.makeDir(folderPath);
		// }
		//
		// File f = new File(folderPath + "/" + fileName);
		// try {
		// BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
		// bw.append(line + "\r\n");
		// bw.close();
		// } catch (IOException e) {
		// System.out.println(e.toString());
		// }
	}

	public synchronized static List<String> read(String fileName) {
		// SimpleDateFormat simpleDateFormat = new
		// SimpleDateFormat(Constant.DATE_FORMAT);
		// String today = simpleDateFormat.format(new Date());
		// String filePath = Config.getParam("result.log.path") + "/" + today +
		// "/" + fileName;
		//
		// List<String> list = null;
		//
		// try {
		// File f = new File(filePath);
		// BufferedReader reader = new BufferedReader(new FileReader(f));
		//
		// list = new ArrayList<String>();
		// String line = null;
		// while ((line = reader.readLine()) != null) {
		// list.add(line);
		// }
		// reader.close();
		// } catch (IOException e) {
		// System.out.println(e.toString());
		// }
		//
		// return list;

		return null;
	}

	public static void saveTextEstablish(String saveString) {

		FileOutputStream fop = null;
		File file;
		String content = saveString;

		try {

			file = new File(Constant.SAVE_ESTABLISH_PATH);
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

			System.out.println("Save Config Done");

		} catch (IOException e) {
			e.printStackTrace();
			ScreeningJob.displayAndWriteLogError(e);
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				ScreeningJob.displayAndWriteLogError(e);
			}
		}
	}

	public static void saveLogFromDBToTxt (String saveString, String pathOfSaveFolder) {

		FileOutputStream fop = null;
		File file;
		String content = saveString;

		try {

			file = new File(pathOfSaveFolder);
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

			System.out.println("Save data Done");

		} catch (IOException e) {
			e.printStackTrace();
			ScreeningJob.displayAndWriteLogError(e);
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				ScreeningJob.displayAndWriteLogError(e);
			}
		}
	}
	
	public static void loadingRecentStatus(JCheckBox cbMonday,
			Boolean cbMondayStatus, JCheckBox cbSunday, Boolean cbSundayStatus,
			JCheckBox cbTuesday, Boolean cbTuesdayStatus,
			JCheckBox cbWednesday, Boolean cbWednesdayStatus,
			JCheckBox cbThursday, Boolean cbThursdayStatus, JCheckBox cbFriday,
			Boolean cbFridayStatus, JCheckBox cbSaturday,
			Boolean cbSaturdayStatus, JSpinner textIntervalPeriod,
			String textIntervalPeriodValue, JRadioButton r1, Boolean r1Status,
			JRadioButton r2, Boolean r2Status, DateTimePicker dateTimeStart,
			LocalDate dateStart, String timeStart,
			DateTimePicker dateTimePickerStop, LocalDate dateStop,
			String timeStop, JTextArea textSaveFolder, String savePath)
			throws Exception {
		setStatusAndVisibleOfRadioButtonFrequency(r1, r1Status);

		setStatusAndVisibleOfRadioButtonFrequency(r2, r2Status);

		textIntervalPeriod.setValue(Integer.parseInt(textIntervalPeriodValue));

		if (r1.isSelected()) {
			BotUI.period = 1;
			textIntervalPeriod.setEnabled(false);
			System.out.println(BotUI.period);
		} else if (r2.isSelected()) {
			if (textIntervalPeriod.getValue().toString().equals("0")) {
				BotUI.period = 24 * 60;
			} else {
				BotUI.period = Integer.parseInt(textIntervalPeriodValue);
			}
		}
	}

	static void setStatusAndVisibleOfRadioButtonFrequency(JRadioButton r,
			Boolean rStatus) {
		r.setSelected(rStatus);
		r.setEnabled(rStatus);
	}
	
	public class SingleTaskTimer {
	    private Timer timer = new Timer();
	    private TimerTask task = null;

	    public void setTask(TimerTask task) {
	        this.task = task;
	    }

	    public void schedule(Date start, Date end, long period) {
	        SingleTaskTimer self = this;
	        timer = new Timer();
	        if (task == null) {
	            throw new IllegalStateException("Task not specified");
	        } 
	        else {
	        		timer.schedule(task, start, period);
		            timer.schedule(new TimerTask() {
		                @Override
		                public void run() {
		                    self.cancel();
		                }
		            }, end);
	        }
	    }

	    public void cancel() {
	        try {
	        	System.out.println("Timer cancel");
	            timer.cancel();
	            timer.purge();
	        } catch (IllegalStateException e) {
	            //Timer already cancelled
	        	ScreeningJob.displayAndWriteLogError(e);
	        } finally {
	            timer = new Timer();
	        }
	    }
	}

}
