/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import javax.swing.RepaintManager;

/**
 *
 * @author User
 */
public class ComponentPrinter implements Printable {
    final Component comp;
    
    public ComponentPrinter(Component comp){
        this.comp = comp;
    }
    @Override
    public int print(Graphics g, PageFormat format, int page_index) throws PrinterException {
        if (page_index > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        // get the bounds of the component
        /*Dimension dim = comp.getSize();
        double cHeight = dim.getHeight();
        double cWidth = dim.getWidth();

        // get the bounds of the printable area
        double pHeight = format.getImageableHeight();
        double pWidth = format.getImageableWidth();

        double pXStart = format.getImageableX();
        double pYStart = format.getImageableY();

        double xRatio = pWidth / cWidth;
        double yRatio = pHeight / cHeight;
        
        if(xRatio < yRatio) {
            yRatio = xRatio;
        }
        else {
            xRatio = yRatio;
        }


        Graphics2D g2 = (Graphics2D) g;
        g2.translate(pXStart, pYStart);
        g2.scale(xRatio, yRatio);
        // g2.drawImage(PrintUtils.getScreenShot(comp), 0, 0, (int) xRatio, (int) yRatio, null);
        
       comp.paintAll(g2);

        return Printable.PAGE_EXISTS;*/
        
        
        // get the bounds of the component
        Dimension dim = comp.getSize();
        double cHeight = dim.getHeight();
        double cWidth = dim.getWidth();

        // get the bounds of the printable area
        double pHeight = format.getImageableHeight();
        double pWidth = format.getImageableWidth();

        double pXStart = format.getImageableX();
        double pYStart = format.getImageableY();

        double xRatio = pWidth / cWidth;
        double yRatio = pHeight / cHeight;

        if (xRatio < yRatio) {
            yRatio = xRatio;
        } else {
            xRatio = yRatio;
        }

       BufferedImage image = PrintUtils.getScreenShot(comp);
        double width = comp.getWidth() * xRatio;
        double height = comp.getHeight() * yRatio;
        //BufferedImage resized = PrintUtils.resize(image, (int) width, (int) height);
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(pXStart, pYStart);
        g2.drawImage(image, 0, 0, (int) width, (int) height, null);
        return PAGE_EXISTS;
    }
}
