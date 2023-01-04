/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aoc2022;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Integer.parseInt;

/**
 *
 * @author Piet
 */
public class day_19 {
    
    private static int day = 19;
    private static boolean test = true;
    
    private static List<Blueprint> blueprints = new ArrayList<>();
    
    public static void main(String... args) throws IOException {
        getInput();
        solveA(24);
    }
    
    private static void getInput() throws IOException {
        var path = AOC2022.getPath(day, test);
        try (var stream = Files.lines(path)) {
            blueprints = stream.map(day_19::processLine).toList();
        }
    }
    
    private static Blueprint processLine(String line) {
        var ar = line.split(" ");
        var id = parseInt(ar[1].substring(0, ar[1].length() - 1));
        var orerobot = new Robot("ore", new Cost("ore", parseInt(ar[6])));
        var clayrobot = new Robot("clay", new Cost("ore", parseInt(ar[12])));
        var obsidianrobot = new Robot("obsidian", new Cost("ore", parseInt(ar[18])), new Cost("clay", parseInt(ar[21])));
        var geoderobot = new Robot("geode", new Cost("ore", parseInt(ar[27])), new Cost("obsidian", parseInt(ar[30])));
        return new Blueprint(id, orerobot, clayrobot, obsidianrobot, geoderobot);
    } 
    
    private static void solveA(int maxminutes) {
        int result = 0;
        for (var b: blueprints) {
            var minuteData = new MinuteData(Command.INIT, 1, 0, 0, 0, 0, 1, 0, 0, 0);
            List<MinuteData> sofar = new ArrayList<>();
            sofar.add(minuteData);
            var best = new ArrayList<MinuteData>();
            process(sofar, best, b, maxminutes);
//            best.forEach(System.out::println);
            var maxgeode = best.get(best.size() - 1).geodes();
            result += maxgeode * b.id;
        }
        System.out.println("A: " + result);
    }
    
    private static void process(List<MinuteData> sofar, List<MinuteData> best, Blueprint b, int maxminutes) {
        var data = sofar.get(sofar.size() - 1);
        if (data.minute() == maxminutes + 1) {
            if (best.isEmpty() || (data.geodes() > best.get(best.size() - 1).geodes())) {
                best.clear();
                best.addAll(sofar);
            }
            return;
        }
        int oresc = data.ores();
        int claysc = data.clays();
        int obsc = data.obsidians();
        int geodesc = data.geodes();
        int reqOres = 0;
        int reqClays = 0;
        int reqObs = 0;
        int newOreR = 0;
        int newClayR = 0;
        int newObsR = 0;
        int newGeoR = 0;
        Command command = null;
        
        // buying goedeRobots?
        reqOres = b.getRobot("geode").getCost("ore");
        reqObs = b.getRobot("geode").getCost("obsidian");
        while (reqOres <= oresc && reqObs <= obsc) {
            newGeoR++;
            oresc -= reqOres;
            obsc -= reqObs;
        }
        // buying obsidianRobots?
        reqOres = b.getRobot("obsidian").getCost("ore");
        reqClays = b.getRobot("obsidian").getCost("clay");
        while (reqOres <= oresc && reqClays <= claysc) {
            newObsR++;
            oresc -= reqOres;
            claysc -= reqClays;
        }
        // buying clayRobots?
        reqOres = b.getRobot("clay").getCost("ore");
        while (reqOres <= oresc) {
            newClayR++;
            oresc -= reqOres;
        }
        // buying oreRobots?
        reqOres = b.getRobot("ore").getCost("ore");
        while (reqOres <= oresc) {
            newOreR++;
            oresc -= reqOres;
        }    
        command = newGeoR > 0  ? Command.BUILT_GEODE :
                  newObsR > 0  ? Command.BUILT_OBSIDIAN :
                  newClayR > 0 ? Command.BUILT_CLAY :
                  newOreR > 0  ? Command.BUILT_ORE :
                                 Command.DID_NOTHING
        ;     
        // go!
        var newdata = new MinuteData(command, data.minute() + 1,
            oresc + data.oreRobots(), claysc + data.clayRobots(), obsc + data.obsidianRobots(), geodesc + data.geodeRobots(),
            data.oreRobots() + newOreR, data.clayRobots() + newClayR, 
            data.obsidianRobots() + newObsR, data.geodeRobots() + newGeoR
        );
        sofar.add(newdata);
        process(sofar, best, b, maxminutes);
        sofar.remove(sofar.size() - 1);
        
        // check for oreRobots
        if ((b.getRobot("ore").getCost("ore") % b.getRobot("clay").getCost("ore") == 0) && (data.clays() < b.getRobot("obsidian").getCost("clay"))) {
            newdata = data.buyOreRobots(b);
            sofar.add(newdata);
            process(sofar, best, b, maxminutes);
            sofar.remove(sofar.size() - 1);
        }
    }
}

//**********************************************************************
class Blueprint {
    int id;
    Set<Robot> robots = new HashSet<>();
    
    Blueprint(int id, Robot... roboten) {
        this.id = id;
        for (var r: roboten) robots.add(r);
    }
    
    public Robot getRobot(String name) {
        return robots.stream().filter(r -> r.name.equals(name)).findFirst().get();
    }
}

