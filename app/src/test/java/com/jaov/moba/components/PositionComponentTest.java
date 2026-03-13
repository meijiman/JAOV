package com.jaov.moba.components;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class PositionComponentTest {
    @Test
    public void testInitialPosition() {
        PositionComponent pos = new PositionComponent(100f, 200f);
        assertEquals(pos.position.x, 100f);
        assertEquals(pos.position.y, 200f);
    }

    @Test
    public void testZeroPosition() {
        PositionComponent pos = new PositionComponent(0f, 0f);
        assertEquals(pos.position.x, 0f);
        assertEquals(pos.position.y, 0f);
    }

    @Test
    public void testNegativePosition() {
        PositionComponent pos = new PositionComponent(-50f, -75f);
        assertEquals(pos.position.x, -50f);
        assertEquals(pos.position.y, -75f);
    }
}
