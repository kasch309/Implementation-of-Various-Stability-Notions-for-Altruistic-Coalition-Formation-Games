package com.example.altruisticwebapp.Components;

import java.util.HashMap;

public class Log extends HashMap<Integer, String> {


    public void addEntry(String str){
        this.put(this.size(), str);
    }

    public Log output(){
        Log output = new Log();
        for (int i = this.size()-1; i >= 0; i--){
            output.put(output.size(), this.get(i));
        }
        return output;
    }
}
