package com.example.altruisticwebapp.Exceptions;

import com.example.altruisticwebapp.Components.Coalition;

public class CoalitionIsNullException extends Exception{
    public CoalitionIsNullException(String additionalInfo){
        super("Coalition is null.\n" + additionalInfo + "\n");
    }
}
