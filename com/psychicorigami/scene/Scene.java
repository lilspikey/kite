package com.psychicorigami.scene;

import java.awt.Graphics2D;
import java.util.Map;
import java.util.TreeMap;

public class Scene {
    private Map<Integer, Layer> layers = new TreeMap<Integer, Layer>();
    
    public void setSize(int width, int height) {
        for ( Layer layer: layers.values() ) {
            layer.setSize(width, height);
        }
    }
    
    public void add(Shape shape, int layer) {
        Layer l = layers.get(layer);
        if ( l == null ) {
            l = new Layer();
            layers.put(layer, l);
        }
        l.add(shape);
    }
    
    public void paint(Graphics2D g) {
        for ( Layer layer: layers.values() ) {
            layer.paint(g);
        }
    }
    
}