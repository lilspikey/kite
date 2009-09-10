package com.psychicorigami.scene;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public abstract class AbstractShape implements Shape {
    private double x, y, angle;
    
    public AbstractShape() {
        this(0, 0);
    }
    
    public AbstractShape(double x, double y) {
        this(x, y, 0);
    }
    
    public AbstractShape(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }
    
    public void paint(Graphics2D g) {
        AffineTransform original = g.getTransform();
        AffineTransform tx = new AffineTransform();

        tx.translate(x, y);
        // Math.atan2(angle.y, angle.x)
        tx.rotate(angle - Math.PI/2);
        
        g.transform(tx);
        
        paintShape(g);
        
        if ( original != null )
            g.setTransform(original);
    }
    
    public abstract void paintShape(Graphics2D g);
    
    public void update() {
        
    }
    
    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public double getAngle() {
        return angle;
    }
    
    public void setAngle(double angle) {
        this.angle = angle;
    }
    
}
