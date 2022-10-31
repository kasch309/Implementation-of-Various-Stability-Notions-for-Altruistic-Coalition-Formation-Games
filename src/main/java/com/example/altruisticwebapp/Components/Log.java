package com.example.altruisticwebapp.Components;

import java.util.HashMap;

public class Log extends HashMap<Integer, String> {

    public void addEntry(String str){
        this.put(this.size(), str);
    }
}
