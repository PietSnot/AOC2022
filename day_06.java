/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aoc2022;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */
public class day_06 {
    
    final static int day = 6;
    final static boolean test = false;
    
    private static List<Integer> input;
    
    public static void main(String... args) throws IOException {
        getInput();
        solve(4);
        solve(14);
    }
    
    private static void getInput() throws IOException {
        var path = AOC2022.getPath(day, test);
        try(var stream = Files.lines(path)) {
            input = stream.limit(1).map(s -> s.chars().mapToObj(c -> c).toList()).findFirst().get();
        }
    }
    
    private static void solve(int lengthToReach) {
        var test = new TestUniqueLength(lengthToReach);
        var result = IntStream.range(0, input.size())
            .filter(i -> test.addInt(input.get(i)))
            .findFirst()
            .getAsInt()
            + 1
        ;
        System.out.println("result: " + result);
    }
}

class TestUniqueLength {
    
    private List<Integer> list = new ArrayList<>();
    private final int lengthToReach;
    
    TestUniqueLength(int length) {
        lengthToReach = length;
    }
    
    public boolean addInt(int c) {
        int x = list.indexOf(c);
        if (x == -1) {
            list.add(c);
            return list.size() == lengthToReach;
        }
        list = list.subList(x + 1, list.size());
        list.add(c);
        return false;
    }
}
