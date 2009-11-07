import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.geom.*;

import java.util.ArrayList;
import java.io.*;

import com.psychicorigami.physics.*;
import com.psychicorigami.scene.PhysicsShape;
import com.psychicorigami.scene.ImageShape;
import com.psychicorigami.scene.RopeShape;
import com.psychicorigami.scene.Scene;
import com.psychicorigami.scene.ColoriseLayerStyle;
import com.psychicorigami.scene.DropShadowLayerStyle;
import com.psychicorigami.scene.PhysicsShapeDragger;

public class KiteCanvas extends JPanel {
    private Space2D space = new Space2D();
    private Scene scene = new Scene();
    ColoriseLayerStyle layerTint = new ColoriseLayerStyle();
    
    private final int SCENE_BACKGROUND = 0;
    private final int SCENE_MIDDLEGROUND = 1;
    
    private Kite kite = null;
    private PhysicsShape kiteShape = null;
    private ArrayList<RopeShape> ropeShapes = new ArrayList<RopeShape>();
    
    private Rope2D mainRope = null;
    private ArrayList<Rope2D> kiteRopes = new ArrayList<Rope2D>();
    
    private Figure figure = null;
    
    private Body2D baseLeft = null;
    private Body2D baseRight = null;
    
    private int updateCount = 10;
    private double dt = 0.04/updateCount;
    private double rope_weight = 1;
    private double kiteMass = 10;
    private double WIND_SPEED = 100;
    private double KITE_INITIAL_SPEED=100;
    
    private Vector2D force = new Vector2D();
    
    private PhysicsShapeDragger dragger = new PhysicsShapeDragger();
    private Vector2D currentMousePos = new Vector2D(0, 0);
    private PhysicsShape currentShapeAtMousePos = null;
    private PhysicsShape dragShape    = null;
    
