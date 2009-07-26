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
    
    public double constrain() {
        double error = 0;
        for ( B b: bodies ) {
            error = Math.max(error, constrain(b));
        }
        return error;
    }
    
    public abstract double constrain(B b);
    
}