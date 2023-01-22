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
import java.util.stream.IntStream;

import static aoc2022.Direction22B.*;
import static java.util.stream.Collectors.toCollection;

/**
 *
 * @author Piet
 */
public class day_22B {
    
    private static final int day = 22;
    private static final boolean test = true;
    
    private static List<int[]> input = new ArrayList<>();
    private static int[][][] sides = new int[12][][];
    private static Map<SideDirection, SideDirection> cube = new HashMap<>();
    private static int rows;
    private static int cols;
    private static int horizontal;
    private static int vertical;
    private final static int OPEN = '.';
    private final static int WALL = '#';
    private final static int NOTHING = ' '; 
    private static List<String> commands;
    
    public static void main(String... args) throws IOException {
        getInput();
        if (test) processTest();
        else processEcht();
    }
    
    private static void getInput() throws IOException {
        var path = AOC2022.getPath(day, test);
        try (var scanner = new Scanner(path)) {
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();
                if (line.isEmpty()) break;
                input.add(line.codePoints().toArray());
            }
            var regex = "\\d++|[LR]";
            var pattern = Pattern.compile(regex);
            var matcher = pattern.matcher(scanner.nextLine());
            commands = matcher.results()
                .map(m -> m.group())
                .collect(toCollection(ArrayList::new))
            ;
        }
    }
    
    private static void solveB() {
        int currentSide = IntStream.range(0, sides.length).filter(i -> sides[i] != null).findFirst().getAsInt();
        var currentDirection = Direction22B.RIGHT;
        int currentRow = 0;
        int currentCol = 0;
        
        for (var command: commands) {
            if ("LR".contains(command)) {
                currentDirection.rotate(command);
                continue;
            }
            // we gaan bewegen!
            int hoeveel = Integer.parseInt(command);
            for (int i = 1; i <= hoeveel; i++) {
                if (currentDirection == LEFT) {
                    if (currentCol == 0) {
                        var sD = cube.get(new SideDirection(currentSide, currentDirection));
                        var pos = fromDirToDir(currentDirection, sD.d(), currentRow);
                        if (sides[sD.square()][pos.row()][pos.col()] == '#') break;
                        currentDirection = sD.d();
                        currentSide = sD.square();
                        currentRow = pos.row();
                        currentCol = pos.col();
                    }
                    else {
                        if (sides[currentSide][currentRow][currentCol - 1] == '#') break;
                        currentCol--;
                    }
                }
                else if (currentDirection == UP) {
                    if (currentRow == 0) {
                        var sD = cube.get(new SideDirection(currentSide, currentDirection));
                        var pos = fromDirToDir(currentDirection, sD.d(), currentCol);
                        if (sides[sD.square()][pos.row()][pos.col()] == '#') break;
                        currentDirection = sD.d();
                        currentSide = sD.square();
                        currentRow = pos.row();
                        currentCol = pos.col();
                    }
                    else {
                        if (sides[currentSide][currentRow - 1][currentCol] == '#') break;
                        currentRow--;
                    }
                }
                else if (currentDirection == RIGHT) {
                    if (currentCol == horizontal - 1) {
                        var sD = cube.get(new SideDirection(currentSide, currentDirection));
                        var pos = fromDirToDir(currentDirection, sD.d(), currentRow);
                        if (sides[sD.square()][pos.row()][pos.col()] == '#') break;
                        currentDirection = sD.d();
                        currentSide = sD.square();
                        currentRow = pos.row();
                        currentCol = pos.col();
                    }
                    else {
                        if (sides[currentSide][currentRow][currentCol + 1] == '#') break;
                        currentCol++;
                    }
                }
                else if (currentDirection == DOWN) {
                    if (currentRow == vertical - 1) {
                        var sD = cube.get(new SideDirection(currentSide, currentDirection));
                        var pos = fromDirToDir(currentDirection, sD.d(), currentCol);
                        if (sides[sD.square()][pos.row()][pos.col()] == '#') break;
                        currentDirection = sD.d();
                        currentSide = sD.square();
                        currentRow = pos.row();
                        currentCol = pos.col();
                    }
                    else {
                        if (sides[currentSide][currentRow - 1][currentCol] == '#') break;
                        currentCol++;
                    }
                }
            }
        }
        
        var pos = translateToInput(currentSide, currentRow, currentCol);
        var result = 1000 * (pos.row() + 1) + 4 * (pos.col() + 1) + currentDirection.value;
    }
    
    private static void processTest() {
        rows = 3;
        cols = 4;
        vertical = input.size() / rows;
        int horizontal = input.stream().mapToInt(arr -> arr.length).max().getAsInt() / cols;
        for (int i = 0; i < sides.length; i++) sides[i] = null;
        sides[2] = getArray(2, vertical, horizontal);
        sides[4] = getArray(4, vertical, horizontal);
        sides[5] = getArray(5, vertical, horizontal);
        sides[6] = getArray(6, vertical, horizontal);
        sides[10] = getArray(10, vertical, horizontal);
        sides[11] = getArray(11, vertical, horizontal);
        
        cube.clear();
        cube.put(new SideDirection(2, UP)    , new SideDirection(4, DOWN));
        cube.put(new SideDirection(2, RIGHT) , new SideDirection(11, LEFT));
        cube.put(new SideDirection(2, DOWN)  , new SideDirection(6, DOWN));
        cube.put(new SideDirection(2, LEFT)  , new SideDirection(5, DOWN));
        cube.put(new SideDirection(4, UP)    , new SideDirection(2, DOWN));
        cube.put(new SideDirection(4, RIGHT) , new SideDirection(5, RIGHT));
        cube.put(new SideDirection(4, DOWN)  , new SideDirection(10, UP));
        cube.put(new SideDirection(4, LEFT)  , new SideDirection(11, UP));
        cube.put(new SideDirection(5, UP)    , new SideDirection(2, RIGHT));
        cube.put(new SideDirection(5, RIGHT) , new SideDirection(6, RIGHT));
        cube.put(new SideDirection(5, DOWN)  , new SideDirection(10, RIGHT));
        cube.put(new SideDirection(5, LEFT)  , new SideDirection(4, LEFT));
        cube.put(new SideDirection(6, UP)    , new SideDirection(2, UP));
        cube.put(new SideDirection(6, RIGHT) , new SideDirection(11, DOWN));
        cube.put(new SideDirection(6, DOWN)  , new SideDirection(10, DOWN));
        cube.put(new SideDirection(6, LEFT)  , new SideDirection(5, LEFT));
        cube.put(new SideDirection(10, UP)   , new SideDirection(6, UP));
        cube.put(new SideDirection(10, RIGHT), new SideDirection(11, RIGHT));
        cube.put(new SideDirection(10, DOWN) , new SideDirection(4, UP));
        cube.put(new SideDirection(10, LEFT) , new SideDirection(5, UP));
        cube.put(new SideDirection(11, UP)   , new SideDirection(6, LEFT));
        cube.put(new SideDirection(11, RIGHT), new SideDirection(2, LEFT));
        cube.put(new SideDirection(11, DOWN) , new SideDirection(4, RIGHT));
        cube.put(new SideDirection(11, LEFT) , new SideDirection(10, LEFT));
    }
    
    private static void processEcht() {
        rows = 4;
        cols = 3;
        vertical = input.size() / rows;
        horizontal = input.stream().mapToInt(arr -> arr.length).max().getAsInt() / cols;
        for (int i = 0; i < sides.length; i++) sides[i] = null;
        sides[1] = getArray(1, vertical, horizontal);
        sides[2] = getArray(2, vertical, horizontal);
        sides[4] = getArray(4, vertical, horizontal);
        sides[6] = getArray(6, vertical, horizontal);
        sides[7] = getArray(7, vertical, horizontal);
        sides[9] = getArray(9, vertical, horizontal);
        
        cube.clear();
        cube.put(new SideDirection(1, UP)    , new SideDirection(9, RIGHT));
        cube.put(new SideDirection(1, RIGHT) , new SideDirection(2, RIGHT));
        cube.put(new SideDirection(1, DOWN)  , new SideDirection(4, DOWN));
        cube.put(new SideDirection(1, LEFT)  , new SideDirection(6, RIGHT));
        cube.put(new SideDirection(2, UP)    , new SideDirection(9, UP));
        cube.put(new SideDirection(2, RIGHT) , new SideDirection(7, LEFT));
        cube.put(new SideDirection(2, DOWN)  , new SideDirection(4, LEFT));
        cube.put(new SideDirection(2, LEFT)  , new SideDirection(1, LEFT));
        cube.put(new SideDirection(4, UP)    , new SideDirection(1, UP));
        cube.put(new SideDirection(4, RIGHT) , new SideDirection(2, UP));
        cube.put(new SideDirection(4, DOWN)  , new SideDirection(7, DOWN));
        cube.put(new SideDirection(4, LEFT)  , new SideDirection(6, DOWN));
        cube.put(new SideDirection(6, UP)    , new SideDirection(4, RIGHT));
        cube.put(new SideDirection(6, RIGHT) , new SideDirection(7, RIGHT));
        cube.put(new SideDirection(6, DOWN)  , new SideDirection(9, DOWN));
        cube.put(new SideDirection(6, LEFT)  , new SideDirection(1, RIGHT));
        cube.put(new SideDirection(7, UP)    , new SideDirection(4, UP));
        cube.put(new SideDirection(7, RIGHT) , new SideDirection(2, LEFT));
        cube.put(new SideDirection(7, DOWN)  , new SideDirection(9, LEFT));
        cube.put(new SideDirection(7, LEFT)  , new SideDirection(6, LEFT));
        cube.put(new SideDirection(9, UP)    , new SideDirection(6, UP));
        cube.put(new SideDirection(9, RIGHT) , new SideDirection(7, UP));
        cube.put(new SideDirection(9, DOWN)  , new SideDirection(2, DOWN));
        cube.put(new SideDirection(9, LEFT)  , new SideDirection(1, DOWN));
    }
    
    private static Position22B fromDirToDir(Direction22B from, Direction22B to, int x) {
        if (from == LEFT) {
            return to == LEFT  ? new Position22B(x, horizontal - 1)                :
                   to == UP    ? new Position22B(vertical - 1, horizontal - 1 - x) :
                   to == RIGHT ? new Position22B(vertical - 1 - x, 0)              :
                                 new Position22B(0, x)  /* to == DOWN */                           
            ;   
        }
        if (from == UP) {
            return to == UP    ? new Position22B(vertical - 1, x)       :
                   to == RIGHT ? new Position22B(x, 0)                  :
                   to == DOWN  ? new Position22B(0, horizontal - 1 - x) :
                                 new Position22B(vertical - 1 - x, horizontal - 1)  // to == LEFT */
            ;
        }
        if (from == RIGHT) {
            return to == UP    ? new Position22B(vertical - 1, x)    :
                   to == RIGHT ? new Position22B(x, 0)               :
                   to == DOWN  ? new Position22B(0, horizontal - 1 - x) :
                                 new Position22B(vertical - 1 - x, horizontal - 1)
            ;
        }
        if (from == DOWN) {
            return to == UP    ? new Position22B(vertical - 1, horizontal - 1 - x) :
                   to == RIGHT ? new Position22B(vertical - 1 - x, 0)              :
                   to == DOWN  ? new Position22B(0, x)                             :
                                 new Position22B(x, horizontal - 1)
            ;
        }
        // from == DOWN
        return null;
    }
    
    private static Position22B translateToInput(int side, int row, int col) {
        int r = side / cols * vertical + row;
        int c = side % cols * horizontal + col;
        return new Position22B(r, c);
    }
    
    private static int[][] getArray(int side, int lenVertical, int lenHorizontal) {
        var arr = new int[lenVertical][lenHorizontal];
        int startrow = side / cols * lenVertical;
        int startcol = side % cols * lenHorizontal;
        for (int r = 0; r < lenVertical; r++) {
            for (int c = 0; c < lenHorizontal; c++) {
                arr[r][c] = input.get(r + startrow)[c + startcol];
            }
        }
        return arr;
    }
}

//******************************************************************************
enum Direction22B {
    
    UP (3), RIGHT (0), DOWN (1), LEFT (2);
    
    final int value;
    final static int length = Direction22.values().length;
    
    //--------------------------------------------------------------------------
    Direction22B(int val) {
        value = val;
    }
    
    //--------------------------------------------------------------------------
    public Direction22B rotate(String to) {
        var ordinal = this.ordinal();
        var nieuw = to.equals("R") ? ordinal + 1 : ordinal - 1;
        nieuw = nieuw < 0 ? nieuw + length : nieuw % length;
        return Direction22B.values()[nieuw];
    }
    
    public int getValue() {
        return value;
    }
}

//******************************************************************************
record Position22B(int row, int col) {}

//******************************************************************************
record SideDirection(int square, Direction22B d) {}

//******************************************************************************
enum TileB {
    NOTHING, OPEN, WALL;
    
    //--------------------------------------------------------------------------
    public static TileB translate(char s) {
        switch(s) {
            case ' ' : return NOTHING;
            case '.' : return OPEN;
            case '#' : return WALL;
            default: throw new RuntimeException("Illegal tile!!!!");
        }
    }
}


