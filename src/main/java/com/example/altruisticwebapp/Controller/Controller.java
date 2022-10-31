package com.example.altruisticwebapp.Controller;

import com.example.altruisticwebapp.Components.*;
import com.example.altruisticwebapp.Exceptions.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;

@org.springframework.stereotype.Controller
public class Controller {

    Game g;
    CoalitionStructure cs;
    String re = "redirect:";
    Result res = new Result();

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
        model.addAttribute("result", res);
        return "construction";
    }

    @PostMapping("/addPlayer")
    public String addPlayer(@RequestParam("name") String name)
            throws NoPlayerSetAssignedException, NoNetworkAssignedException {
        if (name.equals("")) {
            name = "Player " + (g.getSize() + 1);
        }

        Player p = new Player(name);
        if (!cs.isEmpty())
            cs.get(0).add(p);
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
    public String renamePlayer(@RequestParam("renamePlayer") int key, @RequestParam("newPlayerName") String name)
            throws NoPlayerSetAssignedException {
        g.renamePlayer(key, name);
        return re;
    }

    @PostMapping("/removeCoalitionFromCoalitionStructure")
    public String removeCoalitionFromCoalitionStructure(@RequestParam("coalitionRemove") int key) {
        cs.removeCoalition(key);
        return re;
    }

    @PostMapping("/addFriendship")
    public String addFriendship(@RequestParam("addKey1") int key1, @RequestParam("addKey") int key2)
            throws NoNetworkAssignedException {
        if (g.getNetwork().areFriends(key1, key2))
            g.getNetwork().removeFriendship(key1, key2);
        else
            g.getNetwork().addFriendship(key1, key2);
        return re;
    }

    @GetMapping("/analysis")
    public String analysis(Model model)
            throws NoPlayerSetAssignedException, NoNetworkAssignedException, PlayerNotFoundException {
        model.addAttribute("player_set", g.getPlayers());
        model.addAttribute("coalition_structure", cs);
        model.addAttribute("friendMatrix", g.getNetwork());
        model.addAttribute("blocking_coalitions", new HashSet<Coalition>());
        model.addAttribute("weakly_blocking_coalitions", new HashSet<Coalition>());
        model.addAttribute("result", res);
        return "analysis";
    }

    @PostMapping("/analysis/check")
    public String calculateStabilityConcepts(
            @RequestParam("valueBase") String valueBase,
            @RequestParam("treatment") String treatment,
            @RequestParam(required = false, value = "individuallyRational") boolean individuallyRational,
            @RequestParam(required = false, value = "nashStable") boolean nashStable,
            @RequestParam(required = false, value = "individuallyStable") boolean individuallyStable,
            @RequestParam(required = false, value = "contractuallyIndividuallyStable") boolean contractuallyIndividuallyStable,
            @RequestParam(required = false, value = "strictlyPopular") boolean strictlyPopular,
            @RequestParam(required = false, value = "popular") boolean popular,
            @RequestParam(required = false, value = "coreStable") boolean coreStable,
            @RequestParam(required = false, value = "strictlyCoreStable") boolean strictlyCoreStable,
            @RequestParam(required = false, value = "perfect") boolean perfect)
            throws Exception {
        res = new Result();
        g.getPlayers().printPlayers();
        for (int i = 0; i < g.getSize(); i++) {
            System.out.println(g.getPlayer(i).getKey());
        }
        LOA loa = LOA.stringToEnum(valueBase, treatment);

        /*
         * levelOfAltruism is returned as either "average" or "minimum"
         * treatment is returned as either "selfish_first","equal_treatment" or
         * "altruistic_treatment"
         */

        if (perfect && !res.perfect) {
            if (cs.perfect(g, loa)) {
                g.addEntry("The coalition structure is perfect.");
                g.addEntry("The coalition structure is strictly popular.");
                g.addEntry("The coalition structure is popular.");
                g.addEntry("The coalition structure is popular.");
                g.addEntry("The coalition struc");
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
        if (strictlyPopular && !res.strictlyPopular) {
            if (cs.strictlyPopular(g, loa)) {
                res.strictlyPopular = true;
                res.popular = true;
                res.contractuallyIndividuallyStable = true;
            }
        }
        if (popular && !res.popular) {
            if (cs.popular(g, loa)) {
                res.popular = true;
                res.contractuallyIndividuallyStable = true;
            }
        }
        if (strictlyCoreStable && !res.strictlyCoreStable) {
            if (cs.strictlyCoreStable(g, loa)) {
                res.strictlyCoreStable = true;
                res.contractuallyIndividuallyStable = true;
                res.individuallyStable = true;
                res.individuallyRational = true;
                res.coreStable = true;
            }
        }
        if (nashStable && !res.nashStable) {
            if (cs.nashStable(g, loa)) {
                res.nashStable = true;
                res.individuallyStable = true;
                res.contractuallyIndividuallyStable = true;
                res.individuallyRational = true;
            }
        }
        if (individuallyStable && !res.individuallyStable) {
            if (cs.individuallyStable(g, loa)) {
                res.individuallyStable = true;
                res.contractuallyIndividuallyStable = true;
                res.individuallyRational = true;
            }
        }
        if (coreStable && !res.coreStable) {
            if (cs.coreStable(g, loa)) {
                res.coreStable = true;
                res.individuallyRational = true;
            }
        }
        if (contractuallyIndividuallyStable && !res.contractuallyIndividuallyStable) {
            res.contractuallyIndividuallyStable = cs.contractuallyIndividuallyStable(g, loa);
        }
        if (individuallyRational && !res.individuallyRational) {
            res.individuallyRational = cs.individuallyRational(g, loa);
        }

        return "redirect:/analysis";
    }

    @PostMapping("/construct/build")
    public String buildCoalitionStructure(@RequestParam("valueBase") String valueBase,
            @RequestParam("treatment") String treatment,
            @RequestParam(required = false, value = "individuallyRational") boolean individuallyRational,
            @RequestParam(required = false, value = "nashStable") boolean nashStable,
            @RequestParam(required = false, value = "individuallyStable") boolean individuallyStable,
            @RequestParam(required = false, value = "contractuallyIndividuallyStable") boolean contractuallyIndividuallyStable,
            @RequestParam(required = false, value = "strictlyPopular") boolean strictlyPopular,
            @RequestParam(required = false, value = "popular") boolean popular,
            @RequestParam(required = false, value = "coreStable") boolean coreStable,
            @RequestParam(required = false, value = "strictlyCoreStable") boolean strictlyCoreStable,
            @RequestParam(required = false, value = "perfect") boolean perfect) throws Exception {
        boolean changed = false;
        LOA loa = LOA.stringToEnum(valueBase, treatment);
        HashSet<CoalitionStructure> all = g.getPlayers().generateCoalitionStructures();
        g.addEntry(all.size() + " coalition structures generated.");
        for (CoalitionStructure csAll : all) {
            if (individuallyRational) {
                if (!csAll.individuallyRational(g, loa))
                    continue;
            }
            if (nashStable) {
                if (!csAll.nashStable(g, loa))
                    continue;
            }
            if (individuallyStable) {
                if (!csAll.individuallyStable(g, loa))
                    continue;
            }
            if (contractuallyIndividuallyStable) {
                if (!csAll.contractuallyIndividuallyStable(g, loa))
                    continue;
            }
            if (strictlyPopular) {
                if (!csAll.strictlyPopular(g, loa))
                    continue;
            }
            if (popular) {
                if (!csAll.popular(g, loa))
                    continue;
            }
            if (coreStable) {
                if (!csAll.coreStable(g, loa))
                    continue;
            }
            if (strictlyCoreStable) {
                if (!csAll.strictlyCoreStable(g, loa))
                    continue;
            }
            if (perfect) {
                if (!csAll.perfect(g, loa))
                    continue;
            }
            this.cs = csAll;
            g.addEntry("The coalition structure generated fulfilled the requirements.");
            return "redirect:/construction";
        }
        g.addEntry("There exists no coalition structure fulfilling the requirements.");
        return "redirect:/construction";
    }

}