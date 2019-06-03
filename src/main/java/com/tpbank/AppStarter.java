package com.tpbank;

import javax.swing.SwingUtilities;

import com.tpbank.job.ScreeningJob;
import com.tpbank.ui.BotUI;
import org.apache.log4j.Logger;


public class AppStarter {

	public static final Logger logger = Logger.getLogger(AppStarter.class);

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new BotUI();
				} catch (Exception e) {
					e.printStackTrace();
					ScreeningJob.displayAndWriteLogError(e);
				}
				
			}
		});
	}

}
