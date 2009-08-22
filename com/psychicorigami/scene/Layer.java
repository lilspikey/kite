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
    
    private LayerStyle style = new DropShadowLayerStyle();
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void add(Shape shape) {
        shapes.add(shape);
    }
    
    public void paint(Graphics2D g) {
        if ( style == null ) {
            paintShapes(g);
        }
        else {            
            style.paint(this, g);
        }
    }
    
    public void paintShapes(Graphics2D g) {
        for ( Shape shape: shapes ) {
            shape.paint(g);
        }
    }
    
}