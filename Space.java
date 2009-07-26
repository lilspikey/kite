import java.util.List;
import java.util.ArrayList;


public abstract class Space<B extends Body<V>, V extends Vector<V>> {
    private List<B> bodies = new ArrayList<B>();
    private V gravity = null;
    
    public V getGravity() {
        return gravity;
    }
    
    public void setGravity(V gravity) {
        this.gravity = gravity;
    }
    
    public void add(B body) {
        bodies.add(body);
    }
    
    public void remove(B body) {
        bodies.remove(body);
    }
    
    public Iterable<B> getBodies() {
        return bodies;
    }
    
    public void integrate(double dt) {
        for ( B b: bodies ) {
            if ( gravity != null ) {
                b.applyForce(gravity);
            }
            b.integrate(dt);
        }
    }
    
}