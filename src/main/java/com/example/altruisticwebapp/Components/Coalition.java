package com.example.altruisticwebapp.Components;

import java.util.HashSet;

public class Coalition extends HashSet<Player> {

    private String name;
    private int key;

    public Coalition (){
        this.name = "";
    }
    public Coalition (String name){
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setKey(int key){
        this.key = key;
    }
    public int getKey(){
        return this.key;
    }

    public void addPlayerSet(PlayerSet ps){
        for (int i = 0; i < ps.size(); i++){
            this.add(ps.get(i));
        }
    }

    public String getName(){
        return name;
    }
    public int numberOfFriends(Player p, NetworkOfFriends nw)  {
        int friends = 0;
        for (Player q : this){
            if (p.areFriends(q, nw)) {
                friends++;
            }
        }
        return friends;
    }

    public int numberOfEnemies(Player p, NetworkOfFriends nw) {
        int enemies = 0;
        for (Player q : this){
            if (p.areEnemies(q, nw)) enemies++;
        }
        return enemies;
    }

    public int value(Player p, NetworkOfFriends nw) {
        return nw.getSize() * numberOfFriends(p, nw) - numberOfEnemies(p, nw);
    }

    public Coalition duplicate(){
        Coalition a = new Coalition();
        a.addAll(this);
        return a;
    }

    public double avg(Player p, NetworkOfFriends nw){ //average friend oriented valuation of p's friends in coalition

        /*
        avg = {sum of all friends a of i in coalition} value that a gives coalition / amount of friends in coalition
         */

        double sum = 0;
        if (numberOfFriends(p, nw) == 0) return 0;
        for (Player q : this){
            if (p.areFriends(q, nw)){
                sum = sum + this.value(q, nw);
            }
        }
        return sum / numberOfFriends(p, nw);
    }

    public double avgPlus(Player p, NetworkOfFriends nw){ //average friend oriented valuation of p AND p's friends in coalition
        double sum = this.value(p, nw);
        if (numberOfFriends(p, nw) == 0) return value(p, nw);
        int numberOfFriends = 0;
        for (Player q : this){
            if (p.areFriends(q, nw)){
                if (this.contains(q)){
                    numberOfFriends++;
                    sum = sum + this.value(q, nw);
                }
            }
        }
        sum = sum / (numberOfFriends + 1);
        return sum;
    }

    public double min(Player p, NetworkOfFriends nw){
        double minVal = 1000;
        boolean hasFriends = false;
        for (Player q : this){
            if (p.areFriends(q, nw)) {
                hasFriends = true;
                int val = value(q, nw);
                if (val < minVal){
                    minVal = val;
                }
            }
        }
        if (!hasFriends) return 0;
        return minVal;
    }
    public double minPlus(Player p, NetworkOfFriends nw){
        double minVal = min(p, nw);
        if (value(p, nw) < minVal) minVal = value(p, nw);
        return minVal;
    }
}
