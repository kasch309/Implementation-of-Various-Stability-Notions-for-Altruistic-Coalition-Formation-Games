package com.example.altruisticwebapp.Components;

import com.example.altruisticwebapp.Exceptions.*;


import java.util.HashSet;

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

    public void removePlayer(Player p){
        ps.removePlayer(p, nw);
    }
    public Log getLog(){
        return this.log;
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
}
