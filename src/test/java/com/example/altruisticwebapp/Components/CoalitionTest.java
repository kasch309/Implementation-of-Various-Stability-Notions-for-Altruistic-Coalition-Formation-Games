package com.example.altruisticwebapp.Components;

import com.example.altruisticwebapp.Exceptions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoalitionTest {

    @Test
    void numberOfFriends() throws NoNetworkAssignedException, NoPlayerSetAssignedException {
        Game g = new Game(6);
        g.getNetwork().addFriendship(0, 1);
        g.getNetwork().addFriendship(0, 2);
        g.getNetwork().addFriendship(0, 5);
        g.getNetwork().addFriendship(1, 4);
        g.getNetwork().addFriendship(3, 4);
        g.getNetwork().addFriendship(4, 5);
        Coalition a = new Coalition();
        a.add(g.getPlayer(1));
        a.add(g.getPlayer(2));
        a.add(g.getPlayer(4));
        Coalition b = new Coalition();
        b.add(g.getPlayer(3));
        b.add(g.getPlayer(5));

        assertEquals(2, a.numberOfFriends(g.getPlayer(0), g.getNetwork()));
        assertEquals(1, b.numberOfFriends(g.getPlayer(0), g.getNetwork()));
    }

    @Test
    void numberOfEnemies() throws NoNetworkAssignedException, NoPlayerSetAssignedException {
        Game g = new Game(6);
        g.getNetwork().addFriendship(0, 1);
        g.getNetwork().addFriendship(0, 2);
        g.getNetwork().addFriendship(0, 5);
        g.getNetwork().addFriendship(1, 4);
        g.getNetwork().addFriendship(3, 4);
        g.getNetwork().addFriendship(4, 5);
        Coalition a = new Coalition();
        a.add(g.getPlayer(1));
        a.add(g.getPlayer(2));
        a.add(g.getPlayer(4));
        Coalition b = new Coalition();
        b.add(g.getPlayer(3));
        b.add(g.getPlayer(5));

        assertEquals(1, a.numberOfEnemies(g.getPlayer(0), g.getNetwork()));
        assertEquals(1, b.numberOfEnemies(g.getPlayer(0), g.getNetwork()));
    }

    @Test
    void value() throws NoNetworkAssignedException, NoPlayerSetAssignedException {
        Game z = new Game(5);
        z.getNetwork().addFriendship(3, 2);
        z.getNetwork().addFriendship(2, 0);
        z.getNetwork().addFriendship(0, 1);
        z.getNetwork().addFriendship(1, 4);

        Coalition a = new Coalition();
        a.add(z.getPlayer(0));
        a.add(z.getPlayer(1));
        a.add(z.getPlayer(2));

        Coalition b = new Coalition();
        b.add(z.getPlayer(0));
        b.add(z.getPlayer(1));
        b.add(z.getPlayer(2));
        b.add(z.getPlayer(3));

        Coalition c = new Coalition();
        c.add(z.getPlayer(0));
        c.add(z.getPlayer(1));
        c.add(z.getPlayer(2));
        c.add(z.getPlayer(4));

        Coalition d = new Coalition();
        d.add(z.getPlayer(0));
        d.add(z.getPlayer(1));
        d.add(z.getPlayer(2));
        d.add(z.getPlayer(3));
        d.add(z.getPlayer(4));

        Coalition e = new Coalition();
        e.add(z.getPlayer(0));
        e.add(z.getPlayer(1));

        Coalition f = new Coalition();
        f.add(z.getPlayer(0));
        f.add(z.getPlayer(2));

        Coalition g = new Coalition();
        g.add(z.getPlayer(0));
        g.add(z.getPlayer(1));
        g.add(z.getPlayer(3));

        Coalition h = new Coalition();
        h.add(z.getPlayer(0));
        h.add(z.getPlayer(1));
        h.add(z.getPlayer(4));

        Coalition i = new Coalition();
        i.add(z.getPlayer(0));
        i.add(z.getPlayer(2));
        i.add(z.getPlayer(3));

        assertEquals(10, a.value(z.getPlayer(0), z.getNetwork()));
        assertEquals(9, b.value(z.getPlayer(0), z.getNetwork()));
        assertEquals(9, c.value(z.getPlayer(0), z.getNetwork()));
        assertEquals(8, d.value(z.getPlayer(0), z.getNetwork()));
        assertEquals(5, e.value(z.getPlayer(0), z.getNetwork()));
        assertEquals(5, f.value(z.getPlayer(0), z.getNetwork()));
        assertEquals(4, g.value(z.getPlayer(0), z.getNetwork()));
        assertEquals(4, h.value(z.getPlayer(0), z.getNetwork()));
        assertEquals(4, i.value(z.getPlayer(0), z.getNetwork()));

        assertEquals(4, a.value(z.getPlayer(1), z.getNetwork()));
        assertEquals(3, b.value(z.getPlayer(1), z.getNetwork()));
        assertEquals(9, c.value(z.getPlayer(1), z.getNetwork()));
        assertEquals(8, d.value(z.getPlayer(1), z.getNetwork()));
        assertEquals(5, e.value(z.getPlayer(1), z.getNetwork()));
        assertEquals(4, g.value(z.getPlayer(1), z.getNetwork()));
        assertEquals(10, h.value(z.getPlayer(1), z.getNetwork()));

        assertEquals(4, a.value(z.getPlayer(2), z.getNetwork()));
        assertEquals(9, b.value(z.getPlayer(2), z.getNetwork()));
        assertEquals(3, c.value(z.getPlayer(2), z.getNetwork()));
        assertEquals(8, d.value(z.getPlayer(2), z.getNetwork()));
        assertEquals(5, f.value(z.getPlayer(2), z.getNetwork()));
        assertEquals(10, i.value(z.getPlayer(2), z.getNetwork()));
    }

    @Test
    void duplicate() throws NoNetworkAssignedException, NoPlayerSetAssignedException {
        Game g = new Game(6);
        g.getNetwork().addFriendship(0, 1);
        g.getNetwork().addFriendship(0, 2);
        g.getNetwork().addFriendship(0, 5);
        g.getNetwork().addFriendship(1, 4);
        g.getNetwork().addFriendship(3, 4);
        g.getNetwork().addFriendship(4, 5);
        Coalition a = new Coalition();
        a.add(g.getPlayer(1));
        a.add(g.getPlayer(2));
        a.add(g.getPlayer(4));
        Coalition b = a.duplicate();
        assertEquals(a, b);
    }

    @Test
    void avg() throws NoNetworkAssignedException, NoPlayerSetAssignedException {
        Game z = new Game(5);
        z.getNetwork().addFriendship(3, 2);
        z.getNetwork().addFriendship(2, 0);
        z.getNetwork().addFriendship(0, 1);
        z.getNetwork().addFriendship(1, 4);

        Coalition a = new Coalition();
        a.add(z.getPlayer(0));
        a.add(z.getPlayer(1));
        a.add(z.getPlayer(2));

        Coalition b = new Coalition();
        b.add(z.getPlayer(0));
        b.add(z.getPlayer(1));
        b.add(z.getPlayer(2));
        b.add(z.getPlayer(3));

        Coalition c = new Coalition();
        c.add(z.getPlayer(0));
        c.add(z.getPlayer(1));
        c.add(z.getPlayer(2));
        c.add(z.getPlayer(4));

        Coalition d = new Coalition();
        d.add(z.getPlayer(0));
        d.add(z.getPlayer(1));
        d.add(z.getPlayer(2));
        d.add(z.getPlayer(3));
        d.add(z.getPlayer(4));

        Coalition e = new Coalition();
        e.add(z.getPlayer(0));
        e.add(z.getPlayer(1));

        Coalition f = new Coalition();
        f.add(z.getPlayer(0));
        f.add(z.getPlayer(2));

        Coalition g = new Coalition();
        g.add(z.getPlayer(0));
        g.add(z.getPlayer(1));
        g.add(z.getPlayer(3));

        Coalition h = new Coalition();
        h.add(z.getPlayer(0));
        h.add(z.getPlayer(1));
        h.add(z.getPlayer(4));

        Coalition i = new Coalition();
        i.add(z.getPlayer(0));
        i.add(z.getPlayer(2));
        i.add(z.getPlayer(3));

        assertEquals(4, a.avg(z.getPlayer(0), z.getNetwork()));
        assertEquals(6, b.avg(z.getPlayer(0), z.getNetwork()));
        assertEquals(6, c.avg(z.getPlayer(0), z.getNetwork()));
        assertEquals(8, d.avg(z.getPlayer(0), z.getNetwork()));
        assertEquals(5, e.avg(z.getPlayer(0), z.getNetwork()));
        assertEquals(5, f.avg(z.getPlayer(0), z.getNetwork()));
        assertEquals(4, g.avg(z.getPlayer(0), z.getNetwork()));
        assertEquals(10, h.avg(z.getPlayer(0), z.getNetwork()));
        assertEquals(10, i.avg(z.getPlayer(0), z.getNetwork()));
    }
    @Test
    void avgPlus() throws NoPlayerSetAssignedException, NoNetworkAssignedException {
        Game z = new Game(5);
        z.getNetwork().addFriendship(3, 2);
        z.getNetwork().addFriendship(2, 0);
        z.getNetwork().addFriendship(0, 1);
        z.getNetwork().addFriendship(1, 4);

        Coalition a = new Coalition();
        a.add(z.getPlayer(0));
        a.add(z.getPlayer(1));
        a.add(z.getPlayer(2));

        Coalition b = new Coalition();
        b.add(z.getPlayer(0));
        b.add(z.getPlayer(1));
        b.add(z.getPlayer(2));
        b.add(z.getPlayer(3));

        Coalition c = new Coalition();
        c.add(z.getPlayer(0));
        c.add(z.getPlayer(1));
        c.add(z.getPlayer(2));
        c.add(z.getPlayer(4));

        Coalition d = new Coalition();
        d.add(z.getPlayer(0));
        d.add(z.getPlayer(1));
        d.add(z.getPlayer(2));
        d.add(z.getPlayer(3));
        d.add(z.getPlayer(4));

        Coalition e = new Coalition();
        e.add(z.getPlayer(0));
        e.add(z.getPlayer(1));

        Coalition f = new Coalition();
        f.add(z.getPlayer(0));
        f.add(z.getPlayer(2));

        Coalition g = new Coalition();
        g.add(z.getPlayer(0));
        g.add(z.getPlayer(1));
        g.add(z.getPlayer(3));

        Coalition h = new Coalition();
        h.add(z.getPlayer(0));
        h.add(z.getPlayer(1));
        h.add(z.getPlayer(4));

        Coalition i = new Coalition();
        i.add(z.getPlayer(0));
        i.add(z.getPlayer(2));
        i.add(z.getPlayer(3));

        assertEquals(6, a.avgPlus(z.getPlayer(0), z.getNetwork()));
        assertEquals(7, b.avgPlus(z.getPlayer(0), z.getNetwork()));
        assertEquals(7, c.avgPlus(z.getPlayer(0), z.getNetwork()));
        assertEquals(8, d.avgPlus(z.getPlayer(0), z.getNetwork()));
        assertEquals(5, e.avgPlus(z.getPlayer(0), z.getNetwork()));
        assertEquals(5, f.avgPlus(z.getPlayer(0), z.getNetwork()));
        assertEquals(4, g.avgPlus(z.getPlayer(0), z.getNetwork()));
        assertEquals(7, h.avgPlus(z.getPlayer(0), z.getNetwork()));
        assertEquals(7, i.avgPlus(z.getPlayer(0), z.getNetwork()));
    }
}