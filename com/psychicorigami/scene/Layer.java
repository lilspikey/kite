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
    private List<Shape> shapes = new ArrayList<Shape>();
    private int width = 500;
    private int height = 500;
    
    private BufferedImage background = null;
    private BufferedImage backing = null;
    
    private List<LayerStyle> styles = new ArrayList<LayerStyle>();
    {
        styles.add(new DropShadowLayerStyle());
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setSize(int width, int height) {
        this.width  = width;
        this.height = height;
    }
    
    public void setBackground(BufferedImage background) {
        this.background = background;
    }
    
    public void addLayerStyle(LayerStyle style) {
        styles.add(style);
    }
    
    private boolean hasBacking() {
        if ( backing == null )
            return false;
        if ( backing.getWidth() != width || backing.getHeight() != height )
            return false;
        return true;
    }
    
    private void initBacking() {
        backing = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
    
    public void add(Shape shape) {
        shapes.add(shape);
        for ( LayerStyle style: styles ) {
            style.shapeAdded(shape);
        }
    }
    
    private BufferedImage clearBacking() {
        Graphics2D gb = backing.createGraphics();

        // set to all transparent
        gb.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
        gb.fillRect(0, 0, backing.getWidth(), backing.getHeight());

        // now render normally
        gb.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        gb.dispose();
        
        return backing;
    }
    
    private void render(BufferedImage image) {
        Graphics2D gb = image.createGraphics();
        gb.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        paintShapes(gb);
        
        gb.dispose();
    }
    
    public void paintBackground(BufferedImage backing) {
        if ( background != null ) {
            Graphics2D gb = backing.createGraphics();
            
            AffineTransform tx = new AffineTransform();
            
            int width = background.getWidth();
            int height = background.getHeight();
            
            tx.translate((width/2.0), (height/2.0));
            tx.rotate(Math.PI);
            tx.translate(-(width/2.0), -(height/2.0));
            
            gb.transform(tx);
            
            gb.drawImage(background, (width-getWidth())/2, (height-getHeight())/2, null);
        }
    }
    
    public void paint(Graphics2D g) {
        if ( !hasBacking() ) {
            initBacking();
        }
        
        BufferedImage backing = clearBacking();
        paintBackground(backing);
        
        for ( LayerStyle style: styles ) {
            style.preRender(this, backing);
        }
        
        render(backing);
        
        for ( LayerStyle style: styles ) {
            style.postRender(this, backing);
        }
            
        g.drawImage(backing, 0, 0, null);
    }
    
    public void paintShapes(Graphics2D g) {
        for ( Shape shape: shapes ) {
            shape.paint(g);
        }
    }
    
}