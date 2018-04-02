/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.common.misc.utils;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

/**
 *
 * @author c.simon
 */
public class PrintUtils {
    public static BufferedImage getScreenShot(Component component) {

	BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(),
		BufferedImage.TYPE_INT_RGB);
	// call the Component's paint method, using
	// the Graphics object of the image.
	component.paint(image.getGraphics()); // alternately use .printAll(..)
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
    
    public static void printComponent(Component component)
    {
	PrinterJob pj = PrinterJob.getPrinterJob();

	PageFormat pf = pj.defaultPage();
	Paper paper = new Paper();
	double margin = 36; // half inch
	paper.setImageableArea(margin, margin, paper.getWidth() - margin * 2, paper.getHeight()
	    - margin * 2);
	pf.setPaper(paper);
	pj.setPrintable(new ComponentPrinter(component), pf);
	if (pj.printDialog()) {
	      try {
	        pj.print();
	      } catch (PrinterException e1) {
	        System.out.println(e1);
	      }
	    }
    }
    
}
