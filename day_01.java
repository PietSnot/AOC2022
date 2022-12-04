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

/**
 *
 * @author Piet
 */
public class day_01 {
    
    final static int day = 1;
    final static boolean test = false;
    
    public static void main(String... args) {
        solve();
    }
    
    private static void solve() {
        var path = AOC2022.getPath(1, test);
        var elves = new ArrayList<Elve>();
        
        try (var buf = Files.newBufferedReader(path)) {
            var currentElve = new Elve();
            String line;
            while ((line = buf.readLine()) != null) {
                if (line.isEmpty()) {
                    elves.add(currentElve);
                    currentElve = new Elve();
                }
                else {
                    currentElve.addCalories(Integer.parseInt(line));
                }
            }
            if (line == null || !line.isEmpty()) elves.add(currentElve);
        } 
        catch (IOException ex) {
            System.out.println("IO Exception!");
        }
        var sorted = elves.stream()
            .mapToInt(e -> e.getSumOfCalories())
            .boxed()
            .sorted(Comparator.reverseOrder())
            .toList()
        ;
        var resultA = sorted.stream().limit(1).mapToInt(i -> i).sum();
        var resultB = sorted.stream().limit(3).mapToInt(i -> i).sum();
        System.out.println("A: " + resultA);
        System.out.println("A: " + resultB);
    }
}

class Elve {
    private static int idNumber = 0;
    
    private int id;
    private List<Integer> calories = new ArrayList<>();
    
    Elve() {
        id = idNumber++;
    }
    
    public void addCalories(int c) {
        calories.add(c);
    }
    
    public int getSumOfCalories() {
        return calories.stream().mapToInt(i -> i).sum();
    }
}
