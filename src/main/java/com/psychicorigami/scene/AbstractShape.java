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
        AffineTransform tx = createTransform();
        
        g.transform(tx);
        
        paintShape(g);
        
        if ( original != null )
            g.setTransform(original);
    }
    
    /**
     * create affine transform needed to render this shape at correct
     * position and angle
     **/
    public AffineTransform createTransform() {
        AffineTransform tx = new AffineTransform();

        tx.translate(x, y);
        tx.rotate(angle - Math.PI/2);
        
        return tx;
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
