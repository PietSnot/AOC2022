/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aoc2022;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

/**
 *
 * @author Piet
 */
public class day_17 {
    
    private final static int day = 17;
    private static boolean test = false;
    private static boolean printout = false;
    
    private static String input;
    private static List<List<Coordinates>> inputShapes = new ArrayList<>();
    
    public static void main(String... args) throws IOException {
        getInput();
        var delta = 15;
//        for (int x = delta; x <= 2022; x += delta) {
////            System.out.print(x + ":" );
//            solveA(x);
//        }
        solveA(2022);
        solveB(25_000);
     
    }
    
    private static void getInput() throws IOException {
        var path = AOC2022.getPath(day, test);
        try (var scanner = new Scanner(path)) {
            input = scanner.nextLine();
        }
//        inspectInput();
        // vullen van de rocks
        inputShapes.add(List.of(new Coordinates(0,0), new Coordinates(1,0), new Coordinates(2,0), new Coordinates(3,0)));
        inputShapes.add(List.of(new Coordinates(1,0), new Coordinates(0,1), new Coordinates(1,1), new Coordinates(2,1), new Coordinates(1,2)));
        inputShapes.add(List.of(new Coordinates(0,0), new Coordinates(1,0), new Coordinates(2,0), new Coordinates(2,1), new Coordinates(2,2)));
        inputShapes.add(List.of(new Coordinates(0,0), new Coordinates(0,1), new Coordinates(0,2), new Coordinates(0,3)));
        inputShapes.add(List.of(new Coordinates(0,0), new Coordinates(1,0), new Coordinates(0,1), new Coordinates(1,1)));        
    }
    
    private static void inspectInput() {
        var temp = input.repeat(100);
        int start = 15;
        int delta = 35;
        var freqs = IntStream.iterate(start, i -> i < temp.length() - delta, i -> i + delta)
            .mapToObj(i -> temp.substring(i, i + delta))
            .collect(groupingBy(s -> s, counting()))
        ;
        freqs.forEach((k, v) -> System.out.format("%s : %d%n", k, v) );
    }
    
    private static void solveA(int maxRounds) {
        var container = new Container(8, printout);
        for (int round = 0; round < maxRounds; round++) {
            var rots = new Rots(inputShapes.get(round % inputShapes.size()));
            if (printout) System.out.println("nieuw rots");
            var d = input.charAt(round % input.length());
            container.process(rots, input);
//            var flup = container.getContent();
//            var height = container.getHeight();
//            var filled = flup.stream().filter(p -> p.y() == height).count();
//            if (filled == 7L) {
//                System.out.println("filled after round " + round);
//            }
        }
//        System.out.println(container.getHeight());
//        var points = container.getContent();
//        var h = container.getHeight();
//        var result = points.stream()
//            .collect(groupingBy(p -> p.y(), mapping(p -> p.x(), toCollection(TreeSet::new))))
//            .values().stream()
//            .collect(groupingBy(l -> l, counting()))
//        ;
//        result.forEach((k, v) -> System.out.format("%s: %d%n", k, v));
//        
//        ;
        System.out.println(container.getHeight());
    }
    
    private static void solveB(int rounds) {
        var container = new Container(8, printout);
        var heights = new TreeMap<Long, Integer>();
        var rotsheights = new TreeMap<Integer, Long>();
        for (int round = 0; round <= rounds; round++) {
            if (round % 1000 == 0) System.out.println(round);
            var rots = new Rots(inputShapes.get(round % inputShapes.size()));
            container.process(rots, input);
            var h = container.getHeight();
            heights.put(h, round);
            rotsheights.put(round, h);
        }
        var points = container.getContent();
        var fullLines = points.stream()
            .collect(groupingBy(p -> p.y()))
            .entrySet().stream()
            .filter(e -> e.getValue().size() == container.getWidth() - 1)
            .mapToLong(e -> e.getKey())
            .boxed()
            .toList()
        ;
        fullLines.forEach(System.out::println);
        System.out.println("******************************************");
        var h1 = 5459L;
        var h2 = 8130L;
        var h3 = 10801L;
        var h4 = 13472L;
        System.out.println("hoogte en steen: " + heights.floorEntry(h1));
        System.out.println("hoogte en steen: " + heights.floorEntry(h2));
        System.out.println("hoogte en steen: " + heights.floorEntry(h3));
        System.out.println("hoogte en steen: " + heights.floorEntry(h4));
        System.out.println("bestaat 8130?? " + heights.containsKey(h2));
        System.out.println("********************8");
        System.out.println("rots 3460: " + rotsheights.get(3460));
        System.out.println("rots 4194: " + rotsheights.get(4194));
        System.out.println("rots 5155: " + rotsheights.get(5155));
        System.out.println("deltA 4194 en 3460: " + (rotsheights.get(4194) - rotsheights.get(3460)));
        
    }
    
}

record Point17(long x, long y) {
    public final static Comparator<Point17> compareX = Comparator.comparingLong(p -> p.x);
    public final static Comparator<Point17> compareY = Comparator.comparingLong(p -> p.y); 
    
    public Point17 getConcretePoint(long offsetX, long offsetY) {
        return new Point17(x + offsetX, y + offsetY);
    }
}

class Rots {
    private long offsetX = 0;
    private long offsetY = 0;
    List<Point17> content;
    List<Point17> left, up, right, down;
//    private boolean settled = false;
    
    Rots(List<Coordinates> points) {
        content = points.stream().map(c -> new Point17(c.x(), c.y())).toList(); 
        this.left = getLeftOrRight(content, true);
        this.right = getLeftOrRight(content, false); 
        this.up = getUpOrDown(content, true);
        this.down = getUpOrDown(content, false);
    }
    
