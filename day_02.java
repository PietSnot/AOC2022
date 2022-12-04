/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aoc2022;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Piet
 */
public class day_02 {
    
    final static int day = 2;
    final static boolean test = false;
    private static List<String> input;
    private static Map<String, Scores> scores;
    
    public static void main(String... args) throws IOException {
        vulScores();
        getInput();
        System.out.println("A: " + solve(true));
        System.out.println("B: " + solve(false));
    }
    
    private static void vulScores() {
        scores = Map.of(
            "A X", new Scores(4, 3), "A Y", new Scores(8, 4), "A Z", new Scores(3, 8),
            "B X", new Scores(1, 1), "B Y", new Scores(5, 5), "B Z", new Scores(9, 9),
            "C X", new Scores(7, 2), "C Y", new Scores(2, 6), "C Z", new Scores(6, 7)
        );
    }

    private static void getInput() throws IOException {
        var path = AOC2022.getPath(day, test);
        try (var stream = Files.lines(path)) {
            input = stream.toList();
        }
    }
    
    private static int solve(boolean partOne) {
        var result = input.stream()
            .mapToInt(s -> scores.get(s).getScore(partOne))
            .sum()
        ;
        return result;
    }
}

record Scores(int firstPart, int secondPart) {
    public int getScore(boolean first) {
        return first ? firstPart : secondPart;
    }
}