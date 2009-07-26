
public class StickConstraint<B extends Body<V>, V extends Vector<V>> implements Constraint {
    private final B b1;
    private final B b2;
    private final double length;
    
    public StickConstraint(B b1, B b2) {
        this(b1, b2, b1.getPos().subtract(b2.getPos()).length());
    }
    
    public StickConstraint(B b1, B b2, double length) {
        this.b1 = b1;
        this.b2 = b2;
        this.length = length;
    }
    
    public void constrain() {
        V p1 = b1.getPos(),
          p2 = b2.getPos();
        double invM1 = b1.getMassInv(),
               invM2 = b2.getMassInv();
        
        V delta = p2.subtract(p1);
        double deltaLength = delta.length();
        
        double diff = (deltaLength-length)/(deltaLength*(invM1+invM2));

        p1 = p1.add( delta.multiply( invM1*diff ) );
        p2 = p2.subtract( delta.multiply( invM2*diff ) );
        
        b1.setPos(p1);
        b2.setPos(p2);
    }
    
}