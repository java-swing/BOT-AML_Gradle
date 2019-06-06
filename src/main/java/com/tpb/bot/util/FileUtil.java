package com.tpb.bot.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.PropertyConfigurator;

import com.tpb.bot.constant.Constant;
import com.tpb.bot.job.ScreeningJob;
import com.tpb.bot.ui.BotUI;

public class FileUtil {

	public static String downloadFilepath = BotUI.downloadFolder;

	public static String folderTodayPath() {
		String folderToday;
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		folderToday = df.format(date);
		return folderToday;
	}

	public static boolean checkFolderLog(String folderRoot, String curentFoder) {
		File[] directories = new File(folderRoot).listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});

		for (int i = 0; i < directories.length; i++) {
			if (directories[i] != null) {
				// System.out.println(directories[i].getName());
				if (directories[i].getName().equals(curentFoder)) {
					System.out.println("Đã đọc hết dữ liệu cũ");
					ScreeningJob.displayAndWriteLog("Đã đọc hết dữ liệu cũ");
					return true;
				} else {
					continue;
				}
			}
		}
		return false;
	}

	private static ArrayList<String> getDiffFile(String pathInput,
			String pathFileLog, ArrayList<String> lstNameFile,
			ArrayList<String> lstFileIn) {
		File flog = new File(pathFileLog);
		FileReader fr;
		try {
			fr = new FileReader(flog);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				String str_General_information = line.trim();
				String[] information = str_General_information.split("\\|");
				String str_Type_Guest_Name = information[0].trim();
				lstNameFile.add(str_Type_Guest_Name);
			}
			fr.close();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ScreeningJob.displayAndWriteLogError(e);
		} catch (IOException e) {
			e.printStackTrace();
			ScreeningJob.displayAndWriteLogError(e);
		}
		File folderIn = new File(pathInput);
		File[] lstFile = folderIn.listFiles();
		for (int i = 0; i < lstFile.length; i++) {
			lstFileIn.add(lstFile[i].getName());
		}
		lstFileIn.removeAll(lstNameFile);
		return lstFileIn;
	}

	private static String checkStatus(int i, File txtFile, String status) {
		String content;
		if (i == 1) {
			content = txtFile.getName() + "|" + status;
		} else {
			content = "\n" + txtFile.getName() + "|" + status;
		}
		return content;
	}

	private static void readNewFileAdded(String pathInput, String pathFileLog,
			ArrayList<String> lstFileIn) throws FileNotFoundException,
			IOException, InterruptedException, ClassNotFoundException,
			SQLException {
		for (String namefile : lstFileIn) {
			FileReader fRead = new FileReader(new File(pathInput + "/"
					+ namefile));
			BufferedReader bRead = new BufferedReader(fRead);
			String lineFile;
			BufferedWriter bw = new BufferedWriter(new FileWriter(pathFileLog,
					true));
			String content = "";
			boolean lagCheckdone = true;
			while ((lineFile = bRead.readLine()) != null) {
				String str_General_information = lineFile.trim();
				String[] information = str_General_information.split("\\|");
				String str_Guest_Name = information[0].trim();
				String str_Type_Guest_Name = information[1].trim();
				// noi viet chuong trinh

				try {
					ScreeningJob.doProcess(str_Type_Guest_Name.toUpperCase(),
							str_Guest_Name);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ScreeningJob.displayAndWriteLogError(e);
				}
			}
			fRead.close();
			bRead.close();
			if (lagCheckdone) {
				content = "\n" + namefile + "|" + "done";
			} else {
				content = "\n" + namefile + "|" + "false";
			}

			bw.write(content);
			bw.close();
		}
		ScreeningJob.driver.quit();
	}

	private static void modifyErrorReadFile(String pathFileLog, String pathInput)
			throws FileNotFoundException, InterruptedException,
			ClassNotFoundException, SQLException {
		FileReader fr1 = new FileReader(pathFileLog);
		BufferedReader br = new BufferedReader(fr1);
		FileWriter writer = null;
		try {
			String line = br.readLine();
			String oldContent = "";
			while (line != null) {
				if (line.contains("false")) {
					String fError = line.split("\\|")[1];
					String fName = line.split("\\|")[0];

					FileReader fRead = new FileReader(new File(pathInput + "/"
							+ fName));
					BufferedReader bRead = new BufferedReader(fRead);
					String lineFile;
					while ((lineFile = bRead.readLine()) != null) {
						String str_General_information = lineFile.trim();
						String[] information = str_General_information
								.split("\\|");
						String str_Guest_Name = information[0].trim();
						String str_Type_Guest_Name = information[1].trim();
						// noi viet chuong trinh
						ScreeningJob.doProcess(
								str_Type_Guest_Name.toUpperCase(),
								str_Guest_Name);
					}
					fRead.close();
					bRead.close();
					// Thực hiện chương trình với đầu vào là tên file bi lỗi
					System.out.println(fName);
					System.out.println(fError);
					if (line.contains(fError))
						oldContent = oldContent + line.replace("false", "done")
								+ "\n";
					line = br.readLine();
				} else {
					oldContent = oldContent + line + "\n";
					line = br.readLine();
				}
			}
			String newContent = oldContent
					.substring(0, oldContent.length() - 1);
			writer = new FileWriter(pathFileLog);
			writer.write(newContent);
		} catch (IOException e) {
			e.printStackTrace();
			ScreeningJob.displayAndWriteLogError(e);
		} finally {
			try {
				br.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				ScreeningJob.displayAndWriteLogError(e);
			}
		}
		ScreeningJob.driver.quit();

	}

	private static void readNewLog(String currentFolderInput, String pathLog,
			String pathInput, String pathFileLog) throws InterruptedException,
			ClassNotFoundException, SQLException {
		try {
			File folder = new File(pathInput);
			File[] listOfFiles = folder.listFiles();
			if (listOfFiles.length == 0) {
				System.out.println("Không có File dữ liệu trong thư mục "
						+ currentFolderInput);
			} else {
				File dir2 = new File(pathLog);
				dir2.mkdirs();
				folder = new File(pathFileLog);
				folder.createNewFile();
				int i = 1;
				for (File txtFile : listOfFiles) {
					System.out.println(txtFile.getName());
					File fInput = new File(pathInput + "/" + txtFile.getName());
					FileReader fRead = new FileReader(fInput);
					BufferedReader bRead = new BufferedReader(fRead);
					String lineFile;

					BufferedWriter bw = new BufferedWriter(new FileWriter(
							pathFileLog, true));
					String content = "";
					boolean lagCheckdone = true;
					while ((lineFile = bRead.readLine()) != null) {
						String str_General_information = lineFile.trim();
						String[] information = str_General_information
								.split("\\|");
						String str_Guest_Name = information[0].trim();
						String str_Type_Guest_Name = information[1].trim();
						System.out.println(str_Guest_Name);
						System.out.println(str_Type_Guest_Name);
						// noi viet chuong trinh
						ScreeningJob.doProcess(
								str_Type_Guest_Name.toUpperCase(),
								str_Guest_Name);
					}

					fRead.close();
					bRead.close();
					if (lagCheckdone) {
						content = checkStatus(i, txtFile, "done");
					} else {
						content = checkStatus(i, txtFile, "false");
					}
					bw.write(content);
					bw.close();
					i++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();

			ScreeningJob.displayAndWriteLogError(e);

		}
		ScreeningJob.driver.quit();
	}

	public static void saveFileDownload(String typeGuest, String name)
			throws ClassNotFoundException, SQLException, InterruptedException {

		PropertyConfigurator.configure("src/log4j.properties");

		String dateString1 = folderTodayPath();
		String status = "";
		String note = " ";

		Date date2 = new Date();
		DateFormat ddateformat2 = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String dateString2 = ddateformat2.format(date2);
		File srcFile = new File(BotUI.downloadFolder + "/"
				+ "CaseDossierReport.pdf");
		//
		String filec = BotUI.downloadFolder + "/" + dateString1 + "/"
				+ dateString2 + "_" + typeGuest + "_" + name + ".pdf";
		// File đích (Destination file).
		File destFile = new File(filec);
		System.out.println(filec);
		Thread.sleep(1000);
		if (srcFile.exists()) {
			// Tạo thư mục cha của file đích.
			destFile.getParentFile().mkdirs();
			boolean renamed = srcFile.renameTo(destFile);
			System.out.println("Renamed: " + renamed);
			ScreeningJob.displayAndWriteLog("Đổi tên file tải về thành công.");
			status = "Thành công.";
			LocalTime finishLogJob = LocalTime.now();

			DecimalFormat df = new DecimalFormat("#.00");
			Double secondBetween = Double.valueOf(ChronoUnit.MILLIS.between(
					ScreeningJob.startLogJob, finishLogJob)
					/ 1000
					+ Constant.TIME_SLEEP_IN_JOB / 1000);
			BotUI.printLogOut = ("\n\t\tChu kỳ hoạt động: "
					+ df.format(secondBetween) + " giây \n");
			ScreeningJob.displayAndWriteLog(BotUI.printLogOut);
			// FunctionFactory.loadMonitor(BotUI.printLogOut);

		} else {
			System.out.println("Khong doi ten thu muc.");
			status = "Thất bại";
			filec = " ";
			note = "Không tìm thấy file.";
			ScreeningJob.displayAndWriteLog("Không tìm thấy file.");
		}
		// QueryJobToExportLog.queryStringInsertData(typeGuest, dateString3,
		// name,
		// status, filec, note);
		ReadWriteFileLogDownLoad.createLogFileDownload(dateString1, typeGuest,
				name, status, filec, note);
	}

	public static void runBot() {
		PropertyConfigurator.configure("src/log4j.properties");
		ScreeningJob.doLogin();

		String rootFolderLog = "./log";
		String rootfolderInput = "./input";
		String currentFolderInput = folderTodayPath();
		String pathLog = rootFolderLog + "/" + currentFolderInput;
		String pathInput = rootfolderInput + "/" + currentFolderInput;
		String fileLog = "log.txt";
		String pathFileLog = pathLog + "/" + fileLog;
		File f = new File(pathFileLog);
		if (checkFolderLog(rootFolderLog, currentFolderInput)) {
			if (f.exists()) {
				try {
					ArrayList<String> lstNameFile = new ArrayList<String>();
					ArrayList<String> lstFileIn = new ArrayList<String>();
					// Lấy ra file mới
					lstFileIn = getDiffFile(pathInput, pathFileLog,
							lstNameFile, lstFileIn);
					// Trương hợp là thêm file mới
					try {
						if (lstFileIn.size() > 0) {
							readNewFileAdded(pathInput, pathFileLog, lstFileIn);
						}
						// Trường hợp không thêm chỉ sửa lỗi dọc file
						else {
							modifyErrorReadFile(pathFileLog, pathInput);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ScreeningJob.displayAndWriteLogError(e);
					}
				} catch (Exception e) {
					e.printStackTrace();
					try {
						ScreeningJob.displayAndWriteLogError(e);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						ScreeningJob.displayAndWriteLogError(e1);
					}
				}
			} else {
				System.out.println("File " + fileLog
						+ " không tồn tại trong thư mục " + pathFileLog);
				ScreeningJob.displayAndWriteLog("Không tìm thấy file.");

				try {
					if (f.createNewFile())
						System.out
								.println(" Đã Tạo file log thành công trong !"
										+ pathFileLog);
				} catch (IOException e) {
					e.printStackTrace();
					ScreeningJob.displayAndWriteLogError(e);
				}
			}
		} else {
			try {
				readNewLog(currentFolderInput, pathLog, pathInput, pathFileLog);

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				ScreeningJob.displayAndWriteLogError(e);
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
				ScreeningJob.displayAndWriteLogError(e);
			} catch (SQLException e) {
				e.printStackTrace();
				ScreeningJob.displayAndWriteLogError(e);
			}
		}
	}

}
