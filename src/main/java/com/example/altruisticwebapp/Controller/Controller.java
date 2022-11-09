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
        cs = new CoalitionStructure();
        g = new Game(5);
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
            throws NoPlayerSetAssignedException, NoNetworkAssignedException, PlayerNotFoundException {
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
        g.getPlayers().printPlayers();
        for (int i = 0; i < g.getSize(); i++) {
            System.out.println(g.getPlayer(i).getKey());
        }
        LOA loa = LOA.stringToEnum(valueBase, treatment);
        if (perfect && !res.perfect) {
            if (cs.perfect(g, loa)) {
                res.perfect = true;
                res.strictlyPopular = true;
                res.popular = true;
                res.strictlyCoreStable = true;
                res.nashStable = true;
                res.individuallyStable = true;
                res.contractuallyIndividuallyStable = true;
                res.individuallyRational = true;
                res.coreStable = true;
                g.log(logStr.Perf);
                g.log(logStr.SP);
                g.log(logStr.P);
                g.log(logStr.SCS);
                g.log(logStr.CS);
                g.log(logStr.Nash);
                g.log(logStr.IS);
                g.log(logStr.CIS);
                g.log(logStr.IR);


            }
            else g.log(logStr.notPerf);
        }
        if (strictlyPopular && !res.strictlyPopular) {
            if (cs.strictlyPopular(g, loa)) {
                res.strictlyPopular = true;
                res.popular = true;
                res.contractuallyIndividuallyStable = true;
                g.log(logStr.SP);
                g.log(logStr.P);
                g.log(logStr.CIS);
            }
            else g.log(logStr.notSP);
        }
        if (popular && !res.popular) {
            if (cs.popular(g, loa)) {
                res.popular = true;
                res.contractuallyIndividuallyStable = true;
                g.log(logStr.P);
                g.log(logStr.CIS);
            }
            else g.log(logStr.notP);
        }
        if (strictlyCoreStable && !res.strictlyCoreStable) {
            if (cs.strictlyCoreStable(g, loa)) {
                res.strictlyCoreStable = true;
                res.contractuallyIndividuallyStable = true;
                res.individuallyStable = true;
                res.individuallyRational = true;
                res.coreStable = true;
                g.log(logStr.SCS);
                g.log(logStr.CIS);
                g.log(logStr.IS);
                g.log(logStr.IR);
                g.log(logStr.CS);
            }
            else g.log(logStr.notSCS);
        }
        if (nashStable && !res.nashStable) {
            if (cs.nashStable(g, loa)) {
                res.nashStable = true;
                res.individuallyStable = true;
                res.contractuallyIndividuallyStable = true;
                res.individuallyRational = true;
                g.log(logStr.Nash);
                g.log(logStr.IS);
                g.log(logStr.CIS);
                g.log(logStr.IR);
            }
            else g.log(logStr.notNash);
        }
        if (individuallyStable && !res.individuallyStable) {
            if (cs.individuallyStable(g, loa)) {
                res.individuallyStable = true;
                res.contractuallyIndividuallyStable = true;
                res.individuallyRational = true;
                g.log(logStr.IS);
                g.log(logStr.CIS);
                g.log(logStr.IR);
            }
            else g.log(logStr.notIS);
        }
        if (coreStable && !res.coreStable) {
            if (cs.coreStable(g, loa)) {
                res.coreStable = true;
                res.individuallyRational = true;
                g.log(logStr.CS);
                g.log(logStr.IR);
            }
            else g.log(logStr.notCS);
        }
        if (contractuallyIndividuallyStable && !res.contractuallyIndividuallyStable) {
            res.contractuallyIndividuallyStable = cs.contractuallyIndividuallyStable(g, loa);
            if (res.contractuallyIndividuallyStable) g.log(logStr.CIS);
            else g.log(logStr.notCIS);
        }
        if (individuallyRational && !res.individuallyRational) {
            res.individuallyRational = cs.individuallyRational(g, loa);
            if (res.individuallyRational) g.log(logStr.IR);
            else g.log(logStr.notIR);
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
            this.csconstruct = csAll;
            g.addEntry("The coalition structure generated fulfilled the requirements. If you want to check for other stability concepts for this coalition structure, click the button below and use the Analysis tab.");
            return "redirect:/construction";
        }
        g.addEntry("There exists no coalition structure fulfilling the requirements.");
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

    @PostMapping("/")
    public void terminate(){
        System.exit(0);
    }

}