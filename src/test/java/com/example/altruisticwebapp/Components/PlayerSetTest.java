package com.example.altruisticwebapp.Components;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class PlayerSetTest {

    @Test
    void containsPlayerWithName() {
        PlayerSet ps = new PlayerSet(5);
        ps.get(2).setName("Test");
        assertTrue(ps.containsPlayerWithName("Test"));
    }

    @Test
    void generateCoalitionStructures() throws Exception {
        PlayerSet ps = new PlayerSet(4);

        HashSet<CoalitionStructure> coalStruc = ps.generateCoalitionStructures();
        for (CoalitionStructure cs : coalStruc){
            assertNotEquals(null, cs);
            for (int i = 0; i < cs.size(); i++){
                assertNotNull(cs.get(i));
            }
        }
        assertEquals(15, coalStruc.size());
    }

    @Test
    void generateTimeTest5() throws Exception {
        PlayerSet ps = new PlayerSet(5);
        HashSet<CoalitionStructure> cs = ps.generateCoalitionStructures();
    }
    @Test
    void generateTimeTest6() throws Exception {
        PlayerSet ps = new PlayerSet(6);
        HashSet<CoalitionStructure> cs = ps.generateCoalitionStructures();
    }

    @Test
    void generateTimeTest7() throws Exception {
        PlayerSet ps = new PlayerSet(7);
        HashSet<CoalitionStructure> cs = ps.generateCoalitionStructures();
    }

    @Test
    void generateTimeTest8() throws Exception {
        PlayerSet ps = new PlayerSet(8);
        HashSet<CoalitionStructure> cs = ps.generateCoalitionStructures();
    }

    @Test
    void generateTimeTest9() throws Exception {
        PlayerSet ps = new PlayerSet(9);
        HashSet<CoalitionStructure> cs = ps.generateCoalitionStructures();
    }

    @Test
    void generateTimeTest10() throws Exception {
        PlayerSet ps = new PlayerSet(10);
        HashSet<CoalitionStructure> cs = ps.generateCoalitionStructures();
    }





    @Test
    void removePlayer() {
        PlayerSet ps = new PlayerSet(3);
        NetworkOfFriends nw = new NetworkOfFriends(3);
        ps.get(0).setName("TestName");
        nw.addFriendship(0, 1);
        nw.addFriendship(1, 2);
        ps.removePlayer(ps.get(0), nw);
        assertEquals(2, ps.size());
        assertEquals(-1, nw.getVal(0, 0));
        assertEquals(-1, nw.getVal(1, 1));
        assertEquals(1, nw.getVal(1, 0));
        assertEquals(1, nw.getVal(0, 1));
    }
}