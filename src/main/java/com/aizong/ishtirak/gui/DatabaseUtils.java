package com.aizong.ishtirak.gui;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import org.springframework.beans.factory.annotation.Value;

import com.aizong.ishtirak.common.misc.utils.MessageUtils;
import com.aizong.ishtirak.common.misc.utils.ServiceProvider;

public class DatabaseUtils {
    
    @Value("${spring.datasource.url}")
    private String dbName ;
    
    @Value("${spring.datasource.username}")
    private String dbUser;
    
    @Value("${spring.datasource.password}")
    private String dbPass;

    @Value("${mysqldump.path}")
    private String mysqldumpPath;
    
    public void export(Component owner, String filePath){

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
	    executeCmd = ((mysqldumpPath==null || mysqldumpPath.isEmpty()) ? "" :mysqldumpPath)+"mysqldump -u " + dbUser + " -p" + dbPass + "  " + dbName + " -r "+file.getPath();

	    Process runtimeProcess;
	    
	    int processComplete;
	    try {
		runtimeProcess = Runtime.getRuntime().exec(executeCmd);
		processComplete = runtimeProcess.waitFor();
		 if (processComplete == 0) {
			
			MessageUtils.showInfoMessage(owner, ServiceProvider.get().getMessage().getMessage("backup.success"));

		    } else {
			MessageUtils.showErrorMessage(owner, ServiceProvider.get().getMessage().getMessage("backup.failure"));

		    }
	    } catch (InterruptedException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	   
	}
    }

}
