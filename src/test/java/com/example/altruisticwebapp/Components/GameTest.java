package com.example.altruisticwebapp.Components;

import com.example.altruisticwebapp.Exceptions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void getNetwork() throws NoNetworkAssignedException {
        Game g = new Game(5);
        NetworkOfFriends nwControl = new NetworkOfFriends(5);
        nwControl.addFriendship(1, 2);
        nwControl.addFriendship(1, 3);
        g.getNetwork().addFriendship(1, 2);
        g.getNetwork().addFriendship(1, 3);
        for (int i = 0; i < g.getNetwork().getSize(); i++){
            for (int j = 0; j < g.getNetwork().getSize(); j++){
                assertEquals(nwControl.getMatrix()[i][j], g.getNetwork().getMatrix()[i][j]);
            }
        }
    }

    @Test
    void setNetwork() throws NoNetworkAssignedException {
        NetworkOfFriends nw = new NetworkOfFriends(5);
        nw.addFriendship(1, 2);
        nw.addFriendship(1, 3);
        Game g = new Game(new NetworkOfFriends(1));
        g.setNetwork(nw);
        assertEquals(nw, g.getNetwork());
    }

    @Test
    void getPlayers() throws NoPlayerSetAssignedException {
        Game g = new Game (5);
        assertTrue(g.getPlayers().containsKey(4) && !g.getPlayers().containsKey(5));
    }

    @Test
    void addPlayer() throws NoNetworkAssignedException, NoPlayerSetAssignedException {
        Game g = new Game(5);
        g.addPlayer(new Player("Test"));
        assertTrue(g.getPlayers().containsKey(5));
        assertTrue(g.getPlayers().containsPlayerWithName("Test"));
    }

    @Test
    void singletons() throws NoPlayerSetAssignedException {
        Game g = new Game(5);
        CoalitionStructure singletons = g.singletons();
        for (int i = 0; i < singletons.size(); i++){
            Coalition c = singletons.get(i);
            assertEquals(1, c.size());
        }
        assertEquals(5, singletons.size());

    }

    @Test
    void allInOne() throws NoPlayerSetAssignedException {
        Game g = new Game(5);
        CoalitionStructure allInOne = g.allInOne();
        for (int i = 0; i < allInOne.size(); i++){
            Coalition c = allInOne.get(i);
            assertEquals(5, c.size());
        }
        assertEquals(1, allInOne.size());

    }

}