/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aoc2022;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import static java.lang.Long.parseLong;
import static java.lang.Math.abs;

/**
 *
 * @author Piet
 */
public class day_18 {
    
    private final static int day = 18;
    private final static boolean test = false;
    
    private static List<Cube> cubes = new ArrayList<>(); 
    
    public static void main(String... args) throws IOException {
        getInput();
        var resultA = solveA();
        System.out.println("A: " + resultA);
        var resultB = solveB();
        System.out.println("B: " + resultB);
    }
    
    private static void getInput() throws IOException {
        var path = AOC2022.getPath(day, test);
        try (var scanner = new Scanner(path)) {
            var set = new HashSet<Cube>();
            while (scanner.hasNextLine()) {
                var ar = scanner.nextLine().split(",");
                set.add(new Cube(parseLong(ar[0]), parseLong(ar[1]), parseLong(ar[2])));
            }
            cubes.addAll(set);
        }
    }
    
    private static long solveA() {
        long totalTouches = 0;
        for (int i = 0; i < cubes.size() - 1; i++) {
            for (int j = i + 1; j < cubes.size(); j++) {
                if (cubes.get(i).touches(cubes.get(j))) totalTouches += 2;
            }
        }
        return cubes.size() * 6 - totalTouches;
    }
    
    private static long solveB() {
        var water = getWater();
        // get minima
        var minima = getMinXYZ(water);
        var maxima = getMaxXYZ(water);
        long minx = minima.x() - 2;
        long maxx = maxima.x() + 2;
        long miny = minima.y() - 2;
        long maxy = maxima.y() + 2;
        long minz = minima.z() - 2;
        long maxz = maxima.z() + 2;
        long total = (maxx - minx + 1) * (maxy - miny + 1) * (maxz - minz + 1); 
        // floodfilling
        var queue = new LinkedList<Cube>();
        var ccc = new Cube(minima.x() + 1, minima.y() + 1, minima.z() + 1);
        if (water.contains(ccc) || cubes.contains(ccc)) {
            throw new RuntimeException("wrong starting point");
        }
        queue.add(ccc);
        water.add(ccc);
        while (!queue.isEmpty()) {
            var head = queue.removeFirst();
            var neighbors = head.getNeighbors();
            for (var n: neighbors) {
                if (!(water.contains(n) || cubes.contains(n))) {
                    queue.add(n);
                    water.add(n);
                }
            }
            System.out.println("queue: " + queue.size());
        }
        long touchedByWater = 0;
        for (var c: cubes) {
            var neighbors = c.getNeighbors();
            for (var n: neighbors) {
                if (water.contains(n)) {
                    touchedByWater++;
                }
            }
        }
        return touchedByWater;
    }
    
    private static Set<Cube> getWater() {
        var result = new HashSet<Cube>();
        var minima = getMinXYZ(cubes);
        var maxima = getMaxXYZ(cubes);
        for (long x = minima.x() - 2; x <= maxima.x() + 2; x++) {
            for (long z = minima.z() - 2; z <= maxima.z() + 2; z++) {
                result.add(new Cube(x, minima.y() - 2, z));
                result.add(new Cube(x, maxima.y() + 2, z));
            }
        }
        for (long y = minima.y() - 2; y <= maxima.y() + 2; y++) {
            for (long z = minima.z() - 2; z <= maxima.z() + 2; z++) {
                result.add(new Cube(minima.x() - 2, y, z));
                result.add(new Cube(maxima.x() + 2, y, z));
            }
        }
        for (long x = minima.x() - 2; x <= maxima.x() + 2; x++) {
            for (long y = minima.y() - 2; y <= maxima.y() + 2; y++) {
                result.add(new Cube(x, y, minima.z() - 2));
                result.add(new Cube(x, y, maxima.z() + 2));
            }
        }
        return result;
    }
    
    private static Point getMinXYZ(Collection<Cube> col) {
        long xmin = Long.MAX_VALUE;
        long ymin = Long.MAX_VALUE;
        long zmin = Long.MAX_VALUE;
        for (var c: col) {
            if (c.x() < xmin) xmin = c.x();
            if (c.y() < ymin) ymin = c.y();
            if (c.z() < zmin) zmin = c.z();
        }
        return new Point(xmin, ymin, zmin);
    }
    
    private static Point getMaxXYZ(Collection<Cube> col) {
        long xmax = Long.MIN_VALUE;
        long ymax = Long.MIN_VALUE;
        long zmax = Long.MIN_VALUE;
        for (var c: col) {
            if (c.x() > xmax) xmax = c.x();
            if (c.y() > ymax) ymax = c.y();
            if (c.z() > zmax) zmax = c.z();
        }
        return new Point(xmax, ymax, zmax);
    }
}

record Cube(long x, long y, long z) {
    
    public boolean touches(Cube c) {
        return touches(c.x, c.y, c.z);
    }
    
    public boolean touches(long a, long b, long c) {
        return 
            ((abs(x - a) == 1) && (y == b) && (z == c)) ||
            ((x == a) && (abs(y - b) == 1) && (z == c)) ||
            ((x == a) && (y == b) && (abs(z - c) == 1))
        ;
    }
    
    public List<Cube> getNeighbors() {
        var result = new ArrayList<Cube>();
        result.add(new Cube(x + 1, y    , z    ));
        result.add(new Cube(x - 1, y    , z    ));
        result.add(new Cube(x     ,y + 1, z    ));
        result.add(new Cube(x     ,y - 1, z    ));
        result.add(new Cube(x     ,y    , z + 1));
        result.add(new Cube(x     ,y    , z - 1));
        return result;
    }
}

record Point(long x, long y, long z) {}
