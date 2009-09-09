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
    
    private final List<Body2D> bodies;
    private final List<Constraint> constraints;
    
    public Figure(Vector2D center) {
        top    = new Body2D(center.x, center.y + 30);
        bottom = new Body2D(center.x, center.y - 30);
        
        bodies = Arrays.asList(new Body2D[]{ top, bottom });
        
        Constraint[] constraints = {
            new StickConstraint<Body2D, Vector2D>(top, bottom)
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
        return top;
    }
    
    public Body2D getBottom() {
        return bottom;
    }
    
    public Vector2D getBodyPosition() {
        Vector2D p1 = top.getPos();
        Vector2D p2 = bottom.getPos();
        return p1.add(p2).multiply(0.5);
    }
    
    public double getBodyAngle() {
        Vector2D p1 = top.getPos();
        Vector2D p2 = bottom.getPos();
        
        Vector2D angle = p1.subtract(p2).unit();
    
        return Math.atan2(angle.y, angle.x);
    }

}