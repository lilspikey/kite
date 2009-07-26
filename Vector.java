
public interface Vector<V extends Vector> {
    
    public double length();
    
    public V add(V v);
    
    public V subtract(V v);
    
    public V multiply(double s);
    
    public V divide(double s);
    
    public V zero();
}