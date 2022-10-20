package com.example.altruisticwebapp.Controller;

import com.example.altruisticwebapp.Components.*;
import com.example.altruisticwebapp.Exceptions.NoNetworkAssignedException;
import com.example.altruisticwebapp.Exceptions.NoPlayerSetAssignedException;
import com.example.altruisticwebapp.Exceptions.PlayerNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@org.springframework.stereotype.Controller
public class Controller {

    Game g;
    CoalitionStructure cs;
    String re = "redirect:";

    public Controller() {
        cs = new CoalitionStructure();
        g = new Game(0);
    }

    @GetMapping("/")
    public String home(Model model) throws NoPlayerSetAssignedException {
        model.addAttribute("player_set", g.getPlayers());
        model.addAttribute("coalition_structure", cs);
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
    public String removePlayerFromGame(@RequestParam("playerRemove") Player p) {
        g.removePlayer(p);
        return re;
    }

    @PostMapping("/addPlayerToCoalition")
    public String addPlayerToCoalition(@RequestParam("player") int key, @RequestParam("coalitionId") int coal)
            throws NoPlayerSetAssignedException, PlayerNotFoundException {

        System.out.println(g.getPlayer(key));
        System.out.println(coal);
        cs.getPlayersCoalition(g.getPlayer(key)).remove(g.getPlayer(key));
        cs.getCoalition(coal).add(g.getPlayer(key));
        return re;
    }

    @PostMapping("/removeCoalitionFromCoalitionStructure")
    public String removeCoalitionFromCoalitionStructure(@RequestParam("coalitionRemove") Coalition c) {
        cs.remove(c);
        return re;
    }

    @PostMapping("/renamePlayer")
    public String renamePlayer(@RequestParam("renamePlayer") Integer i, @RequestParam("newPlayerName") String name) throws NoPlayerSetAssignedException {
        g.renamePlayer(i, name);
        System.out.println(g.getPlayer(i).getName());
        g.getPlayers().printPlayers();
        return re;
    }
}