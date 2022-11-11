package com.example.altruisticwebapp.Components;
import com.example.altruisticwebapp.Exceptions.*;


import java.util.HashMap;
import java.util.HashSet;


public class CoalitionStructure extends HashMap<Integer, Coalition> {

    public CoalitionStructure(){
    }

    public void fixNames(){
        for (int i = 0; i < this.size(); i++){
            get(i).setName("Coalition " + i);
        }
    }
    public void addCoalition(Coalition c){
        c.setKey(this.size());
        c.setName("Coalition " + c.getKey());
        this.put(this.size(), c);
        for (int i = 0; i<this.size(); i++){
            System.out.println(this.get(i).getName());
        }
        fixNames();

    }

    public CoalitionStructure duplicate(CoalitionStructure cs){
        CoalitionStructure dos = new CoalitionStructure();
        for (int i = 0; i < cs.size(); i++){
            dos.addCoalition(new Coalition());
            for (int j = 0; j < cs.get(i).size(); j++){
                dos.get(i).add(cs.get(j));
            }
        }
        return dos;
    }

    public Coalition getCoalition(int key){
        return this.get(key);
    }

    public void removeCoalition(Integer key){
        for (Player p : this.get(key)){
            if (key == 0){
                this.get(1).add(p);
            }
            else{
                this.get(0).add(p);
            }
        }
        this.remove(key);
        for(int i = key+1; i < this.size()+1; i++){
            Coalition d = this.get(i);
            remove(i);
            d.setKey(i-1);
            put(i-1, d);
        }
        fixNames();
    }

    public Coalition getPlayersCoalition(Player p) {
        for (int i = 0; i < this.size(); i++) {
            Coalition c = this.get(i);
            if (c.contains(p)) return c;
        }
        return null;
    }

    public HashSet<Coalition> blockingCoalitions(Game g, LOA loa) throws Exception {
        /*
        Coalition C blocks coalition structure T if for each player from C it holds that
        coalition C is preferred to any coalition of which i is part of
        */

        HashSet<Coalition> blockers = new HashSet<>();
        HashSet<CoalitionStructure> all = g.getPlayers().generateCoalitionStructures();
        for (CoalitionStructure cs : all){
            for (int j = 0; j < cs.size(); j++){
                boolean blocks = true;
                for (int i = 0; i < g.getSize(); i++){
                    if (cs.get(j).contains(g.getPlayer(i))){
                        if (!g.getPlayer(i).prefers(cs.get(j), this.getPlayersCoalition(g.getPlayer(i)), g.getNetwork(), loa)) {
                            blocks = false;
                            break;
                        }
                    }
                }
                if (blocks) blockers.add(cs.get(j));
            }
        }
        return blockers;
    }

