
public class Body2D extends Body<Vector2D> {
    
    public Body2D() {
        this(0, 0);
    }
    
    public Body2D(double x, double y) {
        this(new Vector2D(x, y));
    }
    
    public Body2D(Vector2D pos) {
        this(pos, pos);
    }
    
    public Body2D(Vector2D pos, Vector2D posPrev) {
        super(pos, posPrev);
    }
    
    
}