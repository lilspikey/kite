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

public class DropShadowLayerStyle implements LayerStyle {
    private BufferedImage backing = null;
    private BufferedImage shadow = null;
    private int[] source = null;
    private int[] destination = null;
    
    private int blurRadius;
    private int distX;
    private int distY;
    
    public DropShadowLayerStyle() {
        this(2, 1, -1);
    }
    
    public DropShadowLayerStyle(int blurRadius, int distX, int distY) {
        this.blurRadius = blurRadius;
        this.distX = distX;
        this.distY = distY;
    }
    
    private boolean hasBacking(Layer layer) {
        if ( backing == null )
            return false;
        if ( backing.getWidth() != layer.getWidth() || backing.getHeight() != layer.getHeight() )
            return false;
        return true;
    }
    
    private void initBacking(Layer layer) {
        int width  = layer.getWidth(),
            height = layer.getHeight();
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
        backing = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        shadow = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        source = new int[width*height];
        destination = new int[width*height];
    }
    
    public void paint(Layer layer, Graphics2D g) {
        if ( !hasBacking(layer) ) {
            initBacking(layer);
        }
        
        Graphics2D gb = backing.createGraphics();
        gb.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // set to all transparent
        gb.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
        gb.fillRect(0, 0, backing.getWidth(), backing.getHeight());
        
        // now render normally
        gb.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        
        layer.paintShapes(gb);
        
        gb.dispose();
        
        BufferedImage shadow = createDropShadow(backing);
        
        // render shadow
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g.drawImage(shadow, distX, distY, null);
        
        // draw original
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        //paintShapes(g);
        g.drawImage(backing, 0, 0, null);
        
    }
    
    public BufferedImage createDropShadow(BufferedImage img) {
        int width = img.getWidth(), height = img.getHeight();
        
        WritableRaster raster = img.getRaster();
        raster.getDataElements(0, 0, width, height, source);
        
        for ( int i = 0; i < source.length; i++ ) source[i] = (source[i] >> 24) & 0xFF;
        blur(source, destination, width, height, blurRadius);
        blur(destination, source, width, height, blurRadius);
        for ( int i = 0; i < source.length; i++ ) source[i] = (source[i] & 0xFF) << 24;
        
        WritableRaster shadowRaster = shadow.getRaster();
        shadowRaster.setDataElements(0, 0, width, height, source);
        
        return shadow;
    }
    
    public static void blur(int[] inPixels, int[] outPixels, final int width, final int height, final int radius) {
        final int radiusPlusOne = radius+1;
        
        int inIndex = 0;
        for (int y = 0; y < height; y++) {
            int outIndex = y;
            for (int x = 0; x < width; x++) {
                int sum = 0;
                
                int xleft  = inIndex + Math.max(0, x-radius);
                int xright = inIndex + Math.min(width, x+radiusPlusOne);
                
                for ( int i = xleft; i < xright; i++ ) {
                    sum += inPixels[i];
                }
                
                int count = (xright-xleft)+1;
                outPixels[outIndex] = ((sum/count) & 0xFF);
                outIndex += height;
            }
            inIndex += width;
        }
    }
    
}