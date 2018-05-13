package com.aizong.ishtirak.common.misc.utils;

import javax.swing.SwingWorker;

import com.aizong.ishtirak.common.form.MyProgressBar;

public class MySwingWorker<T> extends SwingWorker<T,Void> {

    protected ProgressAction<T> progressAction;
    MyProgressBar myProgressBar;
    public MySwingWorker(ProgressAction<T> progressAction) {
	super();
	myProgressBar = new MyProgressBar();
	this.progressAction = progressAction;
	myProgressBar.setAlwaysOnTop(true);
	myProgressBar.setVisible(true);
    }
    
    public static <T> void execute(ProgressAction<T> progressAction) {
	MySwingWorker<T> mySwingWorker = new MySwingWorker<>(progressAction);
	mySwingWorker.execute();
    }

    @Override
    protected T doInBackground() throws Exception {
	return progressAction.action();
    }

    @Override
    protected void done() {
        super.done();
        myProgressBar.dispose();
        try {
	    progressAction.success(get());
	} catch (Exception e) {
	    progressAction.failure(e);
	}
    }
    
}
