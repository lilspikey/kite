import java.util.List;

public interface Shape<B extends Body<V>, V extends Vector<V>> {
    
    public List<B> getBodies();
    
    public List<Constraint> getConstraints();
    
}