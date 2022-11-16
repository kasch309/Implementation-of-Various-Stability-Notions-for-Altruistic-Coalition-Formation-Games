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
        fixNames();

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
            for (int citerator = 0; citerator < cs.size(); citerator++){
                boolean blocks = true;
                for (Player p : cs.get(citerator)){
                    if (!p.prefers(cs.get(citerator), this.getPlayersCoalition(p), g.getNetwork(), loa)) {
                        blocks = false;
                        break;
                    }
                }
                if (blocks) blockers.add(cs.get(citerator));
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
            for (int citerator = 0; citerator < cs.size(); citerator++){
                boolean pref = false;
                for (Player p : cs.get(citerator)){
                    if (p.weaklyPrefers(cs.get(citerator), this.getPlayersCoalition(p), g.getNetwork(), loa)){
                        if (p.prefers(cs.get(citerator), this.getPlayersCoalition(p), g.getNetwork(), loa)) pref = true;
                    }
                }
                if (pref) weakBlockers.add(cs.get(citerator));
            }
        }
        return weakBlockers;
    }

    public boolean individuallyRational(Game g, LOA loa, boolean infos) throws NoPlayerSetAssignedException, NoNetworkAssignedException, InvalidLevelOfAltruismException, CoalitionIsNullException {
        for (int i = 0; i < g.getSize(); i++){
            if (!g.getPlayer(i).acceptable(getPlayersCoalition(g.getPlayer(i)), g.getNetwork(), loa)) {
                if (infos) g.log("Not individually rational: Player '" + g.getPlayer(i).getName() + "' did not find his coalition acceptable.");
                return false;
            }
        }
        if (infos) g.log("The coalition structure is individually rational");
        return true;
    }

    public boolean nashStable(Game g, LOA loa, boolean infos) throws NoPlayerSetAssignedException, NoNetworkAssignedException, InvalidLevelOfAltruismException, CoalitionIsNullException {

        // For all players i their own coalition is weakly preferred to any other, if it would contain i additionally
        Coalition empty = new Coalition();
        for (int piterator = 0; piterator < g.getSize(); piterator++){ //Player iterator
            for (int citerator = 0; citerator < this.size(); citerator++){ //Coalition iterator
                if (!this.get(citerator).equals(this.getPlayersCoalition(g.getPlayer(piterator)))){
                    Coalition c = new Coalition();
                    c.addAll(this.get(citerator));
                    c.add(g.getPlayer(piterator));
                    if (!g.getPlayer(piterator).weaklyPrefers(this.getPlayersCoalition(g.getPlayer(piterator)), c, g.getNetwork(), loa)) {
                        for (int i = 0; i < this.size(); i++){
                            if (this.get(i) == empty) this.remove(i);
                        }
                        if (infos) g.log("Not Nash stable: Player '" + g.getPlayer(piterator).getName() + "' prefers coalition '"+ this.get(citerator).getName() + "' over the one he is in right now.");
                        return false;
                    }
                    //Check if each player prefers the coalition he is on over each other one if it would contiain him.
                }
            }
        }
        for (int i = 0; i < this.size(); i++){
            if (this.get(i) == empty) this.remove(i);
        }
        if (infos) g.log("The coalition structure is Nash stable.");
        return true;
    }

    public boolean individuallyStable(Game g, LOA loa, boolean infos) throws NoNetworkAssignedException, PlayerNotFoundException, NoPlayerSetAssignedException, InvalidLevelOfAltruismException, CoalitionIsNullException {

        /*
        For all players i they either weakly prefer their own coalition to any other coalition c if the other one
        contains them too or there is a player j who prefers c if it contains i too
        */

        Coalition empty = new Coalition();
        this.addCoalition(empty);
        for (int piterator = 0; piterator < g.getSize(); piterator++) { //Player iterator
            for (int citerator = 0; citerator < this.size(); citerator++) {//Coalition iterator
                if (!this.getPlayersCoalition(g.getPlayer(piterator)).equals(this.get(citerator))){
                    Coalition c = new Coalition();
                    c.addAll(this.get(citerator));
                    c.add(g.getPlayer(piterator));
                    if (!g.getPlayer(piterator).weaklyPrefers(this.getPlayersCoalition(g.getPlayer(piterator)), c, g.getNetwork(), loa)) {
                        //If player does not prefer his own coalition C over the other one plus him, then another player must prefer C over
                        boolean thereis = false;
                        for (Player p : c) {
                            if (thereis) break;
                            if (p.prefers(c, this.get(citerator), g.getNetwork(), loa)) thereis = true;
                        }
                        if (!thereis) {
                            for (int i = 0; i < this.size(); i++){
                                if (this.get(i) == empty) this.remove(i);
                            }
                            if (infos) g.log("Not individually stable: There is a player who prefers another coalition over the one he is in, but he would harm other players in his preferred coalition.");
                            return false;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < this.size(); i++){
            if (this.get(i) == empty) this.remove(i);
        }
        if (infos) g.log("The coalition structure is individually stable.");
        return true;
    }

    public boolean contractuallyIndividuallyStable(Game g, LOA loa, boolean infos) throws NoPlayerSetAssignedException, PlayerNotFoundException, NoNetworkAssignedException, InvalidLevelOfAltruismException, CoalitionIsNullException {

        /*
        For all players i they either weakly prefer their own coalition to any other coalition c if the other one
        contains them too or there is a player j who prefers c if it contains i too OR there is player k, i != k, k
        is in coalition of i, k prefers coalition of i over coalition of i if it would not contain i
        */
        Coalition empty = new Coalition();
        this.addCoalition(empty);
        for (int piterator = 0; piterator < g.getSize(); piterator++) { //Player iterator
            for (int citerator = 0; citerator < this.size(); citerator++) { //Coalition iterator
                Coalition comp = new Coalition(); //TODO FIXEN HIER
                comp.addAll(this.get(citerator));
                comp.add(g.getPlayer(piterator));
                if (!g.getPlayer(piterator).prefers(this.getPlayersCoalition(g.getPlayer(piterator)), comp, g.getNetwork(), loa)) {
                    if (this.getPlayersCoalition(g.getPlayer(piterator)).equals(comp)) continue;
                    //If player does not prefer his own coalition C over the other one plus him, then another player must prefer C over
                    boolean thereis = false;
                    for (Player p : comp) {
                        if (thereis) break;
                        if (p.prefers(comp, this.get(citerator), g.getNetwork(), loa)) thereis = true;
                    }
                    if (!thereis){
                        Coalition withoutPlayer = new Coalition();
                        withoutPlayer.addAll(this.getPlayersCoalition(g.getPlayer(piterator)));
                        withoutPlayer.remove(g.getPlayer(piterator));
                        for (Player p : this.getPlayersCoalition(g.getPlayer(piterator))){
                            if (thereis) break;
                            if (p.equals(g.getPlayer(piterator))) continue;
                            if (p.prefers(this.getPlayersCoalition(g.getPlayer(piterator)), withoutPlayer, g.getNetwork(), loa))
                                thereis = true;
                        }
                        if (!thereis) {
                            if (infos) g.log("Not contractually individually stable: There is a player who prefers another coalition over the one he is in, but he would harm other players in his preferred or original coalition.");
                            for (int i = 0; i < this.size(); i++){
                                if (this.get(i) == empty) this.remove(i);
                            }
                            return false;
                        }

                    }
                }
            }
        }
        for (int i = 0; i < this.size(); i++){
            if (this.get(i) == empty) this.remove(i);
        }
        if (infos) g.log("The coalition structure is contractually individually stable.");
        return true;
    }

    public boolean strictlyPopular(Game g, LOA loa, boolean infos) throws Exception {
        int countThis = 0;
        int countCmp = 0;
        HashSet<CoalitionStructure> all = g.getPlayers().generateCoalitionStructures();
        for (CoalitionStructure cs : all){
            if (!cs.equals(this)){
                for (int piterator = 0; piterator < g.getSize(); piterator++){
                    if (g.getPlayer(piterator).prefers(this.getPlayersCoalition(g.getPlayer(piterator)), cs.getPlayersCoalition(g.getPlayer(piterator)), g.getNetwork(), loa)) countThis++;
                    if (g.getPlayer(piterator).prefers(cs.getPlayersCoalition(g.getPlayer(piterator)), this.getPlayersCoalition(g.getPlayer(piterator)), g.getNetwork(), loa)) countCmp++;

                }
                if (!(countThis > countCmp)) {
                    if (infos) g.log("Not strictly popular: There is another coalition structure in which more or equally many players prefer the coalition they are in than in the present coalition structure. ");
                    return false;
                }
            }
        }
        if (infos) g.log("The coalition structure is strictly popular.");
        return true;
    }

    public boolean popular(Game g, LOA loa, boolean infos) throws Exception {
        int countThis = 0;
        int countCmp = 0;
        HashSet<CoalitionStructure> all = g.getPlayers().generateCoalitionStructures();
        for (CoalitionStructure cs : all){
            if (!cs.equals(this)){
                for (int piterator = 0; piterator < g.getSize(); piterator++){
                    if (g.getPlayer(piterator).prefers(this.getPlayersCoalition(g.getPlayer(piterator)), cs.getPlayersCoalition(g.getPlayer(piterator)), g.getNetwork(), loa)) countThis++;
                    if (g.getPlayer(piterator).prefers(cs.getPlayersCoalition(g.getPlayer(piterator)), this.getPlayersCoalition(g.getPlayer(piterator)), g.getNetwork(), loa)) countCmp++;

                }
                if (!(countThis >= countCmp)) {
                    if (infos) g.log("Not popular: There is another coalition structure in which more players prefer the coalition they are in than in the present coalition structure. ");
                    return false;
                }
            }
        }
        if (infos) g.log("The coalition structure is popular.");
        return true;
    }

    public boolean coreStable(Game g, LOA loa, boolean infos) throws Exception {
        HashSet<Coalition> blockers = blockingCoalitions(g, loa);

        if (blockers.isEmpty()) {
            if (infos) g.log("The coalition structure is core stable.");
            return true;
        }
        else{
            if (infos) {
                for (Coalition b : blockers){
                    String str = "";
                    for (Player p : b){
                        str = str.concat(p.getName());
                        str = str.concat(", ");
                    }
                    g.addEntry("Not core stable: There are blocking coalitions. For example, the coalition containing " + str + " blocks.");
                    break;
                }
            }
            return false;
        }
    }

    public boolean strictlyCoreStable(Game g, LOA loa, boolean infos) throws Exception {
        HashSet<Coalition> weakBlockers = weaklyBlockingCoalitions(g, loa);
        if(weakBlockers.isEmpty()){
            if (infos) g.log("The coalition structure is strictly core stable.");
            return true;
        }
        else {
            if (infos) {
                for (Coalition b : weakBlockers){
                    String str = "";
                    for (Player p : b){
                        str = str.concat(p.getName());
                        str = str.concat(", ");
                    }
                    g.addEntry("Not strictly core stable: There are weakly blocking coalitions. For example, the coalition containing " + str + " blocks weakly.");
                    break;
                }
            }
            return false;
        }
    }

    public boolean perfect(Game g, LOA loa, boolean infos) throws Exception {

        // i prefers own coalition over any other possible one
        HashSet<CoalitionStructure> all = g.getPlayers().generateCoalitionStructures();

        for (int piterator = 0; piterator < g.getSize(); piterator++){
            for (CoalitionStructure cs : all){
                for (int citerator = 0; citerator < cs.size(); citerator++){
                    if (cs.get(citerator).contains(g.getPlayer(piterator))){
                        if (!g.getPlayer(piterator).weaklyPrefers(getPlayersCoalition(g.getPlayer(piterator)), cs.get(citerator), g.getNetwork(), loa)) {
                            String playersPreferred = "";
                            for (Player p : cs.get(citerator)){
                                playersPreferred = playersPreferred.concat(p.getName() + ", ");
                            }
                            if (infos) g.log("Not perfect: Player '" + g.getPlayer(piterator).getName() + "' prefers the coalition containing " + playersPreferred + "over his own coalition in the present coalition structure.");
                            return false;
                        }
                    }
                }
            }
        }
        if (infos) g.log("The coalition structure is perfect.");
        return true;
    }
}
