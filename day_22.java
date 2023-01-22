/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aoc2022;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import static aoc2022.Direction.*;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toCollection;

/**
 *
 * @author Piet
 */
public class day_22 {
    
    private final static int day = 22;
    private final static boolean test = false;
    
    private static final List<List<Tile>> tilesList = new ArrayList<>();
    private static List<String> commands;
    
    private static final Map<NumberDirection, NumberDirection> partA = new HashMap<>();
    private static final Map<NumberDirection, NumberDirection> partB = new HashMap<>();
     
    //--------------------------------------------------------------------------
    public static void main(String... args) throws IOException {
        getInput();
        solveA();
    }
    
    //--------------------------------------------------------------------------
    private static void solveA() {
        var curDir = Direction22.RIGHT;
        var curPos = new Position22(0, tilesList.get(0).indexOf(Tile.OPEN));
        for (var c: commands) {
            if (c.equals("R") || c.equals("L")) curDir = curDir.rotate(c);
            else curPos = move(curPos, curDir, parseInt(c));
        }
        var result = 1000 * (curPos.row() + 1) + 4 * (curPos.col() + 1) + curDir.getValue();
        System.out.println("A: " + result);
    }
    
    //--------------------------------------------------------------------------
    public static void getInput() throws IOException {
        var maxlength = 0;
        var path = AOC2022.getPath(day, test);
        try (var scanner = new Scanner(path)) {
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();
                if (line.length() > maxlength) maxlength = line.length();
                if (line.isEmpty()) break;
                var tiles = line.chars().mapToObj(i -> Tile.translate((char) i)).collect(toCollection(ArrayList::new));
                tilesList.add(tiles);
            }
            // make all lines equal in length
            for (var tiles: tilesList) {
                if (tiles.size() < maxlength) {
                    var temp = maxlength - tiles.size();
                    for (int i = 1; i <= temp; i++) {
                        tiles.add(Tile.NOTHING);
                    }
                }
            }
            var line = scanner.nextLine();
            var regex = "\\d++|[LR]";
            var pattern = Pattern.compile(regex);
            var matcher = pattern.matcher(line);
            commands = matcher.results()
                .map(m -> m.group())
                .collect(toCollection(ArrayList::new))
            ;
        }
        
