
public class Rope2D extends Rope<Body2D, Vector2D> {
    
    public Rope2D(Vector2D start, Vector2D end, int numBodies, double bodyMass) {
        super(start, end, numBodies, bodyMass);
    }
    
    public Body2D createBody(Vector2D pos, double mass) {
        Body2D body = new Body2D(pos);
        body.setMass(mass);
        return body;
    }
    
}