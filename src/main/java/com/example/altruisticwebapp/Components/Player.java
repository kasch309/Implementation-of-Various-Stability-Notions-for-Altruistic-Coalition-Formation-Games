package com.example.altruisticwebapp.Components;

import com.example.altruisticwebapp.Exceptions.CoalitionIsNullException;
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

    public boolean acceptable(Coalition c, NetworkOfFriends nw, LOA loa) throws InvalidLevelOfAltruismException, CoalitionIsNullException {
        Coalition onlyPlayer = new Coalition();
        onlyPlayer.add(this);
        return this.weaklyPrefers(c, onlyPlayer, nw, loa);
    }
    public boolean areFriends(Player p, NetworkOfFriends nw){
        return (nw.getMatrix()[this.getKey()][p.getKey()] == 1);
    }

    public boolean areEnemies(Player p, NetworkOfFriends nw){
        return (nw.getMatrix()[this.key][p.getKey()] == 0);
    }

    public double utilitySFavg(Coalition a, NetworkOfFriends nw) throws CoalitionIsNullException {
        //u_i^SFavg = M * value(Player i, Coalition A) + avg(Player i, Friends F, Coalition A)
        if (a == null) throw new CoalitionIsNullException("Player " + this.name);
        int M = (int) Math.pow(nw.getSize(), 2); //M >= n*n
        return M * a.value(this, nw) +a.avg(this, nw);
    }

    public double utilityETavg(Coalition a, NetworkOfFriends nw) throws CoalitionIsNullException {
        //u_i^SFavg = avgPlus(Player i, Friends F, Coalition A)
        if (a == null) throw new CoalitionIsNullException("Player " + this.name);
        return a.avgPlus(this, nw);
        //don't NEED this method but for readability purposes maybe not that bad, can be easily removed
    }

    public double utilityATavg(Coalition a, NetworkOfFriends nw) throws CoalitionIsNullException {
        //u_i^SFavg = value(Player i, Coalition A) + M*avg(Player i, Friends F, Coalition A)
        if (a == null) throw new CoalitionIsNullException("Player " + this.name);

        int M = (int) Math.pow(nw.getSize(), 2); //M >= n*n, Degree of altruism?
        double utility = 0;
        utility = a.value(this, nw)+a.avg(this, nw)*M;
        return utility;
    }

    public double utilitySFmin(Coalition a, NetworkOfFriends nw) throws CoalitionIsNullException {
        //min of values that friends give coalition
        if (a == null) throw new CoalitionIsNullException("Player " + this.name);
        int M = (int) Math.pow(nw.getSize(), 3);
        return (M * a.value(this, nw) + a.min(this, nw));
    }

    public double utilityETmin(Coalition a, NetworkOfFriends nw) throws CoalitionIsNullException {
        if (a == null) throw new CoalitionIsNullException("Player " + this.name);
        return a.minPlus(this, nw);
    }

    public double utilityATmin(Coalition a, NetworkOfFriends nw) throws CoalitionIsNullException {
        if (a == null) throw new CoalitionIsNullException("Player " + this.name);

        int M = (int) Math.pow(nw.getSize(), 3);
        return a.value(this, nw) + M * a.min(this, nw);
    }
    
    public boolean weaklyPrefers(Coalition a, Coalition b, NetworkOfFriends nw, LOA loa) throws InvalidLevelOfAltruismException, CoalitionIsNullException {
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

    public boolean prefers(Coalition a, Coalition b, NetworkOfFriends nw, LOA loa) throws InvalidLevelOfAltruismException, CoalitionIsNullException {
        if (loa == LOA.SFavg){
            double utilityA = utilitySFavg(a, nw);
            double utilityB = utilitySFavg(b, nw);
            return utilityA > utilityB;
        }
        else if (loa == LOA.ETavg){
            return utilityETavg(a, nw) >= utilityETavg(a, nw);
        }
        else if (loa == LOA.ATavg){
            double utilityA = utilityATavg(a, nw);
            double utilityB = utilityATavg(b, nw);
            return utilityA > utilityB;
        }
        else if(loa == LOA.SFmin){
            double utilityA = utilitySFmin(a, nw);
            double utilityB = utilitySFmin(b, nw);
            return utilityA > utilityB;
        }
        else if(loa == LOA.ETmin){
            return utilityETmin(a, nw) >= utilityETmin(a, nw);
        }
        else if(loa == LOA.ATmin){
            double utilityA = utilityATmin(a, nw);
            double utilityB = utilityATmin(b, nw);
            return utilityA > utilityB;
        }
        else throw new InvalidLevelOfAltruismException();
    }
}