        fillTheNumberDirectionMaps();
    }
    
    private static void fillTheNumberDirectionMaps() {
        var oneR = new NumberDirection(1, R);
        var oneL = new NumberDirection(1, L);
        var oneU = new NumberDirection(1, U);
        var oneD = new NumberDirection(1, D);
        var twoR = new NumberDirection(2, R);
        var twoL = new NumberDirection(2, L);
        var twoU = new NumberDirection(2, U);
        var twoD = new NumberDirection(2, D);
        var fourR = new NumberDirection(4, R);
        var fourL  = new NumberDirection(4, L);
        var fourU = new NumberDirection(4, U);
        var fourD = new NumberDirection(4, D);
        var sixR = new NumberDirection(6, R);
        var sixL = new NumberDirection(6, L);
        var sixU = new NumberDirection(6, U);
        var sixD = new NumberDirection(6, D);
        var sevenR = new NumberDirection(7, R);
        var sevenL = new NumberDirection(7, L);
        var sevenU = new NumberDirection(7, U);
        var sevenD = new NumberDirection(7, D);
        var nineR = new NumberDirection(9, R);
        var nineL = new NumberDirection(9, L);
        var nineU = new NumberDirection(9, U);
        var nineD = new NumberDirection(9, D);
        
        partB.put(oneR, twoR);
        partB.put(oneL, sixR);
        partB.put(oneU, nineR);
        partB.put(oneD, fourD);
        partB.put(twoR, sevenL);
        partB.put(twoL, oneL);
        partB.put(twoU, nineU);
        partB.put(twoD,fourL);
        partB.put(fourR, twoU);
        partB.put(fourL, sixD);
        partB.put(fourU, oneU);
        partB.put(fourD, sevenD);
        partB.put(sixR, sevenR);
        partB.put(sixL, oneR);
        partB.put(sixU, fourR);
        partB.put(sixD, nineD);
        partB.put(sevenR, twoL);
        partB.put(sevenL, sixL);
        partB.put(sevenU, fourU);
        partB.put(sevenD, nineL);
        partB.put(nineR, sevenU);
        partB.put(nineL, oneD);
        partB.put(nineU, sixU);
        partB.put(nineD, twoD);
    }
    
    //--------------------------------------------------------------------------
    private static Position22 move(Position22 c, Direction22 d, int hoeveel) {
        if (d == d.UP) return moveUp(c, hoeveel);
        else if (d ==  d.RIGHT) return moveRight(c, hoeveel);
        else if (d == d.DOWN) return moveDown(c, hoeveel);
        else if (d == d.LEFT) return moveLeft(c, hoeveel);
        else throw new RuntimeException("onbekend commando");
    }
    
    //--------------------------------------------------------------------------
    private static Position22 moveUp(Position22 c, int hoeveel) {
        int start = startOfBlock(c);
        int end = endOfBlock(c);
        int line = c.row();
        for (int h = 1; h <= hoeveel; h++) {
            line--;
            if (line < start) line = end;
            if (tilesList.get(line).get(c.col()) == Tile.WALL) {
                if (line == end) line = start;
                else line++;
                break;
            }
        }
        return new Position22(line, c.col());
    }
    
    //--------------------------------------------------------------------------
    private static Position22 moveDown(Position22 c, int hoeveel) {
        int start = startOfBlock(c);
        int end = endOfBlock(c);
        int line = c.row();
        for (int h = 1; h <= hoeveel; h++) {
            line++;
            if (line > end) line = start;
            if (tilesList.get(line).get(c.col()) == Tile.WALL) {
                if (line == start) line = end;
                else line--;
                break;
            }
        }
        return new Position22(line, c.col());
    }
    
    //--------------------------------------------------------------------------
    private static Position22 moveRight(Position22 c, int hoeveel) {
        var line = tilesList.get(c.row());
        int start = startOfLine(c);
        int end = endOfLine(c);
        int col = c.col();
        for (int h = 1; h <= hoeveel; h++) {
            col++;
            if (col > end) col = start;
            if (line.get(col) == Tile.WALL) {
                if (col == start) col = end;
                else col--;
                break;
            }
        }
        return new Position22(c.row(), col);
    }
    
    //--------------------------------------------------------------------------
    private static Position22 moveLeft(Position22 c, int hoeveel) {
        var line = tilesList.get(c.row());
        int start = startOfLine(c);
        int end = endOfLine(c);
        int col = c.col();
        for (int h = 1; h <= hoeveel; h++) {
            col--;
            if (col < start) col = end;
            if (line.get(col) == Tile.WALL) {
                if (col == end) col = start;
                else col++;
                break;
            }
        }
        return new Position22(c.row(), col);
    }    
    
    //--------------------------------------------------------------------------
    private static int startOfBlock(Position22 p) {
//        var result = p.row();
//        while (result > 0 && tilesList.get(result - 1).get(p.col()) != Tile.NOTHING) result--;
//        return result;
        int result;
        for (result = p.row(); result > 0 && tilesList.get(result - 1).get(p.col()) != Tile.NOTHING; result--);
        return result;
    }
    
    //--------------------------------------------------------------------------
    private static int endOfBlock(Position22 p) {
        var result = p.row();
        while (result < tilesList.size() - 1 && tilesList.get(result + 1).get(p.col()) != Tile.NOTHING) result++;
        return result;
    }
    
    //--------------------------------------------------------------------------
    private static int startOfLine(Position22 p) {
        var line = tilesList.get(p.row());
        var result = p.col();
        while (result > 0 && line.get(result - 1) != Tile.NOTHING) result--;
        return result;
    }
    
    //--------------------------------------------------------------------------
    private static int endOfLine(Position22 p) {
        var line = tilesList.get(p.row());
        var result = p.col();
        while (result < line.size() - 1 && line.get(result + 1) != Tile.NOTHING) result++;
        return result;
    }
}

//******************************************************************************
enum Tile {
    NOTHING, OPEN, WALL;
    
    //--------------------------------------------------------------------------
    public static Tile translate(char s) {
        switch(s) {
            case ' ' : return NOTHING;
            case '.' : return OPEN;
            case '#' : return WALL;
            default: throw new RuntimeException("Illegal tile!!!!");
        }
    }
}

//******************************************************************************
enum Direction22 {
    
    UP (3), RIGHT (0), DOWN (1), LEFT (2);
    
    final int value;
    final static int length = Direction22.values().length;
    
    //--------------------------------------------------------------------------
    Direction22(int val) {
        value = val;
    }
    
    //--------------------------------------------------------------------------
    public Direction22 rotate(String to) {
        var ordinal = this.ordinal();
        var nieuw = to.equals("R") ? ordinal + 1 : ordinal - 1;
        nieuw = nieuw < 0 ? nieuw + length : nieuw % length;
        return Direction22.values()[nieuw];
    }
    
    public int getValue() {
        return value;
    }
}

//******************************************************************************
record Position22(int row, int col) {}

//******************************************************************************
record NumberDirection(int square, Direction d) {}

