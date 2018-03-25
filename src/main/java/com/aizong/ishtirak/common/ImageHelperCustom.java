package com.aizong.ishtirak.common;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ImageHelperCustom extends ImageHelper {

    private final static String IMAGE_URL = "images/";

    static ImageHelperCustom instance;

    public static ImageHelperCustom get() {
        if (instance == null) {
            instance = new ImageHelperCustom(IMAGE_URL);
        }

        return instance;
    }

    public ImageHelperCustom(String imageUrl) {
        super(imageUrl);
    }

    
    public Image getFrameImage()
    {
        return getImage("maintenance.png");
    }
    
     public ImageIcon getFrameIcon64()
    {
        return getImageIcon("maintenance32.png");
    }
    
   
}
