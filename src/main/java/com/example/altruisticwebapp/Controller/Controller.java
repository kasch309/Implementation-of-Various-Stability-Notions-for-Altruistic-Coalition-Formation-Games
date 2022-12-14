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
    boolean coalchart = false;
    CoalitionStructure csconstruct = new CoalitionStructure();
    String re = "redirect:";
    Result res = new Result();

    public Controller() {
        g = new Game(3);
        cs = new CoalitionStructure();
    }

    @GetMapping("/")
    public String home(Model model) throws NoPlayerSetAssignedException, NoNetworkAssignedException {
        model.addAttribute("player_set", g.getPlayers());
        model.addAttribute("coalition_structure", cs);
        model.addAttribute("friendMatrix", g.getNetwork());
        model.addAttribute("coalchart", coalchart);
        return "home";
    }

    @PostMapping("/home/changeGraph")
    public String ChangeGraph(@RequestParam("coalchart") String newCoalChart) {
        if (newCoalChart.equals("false"))
            coalchart = false;
        if (newCoalChart.equals("true"))
            coalchart = true;
        return "redirect:/";
    }

    @GetMapping("/construction")
    public String construction(Model model) throws NoPlayerSetAssignedException, NoNetworkAssignedException {
        model.addAttribute("log", g.getLog());
        model.addAttribute("player_set", g.getPlayers());
        model.addAttribute("coalition_structure", csconstruct);
        model.addAttribute("friendMatrix", g.getNetwork());
        model.addAttribute("result", res);
        model.addAttribute("coalchart", coalchart);
        return "construction";
    }

    @PostMapping("/construction/changeGraph")
    public String constChangeGraph(@RequestParam("coalchart") String newCoalChart) {
        if (newCoalChart.equals("false"))
            coalchart = false;
        if (newCoalChart.equals("true"))
            coalchart = true;
        return "redirect:/construction";
    }

    @PostMapping("/addPlayer")
    public String addPlayer(@RequestParam("name") String name)
            throws NoPlayerSetAssignedException, NoNetworkAssignedException {
        if (name.equals("")) {
            for (int i = 0; i < g.getSize(); i++){
                if (!g.getPlayers().containsPlayerWithName("Player " + i)){
                    name = "Player " + i;
                }
            }
            if (name.equals("")) name = "Player " + g.getSize();
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
        g = new Game(3);
        cs = new CoalitionStructure();
        return re;
    }

    @PostMapping("/removePlayerFromGame")
    public String removePlayerFromGame(@RequestParam("playerRemove") int key) throws NoPlayerSetAssignedException {
        g.removePlayer(g.getPlayer(key), cs);
        return re;
    }

    @PostMapping("/addPlayerToCoalition")
    public String addPlayerToCoalition(@RequestParam("player") int key, @RequestParam("coalitionId") int coal)
            throws NoPlayerSetAssignedException {
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
        if (cs.size() == 1) {
            cs = new CoalitionStructure();
            return re;
        }
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
            throws NoPlayerSetAssignedException, NoNetworkAssignedException {
        model.addAttribute("player_set", g.getPlayers());
        model.addAttribute("coalition_structure", cs);
        model.addAttribute("friendMatrix", g.getNetwork());
        model.addAttribute("blocking_coalitions", new HashSet<Coalition>());
        model.addAttribute("weakly_blocking_coalitions", new HashSet<Coalition>());
        model.addAttribute("log", g.getLog());
        model.addAttribute("result", res);
        model.addAttribute("coalchart", coalchart);
        return "analysis";
    }

    @PostMapping("analysis/changeGraph")
    public String analysisChangeGraph(@RequestParam("coalchart") String newCoalChart) {
        if (newCoalChart.equals("false"))
            coalchart = false;
        if (newCoalChart.equals("true"))
            coalchart = true;
        return "redirect:/analysis";

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
        LOA loa = LOA.stringToEnum(valueBase, treatment);
        if (perfect && !res.perfect) {
            if (cs.perfect(g, loa, true)) {
                res.perfect = true;
                res.strictlyPopular = true;
                res.popular = true;
                res.strictlyCoreStable = true;
                res.nashStable = true;
                res.individuallyStable = true;
                res.contractuallyIndividuallyStable = true;
                res.individuallyRational = true;
                res.coreStable = true;
                g.log("Per implication this coalition structure fulfills all of the stability concepts on the left too.");
            }
        }
        if (strictlyPopular && !res.strictlyPopular) {
            if (cs.strictlyPopular(g, loa, true)) {
                res.strictlyPopular = true;
                res.popular = true;
                res.contractuallyIndividuallyStable = true;
                g.log("Per implication this coalition structure is popular and contractually individually stable too.");
            }
        }
        if (popular && !res.popular) {
            if (cs.popular(g, loa, true)) {
                res.popular = true;
                res.contractuallyIndividuallyStable = true;
                g.log("Per implication this coalition structure is contractually individually stable too.");
            }
        }
        if (strictlyCoreStable && !res.strictlyCoreStable) {
            if (cs.strictlyCoreStable(g, loa, true)) {
                res.strictlyCoreStable = true;
                res.contractuallyIndividuallyStable = true;
                res.individuallyStable = true;
                res.individuallyRational = true;
                res.coreStable = true;
                g.log("Per implication this coalition structure is core stable, contractually individually stable, individually stable and individually rational too.");
            }
        }
        if (nashStable && !res.nashStable) {
            if (cs.nashStable(g, loa, true)) {
                res.nashStable = true;
                res.individuallyStable = true;
                res.contractuallyIndividuallyStable = true;
                res.individuallyRational = true;
                g.log("Per implication this coalition structure is contractually individually stable, individually stable and individually rational too.");

            }
        }
        if (individuallyStable && !res.individuallyStable) {
            if (cs.individuallyStable(g, loa, true)) {
                res.individuallyStable = true;
                res.contractuallyIndividuallyStable = true;
                res.individuallyRational = true;
                g.log("Per implication this coalition structure is contractually individually stable and individually rational too.");

            }
        }
        if (coreStable && !res.coreStable) {
            if (cs.coreStable(g, loa, true)) {
                res.coreStable = true;
                res.individuallyRational = true;
                g.log("Per implication this coalition structure is individually rational too.");

            }
        }
        if (contractuallyIndividuallyStable && !res.contractuallyIndividuallyStable) {
            res.contractuallyIndividuallyStable = cs.contractuallyIndividuallyStable(g, loa, true);
        }
        if (individuallyRational && !res.individuallyRational) {
            res.individuallyRational = cs.individuallyRational(g, loa, true);
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
        LOA loa = LOA.stringToEnum(valueBase, treatment);
        HashSet<CoalitionStructure> all = g.getPlayers().generateCoalitionStructures();
        for (CoalitionStructure csAll : all) {
            if (individuallyRational) {
                if (!csAll.individuallyRational(g, loa, false))
                    continue;
            }
            if (nashStable) {
                if (!csAll.nashStable(g, loa, false))
                    continue;
            }
            if (individuallyStable) {
                if (!csAll.individuallyStable(g, loa, false))
                    continue;
            }
            if (contractuallyIndividuallyStable) {
                if (!csAll.contractuallyIndividuallyStable(g, loa, false))
                    continue;
            }
            if (strictlyPopular) {
                if (!csAll.strictlyPopular(g, loa, false))
                    continue;
            }
            if (popular) {
                if (!csAll.popular(g, loa, false))
                    continue;
            }
            if (coreStable) {
                if (!csAll.coreStable(g, loa, false))
                    continue;
            }
            if (strictlyCoreStable) {
                if (!csAll.strictlyCoreStable(g, loa, false))
                    continue;
            }
            if (perfect) {
                if (!csAll.perfect(g, loa, false))
                    continue;
            }
            this.csconstruct = csAll;
            if (perfect) g.log(logStr.Perf);
            if (strictlyCoreStable) g.log(logStr.SCS);
            if (coreStable) g.log(logStr.CS);
            if (strictlyPopular) g.log(logStr.SP);
            if (popular) g.log(logStr.P);
            if (contractuallyIndividuallyStable) g.log(logStr.CIS);
            if (individuallyStable) g.log(logStr.IS);
            if (nashStable) g.log(logStr.Nash);
            if (individuallyRational) g.log(logStr.IR);
            return "redirect:/construction";
        }
        g.addEntry("There exists no coalition structure fulfilling the checked requirements.");
        return "redirect:/construction";
    }

    @PostMapping("/construction/overwritecs")
    public String overwriteCs(){
        cs = csconstruct;
        return "redirect:/construction";
    }

    @PostMapping("/analysis/resetlog")
    public String resetLogA(){
        g.clearLog();
        return "redirect:/analysis";
    }

    @PostMapping("/construction/resetlog")
    public String resetLogC(){
        g.clearLog();
        return "redirect:/construction";
    }

    @GetMapping("/terminate")
    public void terminate(){
        System.exit(0);
    }

}