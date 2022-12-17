/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aoc2022;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.lang.Long.*;
import static java.lang.Math.abs;
import static java.util.stream.Collectors.toSet;

/**
 *
 * @author Piet
 */
public class day_15 {
    
    private static int day = 15;
    private static boolean test = false;
    
    private static final Map<Punt, Punt> sensorbaken = new HashMap<>();
    private static final Map<Punt, Long> distances = new HashMap<>();
    
    public static void main(String... args) throws IOException {
        getInput();
        var A = solveA(test ? 10 : 2_000_000);
        System.out.println("A: " + A);
        long start = System.currentTimeMillis();
        var B = solveB();
        long end = System.currentTimeMillis();
        if (B == null) {
            System.out.println("geen oplossing gevonden voor B");
        }
        else {
            System.out.println("B: " + B);
        }
        System.out.println("duurde: " + (end - start) / 1000. + " seconden");
//        System.out.println("A: " + res);
//        var s1 = new Segment(-5, 4);
//        var s2 = new Segment(5, 5);
//        var list = List.of(s1, s2);
//        var result = Segment.merge(list);
//        System.out.println("s1 bevat 5? " + s1.contains(5));
    }
    
    private static long solveA(long Y) {
        var segments = new ArrayList<Segment>();
        for (var k: sensorbaken.keySet()) {
            var r = distances.get(k);
            var a = abs(Y - k.y());
            var seg = new Segment(-r + a + k.x(), r - a + k.x());
            if (seg.x() <= seg.y()) segments.add(seg);
        }
        var result = Segment.merge(segments);
        long total = 0;
        var values = sensorbaken.values().stream()
            .filter(p -> p.y() == Y)
            .map(p -> p.x())
            .collect(toSet())
        ;
        for (var seg: result) {
            total += seg.length();
            for (var b: values) {
                if (seg.contains(b)) total--;
            }
        }
        return total;
    }
    
    private static Long solveB() {
        for (long Y = 0; Y <= (test ? 20L : 4_000_000L) ; Y++) {
            var list = getSegmentsB(Y);
            if (list.size() > 1) {
                var x = list.get(0).y() + 1;
                var result = x * 4_000_000 + Y;
                return result;
            }
        }
        return null;
    }
    
    private static List<Segment> getSegmentsB(long Y) {
        var segments = new ArrayList<Segment>();
        for (var k: sensorbaken.keySet()) {
            var r = distances.get(k);
            var a = abs(Y - k.y());
            var start = -r + a + k.x();
            var end = r - a + k.x();
            start = max(start, 0L);
            end = min(end, test ? 20L : 4_000_000L);
            if (start <= end) {
                var seg = new Segment(-r + a + k.x(), r - a + k.x());
                segments.add(seg);
            }
        }
        var result = Segment.merge(segments);
        return result;
    }
    
    private static void getInput() throws IOException {
        var path = AOC2022.getPath(day, test);
        var scanner = new Scanner(path);
        while (scanner.hasNextLine()) {
            var s = scanner.nextLine().split(":");
            var cc1 = getCC(s[0]);
            var cc2 = getCC(s[1]);
            sensorbaken.put(cc1, cc2);
            distances.put(cc1, cc1.distance(cc2));
        }
        scanner.close();
    }
    
    private static Punt getCC(String s) {
        var start = s.indexOf("=") + 1;
        var komma = s.indexOf(",");
        var end = s.lastIndexOf("=") + 1;
        var x = parseLong(s.substring(start, komma));
        var y = parseLong(s.substring(end, s.length()));
        return new Punt(x, y);
    }
}
    
record Segment(long x, long y) {
    
    public long length() {
        return y - x + 1;
    }
    
    public boolean contains(long i) {
        return x <= i && y >= i;
    }
    
    public static List<Segment> merge(List<Segment> list) {
        var temp = list.stream().flatMap(segment -> Stream.of(new Eindpunt(true, segment.x), new Eindpunt(false, segment.y)))
            .sorted(Eindpunt.getComparator())
            .toList()
        ;
        var result = new ArrayList<Segment>(); 
        var buffer = new LinkedList<Eindpunt>();
        for (var s: temp) {
            if (s.start()) buffer.addLast(s);
            else {
                var begin = buffer.removeLast().x();
                if (buffer.isEmpty()) result.add(new Segment(begin, s.x()));
            }
        }
        return result;
    }
}

record Eindpunt (boolean start, long x) {
    private static final Comparator<Eindpunt> comp;
    static {
        Comparator<Eindpunt> c1 = Comparator.comparingLong(Eindpunt::x);
        Comparator<Eindpunt> c2 = Comparator.comparing(Eindpunt::start);
        comp = c1.thenComparing(c2.reversed());

    }
    
    public static Comparator<Eindpunt> getComparator() {
        return comp;
    }
}

record Punt(long x, long y) {
    
    public long distance(Punt p) {
        return abs(x - p.x) + abs(y - p.y);
    }
}