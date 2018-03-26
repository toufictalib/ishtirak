/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *
 * @author c.simon
 */
public class PrintUtils {
      public static BufferedImage getScreenShot(
    Component component) {

    BufferedImage image = new BufferedImage(
      component.getWidth(),
      component.getHeight(),
      BufferedImage.TYPE_INT_RGB
      );
    // call the Component's paint method, using
    // the Graphics object of the image.
    component.paint( image.getGraphics() ); // alternately use .printAll(..)
    return image;
  }
      public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = dimg.createGraphics();
    g2d.drawImage(tmp, 0, 0, null);
    g2d.dispose();

    return dimg;
}  
}
