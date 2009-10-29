package com.psychicorigami.scene;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;

import java.util.List;
import java.util.ArrayList;

import com.psychicorigami.physics.Vector2D;

public class RopeShape implements Shape {
    private List<Vector2D> points = new ArrayList<Vector2D>();
    private Color color = null;
    
    public RopeShape() {
        this(Color.BLACK);
    }
    
    public RopeShape(Color color) {
        this.color = color;
    }
    
    public void update() {
        
    }
    
    public void paint(Graphics2D g) {
        g.setColor(color);
        if ( points.size() <= 2 ) {
            Vector2D prev = null;
            for ( Vector2D point: points ) {
                if ( prev != null )
                    g.drawLine((int)prev.x, (int)prev.y, (int)point.x, (int)point.y);
                prev = point;
            }
        }
        else {
            
            double controlLength = 0.2;
            
            for ( int i = 1; i < points.size(); i++ ) {
                Vector2D p1 = points.get(i-1);
                Vector2D p2 = points.get(i);
                
                double len = p1.subtract(p2).length();
                
                Vector2D c = p1.subtract(p2).unit();
                
                /*g.setColor(Color.BLACK);
                g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
                
                g.setColor(Color.BLUE);*/
                
                Vector2D c1 = p1.subtract(c.multiply(len*controlLength));
                if ( i > 1 ) {
                    Vector2D p0 = points.get(i-2);
                    c1 = p1.add(p1.subtract(p0).unit().multiply(len*controlLength));
                }
                Vector2D c2 = p2.add(c.multiply(len*controlLength));
                if ( i < (points.size()-1) ) {
                    Vector2D p3 = points.get(i+1);
                    c2 = p2.add(p2.subtract(p3).unit().multiply(len*controlLength));
                }
                
                
                /*g.setColor(Color.GREEN);
                g.fillOval((int)(c1.x-2), (int)(c1.y-2), 4, 4);
                g.fillOval((int)(c2.x-2), (int)(c2.y-2), 4, 4);
                
                g.setColor(Color.RED);
                g.fillRect((int)(p1.x-2), (int)(p1.y-2), 4, 4);*/
                
                //g.setColor(Color.GRAY);
                CubicCurve2D.Double q = new CubicCurve2D.Double(p1.x, p1.y, c1.x, c1.y, c2.x, c2.y, p2.x, p2.y);
                g.draw(q);
                
                
            }
        }
    }
    
    public void clear() {
        points.clear();
    }
    
    public void add(double x, double y) {
        add(new Vector2D(x, y));
    }
    
    public void add(Vector2D p) {
        points.add(p);
    }
    
    public boolean contains(int x, int y) {
        return false;
    }
    
}
