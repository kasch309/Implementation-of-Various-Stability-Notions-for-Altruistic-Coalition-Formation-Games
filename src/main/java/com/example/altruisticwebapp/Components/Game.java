package com.example.altruisticwebapp.Components;

import com.example.altruisticwebapp.Exceptions.*;

public class Game {
    private NetworkOfFriends nw;
    private PlayerSet ps;
    private Log log = new Log();

    public Game (NetworkOfFriends nw) throws NoNetworkAssignedException {
        this.nw = nw;
        initFromNetwork();
    }
    public Game (PlayerSet ps, NetworkOfFriends nw){
        this.ps = ps;
        this.nw = nw;
    }

    public Game (int size){
        this.ps = new PlayerSet(size);
        this.nw = new NetworkOfFriends(ps);
    }

    public void removePlayer(Player p, CoalitionStructure cs){
        ps.removePlayer(p, nw);
        for (int i = 0; i < cs.size(); i++){
            cs.get(i).remove(p);
        }
    }
    public Log getLog(){
        return log.output();
    }
    public void clearLog(){
        this.log = new Log();
    }
    public void addEntry(String str){
        this.log.addEntry(str);
    }

    public void renamePlayer(Integer i, String name) throws NoPlayerSetAssignedException {
        this.getPlayer(i).setName(name);
    }


    public NetworkOfFriends getNetwork() throws NoNetworkAssignedException {
        if (this.nw == null) throw new NoNetworkAssignedException();
        return nw;
    }
    public void setNetwork(NetworkOfFriends nw){
        this.nw = nw;
    }

    public PlayerSet getPlayers() throws NoPlayerSetAssignedException {
        if (this.ps == null) throw new NoPlayerSetAssignedException();
        return ps;
    }
    public void addPlayer(Player p) throws NoNetworkAssignedException {
        if (this.nw == null) throw new NoNetworkAssignedException();
        if (this.ps == null){
            this.ps = new PlayerSet();
        }
        ps.add(p, this.nw);
    }

    public void addPlayer(String name) throws NoNetworkAssignedException {
        if (this.nw == null) throw new NoNetworkAssignedException();
        if (this.ps == null){
            this.ps = new PlayerSet();
        }
        Player p = new Player(name);
        ps.add(p, this.nw);
    }

    public void initFromNetwork() throws NoNetworkAssignedException {
        this.ps = new PlayerSet(nw.getSize());
    }

    public int getSize() throws NoPlayerSetAssignedException {
        return getPlayers().size();
    }

    public Player getPlayer(int i) throws NoPlayerSetAssignedException {
        return getPlayers().get(i);
    }

    public CoalitionStructure singletons() throws NoPlayerSetAssignedException {
        CoalitionStructure singletons = new CoalitionStructure();
        for (int i = 0; i < getSize(); i++){
            Coalition c = new Coalition();
            c.add(ps.get(i));
            singletons.addCoalition(c);
        }
        return singletons;
    }

    public CoalitionStructure allInOne() throws NoPlayerSetAssignedException {
        CoalitionStructure allInOne = new CoalitionStructure();
        Coalition c = new Coalition();
        for (int i = 0; i < getSize(); i++){
            c.add(ps.get(i));
        }
        allInOne.addCoalition(c);
        return allInOne;
    }

    public void log (String str){
        log.addEntry(str);
    }
    public void log(logStr str){
        switch (str) {
            case IR -> {
                log.addEntry("This coalition structure is individually rational.");
            }
            case notIR -> {
                log.addEntry("This coalition structure is not individually rational.");
            }
            case Nash -> {
                log.addEntry("This coalition structure is Nash stable.");
            }
            case notNash -> {
                log.addEntry("This coalition structure is not Nash stable.");
            }
            case IS -> {
                log.addEntry("This coalition structure is individually stable.");
            }
            case notIS -> {
                log.addEntry("This coalition structure is not individually stable.");
            }
            case CIS -> {
                log.addEntry("This coalition structure is contractually individually stable.");
            }
            case notCIS -> {
                log.addEntry("This coalition structure is not contractually individually stable.");
            }
            case CS -> {
                log.addEntry("This coalition structure is core stable.");
            }
            case notCS -> {
                log.addEntry("This coalition structure is not core stable.");
            }
            case SCS -> {
                log.addEntry("This coalition structure is strictly stable.");
            }
            case notSCS -> {
                log.addEntry("This coalition structure is not strictly core stable.");
            }
            case P -> {
                log.addEntry("This coalition structure is popular");
            }
            case notP -> {
                log.addEntry("This coalition structure is not popular.");
            }
            case SP -> {
                log.addEntry("This coalition structure is strictly popular.");
            }
            case notSP -> {
                log.addEntry("This coalition structure is not strictly popular.");
            }
            case Perf -> {
                log.addEntry("This coalition structure is perfect.");
            }
            case notPerf -> {
                log.addEntry("This coalition structure is not perfect.");
            }
        }
    }
}
