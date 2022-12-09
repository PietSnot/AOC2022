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

import static java.lang.Math.abs;

/**
 *
 * @author Piet
 */
public class day_09 {
    
    private final static int day = 9;
    private final static boolean test = false;
    
    private static List<String> input;
    
    public static void main(String... args) throws IOException {
        getInput();
        solveA();
        solveB();
    }
    
    private static void getInput() throws IOException {
        var path = AOC2022.getPath(day, test);
        try (var stream = Files.lines(path)) {
            input = stream.toList();
        }
    }
    
    private static void solveA() {
        var head = new Head(0, 0);
        var tail = new Tail(0, 0);
        for (var s: input) {
//            System.out.println("processing: " + s);
            var ar = s.split(" ");
            for (int i = 1; i <= Integer.parseInt(ar[1]); i++) {
                head.adjustPosition(ar[0]);
                tail.adjustPosition(head.getCurrentPosition());
//                System.out.println(head.getCurrentPosition());
//                System.out.println(tail.getCurrentPosition());
//                System.out.println("***********************");
            }
        }
        var result = tail.getDifferentPositions();
        System.out.println("A: " + result);
    }
    
    private static void solveB() {
        var head = new Head(0, 0);
        var knots = IntStream.range(0, 9).mapToObj(i -> new Tail(0, 0)).toList();
        for (var s: input) {
            System.out.println("processing: " + s);
            var ar = s.split(" ");
            for (int teller = 1; teller <= Integer.parseInt(ar[1]); teller++) {
                head.adjustPosition(ar[0]);
                for (int i = 0; i < knots.size(); i++) {
                    var curPos = i == 0 ? head.getCurrentPosition() : knots.get(i - 1).getCurrentPosition();
                    knots.get(i).adjustPosition(curPos);
                }
//                System.out.println(head.getCurrentPosition());
//                for (int j = 0; j < knots.size(); j++) {
//                    System.out.println("" + (j + 1) + ": " + knots.get(j).getCurrentPosition());
//                }
//                System.out.println("***********************************");
            }
//            System.out.println(head.getCurrentPosition());
//            for (int i = 0; i < knots.size(); i++) {
//                System.out.println("" + (i + 1) + ": " + knots.get(i).getCurrentPosition());
//            }
//            System.out.println("***********************************");
//            System.out.println("***********************************");
        }
        var result = knots.get(knots.size() - 1).getDifferentPositions();
        System.out.println("B: " + result);
    }
}

record Position(int x, int y) {
    public Position adjust(Position p) {
        return new Position(x + p.x, y + p.y);
    }
    
    public Position adjust(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }
}

class Head {
    private final List<Position> positions = new ArrayList<>();
    
    Head(int x, int y) {
        positions.add(new Position(x, y));
    }
    
    public void adjustPosition(String direction) {
        var curPos = positions.get(positions.size() - 1);
        var dpos = Direction.delta(direction);
        positions.add(curPos.adjust(dpos));
    }
    
    public Position getCurrentPosition() {
        return positions.get(positions.size() - 1);
    }
}

class Tail {
    private final List<Position> positions = new ArrayList<>();
    
    Tail(int x, int y) {
        positions.add(new Position(x, y));
    }
    
    public Position getCurrentPosition() {
        return positions.get(positions.size() - 1);
    }
    
    public void adjustPosition(Position p) {
        var curPos = getCurrentPosition();
        int dx = p.x() - curPos.x();
        int dy = p.y() - curPos.y();
        if (abs(dx) <= 1 && abs(dy) <= 1) return;
        Position newP;
        if (abs(dx) == 2 && abs(dy) == 2) newP = new Position(curPos.x() + dx / 2, curPos.y() + dy / 2);
        else if (abs(dx) == 2) newP = new Position(curPos.x() + dx / 2, curPos.y() + dy);
        else if (abs(dy) == 2) newP = new Position(curPos.x() + dx, curPos.y() + dy / 2);
        else throw new RuntimeException("unexpected: dx = " + dx + " dy = " + dy);
        positions.add(newP);
    }
    
    public long getDifferentPositions() {
        return positions.stream().distinct().count();
    }
}

enum Direction {
    R (1, 0), D (0, -1), L (-1, 0), U(0, 1);
    
    private int dx;
    private int dy;
    
    Direction(int x, int y) {
        dx = x;
        dy = y;
    }
    
    public static Position delta(String s) {
        var dir = Direction.valueOf(s);
        return new Position(dir.dx, dir.dy);
    }
}
