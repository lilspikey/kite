import java.util.List;
import java.util.ArrayList;

import com.psychicorigami.physics.*;

public class FloorConstraint extends GlobalConstraint<Body2D,Vector2D> {
    
    @Override
    public double constrain(Body2D b) {
        Vector2D pos = b.getPos();
        if ( pos.y < 0 ) {
            double error = Math.abs(pos.y);
            b.setPos( new Vector2D(pos.x, 0) );
            
            // handle friction
            Vector2D velocity = b.getVelocity();
            velocity = new Vector2D(velocity.x * 0.99, velocity.y);
            
            b.setVelocity(velocity);
            
            return error;
        }
        return 0;
    }
    
}