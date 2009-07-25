
public class Vector2D {
    private double x;
    private double y;
    
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
    
    public void add(Vector2D v) {
        x += v.x;
        y += v.y;
    }
    
    public void multiply(double s) {
        x *= s;
        y *= s;
    }
    
    public void divide(double s) {
        x /= s;
        y /= s;
    }
}