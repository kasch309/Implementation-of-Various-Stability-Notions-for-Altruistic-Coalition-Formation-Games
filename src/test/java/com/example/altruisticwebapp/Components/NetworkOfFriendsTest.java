package com.example.altruisticwebapp.Components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NetworkOfFriendsTest {

    @Test
    void getSize() {
        NetworkOfFriends nw = new NetworkOfFriends(5);
        assertEquals(5, nw.getSize());
    }

    @Test
    void init() {
        NetworkOfFriends nw = new NetworkOfFriends(5); //init() is called in constructor
        for (int i = 0; i < nw.getMatrix().length; i++){
            for (int j = 0; j < nw.getMatrix().length; j++){
                if (i == j) assertEquals(-1, nw.getMatrix()[i][j]);
                else assertEquals(0, nw.getMatrix()[i][j]);
            }
        }
    }

    @Test
    void numberOfPlayers() {
        NetworkOfFriends nw = new NetworkOfFriends(5);
        assertEquals(5, nw.numberOfPlayers());
    }

    @Test
    void getMatrix() {
        NetworkOfFriends nw = new NetworkOfFriends(5);
        int [][] mat = new int [5][5];
        mat[1][2] = 1;
        mat[2][1] = 1;
        nw.addFriendship(1, 2);
        for (int i = 0; i < nw.getMatrix().length; i++){
            for (int j = 0; j < nw.getMatrix().length; j++){
                if (i == j) assertEquals(-1, nw.getMatrix()[i][j]);
                else assertEquals(mat[i][j], nw.getMatrix()[i][j]);
            }
        }
    }

    @Test
    void addFriendshipAreFriends() {
        NetworkOfFriends nw = new NetworkOfFriends(5);
        nw.addFriendship(1, 2);
        assertTrue(nw.areFriends(1, 2));
    }

    @Test
    void addPlayer() {
        NetworkOfFriends nw = new NetworkOfFriends(4);
        nw.addPlayer();
        assertEquals(5, nw.getMatrix().length);
        assertEquals(5, nw.getSize());
    }
}