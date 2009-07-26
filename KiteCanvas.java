import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

public class KiteCanvas extends JPanel {
    private Space2D space = new Space2D();
    
    private ArrayList<Body2D> kiteString = new ArrayList<Body2D>();
    
    public KiteCanvas() {
        space.setGravity( new Vector2D(0, -10) );
        space.addGlobalConstraint(new FloorConstraint());
        
        Body2D prev = new Body2D(10, 10);
        space.add(prev);
        kiteString.add(prev);
        
        for ( int i = 0; i < 100; i++ ) {
            Body2D bi = new Body2D(prev.getPos().x + 3, prev.getPos().y + 3);
            space.add(bi);
            space.add(new StickConstraint<Body2D, Vector2D>(bi, prev));
            prev = bi;
            kiteString.add(prev);
        }
    }
    
    public void tick() {
        space.update(0.04);
        repaint();
    }
    
    @Override
    public void paint(Graphics g) {
        int height = getHeight();
        g.clearRect(0, 0, getWidth(), height);
        g.translate(0, -10);
        
        Vector2D prevPos = null;
        for ( Body2D b: kiteString ) {
            Vector2D pos = b.getPos();
            if ( prevPos != null ) {
                g.drawLine((int)(pos.x), (int)(height - pos.y), (int)(prevPos.x), (int)(height - prevPos.y));
            }
            prevPos = pos;
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(480, 480);
    }
    
    public static void main(String[] args) {
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