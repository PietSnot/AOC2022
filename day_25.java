/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aoc2022;

import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Piet
 */
public class day_25 {
    
    private static final int day = 25;
    private final static boolean test = false;
    private static List<String> snafus;
    
    public static void main(String... args) {
//        for (int i = 0; i <= 40; i++) {
//            System.out.format("%2d:    %s%n", i, Snafu.makeSnafu(i));
//        }
//        System.out.println("2022: " + Snafu.makeSnafu((2022)));
//        var snafu = new Snafu("1=11-2");
//        System.out.println(snafu + ": " + snafu.value() );
//        var l = 314159265L;
//        var x = Snafu.makeSnafu(l);
//        System.out.format("%d: %s%n", l, x);
//        System.out.format("%d%n", x.value());
//        System.out.println(Long.toString(l, 5));
//        var st = "1121-1110-1=0";
//        var sn = new Snafu(st);
//        System.out.println(sn.value());
        getInput();
        solveA();
    }
    
    private static void getInput() {
        var path = AOC2022.getPath(day, test);
        try {
            snafus = Files.readAllLines(path);
        }
        catch (IOException e) {
            throw new RuntimeException("kan het invoerbestand niet lezen");
        }
    }
    
    private static void solveA() {
        var result = snafus.stream().mapToLong(s -> new Snafu(s.trim()).value()).sum();
        var snafu = Snafu.makeSnafu(result);
        System.out.println("A: " + snafu);
    }
    
}

class Snafu {
    private LinkedList<Integer> list = new LinkedList<>();
    private static Map<Character, Integer> map = Map.of('=', -2, '-', -1, '0', 0, '1' , 1, '2', 2);
    
    public Snafu(String snafu) {
        for (int i = snafu.length() - 1; i >= 0; i--) list.add(map.get(snafu.charAt(i)));
    }
    
    public Snafu(List<Integer> list) {
        this.list = new LinkedList<>(list);
    }
    
    public long value() {
        long result = 0;
        long value = 1;
        for (var it = list.iterator(); it.hasNext(); ) {
            result += it.next() * value;
            value *= 5;
        }
        return result;
    }
    
    public static Snafu makeSnafu(long val) {
        var p = new Penta(val);
        return makeSnafu(p);
    }
    
    public static Snafu makeSnafu(Penta p) {
        var lijst = p.getCoff();
        var result = new LinkedList<Integer>();
        var rest = 0;
        var it = lijst.iterator();
        while (it.hasNext()) {
            var value = it.next();
            value += rest;
            if (value <= 2) {
                result.add(value);
                rest = 0;
            }
            else if (value == 3) {
                result.add(-2);
                rest = 1;
            }
            else if (value == 4) {
                result.add(-1);
                rest = 1;
            }
            else {
                result.add(0);
                rest = 1;
            }
        }
        if (rest == 1) result.add(1);
        return new Snafu(result);
    }
    
    public String toString() {
        var it = list.iterator();
        var sb = new StringBuilder();
        while (it.hasNext()) {
            var c = it.next();
            if (c >= 0) sb.append(c);
            else if (c == -2) sb.append('=');
            else sb.append('-');
        }
        return sb.reverse().toString();
    }
}

class Penta {
    private final LinkedList<Integer> list = new LinkedList<>();
    private final long decimal;
    
    Penta(long val) {
        decimal = val;
        var s = Long.toString(val, 5);
        for (int i = s.length() - 1; i >= 0; i--) list.add(s.charAt(i) - '0');
    }
    
    public long value() {
        return decimal;
    }
    
    public LinkedList<Integer> getCoff() {
        return new LinkedList<>(list);
    }
    
    @Override
    public String toString() {
        return list.stream()
            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
            .reverse()
            .toString()
        ;
    }
}