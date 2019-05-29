package com.tpbank.botaml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.tpbank.GUI.AML_BOTview;

public class TestInput {
	public static String folderpdfexport;

	// path = "log/20-05-2019"
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
					//System.out.println("Thư mục tồn tại");
					return true;
				} else {
					continue;
				}
			}
		}
		return false;
	}

	public static void Amain() throws InterruptedException {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("dd-M-yyyy");
		folderpdfexport = df.format(date);

		New6.doLogin();
		String rootFolderLog = "./log";
		String rootfolderInput = "./input";
		String currentFolderInput = "28-5-2019";
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
					if (lstFileIn.size() > 0) {
						readNewFileAdded(pathInput, pathFileLog, lstFileIn);
					}
					// Trường hợp không thêm chỉ sửa lỗi dọc file
					else {
						modifyErrorReadFile(pathFileLog, pathInput);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("File " + fileLog
						+ " không tồn tại trong thư mục " + pathFileLog);
				try {
					if (f.createNewFile())
						System.out
								.println(" Đã Tạo file log thành công trong !"
										+ pathFileLog);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			readNewLog(currentFolderInput, pathLog, pathInput, pathFileLog);
			Thread.sleep(2000);
			New6.driver.quit();
		}
	}

	@SuppressWarnings("unused")
	private static void readNewLog(String currentFolderInput, String pathLog,
			String pathInput, String pathFileLog) throws InterruptedException {
		try {
			File folder = new File(pathInput);
			File[] listOfFiles = folder.listFiles();
			if (listOfFiles.length == 0) {
				System.out.println("Không có File dữ liệu trong thư mục "
						+ currentFolderInput);
			} else {
				File dir2 = new File(pathLog);
				boolean created = dir2.mkdirs();
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

						New6.doProcess(str_Type_Guest_Name.toUpperCase(),
								str_Guest_Name);

						// ====================================================================

						// ===========================================

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unused", "resource" })
	private static void modifyErrorReadFile(String pathFileLog, String pathInput)
			throws FileNotFoundException, InterruptedException {
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
						// noi viet chuong trinh
						New6.doProcess(str_Type_Guest_Name.toUpperCase(),
								str_Guest_Name);

						// ====================================================================

						// ===========================================
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
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				br.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unused")
	private static void readNewFileAdded(String pathInput, String pathFileLog,
			ArrayList<String> lstFileIn) throws FileNotFoundException,
			IOException, InterruptedException {
		File folder = new File(pathInput);
		File[] listOfFiles = folder.listFiles();
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

				New6.doProcess(str_Type_Guest_Name.toUpperCase(),
						str_Guest_Name);

				// ====================================================================

				// ===========================================
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
		} catch (IOException e) {
			e.printStackTrace();
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

}