//**********************************************************************
class Robot {
    final String name;
    final Set<Cost> costs = new HashSet<>();
    
    Robot(String name, Cost... costs) {
        this.name = name;
        for (var c: costs) this.costs.add(c);
    }
    
    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("robot: " + name);
        costs.stream().forEach(e -> sb.append("  " + e.name() + ": " + e.value()));
        return sb.toString();
    }
    
    public int getCost(String name) {
        return costs.stream().filter(c -> c.name().equals(name)).mapToInt(c -> c.value()).findFirst().getAsInt();
    }
}

//**********************************************************************
record Cost(String name, int value) {}

//**********************************************************************
record MinuteData(
    Command command,
    int minute,
    int ores, int clays, int obsidians, int geodes, 
    int oreRobots, int clayRobots, int obsidianRobots, int geodeRobots) 
{
    public boolean canBuyGeodeRobot(Blueprint b) {
        var reqOres = b.getRobot("geode").getCost("ore");
        var reqObs = b.getRobot("geode").getCost("obsidian");
        return (ores >= reqOres && obsidians >= reqObs);
    }
    
    public boolean canBuyObsidianRobot(Blueprint b) {
        var reqOres = b.getRobot("obsidian").getCost("ore");
        var reqClays = b.getRobot("obsidian").getCost("clay");
        return (ores >= reqOres && clays >= reqClays);
    }
    
    public boolean canBuyClayRobot(Blueprint b) {
        var reqOres = b.getRobot("clay").getCost("ore");
        return ores >= reqOres;
    }
    
    public boolean canBuyOreRobot(Blueprint b) {
        var reqOres = b.getRobot("ore").getCost("ore");
        return ores >= reqOres;
    }
    
    public MinuteData buyGeodeRobots(Blueprint b) {
        var reqOres = b.getRobot("geode").getCost("ore");
        var reqObs = b.getRobot("geode").getCost("obsidian");
        int oresc = ores;
        int obsc = obsidians;
        int newRobots = 0;
        while (oresc >= reqOres && obsc >= reqObs) {
            newRobots++;
            oresc -= reqOres;
            obsc -= reqObs;
        }
        
        return new MinuteData(Command.BUILT_GEODE, minute + 1,
            oresc + oreRobots, clays + clayRobots, obsc + obsidianRobots, geodes + geodeRobots,
            oreRobots, clayRobots, obsidianRobots, geodeRobots + newRobots
        );
    }
    
    public MinuteData doNothing() {
        return new MinuteData(Command.DID_NOTHING, minute + 1,
            ores + oreRobots, clays + clayRobots, obsidians + obsidianRobots, geodes + geodeRobots,
            oreRobots, clayRobots, obsidianRobots, geodeRobots
        );
    }
    
    public MinuteData buyObsidianRobots(Blueprint b) {
        var reqOres = b.getRobot("obsidian").getCost("ore");
        var reqClays = b.getRobot("obsidian").getCost("clay");
        int oresc = ores;
        int claysc = clays;
        int newRobots = 0;
        while (oresc >= reqOres && claysc >= reqClays) {
            newRobots++;
            oresc -= reqOres;
            claysc -= reqClays;
        }
        return new MinuteData(Command.BUILT_OBSIDIAN, minute + 1,
            oresc + oreRobots, claysc + clayRobots, obsidians + obsidianRobots, geodes + geodeRobots,
            oreRobots, clayRobots, obsidianRobots + newRobots, geodeRobots
        );
    }
    
    public MinuteData buyClayRobots(Blueprint b) {
        var reqOres = b.getRobot("clay").getCost("ore");
        int oresc = ores;
        int newRobots = 0;
        while (oresc >= reqOres) {
            newRobots++;
            oresc -= reqOres;
        }
        return new MinuteData(Command.BUILT_CLAY, minute + 1,
            oresc + oreRobots, clays + clayRobots, obsidians + obsidianRobots, geodes + geodeRobots,
            oreRobots, clayRobots + newRobots, obsidianRobots, geodeRobots
        );
    }
    
    public MinuteData buyOreRobots(Blueprint b) {
        var reqOres = b.getRobot("ore").getCost("ore");
        int oresc = ores;
        int newRobots = 0;
        while (oresc >= reqOres) {
            newRobots++;
            oresc -= reqOres;
        }
        return new MinuteData(Command.SPECIAL_ORE, minute + 1,
            oresc + oreRobots, clays + clayRobots, obsidians + obsidianRobots, geodes + geodeRobots,
            oreRobots + newRobots, clayRobots, obsidianRobots, geodeRobots
        );
    }
    
    @Override
    public String toString() {
        return String.format("m: %2d, c: %20s, or: %2d, cl: %2d, ob: %2d, ge: %2d, orR: %2d, clR: %2d, obR: %2d, geR: %2d",
            minute, command, ores, clays, obsidians, geodes,
            oreRobots, clayRobots, obsidianRobots, geodeRobots);
    }
}

//**********************************************************************
enum Command {
    INIT, DID_NOTHING, BUILT_ORE, BUILT_CLAY, BUILT_OBSIDIAN, BUILT_GEODE, SPECIAL_ORE
}