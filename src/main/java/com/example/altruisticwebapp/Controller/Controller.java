package com.example.altruisticwebapp.Controller;

import com.example.altruisticwebapp.Components.*;
import com.example.altruisticwebapp.Exceptions.NoNetworkAssignedException;
import com.example.altruisticwebapp.Exceptions.NoPlayerSetAssignedException;
import com.example.altruisticwebapp.Exceptions.PlayerNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.stereotype.Controller
public class Controller {

    Game g;
    CoalitionStructure cs;
    String re = "redirect:";

    public Controller() {
        cs = new CoalitionStructure();
        g = new Game(5);
    }

    @GetMapping("/")
    public String home(Model model) throws NoPlayerSetAssignedException, NoNetworkAssignedException {
        model.addAttribute("player_set", g.getPlayers());
        model.addAttribute("coalition_structure", cs);
        model.addAttribute("friendMatrix", g.getNetwork());
        return "hello";
    }


    @PostMapping("/addPlayer")
    public String addPlayer(@RequestParam("name") String name)
            throws NoPlayerSetAssignedException, NoNetworkAssignedException {
        if (name.equals("")) {
            name = "Player " + (g.getSize() + 1);
        }

        Player p = new Player(name);
        if (!cs.isEmpty()) cs.get(0).add(p);
        g.addPlayer(p);

        return re;
    }

    @PostMapping("/addCoalition")
    public String addCoalition(@RequestParam("coalitionInput") String coalitionId) throws NoPlayerSetAssignedException {
        if (cs.isEmpty()) {
            Coalition init = new Coalition(coalitionId);
            init.addPlayerSet(g.getPlayers());
            cs.addCoalition(init);
            return re;
        }
        cs.addCoalition(new Coalition(coalitionId));
        return re;
    }

    @GetMapping("/resetGame")
    public String resetGame() {
        g = new Game(0);
        cs = new CoalitionStructure();
        return re;
    }

    @PostMapping("/removePlayerFromGame")
    public String removePlayerFromGame(@RequestParam("playerRemove") int key) throws NoPlayerSetAssignedException {
        g.removePlayer(g.getPlayer(key));
        return re;
    }

    @PostMapping("/addPlayerToCoalition")
    public String addPlayerToCoalition(@RequestParam("player") int key, @RequestParam("coalitionId") int coal)
            throws NoPlayerSetAssignedException, PlayerNotFoundException {
        cs.getPlayersCoalition(g.getPlayer(key)).remove(g.getPlayer(key));
        cs.getCoalition(coal).add(g.getPlayer(key));
        return re;
    }

    @PostMapping("/renamePlayer")
    public String renamePlayer(@RequestParam("renamePlayer") int key, @RequestParam("newPlayerName") String name) throws NoPlayerSetAssignedException {
        g.renamePlayer(key, name);
        return re;
    }

    @PostMapping("/renameCoalition")
    public String renameCoalition(@RequestParam("renameCoalition") int key, @RequestParam("newCoalitionName") String name){
        cs.renameCoalition(cs.getCoalition(key), name);
        return re;
    }
    @PostMapping("/removeCoalitionFromCoalitionStructure")
    public String removeCoalitionFromCoalitionStructure(@RequestParam("coalitionRemove") int key) {
        System.out.println("Arg: " + key);
        System.out.println("Name: " + cs.get(key).getName() + "; Key: " + cs.get(key).getKey());
        cs.removeCoalition(key);
        cs.printCoalitions();
        return re;
    }

    @PostMapping("/addFriendship")
    public String addFriendship(@RequestParam("addKey1") int key1, @RequestParam("addKey") int key2) throws NoNetworkAssignedException {
        if (g.getNetwork().areFriends(key1, key2)) g.getNetwork().removeFriendship(key1, key2);
        else g.getNetwork().addFriendship(key1, key2);
        return re;
    }
}