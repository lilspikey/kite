package com.psychicorigami.scene;

import java.awt.Graphics2D;
import java.util.List;
import java.util.ArrayList;

public class Layer {
    private List<Shape> shapes = new ArrayList<Shape>();
        
    public void add(Shape shape) {
        shapes.add(shape);
    }
    
    
    public void paint(Graphics2D g) {
        for ( Shape shape: shapes ) {
            shape.paint(g);
        }
    }
    
}