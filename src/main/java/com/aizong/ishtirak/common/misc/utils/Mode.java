package com.aizong.ishtirak.common.misc.utils;

import javax.swing.ImageIcon;

public enum Mode {

    VIEW, NEW, UPDATE;
    
    public ImageIcon getImage() {
	switch(this) {
	case UPDATE:
	    return ImageUtils.getEditIcon();
	case  VIEW:
	    return  ImageUtils.getViewIcon();
	case NEW:
	    return ImageUtils.getAddIcon();
	    default:
		throw new UnsupportedOperationException(Mode.class.getSimpleName()+"=>"+this+" is not supported yet");
	}
	
	
    }

}
