package com.aizong.ishtirak.gui;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFileChooser;

import com.aizong.ishtirak.common.misc.utils.MessageUtils;

public class DatabaseUtils {

    public static void export(Component owner, String filePath) throws IOException, InterruptedException {

	JFileChooser fc = new JFileChooser();
	fc.setSelectedFile(new File(filePath+"_backup.sql"));
	int returnVal = fc.showSaveDialog(owner);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    File file = fc.getSelectedFile();

	    /******************************************************/
	    // Database Properties
	    /******************************************************/
	    String dbName = "ishtirak";
	    String dbUser = "root";
	    String dbPass = "manager";

	    /***********************************************************/
	    // Execute Shell Command
	    /***********************************************************/
	    String executeCmd = "";
	    executeCmd = "mysqldump -u " + dbUser + " -p" + dbPass + "  " + dbName + " -r "+file.getPath();

	    Process runtimeProcess;
	    runtimeProcess = Runtime.getRuntime().exec(executeCmd);
	    int processComplete = runtimeProcess.waitFor();
	    if (processComplete == 0) {
		
		MessageUtils.showInfoMessage(owner, "Backup taken successfully");

	    } else {
		MessageUtils.showErrorMessage(owner, "Could not take mysql backup");

	    }
	}
    }

    public static void main(String[] args) {
	LocalDateTime dateTime = LocalDateTime.now();
	String format = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	try {
	    DatabaseUtils.export(null, format);
	} catch (IOException | InterruptedException e) {
	    e.printStackTrace();
	}
    }
}
