package com.psychicorigami.scene;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public interface Shape {
    
    public void paint(Graphics2D g);
    
    public void update();
    
    public boolean contains(int x, int y);
    
}
