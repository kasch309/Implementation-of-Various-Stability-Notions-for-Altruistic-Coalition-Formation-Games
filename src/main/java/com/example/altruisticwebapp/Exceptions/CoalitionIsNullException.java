package com.example.altruisticwebapp.Exceptions;

import com.example.altruisticwebapp.Components.Coalition;

public class CoalitionIsNullException extends Exception{
    public CoalitionIsNullException(Coalition c){
        super("Coalition is null.\n");
    }
}
