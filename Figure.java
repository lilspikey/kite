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
    
    private final Body2D leftShoulder;
    private final Body2D leftHand;
    
    private final List<Body2D> bodies;
    private final List<Constraint> constraints;
    
    public Figure(Vector2D center) {
        top    = new Body2D(center.x, center.y + 30);
        bottom = new Body2D(center.x, center.y - 30);
        
        rightShoulder = new Body2D(center.x+5, center.y + 5);
        rightHand = new Body2D(center.x+25, center.y + 5);
        
        leftShoulder = new Body2D(center.x-8, center.y + 5);
        leftHand = new Body2D(center.x-28, center.y + 5);
        
        bodies = Arrays.asList(new Body2D[]{ top, bottom, rightShoulder, rightHand, leftShoulder, leftHand });
        
        Constraint[] constraints = {
            new StickConstraint<Body2D, Vector2D>(top, bottom),
            new StickConstraint<Body2D, Vector2D>(top, rightShoulder),
            new StickConstraint<Body2D, Vector2D>(rightShoulder, bottom),
            new StickConstraint<Body2D, Vector2D>(rightShoulder, rightHand),
            new StickConstraint<Body2D, Vector2D>(top, leftShoulder),
            new StickConstraint<Body2D, Vector2D>(leftShoulder, bottom),
            new StickConstraint<Body2D, Vector2D>(leftShoulder, leftHand)
        };
        this.constraints = Arrays.asList(constraints);
    }
    
    public List<Body2D> getBodies() {
        return bodies;
    }
    
    public List<Constraint> getConstraints() {
        return constraints;
    }
    
    public Body2D getRightShoulder() {
        return rightShoulder;
    }
    
    public Body2D getRightHand() {
        return rightHand;
    }
    
    public Body2D getLeftShoulder() {
        return leftShoulder;
    }
    
    public Body2D getLeftHand() {
        return leftHand;
    }
    
    public Body2D getTop() {
        return top;
    }
    
    public Body2D getBottom() {
        return bottom;
    }

}