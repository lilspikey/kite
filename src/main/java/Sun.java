import com.psychicorigami.physics.MultiBody;
import com.psychicorigami.physics.Body2D;
import com.psychicorigami.physics.Vector2D;
import com.psychicorigami.physics.Constraint;
import com.psychicorigami.physics.StickConstraint;
import com.psychicorigami.scene.MultiShape;
import com.psychicorigami.scene.Shape;
import com.psychicorigami.scene.PhysicsShape;

import java.awt.image.*;
import javax.imageio.*;
import java.io.IOException;

import java.util.List;
import java.util.Arrays;

public class Sun implements MultiBody<Body2D,Vector2D>, MultiShape {
    private final Body2D top;
    private final Body2D bottom;
    
    private final List<Body2D> bodies;
    private final List<Constraint> constraints;
    
    private final List<Shape> shapes;
    
    public Sun(Vector2D center) {
        top    = new Body2D(center.x, center.y + 30);
        bottom = new Body2D(center.x, center.y - 30);
        
        bodies = Arrays.asList(new Body2D[]{ top, bottom });
        
        Constraint[] constraints = {
            new StickConstraint<Body2D, Vector2D>(top, bottom),
        };
        this.constraints = Arrays.asList(constraints);
        
        try {
            BufferedImage sunImg = ImageIO.read(getClass().getResource("/images/sun.png"));
            
            PhysicsShape shape = new PhysicsShape(sunImg, top, bottom);
        
            this.shapes = Arrays.asList(new Shape[]{ shape });
        }
        catch(IOException ioe) {
            throw new RuntimeException(ioe);
        }
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