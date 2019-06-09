package com.tpb.bot;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.tpb.bot.job.ScreeningJob;
import com.tpb.bot.ui.BotUI;

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
