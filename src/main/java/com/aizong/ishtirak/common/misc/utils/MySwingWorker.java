package com.aizong.ishtirak.common.misc.utils;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

public class MySwingWorker<T> extends SwingWorker<T,Void> {

    protected ProgressAction<T> progressAction;
    
    public MySwingWorker(ProgressAction<T> progressAction) {
	super();
	this.progressAction = progressAction;
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
        try {
	    progressAction.success(get());
	} catch (InterruptedException | ExecutionException e) {
	    progressAction.failure(e);
	}
    }
    
}
