package com.psychicorigami.scene;

import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsConfiguration;
import java.awt.Transparency;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.BufferedImageOp;
import java.util.List;
import java.util.ArrayList;

import java.awt.image.*;

public class Layer {
    private BufferedImage backing = null;
    private int[] source = null;
    private int[] destination = null;
    
    private List<Shape> shapes = new ArrayList<Shape>();
        
    public void add(Shape shape) {
        shapes.add(shape);
        setBacking(500, 500);
    }
    
    public void setBacking(int width, int height) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
        backing = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        source = new int[width*height];
        destination = new int[width*height];
    }
    
    public void paint(Graphics2D g) {
        if ( backing == null ) {
            paintShapes(g);
        }
        else {            
            Graphics2D gb = backing.createGraphics();
            gb.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // set to all transparent
            gb.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
            gb.fillRect(0, 0, backing.getWidth(), backing.getHeight());
            
            // now render normally
            gb.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            
            paintShapes(gb);
            
            gb.setComposite(AlphaComposite.SrcIn);
            gb.setColor(Color.BLACK);
            gb.fillRect(0, 0, backing.getWidth(), backing.getHeight());
            
            gb.dispose();
            
            BufferedImage blurred = blur(backing);
            
            // render shadow
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g.drawImage(blurred, 1, -1, null);
            
            // draw original
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            paintShapes(g);
        }
    }
    
    public BufferedImage blur(BufferedImage img) {
        int width = img.getWidth(), height = img.getHeight();
        
        WritableRaster raster = img.getRaster();
        raster.getDataElements(0, 0, width, height, source);
        
        convolveAndTranspose(source, destination, width, height);
        convolveAndTranspose(destination, source, width, height);
        
        raster.setDataElements(0, 0, width, height, source);
        
        return img;
    }
    
    public static void convolveAndTranspose(int[] inPixels, int[] outPixels, int width, int height) {
        for (int y = 0; y < height; y++) {
            int index = y;
            int ioffset = y*width;
            for (int x = 0; x < width; x++) {
                int sum = 0;
                int count = 0;
                
                int radius = 2;
                
                int xleft  = Math.max(0, x-radius);
                int xright = Math.min(width-1, x+radius);
                
                for ( int i = xleft; i <= xright; i++ ) {
                    int p = inPixels[ioffset + i];
                    int a = (p >> 24) & 0xFF;
                    sum += a;
                    count++;
                }
                
                outPixels[index] = ((sum/count) & 0xFF) << 24;
                index += height;
            }
        }
    }
    
    public void paintShapes(Graphics2D g) {
        for ( Shape shape: shapes ) {
            shape.paint(g);
        }
    }
    
}