    public boolean contains(Point17 p) {
        var np = new Point17(p.x() - offsetX, p.y() - offsetY);
        return content.contains(p);
    }
    
    public void setOffset(long dx, long dy) {
        offsetX = dx;
        offsetY = dy;
    }
    
    private List<Point17> getLeftOrRight(List<Point17> input, boolean left) {
        var collector = left ? minBy(Point17.compareX) : maxBy(Point17.compareX);
        return new ArrayList<>(input.stream()
            .collect(groupingBy(p -> p.y(), collectingAndThen(collector, Optional::get)))
            .values()
        );
    }
    
    private List<Point17> getUpOrDown(List<Point17> input, boolean up) {
        var collector = up ? maxBy(Point17.compareY) : minBy(Point17.compareY);
        return new ArrayList<>(input.stream()
            .collect(groupingBy(p -> p.x(), collectingAndThen(collector, Optional::get)))
            .values()
        );
    }
    
    public List<Point17> giveConcretePoints() {
        var result = content.stream().map(p -> p.getConcretePoint(offsetX, offsetY)).toList();
        return result;
    }

    
    public void moveHorizontal(long dx) {
        offsetX += dx;
    }
    
    public void moveVertical(long dy) {
        offsetY += dy;
    }
    
    public boolean canMoveLeft(Container container) {
        var isBlocked = left.stream()
            .map(p -> p.getConcretePoint(offsetX, offsetY))
            .anyMatch(p -> p.x() == 1 || container.contains(new Point17(p.x() - 1, p.y())));
        return !isBlocked;
    }
    
    public boolean canMoveRight(Container container) {
        var isBlocked = right.stream()
            .map(p -> p.getConcretePoint(offsetX, offsetY))
            .anyMatch(p -> p.x() == container.getWidth() - 1 || container.contains(new Point17(p.x() + 1, p.y())));
        return !isBlocked;
    }
    
    public boolean canMoveDown(Container container) {
        var isBlocked = down.stream()
            .map(p -> p.getConcretePoint(offsetX, offsetY))
            .anyMatch(p -> p.y() == 1 || container.contains(new Point17(p.x(), p.y() - 1)))
        ;
        return !isBlocked;
    }
    
    public void setSettled(boolean b) {
//        settled = b;
    }
}

class Container {
    private final int width;
    private long height = 0;
    private final Set<Point17> content = new HashSet<>();
    private int currentLocation = 0;
    private boolean printout;
    
    Container(int width, boolean printout) {
        this.width = width;
        this.printout = printout;
    }
    
    public void resetLocation() {
        currentLocation = 0;
    }
    
    public void addPoints(List<Point17> list) {
        content.addAll(list);
        var h = list.stream().mapToLong(p -> p.y()).max().getAsLong();
        if (h > height) height = h;
        
    }
    
    public long getHeight() {
        return height;
    }
    
    public int getWidth() {
        return width;
    }
    
    public boolean contains(Point17 p) {
        return content.contains(p);
    }
    
    public List<Point17> getContent() {
        return new ArrayList<>(content);
    }
    
    public void process(Rots rots, String directions) {
        long x = 3;
        long y = this.getHeight() + 4;
        rots.setOffset(x, y);
        var canMove = true;
        if (printout) System.out.println("nieuwe rots");
        if (printout) print(rots);
        if (printout) System.out.println("====================");
        while (canMove) {
            var direction = directions.charAt(currentLocation);
            currentLocation = (currentLocation + 1) % directions.length();
            if (direction == '<') {
                if (rots.canMoveLeft(this)) {
                    rots.moveHorizontal(-1);
                    if (printout) System.out.println("direction " + direction);
                    if (printout) print(rots);
                    if (printout) System.out.println("================");
                }
            }
            else if (direction == '>') {
                if (rots.canMoveRight(this)) {
                    rots.moveHorizontal(1);
                    if (printout) System.out.println("direction " + direction);
                    if (printout) print(rots);
                    if (printout) System.out.println("================");
                }
            }
            if (rots.canMoveDown(this)) {
                rots.moveVertical(-1);
                if (printout) System.out.println("direction " + "down");
                if (printout) print(rots);
                if (printout) System.out.println("================");
            }
            else {
                canMove = false;
                addPoints(rots.giveConcretePoints());
                if (printout) System.out.println("klaar");
                if (printout) System.out.println(toString());
                if (printout) System.out.println("================");
            }
        }
    }
    
    public void print(Rots rots) {
        var rotsPoints = rots.giveConcretePoints();
        long height = rotsPoints.stream().mapToLong(p -> p.y()).max().getAsLong();
        for (long row = height; row >= 0; row--) {
            for (long col = 0; col <= width; col++) {
                var p = new Point17(col, row);
                if (row == 0) System.out.print("_");
                else if (col == 0 || col == width) System.out.print("|");
                else if (rotsPoints.contains(p)) System.out.print("@");
                else if (content.contains(p)) System.out.print("#");
                else System.out.print(".");
            }
            System.out.println();
        }
    }
    
    @Override
    public String toString() {
        var result = new StringBuilder();
        long y = this.getHeight();
        for (long row = y; row >= 0; row--) {
            var sb = new StringBuilder();
            for (int x = 0; x <= this.getWidth(); x++) {
                if (row == 0) sb.append("_");
                else if (x == 0 || x == this.getWidth()) sb.append("|");
                else if (content.contains(new Point17(x, row))) sb.append("#");
                else sb.append(".");
            }
            result.append(sb.toString());
            result.append('\n');
        }
        return result.toString();
    }
}

record Coordinates(int x, int y) {}