package com.aizong.ishtirak.common.misc.utils;

public class VoidSwingWorker extends MySwingWorker<Void> {

    public VoidSwingWorker(VoidProgressAction voidProgressAction) {
	super(voidProgressAction);
    }

    public static void execute(VoidProgressAction progressAction) {
	VoidSwingWorker mySwingWorker = new VoidSwingWorker(progressAction);
	mySwingWorker.execute();
    }

}
