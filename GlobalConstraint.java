import java.util.Set;
import java.util.LinkedHashSet;

public abstract class GlobalConstraint<B extends Body<V>, V extends Vector<V>> implements Constraint {
    private Set<B> bodies = new LinkedHashSet<B>();
    
    public void add(B body) {
        bodies.add(body);
    }
    
    public void remove(B body) {
        bodies.remove(body);
    }
    
    public void constrain() {
        for ( B b: bodies ) {
            constrain(b);
        }
    }
    
    public abstract void constrain(B b);
    
}