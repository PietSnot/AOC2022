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

/**
 *
 * @author Piet
 */
public class day_12 {
    
    private final static int day = 12;
    private final static boolean test = false;
    
    private static int[][] input;
    private static Coord start;
    private static Coord end;
    private static LinkedList<Coord> queue = new LinkedList<>();
    private static boolean[][] visited;
    
    public static void main(String... args) throws IOException {
        getInput();
        var c = solveA();
        System.out.println("A: " + c);
        solveB();
    }
    
    private static void getInput() throws IOException {
        var path = AOC2022.getPath(day, test);
        try (var stream = Files.lines(path)) {
            input = stream.map(row -> row.chars().toArray()).toArray(int[][]::new);
            for (int r = 0; r < input.length; r++) {
                for (int c = 0; c < input[0].length; c++) {
                    if (input[r][c] == 'S') start = new Coord(r, c, 0);
                    if (input[r][c] == 'E') end = new Coord(r, c, 0);
                }
            }
            if (start == null || end == null) {
                throw new RuntimeException("Missing start and/or end");
            }
        }
        input[start.row()][start.col()] = 'a';
        input[end.row()][end.col()] = 'z';
        visited = new boolean[input.length][input[0].length];
    }
    
    private static Coord solveA() {
        queue.addFirst(start);
        visited[start.row()][start.col()] = true;
        loop:
        while (!queue.isEmpty()) {
            var head = queue.removeFirst();
            var neighbors = getNeighbors(head);
            for (var c: neighbors) {
                if (c.row() == end.row() && c.col() == end.col()) return c;
                queue.addLast(c);
                visited[c.row()][c.col()] = true;
            }
        }
        return null;
    }
    
    private static void solveB() {
        var startpoints = new ArrayList<Coord>();
        var map = new HashMap<Coord, Coord>();
        for (int r = 0; r < input.length; r++) {
            for (int c = 0; c < input[r].length; c++) {
                if (input[r][c] == 'a') startpoints.add(new Coord(r, c, 0));
            }
        }
        var result = new ArrayList<Coord>();
        for (var startpoint: startpoints) {
            clearVisited();
            queue.clear();
            start = startpoint;
            var x = solveA();
            map.put(startpoint, x);
            result.add(x);
        }
        var B = result.stream().filter(c -> c != null).mapToInt(c -> c.level()).min().getAsInt();
        System.out.println("B: " + B);
    }
    
    private static void clearVisited() {
        visited = new boolean[input.length][input[0].length];
    }
    
    private static List<Coord> getNeighbors(Coord coor) {
        var result = new ArrayList<Coord>();
        int row = coor.row();
        int col = coor.col();
        int level = coor.level() + 1;
        int val = input[row][col] + 1;
        int r = row - 1; int c = col;
        if (r >= 0 && !visited[r][c] && input[r][c] <= val) result.add(new Coord(r, c, level));
        r = row + 1; c = col;
        if (r < input.length && !visited[r][c] && input[r][c] <= val) result.add(new Coord(r, c, level));
        r = row; c = col - 1;
        if (c >= 0 && !visited[r][c] && input[r][c] <= val) result.add(new Coord(r, c, level));
        r = row; c = col + 1;
        if (c < input[r].length && !visited[r][c] && input[r][c] <= val) result.add(new Coord(r, c, level));
//        System.out.println(coor + ": " + result);
        return result;
    }
}

record Coord(int row, int col, int level) {}
