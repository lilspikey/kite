import java.util.List;
import java.util.ArrayList;

public class FloorConstraint extends GlobalConstraint<Body2D,Vector2D> {
    
    @Override
    public void constrain(Body2D b) {
        Vector2D pos = b.getPos();
        if ( pos.y < 0 ) {
            b.setPos( new Vector2D(pos.x, 0) );
        }
    }
    
}