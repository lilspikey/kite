package com.psychicorigami.scene;

import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.BufferedImageOp;
import java.util.List;
import java.util.ArrayList;

public class Layer {
    private BufferedImage backing = null;
    
    private List<Shape> shapes = new ArrayList<Shape>();
        
    public void add(Shape shape) {
        shapes.add(shape);
        setBacking(500, 500);
    }
    
    public void setBacking(int width, int height) {
        backing = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
    
    public void paint(Graphics2D g) {
        if ( backing == null ) {
            paintShapes(g);
        }
        else {            
            Graphics2D gb = backing.createGraphics();
            
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
            
            // render shadow
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
            g.drawImage(backing, 5, -5, null);
            
            // draw original
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            paintShapes(g);
        }
    }
    
    public void paintShapes(Graphics2D g) {
        for ( Shape shape: shapes ) {
            shape.paint(g);
        }
    }
    
}