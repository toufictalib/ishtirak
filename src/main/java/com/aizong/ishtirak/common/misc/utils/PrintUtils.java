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

    public static boolean printLandscapeComponent(Component component) throws PrinterException {
	return printComponent(component, PageFormat.LANDSCAPE);
    }
    
    public static boolean printProtraitComponent(Component component) throws PrinterException {
	return printComponent(component, PageFormat.PORTRAIT);
    }
    public static boolean printComponent(Component component, int orientation) throws PrinterException {
	PrinterJob pj = PrinterJob.getPrinterJob();

	PageFormat pf = pj.defaultPage();
	Paper paper = new Paper();
	pf.setOrientation(orientation);
	PageFormat postformat = pj.pageDialog(pf);
	if (pf != postformat) {
	    double margin = 36; // half inch
	    paper.setImageableArea(margin, margin, paper.getWidth() - margin * 2, paper.getHeight() - margin * 2);
	    pf.setPaper(paper);
	    pj.setPrintable(new ComponentPrinter(component), pf);
	    if (pj.printDialog()) {
		pj.print();
		return true;
	    }
	}
	return false;
    }

    /*
     * private void print() { PrinterJob pjob = PrinterJob.getPrinterJob();
     * PageFormat preformat = pjob.defaultPage();
     * preformat.setOrientation(PageFormat.LANDSCAPE); PageFormat postformat =
     * pjob.pageDialog(preformat); //If user does not hit cancel then print. if
     * (preformat != postformat) { //Set print component pjob.setPrintable(new
     * ComponentPrinter(ResultForm.this), postformat); if (pjob.printDialog()) {
     * try { pjob.print(); } catch (PrinterException e1) { // TODO
     * Auto-generated catch block e1.printStackTrace(); } } } }
     */

}