    public KiteCanvas() throws IOException {
        
        scene.addLayerStyle(new DropShadowLayerStyle(), SCENE_MIDDLEGROUND);
        scene.addLayerStyle(layerTint, SCENE_MIDDLEGROUND);
        layerTint.setOpacity(1);
        
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                scene.setSize(getWidth(), getHeight());
            }
        });
        
        space.setGravity( new Vector2D(0, -10) );
        space.addGlobalConstraint(new FloorConstraint());
        
        /*baseLeft = new Body2D(-10, 55);
        baseLeft.setMass(100);
        //baseLeft.setImmovable();
        space.add(baseLeft);
        
        baseRight = new Body2D(10, 55);
        baseRight.setMass(100);
        //baseRight.setImmovable();
        space.add(baseRight);*/
        
        figure = new Figure(new Vector2D(0, 50));
        space.add(figure);
        //space.add(new StickConstraint<Body2D, Vector2D>(baseLeft, figure.getLeftShoulder()));
        //space.add(new StickConstraint<Body2D, Vector2D>(baseRight, figure.getRightShoulder()));
        //space.add(new RopeConstraint<Body2D, Vector2D>(baseRight, figure.getLeftShoulder()));
        //space.add(new RopeConstraint<Body2D, Vector2D>(baseLeft, figure.getRightShoulder()));
        
        Body2D hand = figure.getRightHand();
        
        mainRope = new Rope2D(new Vector2D(hand.getPos().x+0.01, hand.getPos().y), new Vector2D(300, 70), 10, rope_weight);
        space.add(mainRope);
        kiteRopes.add(mainRope);
        
        space.add(new RopeConstraint<Body2D, Vector2D>(hand, mainRope.getStart()));
        
        Body2D joint = mainRope.getEnd();
        Vector2D center = joint.getPos().add(new Vector2D(20, -10));
        
        kite = new Kite(center, kiteMass);
        kite.setInitialVelocity(new Vector2D(0, KITE_INITIAL_SPEED), dt);
        
        space.add(kite);
        
        // now add rigging
        Rope2D topRope = new Rope2D(joint.getPos().add(new Vector2D(0.01,0.01)),
                                    kite.getTopHook().getPos().add(new Vector2D(-0.01,-0.01)),
                                    2, rope_weight);
        Rope2D bottomRope = new Rope2D(joint.getPos().add(new Vector2D(0.01,0.01)),
                                    kite.getBottomHook().getPos().add(new Vector2D(-0.01,-0.01)),
                                    2, rope_weight);
        
        space.add(topRope);
        kiteRopes.add(topRope);
        
        space.add(bottomRope);
        kiteRopes.add(bottomRope);
        
        space.add(new RopeConstraint<Body2D, Vector2D>(joint, topRope.getStart()));
        space.add(new RopeConstraint<Body2D, Vector2D>(kite.getTopHook(), topRope.getEnd()));
        
        space.add(new RopeConstraint<Body2D, Vector2D>(joint, bottomRope.getStart()));
        space.add(new RopeConstraint<Body2D, Vector2D>(kite.getBottomHook(), bottomRope.getEnd()));
        
        //space.add(dragger);
        
        // add a tail
        BufferedImage kiteImg = ImageIO.read(getClass().getResource("/images/kite.png"));
        
        kiteShape = new PhysicsShape(kiteImg, kite.getTopCorner(), kite.getBottomCorner());
        scene.add(kiteShape, SCENE_MIDDLEGROUND);
        
        Color ropeColor = new Color(0xFF664411);
        for ( Rope2D r: kiteRopes ) {
            RopeShape rope = new RopeShape(ropeColor);
            ropeShapes.add(rope);
            scene.add(rope, SCENE_MIDDLEGROUND);
        }
        
        scene.add(figure, SCENE_MIDDLEGROUND);
        
        BufferedImage skyImg = ImageIO.read(getClass().getResource("/images/sky.jpg"));
        scene.setBackground(skyImg, SCENE_BACKGROUND);
        
        
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                requestFocus();
                updateMousePosition(me, true);
            }
            
            public void mouseReleased(MouseEvent me) {
                dragger.stopDragging();
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent me) {
                updateMousePosition(me, false);
            }
            
            public void mouseDragged(MouseEvent me) {
                Vector2D v = transformedPosition(me);
                dragger.updateMousePos(v);
                currentMousePos = v;
            }
        });
        
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                double dx = 0;
                switch(ke.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        dx = -25;
                        break;
                    case KeyEvent.VK_RIGHT:
                        dx = 25;
                        break;
                    case KeyEvent.VK_UP:
                        changeRopeLength(2);
                        return;
                    case KeyEvent.VK_DOWN:
                        changeRopeLength(-2);
                        return;
                }
                //base.setPos(new Vector2D(base.getPos().x+dx, base.getPos().y));
                //baseLeft.setVelocity(new Vector2D(dt*dx, 0));
                //baseRight.setVelocity(new Vector2D(dt*dx, 0));
            }
            public void keyReleased(KeyEvent ke) {
                //baseLeft.setVelocity(new Vector2D(0, 0));
                //baseRight.setVelocity(new Vector2D(0, 0));
            }
        });
    }
    
    public Vector2D transformedPosition(MouseEvent me) {
        AffineTransform tx = createTransform();
        Point2D p = tx.transform(me.getPoint(), null);
        return new Vector2D(p.getX(), p.getY());
    }
    
    public MultiBody<Body2D, Vector2D> findMatchingBody(PhysicsShape shape) {
        if ( shape == kiteShape ) {
            return kite;
        }
        else {
            if ( figure.getShapes().contains(shape) ) {
                return figure;
            }
        }
        return null;
    }
    
    public void updateMousePosition(MouseEvent me, boolean pressed) {
        currentMousePos = transformedPosition(me);
        updateCursor();
        if ( pressed && currentShapeAtMousePos != null ) {
            dragger.dragShape(findMatchingBody(currentShapeAtMousePos), currentMousePos);
        }
    }
    
    private void updateCursor() {
        com.psychicorigami.scene.Shape shape = scene.findShapeAt((int)currentMousePos.x, (int)currentMousePos.y);
        if ( (shape != null && (shape instanceof PhysicsShape)) || dragger.isDragging() ) {
            if ( shape != null ) {
                currentShapeAtMousePos = (PhysicsShape)shape;
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        else {
            currentShapeAtMousePos = null;
            setCursor(null);
        }
    }
    
    public void changeRopeLength(double dx) {
        double length = mainRope.calcLength();
        mainRope.changeLength(length + dx);
    }
    
    public void tick() {
        for ( int i = 0; i < 10*updateCount; i++ ) {
            applyForces();
            space.update(dt);
        }
        updateScene();
        updateCursor();
        repaint();
    }
    
    public void applyForces() {
        kite.applyWindForce(new Vector2D(WIND_SPEED, 0), dt);
        dragger.update();
    }
    
    public void updateScene() {
        
        for ( int i = 0; i < kiteRopes.size(); i++ ) {
            Rope2D rope = kiteRopes.get(i);
            RopeShape shape = ropeShapes.get(i);
            shape.clear();
            for ( Body2D b: rope.getBodies() ) {
                shape.add(b.getPos());
            }
        }
        
        float opacity = layerTint.getOpacity();
        opacity -= 0.01;
        if ( opacity < 0 )
            opacity = 0;
        layerTint.setOpacity(opacity);
    }
    
    public AffineTransform createTransform() {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -getHeight());
        return tx;
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int height = getHeight();
        g2d.clearRect(0, 0, getWidth(), height);
        
        AffineTransform tx = createTransform();
        g2d.transform(tx);
        
        scene.paint(g2d);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(480, 480);
    }
    
    public static void main(String[] args) throws IOException {
        final KiteCanvas canvas = new KiteCanvas();
        JFrame frame = new JFrame("Kite");
        frame.add(canvas);
        frame.pack();
        
        frame.setVisible(true);
        
        new Timer(40, new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                canvas.tick();
            }
        }).start();
    }
    
}