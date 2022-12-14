package com.example.altruisticwebapp.Components;

import com.example.altruisticwebapp.Exceptions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoalitionStructureTest {

    @Test
    void getPlayersCoalition() throws NoPlayerSetAssignedException {
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
    void removePlayersAndGetCoalitions() throws NoPlayerSetAssignedException, NoNetworkAssignedException {
        Game g = new Game(6);
        CoalitionStructure cs = new CoalitionStructure();
        Coalition c = new Coalition();
        Coalition d = new Coalition();
        cs.addCoalition(c);
        cs.addCoalition(d);
        cs.get(0).add(g.getPlayer(0));
        g.removePlayer(g.getPlayer(2), cs);
        g.addPlayer("new player");

    }

    @Test
    void blockingCoalitions() {
    }

    @Test
    void weaklyBlockingCoalitions() {
    }

    @Test
    void individuallyRational() throws NoNetworkAssignedException, NoPlayerSetAssignedException, InvalidLevelOfAltruismException, CoalitionIsNullException {
        Game g = new Game(20);
        g.getNetwork().addFriendship(0, 1);
        g.getNetwork().addFriendship(2, 3);
        g.getNetwork().addFriendship(3, 4);
        g.getNetwork().addFriendship(16,18);
        CoalitionStructure cs = g.singletons();
        assertTrue(cs.individuallyRational(g, LOA.ETavg, false));
    }

    @Test
    void nashStable() throws NoNetworkAssignedException, NoPlayerSetAssignedException, InvalidLevelOfAltruismException, CoalitionIsNullException {
        Game g = new Game(5);
        g.getNetwork().addFriendship(0, 1);
        g.getNetwork().addFriendship(2, 3);
        g.getNetwork().addFriendship(3, 4);
        CoalitionStructure cs = new CoalitionStructure();
        Coalition c1 = new Coalition();
        c1.add(g.getPlayer(0));
        c1.add(g.getPlayer(1));
        Coalition c2 = new Coalition();
        c2.add((g.getPlayer(2)));
        c2.add(g.getPlayer(3));
        c2.add(g.getPlayer(4));
        Coalition c3 = new Coalition();
        cs.addCoalition(c1);
        cs.addCoalition(c2);
        cs.addCoalition(c3);
        assertTrue(cs.nashStable(g, LOA.ETavg, false));
    }

    @Test
    void individuallyStable() throws NoPlayerSetAssignedException, NoNetworkAssignedException, PlayerNotFoundException, CoalitionIsNullException, InvalidLevelOfAltruismException {
        Game g = new Game(5);
        g.getNetwork().addFriendship(0, 1);
        g.getNetwork().addFriendship(2, 3);
        g.getNetwork().addFriendship(3, 4);
        CoalitionStructure cs = new CoalitionStructure();
        Coalition c1 = new Coalition();
        c1.add(g.getPlayer(0));
        c1.add(g.getPlayer(1));
        Coalition c2 = new Coalition();
        c2.add((g.getPlayer(2)));
        c2.add(g.getPlayer(3));
        c2.add(g.getPlayer(4));
        Coalition c3 = new Coalition();
        cs.addCoalition(c1);
        cs.addCoalition(c2);
        cs.addCoalition(c3);
        assertTrue(cs.individuallyStable(g, LOA.ETavg, false));
    }

    @Test
    void contractuallyIndividuallyStable() throws NoNetworkAssignedException, PlayerNotFoundException, CoalitionIsNullException, InvalidLevelOfAltruismException, NoPlayerSetAssignedException {
        Game g = new Game(5);
        g.getNetwork().addFriendship(0, 1);
        g.getNetwork().addFriendship(2, 3);
        g.getNetwork().addFriendship(3, 4);
        CoalitionStructure cs = new CoalitionStructure();
        Coalition c1 = new Coalition();
        c1.add(g.getPlayer(0));
        c1.add(g.getPlayer(1));
        Coalition c2 = new Coalition();
        c2.add((g.getPlayer(2)));
        c2.add(g.getPlayer(3));
        c2.add(g.getPlayer(4));
        Coalition c3 = new Coalition();
        cs.addCoalition(c1);
        cs.addCoalition(c2);
        cs.addCoalition(c3);
        assertTrue(cs.contractuallyIndividuallyStable(g, LOA.ETavg, false));
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
    void perfect() throws Exception {
        Game g = new Game(4);
        CoalitionStructure cs = new CoalitionStructure();
        cs.addCoalition(new Coalition());
        cs.get(0).add(g.getPlayer(0));
        cs.addCoalition(new Coalition());
        cs.get(1).add(g.getPlayer(1));
        cs.addCoalition(new Coalition());
        cs.get(2).add(g.getPlayer(2));
        cs.addCoalition(new Coalition());
        cs.get(3).add(g.getPlayer(3));
        assertTrue(cs.perfect(g, LOA.ETavg, false));
    }

    @Test
    void addCoalition() {
        CoalitionStructure cs = new CoalitionStructure();
        cs.addCoalition(new Coalition());
        assertEquals(1, cs.size());
        cs.addCoalition(new Coalition());
        assertEquals(2, cs.size());
    }

    @Test
    void removeCoalition() {
        CoalitionStructure cs = new CoalitionStructure();
        Coalition a = new Coalition();
        Coalition b = new Coalition();
        Coalition c = new Coalition();
        cs.addCoalition(a);
        assertEquals(0, a.getKey());
        cs.addCoalition(b);
        assertEquals(1, b.getKey());
        cs.addCoalition(c);
        assertEquals(2, c.getKey());
        cs.removeCoalition(1);
        assertEquals(1, c.getKey());
        assertTrue(cs.containsKey(1));
        assertEquals(c, cs.get(1));
    }

    @Test
    void edgeCaseNash() throws NoNetworkAssignedException, NoPlayerSetAssignedException, CoalitionIsNullException, InvalidLevelOfAltruismException {
        Game g = new Game(5);
        g.getNetwork().addFriendship(0, 1);
        g.getNetwork().addFriendship(0, 2);
        Coalition player012 = new Coalition();
        Coalition player3 = new Coalition();
        Coalition player4 = new Coalition();
        player012.add(g.getPlayer(0));
        player012.add(g.getPlayer(1));
        player012.add(g.getPlayer(2));
        player3.add(g.getPlayer(3));
        player4.add(g.getPlayer(4));
        CoalitionStructure cs = new CoalitionStructure();
        cs.addCoalition(player012);
        cs.addCoalition(player3);
        cs.addCoalition(player4);
        assertTrue(cs.nashStable(g, LOA.SFavg, false));
    }

    @Test
    void notPerfect() throws Exception {
        Game g = new Game(5);
        g.getNetwork().addFriendship(0, 1);
        g.getNetwork().addFriendship(0, 2);
        Coalition player012 = new Coalition();
        Coalition player3 = new Coalition();
        Coalition player4 = new Coalition();
        player012.add(g.getPlayer(0));
        player012.add(g.getPlayer(1));
        player012.add(g.getPlayer(2));
        player3.add(g.getPlayer(3));
        player4.add(g.getPlayer(4));
        CoalitionStructure cs = new CoalitionStructure();
        cs.addCoalition(player012);
        cs.addCoalition(player3);
        cs.addCoalition(player4);
        assertFalse(cs.perfect(g, LOA.SFavg, false));
    }

    @Test
    void perfectMinAl() throws Exception {
        Game g = new Game(4);
        g.getNetwork().addFriendship(0, 1);
        g.getNetwork().addFriendship(0, 2);
        g.getNetwork().addFriendship(0, 3);
        g.getNetwork().addFriendship(1, 2);
        g.getNetwork().addFriendship(1, 3);
        g.getNetwork().addFriendship(2, 3);
        CoalitionStructure cs = new CoalitionStructure();
        Coalition c = new Coalition();
        c.add(g.getPlayer(0));
        c.add(g.getPlayer(1));
        c.add(g.getPlayer(2));
        c.add(g.getPlayer(3));
        cs.addCoalition(c);
        assertTrue(cs.perfect(g, LOA.ATmin, false));
    }

    @Test
    void ScsAvgSf() throws Exception {
        Game g = new Game(5);
        g.getNetwork().addFriendship(0, 1);
        g.getNetwork().addFriendship(0, 2);
        g.getNetwork().addFriendship(2, 3);
        CoalitionStructure cs = new CoalitionStructure();
        Coalition c1 = new Coalition();
        c1.add(g.getPlayer(1));
        Coalition c02 = new Coalition();
        c02.add(g.getPlayer(0));
        c02.add(g.getPlayer(2));
        Coalition c34 = new Coalition();
        c34.add(g.getPlayer(3));
        c34.add(g.getPlayer(4));
        cs.addCoalition(c1);
        cs.addCoalition(c02);
        cs.addCoalition(c34);
        assertFalse(cs.strictlyCoreStable(g, LOA.SFavg, false));
    }
}