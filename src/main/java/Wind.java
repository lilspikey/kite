import com.psychicorigami.physics.MultiBody;
import com.psychicorigami.physics.Body2D;
import com.psychicorigami.physics.Vector2D;
import com.psychicorigami.physics.Constraint;
import com.psychicorigami.physics.StickConstraint;
import com.psychicorigami.scene.MultiShape;
import com.psychicorigami.scene.Shape;
import com.psychicorigami.scene.PhysicsShape;
import com.psychicorigami.variable.Variable;

import java.awt.image.*;
import javax.imageio.*;
import java.io.IOException;

import java.util.List;
import java.util.Arrays;

public class Wind implements MultiBody<Body2D,Vector2D>, MultiShape {
    private final Body2D top;
    private final Body2D bottom;
    private final Body2D focus;
    
    private final List<Body2D> bodies;
    private final List<Constraint> constraints;
    
    private final List<Shape> shapes;
    
    private final double windSpeed;
    
    public Wind(Vector2D center, double windSpeed) {
        this.windSpeed = windSpeed;
        
        top    = new Body2D(center.x, center.y + 30);
        bottom = new Body2D(center.x, center.y - 30);
        
        focus = new Body2D(center.x + 200, center.y);
        focus.setImmovable();
        
        bodies = Arrays.asList(new Body2D[]{ top, bottom, focus });
        
        Constraint[] constraints = {
            new StickConstraint<Body2D, Vector2D>(top, bottom),
            new StickConstraint<Body2D, Vector2D>(top, focus),
            new StickConstraint<Body2D, Vector2D>(bottom, focus)
        };
        this.constraints = Arrays.asList(constraints);
        
        try {
            BufferedImage windImg = ImageIO.read(getClass().getResource("/images/wind.png"));
            
            PhysicsShape shape = new PhysicsShape(windImg, top, bottom);
        
            this.shapes = Arrays.asList(new Shape[]{ shape });
        }
        catch(IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    public Variable<Vector2D> getWindForce() {
        return new Variable<Vector2D>() {
            public Vector2D val() {
                return focus.getPos().subtract(getPos()).unit().multiply(windSpeed);
            }
        };
    }
    
    public Vector2D getPos() {
        return top.getPos().add( bottom.getPos() ).divide(2);
    }
    
    public List<Body2D> getBodies() {
        return bodies;
    }
    
    public List<Constraint> getConstraints() {
        return constraints;
    }
    
    public List<Shape> getShapes() {
        return shapes;
    }

}