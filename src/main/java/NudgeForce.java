
import com.psychicorigami.variable.Variable;
import static com.psychicorigami.variable.ConstVariable.const_var;

import com.psychicorigami.physics.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Nudge bodies back over line
 **/
public class NudgeForce implements Force {
    private final List<Body2D> bodies = new ArrayList<Body2D>();
    private final Variable<Integer> x;
    private final boolean left;
    
    public NudgeForce(Variable<Integer> x, boolean left) {
        this.x = x;
        this.left = left;
    }
    
    public void add(Body2D body) {
        bodies.add(body);
    }
    
    public void add(MultiBody<Body2D,Vector2D> multiBody) {
        for ( Body2D body: multiBody.getBodies() ) {
            add(body);
        }
    }
    
    public void update(double dt) {
        for ( Body2D b: bodies ) {
            if ( !b.isImmovable() ) {
                int x = this.x.val();
                Vector2D force = null;
                if ( left ) {
                    if ( b.getPos().x < x ) {
                        force = new Vector2D(100, 0);
                    }
                }
                else {
                    if ( b.getPos().x > x ) {
                        force = new Vector2D(-100, 0);
                    }
                }
                if ( force != null ) {
                    b.applyForce(force.divide(b.getMassInv()));
                }
            }
        }
    }

}