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
        assertTrue(cs.individuallyRational(g, LOA.ETavg));
    }

    @Test
    void nashStable() throws NoNetworkAssignedException, NoPlayerSetAssignedException, PlayerNotFoundException, InvalidLevelOfAltruismException, CoalitionIsNullException {
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
        assertTrue(cs.nashStable(g, LOA.ETavg));
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
        assertTrue(cs.individuallyStable(g, LOA.ETavg));
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
        assertTrue(cs.contractuallyIndividuallyStable(g, LOA.ETavg));
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
        for (int i = 0; i < 4; i++){
            System.out.println("Player " + i + ":");
            for (int j = 0; j < 4; j++){
                System.out.println("  Coalition " + j + ":");
                System.out.println("    UtilitySFavg: " + g.getPlayer(i).utilitySFavg(cs.get(j), g.getNetwork()));
                System.out.println("    UtilityEQavg: " + g.getPlayer(i).utilityETavg(cs.get(j), g.getNetwork()));
                System.out.println("    UtilityALavg: " + g.getPlayer(i).utilityATavg(cs.get(j), g.getNetwork()));
            }
        }
        assertTrue(cs.perfect(g, LOA.ETavg));
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
}