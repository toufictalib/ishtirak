package com.aizong.ishtirak.common.misc.utils;

import javax.swing.ImageIcon;

public enum Mode {

    VIEW, NEW, UPDATE, DELETE;
    
    public ImageIcon getImage() {
	switch(this) {
	case UPDATE:
	    return ImageUtils.getEditIcon();
	case  VIEW:
	    return  ImageUtils.getViewIcon();
	case NEW:
	    return ImageUtils.getAddIcon();
	case DELETE:
	    return ImageUtils.getDeleteIcon();
	    default:
		throw new UnsupportedOperationException(Mode.class.getSimpleName()+"=>"+this+" is not supported yet");
	}
	
	
    }

}
