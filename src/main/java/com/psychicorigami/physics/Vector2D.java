package com.psychicorigami.physics;

public class Vector2D implements Vector<Vector2D> {
    public final double x;
    public final double y;
    
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2D() {
        this(0, 0);
    }
    
    public Vector2D(Vector2D v) {
        this(v.x, v.y);
    }
    
    public double length() {
        return Math.sqrt(x*x + y*y);
    }
    
    public Vector2D add(Vector2D v) {
        return new Vector2D(x + v.x, y + v.y);
    }
    
    public Vector2D subtract(Vector2D v) {
        return new Vector2D(x - v.x, y - v.y);
    }
    
    public Vector2D multiply(double s) {
        return new Vector2D(x * s, y * s);
    }
    
    public Vector2D multiply_add(double s, Vector2D v) {
        return new Vector2D((x * s) + v.x, (y * s) + v.y);
    }
    
    public Vector2D multiply(Vector2D v) {
        return new Vector2D(x * v.x, y * v.y);
    }
    
    public Vector2D divide(double s) {
        return new Vector2D(x / s, y / s);
    }
    
    public Vector2D zero() {
        return new Vector2D(0, 0);
    }
    
    public Vector2D unit() {
        return divide(length());
    }
    
    public double dotProduct(Vector2D v) {
        return (x * v.x) + (y * v.y);
    }
    
    @Override
    public String toString() {
        return String.format("%f, %f", x, y);
    }
    
}