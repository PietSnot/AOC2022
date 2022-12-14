/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aoc2022;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.LongUnaryOperator;

import static aoc2022.day_11.monkeys;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.stream.Collectors.toCollection;

/**
 *
 * @author Piet
 */
public class day_11 {
    
    private final static int day = 11;
    private final static boolean test = false;
    
    final static List<Monkey> monkeys = new ArrayList<>();
    
    public static void main(String... args) throws IOException {
        getInput();
        solveA(20, true);
        System.out.println("**************************");
        System.out.println("beginnen met B");
        solveB();
    }
    
    private static void getInput() throws IOException {
        var path = AOC2022.getPath(day, test);
        List<String> inputList;
        try (var stream = Files.lines(path)) {
            inputList = stream.collect(toCollection(ArrayList::new));
            inputList.add("");
        }
        Monkey currentMonkey = null;
        for (var line: inputList) {
            line = line.trim();
            if (line.startsWith("Monkey")) {
                var space = line.indexOf(" ");
                var id = line.substring(space + 1, line.length() - 1);
                currentMonkey = new Monkey(parseInt(id));
            }
            else if (line.startsWith("Starting items")) {
                var s = line.split(": ");
                s[1] = s[1].replaceAll(",", "");
                var ar = s[1].split(" ");
                for (var n: ar) {
                    currentMonkey.addItem(parseLong(n.trim()));
                }
            }
            else if (line.startsWith("Operation")) {
                currentMonkey.setOperation(interprete(line));
            }
            else if (line.startsWith("Test")) {
                var ar = line.split(" ");
                currentMonkey.setDivBy(parseInt(ar[3]));
            }
            else if(line.startsWith("If true")) {
               var ar = line.split("\s+");
               currentMonkey.setIfTrue(parseInt(ar[5]));
            }
            else if(line.startsWith("If false")) {
                var ar = line.split("\s+");
                currentMonkey.setIfFalse(parseInt(ar[5]));
            }
            else if (line.isEmpty()) monkeys.add(currentMonkey);
        }
        
        long total = 1;
        for (var m: monkeys) {
            var val = m.getDivBy();
            total *= val;
        }
        Monkey.totalDivBy = total;
    }
    
    private static LongUnaryOperator interprete(String s) {
        var ar = s.split(" ");
        var plus = ar[4].equals("+");
        return ar[5].equals("old") ? 
            plus ? x -> x + x : x -> x * x :
            plus ? x -> x + parseLong(ar[5]) : x -> x * parseLong(ar[5]);
    }
    
    private static void solveA(int rounds, boolean isPart1) {
        for (int i = 1; i <= rounds; i++) {
            for (var m: monkeys) m.process(isPart1);
        }
        for (var m: monkeys) System.out.println(m);
        System.out.println("*******************************");
        var processed = monkeys.stream().mapToLong(Monkey::getProcessed).sorted().toArray();
        var len = processed.length;
        var result = processed[len - 2] * processed[len - 1];
        System.out.println("A: " + result);   
    }
    
    private static void solveB() throws IOException {
        monkeys.clear();
        getInput();
        solveA(10_000, false);
    }
}

class Monkey {
    public static long totalDivBy;
    private int id;
    private LinkedList<Long> items = new LinkedList<>();
    private LongUnaryOperator operation;
    private long divBy;
    private Map<Boolean, Integer> throwTo = new HashMap<>();
    private long itemsProcessed = 0;
    
    Monkey(int id) {
        this.id = id;
    }
    
    public void addItem(long item) {
        items.add(item);
    }
    
    public void setOperation(LongUnaryOperator f) {
        operation = f;
    }
    
    public void setDivBy(int div) {
        divBy = div;
    }
    
    public void setIfTrue(int monkey) {
        throwTo.put(true, monkey);
    }
    
    public void setIfFalse(int monkey) {
        throwTo.put(false, monkey);
    }
    
    public long getDivBy() {
        return (long) divBy;
    }
    
    public long getProcessed() {
        return itemsProcessed;
    }
    
    public void process(boolean part1) {
        itemsProcessed += items.size();
        while (!items.isEmpty()) {
            long retrieved = items.removeFirst();
            retrieved %= totalDivBy;
            long value = operation.applyAsLong(retrieved);
            if (part1) {
                value /= 3;
                if (value % divBy == 0) monkeys.get(throwTo.get(true)).addItem(value);
                else monkeys.get(throwTo.get(false)).addItem(value);
            }
            else {
                Monkey monkey;
                if (value % divBy == 0) monkey = monkeys.get(throwTo.get(true));
                else monkey = monkeys.get(throwTo.get(false));
                monkey.addItem(value);
            }
        }
    }
    
    @Override
    public String toString() {
        return String.format("m %d: items: %s, inspected: %d, divBy: %d", id, items, itemsProcessed, divBy);
    }
}

