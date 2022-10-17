package com.example.altruisticwebapp.Components;

import com.example.altruisticwebapp.Exceptions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoalitionStructureTest {

    @Test
    void getPlayersCoalition() throws NoPlayerSetAssignedException, PlayerNotFoundException {
        Game g = new Game(2);
        CoalitionStructure cs = new CoalitionStructure();

        Coalition c = new Coalition();
        Coalition d = new Coalition();
        cs.addCoalition(c);
        cs.addCoalition(d);
        c.add(g.getPlayer(0));
        d.add(g.getPlayer(1));
        assertEquals(c, cs.getPlayersCoalition(g.getPlayer(0)));
        assertEquals(d, cs.getPlayersCoalition(g.getPlayer(1)));
    }

    @Test
    void blockingCoalitions() {
    }

    @Test
    void weaklyBlockingCoalitions() {
    }

    @Test
    void individuallyRational() throws NoNetworkAssignedException, PlayerNotFoundException, NoPlayerSetAssignedException {
        Game g = new Game(20);
        g.getNetwork().addFriendship(0, 1);
        g.getNetwork().addFriendship(2, 3);
        g.getNetwork().addFriendship(3, 4);
        g.getNetwork().addFriendship(16,18);
        CoalitionStructure cs = g.singletons();
        assertTrue(cs.individuallyRational(g));
    }

    @Test
    void nashStable() {
    }

    @Test
    void individuallyStable() throws NoPlayerSetAssignedException, NoNetworkAssignedException, PlayerNotFoundException {
    }

    @Test
    void contractuallyIndividuallyStable() {
    }

    @Test
    void strictlyPopular() {
    }

    @Test
    void popular() {
    }

    @Test
    void coreStable() {
    }

    @Test
    void strictlyCoreStable() {
    }

    @Test
    void perfect() {
    }

    @Test
    void addCoalition() {
        CoalitionStructure cs = new CoalitionStructure();
        cs.addCoalition("test1");
        assertEquals(1, cs.size());
        cs.addCoalition("k");
        assertEquals(2, cs.size());
    }
}