package com.aizong.ishtirak.common.misc.utils;

import java.awt.Image;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageHelper {

    protected String imageUrl;

    static ImageHelper instance;

    public ImageHelper(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ImageIcon getImageIcon(String url) {
        return new ImageIcon(getImage(url));
    }

    public Image getImage(String path) {
        Image image = null;
        try {

            String url = imageUrl + path;
            image = ImageIO.read(getUrl(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    private URL getUrl(String path) {
        URL url = getClass().getResource(path);
        if (url == null) {
            url = getClass().getClassLoader().getResource(path);
        }
        return url;
    }

}
