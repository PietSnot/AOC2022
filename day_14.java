/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aoc2022;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

import static java.lang.Integer.*;

/**
 *
 * @author Piet
 */
public class day_14 {
    
    private final static int day = 14;
    private final static boolean test = false;
    
    private static int minx;
    private static int miny;
    private static int maxx;
    private static int maxy;
    private static int maxyB;
    
    private static List<String> inputlist;
    private static Map<C, Boolean> map = new HashMap<>();
    private static C start = C.of(500, 0);
    
    public static void main(String... args) throws IOException {
        getInput();
        solveA();
        solveB();
    }
    
    private static void solveA() {
        var teller = 0;
        while(processSandA()) {
            teller++;
        }
//        var result = IntStream.iterate(1, i -> i + 1).filter(i -> !processSand()).findFirst().getAsInt();
//        System.out.println("A: " + (result - 1));
        System.out.println("teller: " + teller);
        var result = map.values().stream().filter(v -> v).count();
        System.out.println("A: " + result);
    }
    
    private static boolean processSandA() {
        var current = C.of(start.x(), start.y());
        var ongoing = true;
        var changed = true;
        while (changed) {
            changed = false;
            if (map.containsKey(changed)) continue;
            var c = C.of(current.x(), current.y() + 1);
            if (!map.containsKey(c)) {
                changed = true;
                current = c;
            }
            else {
                c = C.of(current.x() - 1, current.y() + 1);
                if (!map.containsKey(c)) {
                    changed = true;
                    current = c;
                }
                else {
                    c = C.of(current.x() + 1, current.y() + 1);
                    if (!map.containsKey(c)) {
                        changed = true;
                        current = c;
                    }
                }
            }
            if (current.x() < minx || current.x() > maxx || current.y() < miny || current.y() > maxy) {
                return false;
            }
        }
//        if (current.x() < minx) minx = current.x();
//        if (current.x() > maxx) maxx = current.x();
//        if (current.y() < miny) miny = current.y();
//        if (current.y() > maxy) maxy = current.y();
        map.put(current, true);
//        print();
        return true;
    }
    
    private static void solveB() throws IOException {
        map.clear();
        getInput();
//        print();
//        var teller = 0;
        while (processSandB()) {
//            System.out.println(++teller);
//            print();
        }
//        processSandB();
        print();
//        System.out.println("B: teller = " + teller);
        var result = map.values().stream().filter(v -> v).count();
        System.out.println("result: " + result);
        
    }
    
    private static void getInput() throws IOException {
        var path = AOC2022.getPath(day, test);
        try (var stream = Files.lines(path)) {
            inputlist = stream.toList();
        }
        processInputList();
    }
    
    private static void processInputList() {
        for (var line: inputlist) {
            var pts = line.split(" -> ");
            var points = Arrays.stream(pts).map(C::of).toList();
            for (int i = 1; i < points.size(); i++) {
                inBetween(points.get(i - 1), points.get(i)).stream().forEach(p -> map.put(p, false));
            }
        }
        map.put(start, false);
        minx = map.keySet().stream().mapToInt(c -> c.x()).min().getAsInt();
        maxx = map.keySet().stream().mapToInt(c -> c.x()).max().getAsInt();
        miny = map.keySet().stream().mapToInt(c -> c.y()).min().getAsInt();
        maxy = map.keySet().stream().mapToInt(c -> c.y()).max().getAsInt();
        maxyB = maxy + 2;
    }
    
    private static List<C> inBetween(C start, C end) {
        var result = new ArrayList<C>();
        if (start.x() != end.x() && start.y() != end.y()) {
            throw new RuntimeException("incorrect start- and endpoints");
        }
        if (start.x() != end.x()) {
            for (int c = min(start.x(), end.x()); c <= max(start.x(), end.x()); c++) {
                result.add(C.of(c, end.y()));
            }
        }
        else {
            for (int c = min(start.y(), end.y()); c <= max(start.y(), end.y()); c++) {
                result.add(C.of(start.x(), c));
            }
        }
        return result;
    }
    
    private static void print() {
        System.out.println("*******************************************************************");
        int cols = maxx - minx + 1;
        int rows = maxyB - miny + 1;
        var buf = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_ARGB);
        var color = Color.RED.getRGB();
        var ar = new String[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                var p = C.of(c + minx, r + miny);
                if (!map.containsKey(p)) {
                    ar[r][c] = ".";
//                    buf.setRGB(c, r, color);
                }
                else if (map.get(p)) {
                    ar[r][c] = "o";
                    buf.setRGB(c, r, color);
                }
                else {
                    ar[r][c] = "#";
                    buf.setRGB(c, r, color);
                }
            }
        }
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                System.out.print(ar[r][c]);
            }
            System.out.println();
        }
        var file = new File("D:/AOC2022/src/aoc2022/Resources/uitdraaidag14.png");
        try {
            ImageIO.write(buf, "png", file);
        } catch (IOException ex) {
            System.out.println("kan image niet wegschrijven");
        }
    }
    
    private static boolean processSandB() {
        var current = C.of(start.x(), start.y());
//        if (map.containsKey(current)) return false;
        var ongoing = true;
        var changed = true;
        while (changed) {
            changed = false;
            if (map.containsKey(current) && !current.equals(start)) continue;
            var c = C.of(current.x(), current.y() + 1);
            if (c.y() == maxyB) {
                map.put(c, false);
                map.put(C.of(c.x() - 1, c.y()), false);
                map.put(C.of(c.x() + 1, c.y()), false);
            }
            if (!map.containsKey(c)) {
                changed = true;
                current = c;
            }
            else {
                c = C.of(current.x() - 1, current.y() + 1);
                if (!map.containsKey(c)) {
                    changed = true;
                    current = c;
                }
                else {
                    c = C.of(current.x() + 1, current.y() + 1);
                    if (!map.containsKey(c)) {
                        changed = true;
                        current = c;
                    }
                }
            }
//            if (current.x() < minx || current.x() > maxx || current.y() < miny || current.y() > maxy) {
//                return false;
//            }
        }
        if (current.x() < minx) minx = current.x();
        if (current.x() > maxx) maxx = current.x();
        if (current.y() < miny) miny = current.y();
        if (current.y() > maxy) maxy = current.y();
        map.put(current, true);
//        print();
        return (current.equals(start)) ? false : true;
    }
}

record C(int x, int y) {
    public static C of(int a, int b) {
        return new C(a, b);
    }
    
    public static C of(String s) {
        var x = s.split(",");
        return new C(parseInt(x[0]), parseInt(x[1]));
    }
}

 
