package com.aizong.ishtirak.common.misc.utils;

public abstract class VoidProgressAction  implements ProgressAction<Void>{

    @Override
    public Void action() {
	 apply();
	
	return null;
    }

    public abstract void apply();

    public abstract void success();

    @Override
    public void success(Void t) {
	success();
    }

    @Override
    public void failure(Exception e) {
	
    }

}
