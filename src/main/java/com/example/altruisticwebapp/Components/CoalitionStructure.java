package com.example.altruisticwebapp.Components;
import com.example.altruisticwebapp.Exceptions.*;


import java.util.HashSet;


public class CoalitionStructure extends HashSet<Coalition> {

    private String name;

    public void addCoalition(String name){
        Coalition c = new Coalition(name);
        this.add( c);
    }

    public boolean containsName(String name){
        for (Coalition c : this){
            if (c.getName().equals(name)) return true;
        }
        return false;
    }

    public void removeCoalition(Coalition c){
        this.remove(c);
    }

    public Coalition getCoalition(String name){
        for (Coalition c : this){
            if (c.getName().equals(name)) return c;
        }
        Coalition d = new Coalition(name);
        this.add(d);
        return d;
    }

    public Coalition getPlayersCoalition(Player p) throws PlayerNotFoundException {
        for (Coalition c : this){
            if (c.contains(p)) return c;
        }
        throw new PlayerNotFoundException(p.getName());
    }

    public HashSet<Coalition> blockingCoalitions(Game g) throws PlayerNotFoundException, NoNetworkAssignedException, NoPlayerSetAssignedException {

        /*
        Coalition C blocks coalition structure T if for each player from C it holds that
        coalition C is preferred to any coalition of which i is part of
        */

        HashSet<Coalition> blockers = new HashSet<>();
        for (Coalition c : this){
            for (int i = 0; i < g.getSize(); i++){
                if (g.getPlayer(i).prefers(c, getPlayersCoalition(g.getPlayer(i)), g.getNetwork())) blockers.add(c);
            }
        }
        return blockers;
    }

    public HashSet<Coalition> weaklyBlockingCoalitions(Game g) throws PlayerNotFoundException, NoNetworkAssignedException, NoPlayerSetAssignedException {
        HashSet<Coalition> weakBlockers = new HashSet<>();

        /*
        Coalition C weakly blocks if there is at least one player who prefers C to any coalition
        that i is part of while another player j weakly prefers C to any coalition of which j
        is part of
        */

        for (Coalition c : this){
            for (int i = 0; i < g.getSize(); i++){
                if (g.getPlayer(i).prefers(c, getPlayersCoalition(g.getPlayer(i)), g.getNetwork())) {
                    for (int j = 0; j < g.getSize(); j++){
                        if (g.getPlayer(i).prefers(c, getPlayersCoalition(g.getPlayer(j)), g.getNetwork())){
                            weakBlockers.add(c);
                        }
                    }
                }
            }
        }
        return weakBlockers;
    }

    public boolean individuallyRational(Game g) throws PlayerNotFoundException, NoPlayerSetAssignedException, NoNetworkAssignedException {
        for (int i = 0; i < g.getSize(); i++){
            if (!g.getPlayer(i).acceptable(getPlayersCoalition(g.getPlayer(i)), g.getNetwork())) return false;
        }
        return true;
    }

    public boolean nashStable(Game g) throws PlayerNotFoundException, NoPlayerSetAssignedException, NoNetworkAssignedException {

        // For all players i their own coalition is weakly preferred to any other, if it would contain i additionally
        Coalition empty = new Coalition();
        this.add(empty);
        for (int i = 0; i < g.getSize(); i++){
            for (Coalition c : this) {
                Coalition d = c.duplicate();
                d.add(g.getPlayer(i));
                if (!g.getPlayer(i).weaklyPrefers(getPlayersCoalition(g.getPlayer(i)), d, g.getNetwork()))
                    return false;
            }
        }
        return true;
    }

    public boolean individuallyStable(Game g) throws NoNetworkAssignedException, PlayerNotFoundException, NoPlayerSetAssignedException {

        /*
        For all players i they either weakly prefer their own coalition to any other coalition c if the other one
        contains them too or there is a player j who prefers c if it contains i too
        */
        Coalition empty = new Coalition();
        this.add(empty);

        for (int i = 0; i < g.getSize(); i++){
            Player p = g.getPlayer(i);
            for (Coalition c : this){
                if (this.getPlayersCoalition(p).equals(c)) continue;
                Coalition dup = c.duplicate();
                dup.add(p);
                if (!p.weaklyPrefers(c, dup, g.getNetwork())){
                    for(Player q : c){
                        if(!q.prefers(c, dup, g.getNetwork())) return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean contractuallyIndividuallyStable(Game g) throws NoPlayerSetAssignedException, PlayerNotFoundException, NoNetworkAssignedException {

        /*
        For all players i they either weakly prefer their own coalition to any other coalition c if the other one
        contains them too or there is a player j who prefers c if it contains i too OR there is player k, i != k, k
        is in coalition of i, k prefers coalition of i over coalition of i if it would not contain i
        */

        Coalition empty = new Coalition();
        this.add(empty);

        for (int i = 0; i < g.getSize(); i++){
            for (Coalition c : this){
                Coalition d = c.duplicate();
                d.add(g.getPlayer(i));
                if (!g.getPlayer(i).weaklyPrefers(getPlayersCoalition(g.getPlayer(i)), d, g.getNetwork())){
                    for (int j = 0; j < g.getSize(); j++){
                        if (!g.getPlayer(j).prefers(c, d, g.getNetwork())){
                            for (Player p : this.getPlayersCoalition(g.getPlayer(i))){
                                Coalition e = c.duplicate();
                                e.remove(g.getPlayer(i));
                                if (g.getPlayer(j).prefers(c, d, g.getNetwork())) return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean strictlyPopular(Game g, CoalitionStructure cmp) throws NoPlayerSetAssignedException, PlayerNotFoundException, NoNetworkAssignedException {
        int countThis = 0;
        int countCmp = 0;
        for (int i = 0; i < g.getSize(); i++){
            if (g.getPlayer(i).prefers(this.getPlayersCoalition(g.getPlayer(i)), cmp.getPlayersCoalition(g.getPlayer(i)), g.getNetwork())) countThis++;
            else countCmp++;
        }
        return countThis > countCmp;
    }

    public boolean popular(Game g, CoalitionStructure cmp) throws NoPlayerSetAssignedException, PlayerNotFoundException, NoNetworkAssignedException {
        int countThis = 0;
        int countCmp = 0;
        for (int i = 0; i < g.getSize(); i++){
            if (g.getPlayer(i).prefers(this.getPlayersCoalition(g.getPlayer(i)), cmp.getPlayersCoalition(g.getPlayer(i)), g.getNetwork())) countThis++;
            else countCmp++;
        }
        return countThis >= countCmp;
    }

    public boolean coreStable(Game g) throws NoNetworkAssignedException, PlayerNotFoundException, NoPlayerSetAssignedException {
        return blockingCoalitions(g).isEmpty();
    }

    public boolean strictlyCoreStable(Game g) throws NoNetworkAssignedException, PlayerNotFoundException, NoPlayerSetAssignedException {
        return weaklyBlockingCoalitions(g).isEmpty();
    }

    public boolean perfect(Game g) throws NoPlayerSetAssignedException, PlayerNotFoundException, NoNetworkAssignedException {

        // i prefers own coalition over any other possible one

        for (int i = 0; i < g.getSize(); i++){
            for (Coalition c : this){
                if (!g.getPlayer(i).weaklyPrefers(getPlayersCoalition(g.getPlayer(i)), c, g.getNetwork())) return false;
            }
        }
        return true;
    }




}
