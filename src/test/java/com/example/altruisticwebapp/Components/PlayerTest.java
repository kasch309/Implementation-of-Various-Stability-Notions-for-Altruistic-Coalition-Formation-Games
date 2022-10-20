package com.example.altruisticwebapp.Components;

import com.example.altruisticwebapp.Exceptions.NoPlayerSetAssignedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void setName() throws NoPlayerSetAssignedException {
        Game g = new Game(2);
        g.getPlayer(1).setName("Günni");
        assertEquals("Günni", g.getPlayer(1).getName());

    }
}