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

public class Figure implements MultiBody<Body2D,Vector2D>, MultiShape {
    private final Body2D top;
    private final Body2D bottom;
    
    private final Body2D rightShoulder;
    private final Body2D rightHand;
    
    private final Body2D rightHip;
    private final Body2D rightFoot;
    
    private final Body2D leftShoulder;
    private final Body2D leftHand;
    
    private final Body2D leftHip;
    private final Body2D leftFoot;
    
    private final List<Body2D> bodies;
    private final List<Constraint> constraints;
    
    private final PhysicsShape figureShape;
    private final PhysicsShape rightArmShape;
    private final PhysicsShape rightLegShape;
    private final PhysicsShape leftArmShape;
    private final PhysicsShape leftLegShape;
    
    private final List<Shape> shapes;
    
    public Figure(Vector2D center) {
        top    = new Body2D(center.x, center.y + 30);
        bottom = new Body2D(center.x, center.y - 30);
        
        rightShoulder = new Body2D(center.x+5, center.y + 5);
        rightHand = new Body2D(center.x+25, center.y + 5);
        
        rightHip = new Body2D(center.x+8, center.y -22);
        rightFoot = new Body2D(center.x+9, center.y -48);
        
        leftShoulder = new Body2D(center.x-8, center.y + 5);
        leftHand = new Body2D(center.x-28, center.y + 5);
        
        leftHip = new Body2D(center.x-8, center.y -22);
        leftFoot = new Body2D(center.x-9, center.y -48);
        
        leftFoot.setMass(800);
        rightFoot.setMass(800);
        
        bodies = Arrays.asList(new Body2D[]{ top, bottom, rightShoulder, rightHand, rightHip, rightFoot, leftShoulder, leftHand, leftHip, leftFoot });
        
        Constraint[] constraints = {
            new StickConstraint<Body2D, Vector2D>(top, bottom),
            new StickConstraint<Body2D, Vector2D>(top, rightShoulder),
            new StickConstraint<Body2D, Vector2D>(rightShoulder, bottom),
            new StickConstraint<Body2D, Vector2D>(rightShoulder, rightHand),
            new StickConstraint<Body2D, Vector2D>(top, leftShoulder),
            new StickConstraint<Body2D, Vector2D>(leftShoulder, bottom),
            new StickConstraint<Body2D, Vector2D>(leftShoulder, leftHand),
            new StickConstraint<Body2D, Vector2D>(bottom, rightHip),
            new StickConstraint<Body2D, Vector2D>(top, rightHip),
            new StickConstraint<Body2D, Vector2D>(rightHip, rightFoot),
            new StickConstraint<Body2D, Vector2D>(rightHip, leftFoot),
            new StickConstraint<Body2D, Vector2D>(bottom, leftHip),
            new StickConstraint<Body2D, Vector2D>(top, leftHip),
            new StickConstraint<Body2D, Vector2D>(leftHip, leftFoot),
            new StickConstraint<Body2D, Vector2D>(leftHip, rightFoot)
        };
        this.constraints = Arrays.asList(constraints);
        
        // TODO load images via some other mechanism
        try {
            BufferedImage rightArmShapeImg = ImageIO.read(getClass().getResource("/images/figure-arm-right.png"));
            BufferedImage rightLegShapeImg = ImageIO.read(getClass().getResource("/images/figure-leg-right.png"));
            BufferedImage leftArmShapeImg = ImageIO.read(getClass().getResource("/images/figure-arm-left.png"));
            BufferedImage leftLegShapeImg = ImageIO.read(getClass().getResource("/images/figure-leg-left.png"));
            BufferedImage figBodyImg = ImageIO.read(getClass().getResource("/images/figure-body.png"));
        
            rightArmShape = new PhysicsShape(rightArmShapeImg, rightShoulder, rightHand);
            rightLegShape = new PhysicsShape(rightLegShapeImg, rightHip, rightFoot);
            leftArmShape = new PhysicsShape(leftArmShapeImg, leftShoulder, leftHand);
            leftLegShape = new PhysicsShape(leftLegShapeImg, leftHip, leftFoot);
            figureShape = new PhysicsShape(figBodyImg, top, bottom);
        
            this.shapes = Arrays.asList(new Shape[]{
                rightArmShape,
                rightLegShape,
                leftArmShape,
                leftLegShape,
                figureShape
            });
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