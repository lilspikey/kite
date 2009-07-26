
public class StickConstraint<B extends Body<V>, V extends Vector<V>> implements Constraint {
    private final B b1;
    private final B b2;
    private final double length;
    
    public StickConstraint(B b1, B b2, double length) {
        this.b1 = b1;
        this.b2 = b2;
        this.length = length;
    }
    
    public void constrain() {
        V p1 = b1.getPos(),
          p2 = b2.getPos();
        
        V delta = p2.subtract(p1);
        double deltaLength = delta.length();
        
        double diff = (deltaLength-length)/deltaLength;
        
        delta = delta.multiply( 0.5*diff );
        
        p1 = p1.add( delta );
        p2 = p2.subtract( delta );
        
        b1.setPos(p1);
        b2.setPos(p2);
    }
    
}