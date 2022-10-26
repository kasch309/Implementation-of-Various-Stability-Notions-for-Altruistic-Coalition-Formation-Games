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
    Result res;

    public Controller() {
        cs = new CoalitionStructure();
        g = new Game(5);
    }

    @GetMapping("/")
    public String home(Model model) throws NoPlayerSetAssignedException, NoNetworkAssignedException {
        model.addAttribute("player_set", g.getPlayers());
        model.addAttribute("coalition_structure", cs);
        model.addAttribute("friendMatrix", g.getNetwork());
        return "home";
    }

    @GetMapping("/construction")
    public String construction(Model model) throws NoPlayerSetAssignedException, NoNetworkAssignedException {
        model.addAttribute("player_set", g.getPlayers());
        model.addAttribute("coalition_structure", cs);
        model.addAttribute("friendMatrix", g.getNetwork());
        return "construction";
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
    public String addCoalition() throws NoPlayerSetAssignedException {
        if (cs.isEmpty()) {
            Coalition init = new Coalition("Coalition 0");
            init.addPlayerSet(g.getPlayers());
            cs.addCoalition(init);
            return re;
        }
        cs.addCoalition(new Coalition("Coalition " + cs.size()));
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

    @PostMapping("/removeCoalitionFromCoalitionStructure")
    public String removeCoalitionFromCoalitionStructure(@RequestParam("coalitionRemove") int key) {
        cs.removeCoalition(key);
        return re;
    }

    @PostMapping("/addFriendship")
    public String addFriendship(@RequestParam("addKey1") int key1, @RequestParam("addKey") int key2) throws NoNetworkAssignedException {
        if (g.getNetwork().areFriends(key1, key2)) g.getNetwork().removeFriendship(key1, key2);
        else g.getNetwork().addFriendship(key1, key2);
        return re;
    }

    @GetMapping("/analysis")
    public String analysis(Model model) throws NoPlayerSetAssignedException, NoNetworkAssignedException, PlayerNotFoundException {
        model.addAttribute("player_set", g.getPlayers());
        model.addAttribute("coalition_structure", cs);
        model.addAttribute("friendMatrix", g.getNetwork());
        model.addAttribute("blocking_coalitions", cs.blockingCoalitions(g));
        model.addAttribute("weakly_blocking_coalitions", cs.weaklyBlockingCoalitions(g));
        model.addAttribute("result", res);
        return "analysis";
    }

    @PostMapping("/analysis/check")
    public String calculateStabilityConcepts(@RequestParam("individuallyRational") boolean individuallyRational,
                                             @RequestParam("nashStable") boolean nashStable,
                                             @RequestParam("individuallyStable") boolean individuallyStable,
                                             @RequestParam("contractuallyIndividuallyStable") boolean contractuallyIndividuallyStable,
                                             @RequestParam("strictlyPopular") boolean strictlyPopular,
                                             @RequestParam("popular") boolean popular,
                                             @RequestParam("coreStable") boolean coreStable,
                                             @RequestParam("strictlyCoreStable") boolean strictlyCoreStable,
                                             @RequestParam("perfect") boolean perfect) throws NoNetworkAssignedException, PlayerNotFoundException, NoPlayerSetAssignedException {
        res = new Result();
        if (perfect && !res.perfect){
            if (cs.perfect(g)){
                res.perfect = true;
                res.strictlyPopular = true;
                res.popular = true;
                res.strictlyCoreStable = true;
                res.nashStable = true;
                res.individuallyStable = true;
                res.contractuallyIndividuallyStable = true;
                res.individuallyRational = true;
                res.coreStable = true;
            }
        }
        if (strictlyPopular && !res.strictlyPopular){
            if (cs.strictlyPopular(g)){
                res.strictlyPopular = true;
                res.popular = true;
                res.contractuallyIndividuallyStable = true;
            }
        }
        if (popular && !res.popular){
            if (cs.popular(g)){
                res.popular = true;
                res.contractuallyIndividuallyStable = true;
            }
        }
        if (strictlyCoreStable && !res.strictlyCoreStable){
            if (cs.strictlyCoreStable(g)){
                res.strictlyCoreStable = true;
                res.contractuallyIndividuallyStable = true;
                res.individuallyStable = true;
                res.individuallyRational = true;
                res.coreStable = true;
            }
        }
        if (nashStable && !res.nashStable){
            if (cs.nashStable(g)){
                res.nashStable = true;
                res.individuallyStable = true;
                res.contractuallyIndividuallyStable = true;
                res.individuallyRational = true;
            }
        }
        if (individuallyStable && !res.individuallyStable){
            if (cs.individuallyStable(g)){
                res.individuallyStable = true;
                res.contractuallyIndividuallyStable = true;
                res.individuallyRational = true;
            }
        }
        if (coreStable && !res.coreStable){
            if (cs.coreStable(g)){
                res.coreStable = true;
                res.individuallyRational = true;
            }
        }
        if (contractuallyIndividuallyStable && !res.contractuallyIndividuallyStable){
            res.contractuallyIndividuallyStable = cs.contractuallyIndividuallyStable(g);
        }
        if (individuallyRational && !res.individuallyRational){
            res.individuallyRational = cs.individuallyRational(g);
        }

        return "analysis";
    }

}