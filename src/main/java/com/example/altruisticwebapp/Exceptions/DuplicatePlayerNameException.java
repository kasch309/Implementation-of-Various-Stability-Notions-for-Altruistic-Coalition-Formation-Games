package com.example.altruisticwebapp.Exceptions;

public class DuplicatePlayerNameException extends Exception{
    public DuplicatePlayerNameException(){
        super("Duplicate player name. \n");
    }
}
