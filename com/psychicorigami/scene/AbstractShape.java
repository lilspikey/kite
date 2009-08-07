package com.psychicorigami.scene;

import com.psychicorigami.physics.Body2D;
import com.psychicorigami.physics.Vector2D;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public abstract class AbstractShape implements Shape {
    private final Body2D b1;
    private final Body2D b2;
    
    public AbstractShape(Body2D b1, Body2D b2) {
        this.b1 = b1;
        this.b2 = b2;
    }
    
    public void paint(Graphics2D g) {
        Vector2D p1 = b1.getPos();
        Vector2D p2 = b2.getPos();
        
        Vector2D angle = p1.subtract(p2).unit();
        Vector2D position = p1.add(p2).multiply(0.5);
        
        AffineTransform original = g.getTransform();
        AffineTransform tx = new AffineTransform();

        tx.translate(position.x, position.y);
        tx.rotate(Math.atan2(angle.y, angle.x) - Math.PI/2);
        
        g.transform(tx);
        
        paintShape(g);
        
        if ( original != null )
            g.setTransform(original);
    }
    
    public abstract void paintShape(Graphics2D g);
    
}
