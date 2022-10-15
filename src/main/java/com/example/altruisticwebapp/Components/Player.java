package com.example.altruisticwebapp.Components;

import com.example.altruisticwebapp.Exceptions.InvalidLevelOfAltruismException;
public class Player {
    private int key;
    private String name;
    public Player(){ // notwendig? weil wann kein key oder wann neuer spieler dazu
        this.name = "";
    }

    public Player (int key){
        this.name = "Player " + key;
        this.key = key;
    }

    public Player (int key, String name){
        this.key = key;
        this.name = name;
    }

    public Player(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setKey(int key){
        this.key = key;
    }

    public int getKey(){
        return key;
    }

    public boolean acceptable(Coalition c, NetworkOfFriends nw) {
        Coalition onlyPlayer = new Coalition();
        onlyPlayer.add(this);
        return onlyPlayer.value(this, nw) <= c.value(this, nw);
    }

    public boolean weaklyPrefers(Coalition a, Coalition b, NetworkOfFriends nw) {
        return a.value(this, nw) >= b.value(this, nw);
    }

    public boolean prefers(Coalition a, Coalition b, NetworkOfFriends nw){
        return a.value(this, nw) > b.value(this, nw);
    }

    public boolean areFriends(Player p, NetworkOfFriends nw){
        return (nw.getMatrix()[this.key][p.getKey()] == 1);
    }

    public boolean areEnemies(Player p, NetworkOfFriends nw){
        return (nw.getMatrix()[this.key][p.getKey()] == 0);
    }

    public double utilitySFavg(Coalition a, NetworkOfFriends nw){
        //u_i^SFavg = M * value(Player i, Coalition A) + avg(Player i, Friends F, Coalition A)
        int M = 1; //M >= n*n
        double utility = 0;
        utility = M * a.value(this, nw);
        utility += a.avg(this, nw);
        return utility;
    }

    public double utilityETavg(Coalition a, NetworkOfFriends nw){
        //u_i^SFavg = avgPlus(Player i, Friends F, Coalition A)
        return a.avgPlus(this, nw);
        //don't NEED this method but for readability purposes maybe not that bad, can be easily removed
    }

    public double utilityATavg(Coalition a, NetworkOfFriends nw){
        //u_i^SFavg = value(Player i, Coalition A) + M*avg(Player i, Friends F, Coalition A)
        int M = 1; //M >= n*n, Degree of altruism? 
        double utility = 0;
        utility = a.value(this, nw);
        utility += a.avg(this, nw);
        utility *= M;
        return utility;
    }

    public double utilitySFmin(Coalition a, NetworkOfFriends nw){
        //min of values that friends give coalition
        int M = 1;
        double utility = 0;
        utility = M * a.value(this, nw) + a.min(this, nw);
        return utility;
    }

    public double utilityETmin(Coalition a, NetworkOfFriends nw){
        int M = 1;
        double utility = 0;
        utility = a.minPlus(this, nw);
        return utility;
    }

    public double utilityATmin(Coalition a, NetworkOfFriends nw){
        int M = 1;
        double utility = 0;
        utility = a.value(this, nw) + M * a.min(this, nw);
        return utility;
    }
    
    public boolean weaklyPrefers(Coalition a, Coalition b, NetworkOfFriends nw, LOA loa) throws InvalidLevelOfAltruismException {
        if (loa == LOA.SFavg){
            double utilityA = utilitySFavg(a, nw);
            double utilityB = utilitySFavg(b, nw);
            return utilityA >= utilityB;
        }
        else if (loa == LOA.ETavg){
            return utilityETavg(a, nw) >= utilityETavg(a, nw);
        }
        else if (loa == LOA.ATavg){
            double utilityA = utilityATavg(a, nw);
            double utilityB = utilityATavg(b, nw);
            return utilityA >= utilityB;
        }
        else if(loa == LOA.SFmin){
            double utilityA = utilitySFmin(a, nw);
            double utilityB = utilitySFmin(b, nw);
            return utilityA >= utilityB;
        }
        else if(loa == LOA.ETmin){
            return utilityETmin(a, nw) >= utilityETmin(a, nw);
        }
        else if(loa == LOA.ATmin){
            double utilityA = utilityATmin(a, nw);
            double utilityB = utilityATmin(b, nw);
            return utilityA >= utilityB;
        }
        else throw new InvalidLevelOfAltruismException();
    }
}
