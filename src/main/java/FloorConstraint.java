import java.util.List;
import java.util.ArrayList;

import com.psychicorigami.physics.*;

public class FloorConstraint extends GlobalConstraint<Body2D,Vector2D> implements Force {
    private double contactDistance = 0.01;
    
    @Override
    public double constrain(Body2D b) {
        Vector2D pos = b.getPos();
        if ( pos.y < 0 ) {
            double error = Math.abs(pos.y);
            b.addConstrainedPosition( new Vector2D(pos.x, 0) );
            return error;
        }
        return 0;
    }
    
    public void update(double dt) {
        for ( Body2D b: bodies ) {
            if ( !b.isImmovable() ) {
                Vector2D pos = b.getPos();
                if ( pos.y <= contactDistance ) {
                    // handle friction
                    Vector2D velocity = b.getVelocity();
                    Vector2D friction = new Vector2D(-velocity.x * 100 * b.getMass(), 0);
                    
                    b.applyForce(friction);
                }
            }
        }
    }
    
}