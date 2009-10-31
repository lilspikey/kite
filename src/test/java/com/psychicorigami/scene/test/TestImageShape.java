package com.psychicorigami.scene.test;

import com.psychicorigami.scene.ImageShape;

import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.IOException;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class TestImageShape {
    private BufferedImage block32x32 = null;
    private ImageShape blockShape = null;
    
    @Before
    public void setUp() throws IOException {
        block32x32 = ImageIO.read(getClass().getResourceAsStream("/block32x32.png"));
        blockShape = new ImageShape(block32x32, 0, 0);
    }
    
    @Test
    public void testContainsNotRotatedSolidBlock() {
        assertTrue( blockShape.contains(0, 0) );
        
        // check edges
        assertTrue( blockShape.contains(16, 0) );
        assertTrue( blockShape.contains(0, 16) );
        assertTrue( blockShape.contains(-15, 0) );
        assertTrue( blockShape.contains(0, -15) );
        
        // check corners
        assertTrue( blockShape.contains(16, 16) );
        assertTrue( blockShape.contains(-15, 16) );
        assertTrue( blockShape.contains(-15, -15) );
        assertTrue( blockShape.contains(16, -15) );
        
        // check just outside area
        assertFalse( blockShape.contains(17, 0) );
        assertFalse( blockShape.contains(0, 17) );
        assertFalse( blockShape.contains(-16, 0) );
        assertFalse( blockShape.contains(0, -16) );
    }
    
    @Test
    public void testContainsRotatedSolidBlock() {
        blockShape.setAngle(Math.PI/4);
        
        assertTrue( blockShape.contains(0, 0) );
        
        // corners (when rotated)
        assertTrue( blockShape.contains(22, 0) );
        assertTrue( blockShape.contains(0, 22) );
        assertTrue( blockShape.contains(-22, 0) );
        assertTrue( blockShape.contains(0, -22) );
        
        // now check un-rotated corner positions aren't inside
        assertFalse( blockShape.contains(16, 16) );
        assertFalse( blockShape.contains(-15, 16) );
        assertFalse( blockShape.contains(-15, -15) );
        assertFalse( blockShape.contains(16, -15) );
    }
    
}