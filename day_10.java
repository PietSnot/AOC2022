package aoc2022;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.lang.System.out;
import static java.util.stream.Collectors.joining;

/**
 *
 * @author Piet
 */
public class day_10 {
    
    private final static int day = 10;
    private final static boolean test = false;
    
    private final static List<Integer> cycles = new ArrayList<>();
    private static List<String> instructions;
    private final static Map<String, Integer> clocktics = new HashMap<>();
    
    public static void main(String... args) throws IOException {
        fillClockticsMap();
        getInput();
        solveA();
        solveB();
    }
    
    private static void fillClockticsMap() {
        clocktics.put("noop", 1);
        clocktics.put("addx", 2);
    }
    
    private static void getInput() throws IOException {
        var path = AOC2022.getPath(day, test);
        try (var stream = Files.lines(path)) {
            instructions = stream.toList();
        }
    }
    
    private static void solveA() {
        cycles.add(1);
        for (var s: instructions) {
            var ar = s.split(" ");
            var old = cycles.get(cycles.size() - 1);
            for (int i = 1; i < clocktics.get(ar[0]); i++) cycles.add(old);
            cycles.add(old + (ar.length > 1 ? Integer.parseInt(ar[1]) : 0));
        }
        int sum = 0;
        for (int i = 20; i < 221; i += 40) {
            sum = sum + cycles.get(i - 1) * i;
        }
        System.out.println("A: " + sum);
    }
    
    private static void solveB() {
        String[][] screen = IntStream.range(0, 6)
            .mapToObj(i -> IntStream.range(0, 40).mapToObj(j -> " ").toArray(String[]::new))
            .toArray(String[][]::new)
        ;
        for (int i = 0; i < cycles.size(); i++) {
            int row = i / 40;
            int col = i % 40;
            int mid = cycles.get(i);
            if (col == mid - 1 || col == mid || col == mid + 1) screen[row][col] = "#";
        }
        print(screen);
    }
    
    private static void print(String[][] s) {
        out.println(
            IntStream.range(0, s.length).mapToObj(i ->
            IntStream.range(0, s[i].length).mapToObj(j -> s[i][j]).collect(joining()))
            .collect(joining("\n"))
        );  
    }
}
