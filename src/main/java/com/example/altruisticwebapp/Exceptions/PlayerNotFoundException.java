package com.example.altruisticwebapp.Exceptions;

public class PlayerNotFoundException extends Exception{

    public PlayerNotFoundException(String player) {
        super("Player " + player + " not found.\n");

    }

}