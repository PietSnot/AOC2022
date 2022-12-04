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
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */
public class day_03 {
    
    final private static int day = 3;
    final private static boolean test = false;
    private static List<List<Integer>> input = new ArrayList<>();
    
    public static void main(String... args) throws IOException {
        getInput();
        solveA();
        solveB();
    }
    
    private static void getInput() throws IOException {
        var path = AOC2022.getPath(day, test);
        try( var stream = Files.lines(path)) {
            input = stream
                .map(s -> s.chars().map(day_03::convertChar).boxed().toList())
                .toList()
            ;
        }
    }
    
    private static int convertChar(int c) {
        return c > 'Z' ? c - 'a' + 1 : c - 'A' + 27;
    }
    
    private static void solveA() {
        var result = input.stream().mapToInt(day_03::getValueOfCommonCharS).sum();
        System.out.println("A: " + result);
    }
    
    private static void solveB() {
        var result = IntStream.iterate(0, i -> i < input.size(), i -> i + 3)
            .map(i -> getValueOfCommonChars(i, i + 1, i + 2))
            .sum()
        ;
        System.out.println("B: " + result);
    }
    
    private static int getValueOfCommonCharS(List<Integer> list) {
        var firstHalve = new HashSet<>(list.subList(0, list.size() / 2));
        var secondHalve = list.subList(list.size() / 2, list.size());
        firstHalve.retainAll(secondHalve);
        return firstHalve.stream().mapToInt(i -> i).sum();
    }
    
    private static int getValueOfCommonChars(int index1, int index2, int index3) {
        var first = new HashSet<>(input.get(index1));
        first.retainAll(input.get(index2));
        first.retainAll(input.get(index3));
        return first.stream().mapToInt(i -> i).sum();
    }
}
