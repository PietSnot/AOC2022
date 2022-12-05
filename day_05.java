/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aoc2022;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.joining;

/**
 *
 * @author Piet
 */
public class day_05 {
    
    private static int day = 5;
    private static boolean test = false;
    private static List<Move> moves;
    private static List<LinkedList<String>> stapels = new ArrayList<>();
    private static LinkedList<String> temp = new LinkedList<>();
    
    public static void main(String... args) throws IOException {
        getMoves();
        solveA();
        solveB();
    }
    
    private static void getMoves() throws IOException {
        var path = AOC2022.getPath(day, test);
        try(var stream = Files.lines(path)) {
            moves = stream.map(day_05::processLine).toList();
        }
    }
    
    private static void getStapels() {
        if (test) getStapelsTest();
        else getStapelsEcht();
    }
    
    private static void getStapelsTest() {
        stapels.clear();
        // dummy
        stapels.add(new LinkedList<>());
        stapels.add(new LinkedList<>(List.of("Z", "N")));
        stapels.add(new LinkedList<>(List.of("M", "C", "D")));
        stapels.add(new LinkedList<>(List.of("P")));
    }
    
    private static void getStapelsEcht() {
        stapels.clear();
        // dummy
        stapels.add(new LinkedList<>());    //   dummy
        stapels.add(new LinkedList<>(List.of("D", "T", "W", "F", "J", "S", "H", "N")));    //   1
        stapels.add(new LinkedList<>(List.of("H", "R", "P", "Q", "T", "N", "B", "G")));    //   2
        stapels.add(new LinkedList<>(List.of("L", "Q", "V")));                             //   3
        stapels.add(new LinkedList<>(List.of("N", "B", "S", "W", "R", "Q")));              //   4
        stapels.add(new LinkedList<>(List.of("N","D","F","T","V","M","B")));               //   5
        stapels.add(new LinkedList<>(List.of("M","D","B","V","H","T","R")));               //   6
        stapels.add(new LinkedList<>(List.of("D","B","Q","J")));                           //   7
        stapels.add(new LinkedList<>(List.of("D","N","J","V","R","Z","H","Q")));           //   8
        stapels.add(new LinkedList<>(List.of("B","N","H","M","S")));                       //   9     
    }
    
    private static void solveA() throws IOException {
        getStapels();
        moves.stream().forEach(day_05::processMoveA);
        var result = stapels.stream().skip(1).map(list -> list.getLast()).collect(joining());
        System.out.println("oplossing A: " + result);
    }
    
    private static void solveB() throws IOException {
        getStapels();
        moves.stream().forEach(day_05::processMoveB);
        var result = stapels.stream().skip(1).map(list -> list.getLast()).collect(joining());
        System.out.println("oplossing B: " + result);
    }
    
    private static void processMoveA(Move move) {
        for (int i = 1; i <= move.size(); i++) {
            stapels.get(move.to()).addLast(stapels.get(move.from()).removeLast());
        }
    }
    
    private static void processMoveB(Move move) {
        temp.clear();
        for (int i = 1; i <= move.size(); i++) {
            temp.addLast(stapels.get(move.from()).removeLast());
        }
        for (int i = 1; i <= move.size(); i++) {
            stapels.get(move.to()).addLast(temp.removeLast());
        }
    }
    
    private static Move processLine(String s) {
        var x = s.split(" ");
        return new Move(parseInt(x[1]), parseInt(x[3]), parseInt(x[5]));
    }
    
    
    
}

record Move(int size, int from, int to) {}
