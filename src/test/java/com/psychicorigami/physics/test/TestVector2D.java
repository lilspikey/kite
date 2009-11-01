package com.psychicorigami.physics.test;

import com.psychicorigami.physics.Vector2D;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestVector2D {
    private final double EPSILON = 0.00000001;
    
    @Test
    public void testDefaultConstructor() {
        Vector2D v = new Vector2D();
        assertEquals( 0.0, v.x, EPSILON );
        assertEquals( 0.0, v.y, EPSILON );
    }
    
    @Test
    public void testConstructor() {
        double x = 1.1;
        double y = 5.0;
        Vector2D v = new Vector2D(x, y);
        assertEquals( x, v.x, EPSILON );
        assertEquals( y, v.y, EPSILON );
    }
    
    @Test
    public void testLength() {
        assertEquals( 5.0, new Vector2D(3.0, 4.0).length(), EPSILON );
    }
    
    @Test
    public void testAdd() {
        Vector2D v = new Vector2D();
        v = v.add(new Vector2D(2.0, 3.0) );
        
        assertEquals( 2.0, v.x, EPSILON );
        assertEquals( 3.0, v.y, EPSILON );
        
        v = v.add(new Vector2D(1.0, 0.5) );
        
        assertEquals( 3.0, v.x, EPSILON );
        assertEquals( 3.5, v.y, EPSILON );
    }
    
    @Test
    public void testSubtract() {
        Vector2D v = new Vector2D();
        v = v.subtract(new Vector2D(2.0, 3.0) );
        
        assertEquals( -2.0, v.x, EPSILON );
        assertEquals( -3.0, v.y, EPSILON );
        
        v = v.subtract(new Vector2D(1.0, 0.5) );
        
        assertEquals( -3.0, v.x, EPSILON );
        assertEquals( -3.5, v.y, EPSILON );
    }
    
    @Test
    public void testScalarMultiply() {
        Vector2D v = new Vector2D(1.0, 2.0);
        v = v.multiply(2.0);
        
        assertEquals( 2.0, v.x, EPSILON );
        assertEquals( 4.0, v.y, EPSILON );
    }
    
    @Test
    public void testMultiplyAdd() {
        Vector2D v = new Vector2D(1.0, 2.0);
        v = v.multiply_add(2.0, new Vector2D(2.0, 1.0));
        
        assertEquals( 4.0, v.x, EPSILON );
        assertEquals( 5.0, v.y, EPSILON );
    }
    
    @Test
    public void testVectorMultiply() {
        Vector2D v = new Vector2D(1.0, 2.0);
        v = v.multiply(new Vector2D(3.0, 4.0));
        
        assertEquals( 3.0, v.x, EPSILON );
        assertEquals( 8.0, v.y, EPSILON );
    }
    
    @Test
    public void testScalarDivide() {
        Vector2D v = new Vector2D(1.0, 2.0);
        v = v.divide(2.0);
        
        assertEquals( 0.5, v.x, EPSILON );
        assertEquals( 1.0, v.y, EPSILON );
    }
    
    @Test
    public void testZero() {
        Vector2D v = new Vector2D(1.0, 2.0);
        v = v.zero();
        
        assertEquals( 0.0, v.x, EPSILON );
        assertEquals( 0.0, v.y, EPSILON );
    }
    
    
    @Test
    public void testUnit() {
        for ( double x: new double[]{ 1.0, 2.0, 3.0, 10.0, 100.0 } ) {
            for ( double y: new double[]{ 1.0, 2.0, 3.0, 10.0, 100.0 } ) {
                Vector2D v = new Vector2D(x, y);
                v = v.unit();
                
                assertEquals(1.0, v.length(), EPSILON );
            }
        }
    }
    
    @Test
    public void testDotProduct() {
        Vector2D v = new Vector2D(1.0, 2.0);
        
        assertEquals( 1.0*2.0 + 2.0*3.0, v.dotProduct(new Vector2D(2.0, 3.0)), EPSILON );
    }
}