import com.psychicorigami.physics.MultiBody;
import com.psychicorigami.physics.Body2D;
import com.psychicorigami.physics.Vector2D;
import com.psychicorigami.physics.Constraint;
import com.psychicorigami.physics.StickConstraint;

import java.util.List;
import java.util.Arrays;

public class Figure implements MultiBody<Body2D,Vector2D> {
    private final Body2D top;
    private final Body2D bottom;
    private final Body2D rightShoulder;
    private final Body2D rightHand;
    
    private final List<Body2D> bodies;
    private final List<Constraint> constraints;
    
    public Figure(Vector2D center) {
        top    = new Body2D(center.x, center.y + 30);
        bottom = new Body2D(center.x, center.y - 30);
        
        rightShoulder = new Body2D(center.x+5, center.y + 5);
        rightHand = new Body2D(center.x+25, center.y + 5);
        
        bodies = Arrays.asList(new Body2D[]{ top, bottom, rightShoulder, rightHand });
        
        Constraint[] constraints = {
            new StickConstraint<Body2D, Vector2D>(top, bottom),
            new StickConstraint<Body2D, Vector2D>(top, rightShoulder),
            new StickConstraint<Body2D, Vector2D>(rightShoulder, bottom),
            new StickConstraint<Body2D, Vector2D>(rightShoulder, rightHand)
        };
        this.constraints = Arrays.asList(constraints);
    }
    
    public List<Body2D> getBodies() {
        return bodies;
    }
    
    public List<Constraint> getConstraints() {
        return constraints;
    }
    
    public Body2D getKiteHand() {
        return rightHand;
    }
    
    public Body2D getBottom() {
        return bottom;
    }
    
    private Vector2D calcPosition(Body2D b1, Body2D b2) {
        Vector2D p1 = b1.getPos();
        Vector2D p2 = b2.getPos();
        return p1.add(p2).multiply(0.5);
    }
    
    public Vector2D getBodyPosition() {
        return calcPosition(top, bottom);
    }
    
    public Vector2D getRightArmPosition() {
        return calcPosition(rightShoulder, rightHand);
    }

    private double calcAngle(Body2D b1, Body2D b2) {
        Vector2D p1 = b1.getPos();
        Vector2D p2 = b2.getPos();
        
        Vector2D angle = p1.subtract(p2).unit();
    
        return Math.atan2(angle.y, angle.x);
    }
    
    public double getBodyAngle() {
        return calcAngle(top, bottom);
    }
    
    public double getRightArmAngle() {
        return calcAngle(rightShoulder, rightHand);
    }
}