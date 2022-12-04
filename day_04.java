/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aoc2022;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author Piet
 */
public class day_04 {
    final private static int day = 4;
    final private static boolean test = false;
    private static List<List<Range>> listsOfRanges;
    
    public static void main(String... args) throws IOException {
        getInput();
        solveA();
        solveB();
    }
    
    private static void solveA() {
        var result = listsOfRanges.stream()
            .filter(list -> Range.completelyOverlap(list.get(0), list.get(1)))
            .count()
        ;
        System.out.format("A: %d%n", result);
    }
    
    private static void solveB() {
        var result = listsOfRanges.stream()
            .filter(list -> Range.partlyOverlap(list.get(0), list.get(1)))
            .count()
        ;
        System.out.format("B: %d%n", result);
    }
    
    private static void getInput() throws IOException {
        var path = AOC2022.getPath(day, test);
        try(var stream = Files.lines(path)) {
            listsOfRanges = stream.map(day_04::generateRecord).toList();
        }
    }
    
    private static List<Range> generateRecord(String s) {
        var ar = s.split(",");
        return Stream.of(Range.of(ar[0]), Range.of(ar[1])).toList();
    }
}

record Range(int first, int last) {
    static Range of(String s) {
        var ar = s.split("-");
        return new Range(Integer.parseInt(ar[0]), Integer.parseInt(ar[1]));
    }
    public static boolean completelyOverlap(Range a, Range b) {
        return (a.first <= b.first && a.last >= b.last) ||
               (a.first >= b.first && a.last <= b.last)
        ;
    }
    
    public static boolean partlyOverlap(Range a, Range b) {
        return (a.first <= b.last && a.last >= b.first);
    }
}
