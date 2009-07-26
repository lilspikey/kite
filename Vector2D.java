
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
    
    public Vector2D divide(double s) {
        return new Vector2D(x / s, y / s);
    }
    
    public Vector2D zero() {
        return new Vector2D(0, 0);
    }
    
}