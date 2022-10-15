package com.example.altruisticwebapp.Components;

import com.example.altruisticwebapp.Exceptions.DuplicatePlayerNameException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class PlayerSet extends HashMap<Integer, Player> {//generell was geht hier ab

    public PlayerSet (){
    }
    public PlayerSet(int size){
        for (int i = 0; i < size; i++){
            this.put(i, new Player(i));
        }
    }

    public void printPlayers(){
        for (int i = 0; i < size(); i++){
            System.out.println(get(i).getName());
        }
    }
    public void add(Player p, NetworkOfFriends nw) {
        try{
            if (this.containsPlayerWithName(p.getName())) throw new DuplicatePlayerNameException();
            p.setKey(this.size());
            this.put(this.size(), p);
            nw.addPlayer();
        }catch (DuplicatePlayerNameException e){
            //Todo: Output Error Message
        }
    }
    public boolean containsPlayerWithName(String name){
        for (int i = 0; i < this.size(); i++){
            System.out.println(get(i).getName() + "; i: " + i);
            get(i);
            if (get(i).getName().equals(name)) return true;
        }
        return false;
    }

    public HashSet<CoalitionStructure> generateCoalitionStructures(){
        HashSet<CoalitionStructure> coalStruc = new HashSet<>();
        int n = this.size();
        int r = 1;
        int [] c = new int [n];
        int j = 0;
        int [] b = new int [n];
        for (int i = 0; i < n; i++){
            c[i] = 1;
            b[i] = 1;
        }
        int n1 = n-1;

        do{
            while (r < n1){
                r++;
                c[r-1] = 1;
                j++;
                b[j] = r;
            }
            for(int m = 1; m <= n-j; m++){
                c[n-1] = m;

                CoalitionStructure cs = new CoalitionStructure();
                splitPlayers(c, cs);
                coalStruc.add(cs);
            }
            r = b[j];
            c[r-1]++;
            if (c[r-1] > r-j) j--;
        }while (r != 1);

        return coalStruc;
    }

    public void splitPlayers(int [] pos, CoalitionStructure cs){
        int largestVal = 0;
        for (int po : pos) {
            if (po > largestVal) largestVal = po;
        }
        Coalition [] c = new Coalition[largestVal];
        for (int i = 0; i < c.length; i++){
            c[i] = new Coalition();
        }
        for (int i = 0; i < pos.length; i++){
            c[pos[i]-1].add(get(i));
        }
        cs.addAll(Arrays.asList(c));
    }

    public void removePlayer(Player p, NetworkOfFriends nw){
        int key = 0;
        for (int i = 0; i < this.size(); i++){
            if (get(i).equals(p)) {
                key = i;
                break;
            }
        }
        nw.removePlayer(key);
        for (Integer k : keySet()){
            System.out.println(k);
        }
        remove(key);
        for(int i = key+1; i < this.size()+1; i++){
            Player player = get(i);
            remove(i);
            player.setKey(i-1);
            put(i-1, player);
        }
    }
}
