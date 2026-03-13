package com.jaov.moba.components;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MovementComponentTest {

    @Test
    public void testInitialSpeed() {
        MovementComponent move = new MovementComponent(200f);
        assertEquals(move.speed, 200f);
    }

    @Test
    public void testInitialState() {
        MovementComponent move = new MovementComponent(200f);
        assertFalse(move.moving);
        assertEquals(move.target.x, 0f);
        assertEquals(move.target.y, 0f);
    }
}