    public HashSet<Coalition> weaklyBlockingCoalitions(Game g, LOA loa) throws Exception {
        /*
        Coalition C weakly blocks if there is at least one player who prefers C to any coalition
        that i is part of while another player j weakly prefers C to any coalition of which j
        is part of
        */

        HashSet<Coalition> weakBlockers = new HashSet<>();
        HashSet<CoalitionStructure> all = g.getPlayers().generateCoalitionStructures();
        for (CoalitionStructure cs : all){
            for (int j = 0; j < cs.size(); j++){
                boolean blocks = false;
                for (int i = 0; i < g.getSize(); i++){
                    if (cs.get(j).contains(g.getPlayer(i))){
                        if (g.getPlayer(i).prefers(cs.get(j), cs.getPlayersCoalition(g.getPlayer(i)), g.getNetwork(), loa)) {
                            blocks = true;
                            for (int k = 0; k < g.getSize(); k++){
                                if (!g.getPlayer(k).weaklyPrefers(cs.get(j), this.getPlayersCoalition(g.getPlayer(k)), g.getNetwork(), loa)){
                                    blocks = false;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (blocks) weakBlockers.add(cs.get(j));
            }
        }
        return weakBlockers;
    }

    public boolean individuallyRational(Game g, LOA loa) throws NoPlayerSetAssignedException, NoNetworkAssignedException, InvalidLevelOfAltruismException, CoalitionIsNullException {
        for (int i = 0; i < g.getSize(); i++){
            if (!g.getPlayer(i).acceptable(getPlayersCoalition(g.getPlayer(i)), g.getNetwork(), loa)) {
                return false;
            }
        }
        return true;
    }

    public boolean nashStable(Game g, LOA loa) throws NoPlayerSetAssignedException, NoNetworkAssignedException, InvalidLevelOfAltruismException, CoalitionIsNullException {

        // For all players i their own coalition is weakly preferred to any other, if it would contain i additionally
        CoalitionStructure test = duplicate(this);
        Coalition empty = new Coalition();
        test.addCoalition(empty);
        for (int piterator = 0; piterator < g.getSize(); piterator++){ //Player iterator i
            for (int citerator = 0; citerator < test.size(); citerator++){ //Coalition iterator j
                test.get(citerator).add(g.getPlayer(piterator)); //Add player to coalition
                if (!g.getPlayer(piterator).weaklyPrefers(this.getPlayersCoalition(g.getPlayer(piterator)), test.get(citerator), g.getNetwork(), loa)) return false;
                //Check if each player prefers the coalition he is on over each other one if it would contiain him.
            }
        }
        return true;
    }

    public boolean individuallyStable(Game g, LOA loa) throws NoNetworkAssignedException, PlayerNotFoundException, NoPlayerSetAssignedException, InvalidLevelOfAltruismException, CoalitionIsNullException {

        /*
        For all players i they either weakly prefer their own coalition to any other coalition c if the other one
        contains them too or there is a player j who prefers c if it contains i too
        */

        CoalitionStructure test = duplicate(this);
        Coalition empty = new Coalition();
        test.addCoalition(empty);
        for (int piterator = 0; piterator < g.getSize(); piterator++) { //Player iterator
            for (int citerator = 0; citerator < test.size(); citerator++) { //Coalition iterator
                Coalition comp = test.get(citerator);
                test.get(citerator).add(g.getPlayer(piterator)); //Add player to coalition
                if (this.getPlayersCoalition(g.getPlayer(piterator)).equals(test.get(citerator)))continue;
                if (!g.getPlayer(piterator).weaklyPrefers(this.getPlayersCoalition(g.getPlayer(piterator)), test.get(citerator), g.getNetwork(), loa)) {
                    //If player does not prefer his own coalition C over the other one plus him, then another player must prefer C over
                    boolean thereis = false;
                    for (Player p : comp) {
                        if (thereis) break;
                        if (p.prefers(comp, test.get(citerator), g.getNetwork(), loa)) thereis = true;
                    }
                    if (!thereis) return false;
                }
            }
        }
        return true;
    }

    public boolean contractuallyIndividuallyStable(Game g, LOA loa) throws NoPlayerSetAssignedException, PlayerNotFoundException, NoNetworkAssignedException, InvalidLevelOfAltruismException, CoalitionIsNullException {

        /*
        For all players i they either weakly prefer their own coalition to any other coalition c if the other one
        contains them too or there is a player j who prefers c if it contains i too OR there is player k, i != k, k
        is in coalition of i, k prefers coalition of i over coalition of i if it would not contain i
        */
        CoalitionStructure test = duplicate(this);
        Coalition empty = new Coalition();
        test.addCoalition(empty);
        for (int piterator = 0; piterator < g.getSize(); piterator++) { //Player iterator
            for (int citerator = 0; citerator < test.size(); citerator++) { //Coalition iterator
                Coalition comp = test.get(citerator);
                test.get(citerator).add(g.getPlayer(piterator)); //Add player to coalition
                if (!g.getPlayer(piterator).prefers(this.getPlayersCoalition(g.getPlayer(piterator)), test.get(citerator), g.getNetwork(), loa)) {
                    if (this.getPlayersCoalition(g.getPlayer(piterator)).equals(test.get(citerator)))continue;
                    //If player does not prefer his own coalition C over the other one plus him, then another player must prefer C over
                    boolean thereis = false;
                    for (Player p : comp) {
                        if (thereis) break;
                        if (p.prefers(comp, test.get(citerator), g.getNetwork(), loa)) thereis = true;
                    }
                    if (!thereis){
                        Coalition withoutPlayer = this.getPlayersCoalition(g.getPlayer(piterator)).duplicate();
                        withoutPlayer.remove(g.getPlayer(piterator));
                        if (withoutPlayer.contains(g.getPlayer(piterator))) g.log("Player still inside.");
                        for (Player p : this.getPlayersCoalition(g.getPlayer(piterator))){
                            if (thereis) break;
                            if (p.equals(g.getPlayer(piterator))) continue;
                            if (p.prefers(this.getPlayersCoalition(g.getPlayer(piterator)), withoutPlayer, g.getNetwork(), loa))
                                thereis = true;
                        }
                        if (!thereis) {
                            g.log("No player k found");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean strictlyPopular(Game g, LOA loa) throws Exception {
        int countThis = 0;
        int countCmp = 0;
        HashSet<CoalitionStructure> all = g.getPlayers().generateCoalitionStructures();
        for (CoalitionStructure cs : all){
            if (!cs.equals(this)){
                for (int piterator = 0; piterator < g.getSize(); piterator++){
                    if (g.getPlayer(piterator).prefers(this.getPlayersCoalition(g.getPlayer(piterator)), cs.getPlayersCoalition(g.getPlayer(piterator)), g.getNetwork(), loa)) countThis++;
                    if (g.getPlayer(piterator).prefers(cs.getPlayersCoalition(g.getPlayer(piterator)), this.getPlayersCoalition(g.getPlayer(piterator)), g.getNetwork(), loa)) countCmp++;

                }
                if (!(countThis > countCmp)) return false;
            }
        }
        return true;
    }

    public boolean popular(Game g, LOA loa) throws Exception {
        int countThis = 0;
        int countCmp = 0;
        HashSet<CoalitionStructure> all = g.getPlayers().generateCoalitionStructures();
        for (CoalitionStructure cs : all){
            if (!cs.equals(this)){
                for (int piterator = 0; piterator < g.getSize(); piterator++){
                    if (g.getPlayer(piterator).prefers(this.getPlayersCoalition(g.getPlayer(piterator)), cs.getPlayersCoalition(g.getPlayer(piterator)), g.getNetwork(), loa)) countThis++;
                    if (g.getPlayer(piterator).prefers(cs.getPlayersCoalition(g.getPlayer(piterator)), this.getPlayersCoalition(g.getPlayer(piterator)), g.getNetwork(), loa)) countCmp++;

                }
                if (!(countThis >= countCmp)) return false;
            }
        }
        return true;
    }

    public boolean coreStable(Game g, LOA loa) throws Exception {
        HashSet<Coalition> blockers = blockingCoalitions(g, loa);

        if (blockers.isEmpty()) return true;
        else{
            String str = "";
            for (Coalition b : blockers){
                for (Player p : b){
                    str = str.concat(p.getName());
                    str = str.concat(", ");
                }

                g.addEntry("The coalition containing " + str + " blocks.");
                break;
            }
            return false;
        }
    }

    public boolean strictlyCoreStable(Game g, LOA loa) throws Exception {
        HashSet<Coalition> weakBlockers = weaklyBlockingCoalitions(g, loa);
        if(weakBlockers.isEmpty()){
            return true;
        }
        else {
            String str = "";
            for (Coalition b : weakBlockers){
                for (Player p : b){
                    str = str.concat(p.getName());
                    str = str.concat(", ");
                }

                g.addEntry("The coalition containing " + str + " blocks weakly.");
                break;
            }
            return false;
        }
    }

    public boolean perfect(Game g, LOA loa) throws Exception {

        // i prefers own coalition over any other possible one
        HashSet<CoalitionStructure> all = g.getPlayers().generateCoalitionStructures();

        for (CoalitionStructure cs : all){
            for (int i = 0; i < g.getSize(); i++){
                for (int j = 0; j < cs.size(); j++){
                    Coalition c = cs.get(j).duplicate();
                    if (getPlayersCoalition(g.getPlayer(j)) == null) throw new CoalitionIsNullException("Player has no coalition in cs.");
                    if (c == null) throw new CoalitionIsNullException("Coalition c is null.");
                    if (!g.getPlayer(j).weaklyPrefers(getPlayersCoalition(g.getPlayer(j)), c, g.getNetwork(), loa)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
