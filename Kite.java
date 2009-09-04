import com.psychicorigami.physics.MultiBody;
import com.psychicorigami.physics.Body2D;
import com.psychicorigami.physics.Vector2D;
import com.psychicorigami.physics.Constraint;
import com.psychicorigami.physics.StickConstraint;

import java.util.List;
import java.util.Arrays;

public class Kite implements MultiBody<Body2D,Vector2D> {
    private double AIR_RESISTANCE = 40;
        
    private final Body2D topCorner;
    private final Body2D rightCorner;
    private final Body2D bottomCorner;
    private final Body2D leftCorner;
    
    private final Body2D centreOfPressure;
    
    private final Body2D topHook;
    private final Body2D bottomHook;
    
    private final List<Body2D> bodies;
    private final List<Constraint> constraints;
    
    public Kite(Vector2D center, double kiteMass) {
        topCorner    = new Body2D(center.x, center.y + 20);
        rightCorner  = new Body2D(center.x + 30, center.y);
        bottomCorner = new Body2D(center.x, center.y - 40);
        leftCorner   = new Body2D(center.x - 30, center.y);
        
        centreOfPressure = new Body2D(center.x, center.y+1);
        
        topHook = new Body2D(center.x, center.y+10);
        bottomHook = new Body2D(center.x, center.y-30);
        
        topCorner.setMass(kiteMass);
        rightCorner.setMass(kiteMass);
        bottomCorner.setMass(kiteMass);
        leftCorner.setMass(kiteMass);
        
        bodies = Arrays.asList(new Body2D[]{ topCorner, rightCorner, bottomCorner, leftCorner, topHook, bottomHook, centreOfPressure });
        
        Constraint[] constraints = {
            new StickConstraint<Body2D, Vector2D>(topCorner, rightCorner),
            new StickConstraint<Body2D, Vector2D>(rightCorner, bottomCorner),
            new StickConstraint<Body2D, Vector2D>(bottomCorner, leftCorner),
            new StickConstraint<Body2D, Vector2D>(leftCorner, topCorner),
            // cross
            new StickConstraint<Body2D, Vector2D>(topCorner, bottomCorner),
            new StickConstraint<Body2D, Vector2D>(rightCorner, leftCorner),
            // hooks and centre of pressure
            new StickConstraint<Body2D, Vector2D>(topCorner, centreOfPressure),
            new StickConstraint<Body2D, Vector2D>(centreOfPressure, bottomCorner),
            new StickConstraint<Body2D, Vector2D>(topCorner, topHook),
            new StickConstraint<Body2D, Vector2D>(topHook, bottomCorner),
            new StickConstraint<Body2D, Vector2D>(topCorner, bottomHook),
            new StickConstraint<Body2D, Vector2D>(bottomHook, bottomCorner)
        };
        this.constraints = Arrays.asList(constraints);
    }
    
    public List<Body2D> getBodies() {
        return bodies;
    }
    
    public List<Constraint> getConstraints() {
        return constraints;
    }
    
    public Body2D getTopHook() {
        return topHook;
    }
    
    public Body2D getBottomHook() {
        return bottomHook;
    }
    
    public Vector2D getPosition() {
        Vector2D p1 = topCorner.getPos();
        Vector2D p2 = bottomCorner.getPos();
        return p1.add(p2).multiply(0.5);
    }
    
    public double getAngle() {
        Vector2D p1 = topCorner.getPos();
        Vector2D p2 = bottomCorner.getPos();
        
        Vector2D angle = p1.subtract(p2).unit();
    
        return Math.atan2(angle.y, angle.x);
    }
    
    public void applyWindForce(Vector2D windForce, double dt) {
        Vector2D crossBar = rightCorner.getPos().subtract( leftCorner.getPos() ).unit();
        
        Vector2D airSpeed = new Vector2D();
        List<Body2D> bodies = getBodies();
        for ( Body2D b: bodies ) {
            Vector2D v = b.getVelocity().divide(dt);
            airSpeed = airSpeed.add(v);
        }
        
        airSpeed = airSpeed.divide(-bodies.size());
        
        airSpeed = airSpeed.add(windForce);
        
        Vector2D force = crossBar.multiply(crossBar.dotProduct(airSpeed)).multiply(AIR_RESISTANCE);
        
        /*for ( Body2D b: bodies ) {
            b.applyForce(force);
        }*/
        centreOfPressure.applyForce(force);
    }
    
    public void setInitialVelocity(Vector2D v, double dt) {
        for ( Body2D b: getBodies() ) {
            b.setVelocity(v.multiply(dt));
        }
    }
}