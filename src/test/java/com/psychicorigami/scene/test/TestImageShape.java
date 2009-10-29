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
        assertTrue( blockShape.contains(15, 0) );
        assertTrue( blockShape.contains(0, 15) );
        assertTrue( blockShape.contains(-16, 0) );
        assertTrue( blockShape.contains(0, -16) );
        
        assertFalse( blockShape.contains(16, 0) );
        assertFalse( blockShape.contains(0, 16) );
        assertFalse( blockShape.contains(-17, 0) );
        assertFalse( blockShape.contains(0, -17) );
    }
    
    /*@Test
    public void testContainsRotatedSolidBlock() {
        blockShape.setAngle(Math.PI/4);
        
        assertTrue( blockShape.contains(0, 0) );
        assertTrue( blockShape.contains(11, 0) );
        assertFalse( blockShape.contains(12, 0) );
    }*/
    
}