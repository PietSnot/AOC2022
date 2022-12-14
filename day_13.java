/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aoc2022;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.Integer.min;
import static java.util.stream.Collectors.toCollection;

/**
 *
 * @author Piet
 */
public class day_13 {
    
    private final static int day = 13;
    private final static boolean test = false;
    private static final Comparator<List<Object>> comp = (a, b) -> {
        var result = check(a, b);
        return result == Verdict.ORDERED   ? -1 :
               result == Verdict.UNORDERED ?  1 :
                                              0
        ;
    };
    
    private final static List<List<Object>> input = new ArrayList<>();
    
    public static void main(String... args) throws IOException {
        getInput();
        solveA();
        solveB();
    }
    
    public static void getInput() throws IOException {
        var path = AOC2022.getPath(day, test);
        List<String> lines;
        try (var stream = Files.lines(path)) {
            lines = stream.toList();        
        }
        process(lines);
    }
    
    private static void process(List<String> lines) {
        for (var s: lines) {
            if (s.isEmpty()) continue;
            input.add(interprete(s));
        }
        System.out.println(input.size());
    }
    
    private static void solveA() {
        int score = 0;
        int pair = 0;
        for (int i = 0; i < input.size(); i += 2) {
            pair++;
            var first = input.get(i);
            var second = input.get(i + 1);
            var result = check(first, second);
            if (result == Verdict.ORDERED) score += pair;
        }
        System.out.println("A: " + score);
    }
    
    private static void solveB() {
        var copy = input.stream().collect(toCollection(ArrayList::new));
        var p1 = interprete("[[2]]");
        copy.add(p1);
        var p2 = interprete("[[6]]");
        copy.add(p2);
        copy.sort(comp);
        var key1 = copy.indexOf(p1);
        var key2 = copy.indexOf(p2);
        System.out.println("index key 1: " + key1);
        System.out.println("index key 1: " + key2);
        System.out.println("B: " + (key1 + 1) * (key2 + 1));
    }
    
    private static Verdict check(Integer first, Integer second) {
        return first < second ? Verdict.ORDERED :
               first > second ? Verdict.UNORDERED :
                                Verdict.UNKNOWN
        ;
    }
    
    private static Verdict check(Integer first, List<Object> second) {
        if (second.isEmpty()) return Verdict.UNORDERED;
        return check(List.of(first), second);
    }
    
    private static Verdict check(List<Object> first, Integer second) {
        if (first.isEmpty()) return Verdict.ORDERED;
        return check(first, List.of(second));
    }
    
    private static Verdict check(List<Object> first, List<Object> second) {
        Verdict result;
        if (first.isEmpty() && second.isEmpty()) return Verdict.UNKNOWN;
        if (first.isEmpty()) return Verdict.ORDERED;
        if (second.isEmpty()) return Verdict.UNORDERED;
        for (int i = 0; i < min(first.size(), second.size()); i++) {
            Object f = first.get(i);
            Object s = second.get(i);
            if (f instanceof Integer) {
                if (s instanceof Integer) {
                    result = check((Integer) f, (Integer) s);
                    if (result != Verdict.UNKNOWN) return result;
                }
                else {
                    result = check(List.of(f), (List<Object>) s);
                    if (result != Verdict.UNKNOWN) return result;
                }
            }
            else {
                if (s instanceof Integer) {
                    result = check((List<Object>) f, List.of(s));
                    if (result != Verdict.UNKNOWN) return result;
                }
                else {
                    result = check((List<Object>) f, (List<Object>) s);
                    if (result != Verdict.UNKNOWN) return result;
                }
            }
        } 
        // geen beslissing, we kijken naar de lengte van beide parameters
        return first.size() < second.size()  ? Verdict.ORDERED : 
               first.size() == second.size() ? Verdict.UNKNOWN :
                                               Verdict.UNORDERED;
    }
    
    private static List<Object> interprete(String s) {
        var result = new ArrayList<Object>();
        if (!(s.startsWith("[") && s.endsWith("]"))) throw new RuntimeException("Bad inputstring: " + s);
        int index = 1;
        while (index < s.length() - 1) {
            if (s.charAt(index) == ',') {
                index++;
                continue;
            }
            if (Character.isDigit(s.charAt(index))) {
                var str = "" + s.charAt(index);
                index++;
                while (Character.isDigit(s.charAt(index))) {
                    str += "" + s.charAt(index);
                    index++;
                }
                result.add(Integer.parseInt(str));
                continue;
            }
            if (s.charAt(index) == '[') {
                var endindex = index + 1;
                var open = 1;
                while (open > 0) {
                    if (s.charAt(endindex) == '[') open++;
                    else if (s.charAt(endindex) == ']') open--;
                    endindex++;
                }
                result.add(interprete(s.substring(index, endindex)));
                index = endindex;
            }
        }
        return result;
    }
}

enum Verdict {
    ORDERED, UNORDERED, UNKNOWN; 
}
