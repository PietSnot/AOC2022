/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aoc2022;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 *
 * @author Piet
 */
public class day_08 {
    
    private static final int day = 8;
    private static final boolean test = false;
    
    private static int[][] input;
    
    public static void main(String... args) throws IOException {
        getInput();
        solveA();
        solveB();
    }
    
    private static void getInput() throws IOException {
        var path = AOC2022.getPath(day, test);
        try (var stream = Files.lines(path)) {
            input = stream.map(s -> s.chars().map(c -> c - '0').toArray()).toArray(int[][]::new);
        }
    }
    
    private static void solveA() {
        var visibleHorizontal = Arrays.stream(input).map(day_08::determineVisible).toArray(boolean[][]::new);
        var visibleVertical = IntStream.range(0, input[0].length)
            .mapToObj(i -> getColumn(input, i))
            .map(day_08::determineVisible)
            .toArray(boolean[][]::new)
        ;
        int count = 0;
        for (int row = 0; row < input.length; row++) {
            for (int col = 0; col < input[0].length; col++) {
                if (visibleHorizontal[row][col] || visibleVertical[col][row]) count++;
            }
        }
        System.out.println("A: " + count);
    }
    
    private static void solveB() {
        int max = 0;
        int[][] result = new int[input.length][input[0].length];
        for (int r = 0; r < input.length; r++) {
            for (int c = 0; c < input[0].length; c++) {
                result[r][c] = getVisibleTrees(r, c);
                if (result[r][c] > max) max = result[r][c];
            }
        }
        System.out.println("B: " + max);
    }
    
    private static int getVisibleTrees(int row, int col) {
        if (col == 0 || col == input[0].length - 1 || row == 0 || row == input.length - 1) return 0;
        // to left
        int left = 0;
        for (int c = col - 1; c >= 0; c--) {
            left++;
            if (input[row][c] >= input[row][col]) break;
        }
        
        // to right
        int right = 0;
        for (int c = col + 1; c < input[0].length; c++) {
            right++;
            if (input[row][c] >= input[row][col]) break;
        }
        // to bottom
        int bottom = 0;
        for (int r = row + 1; r < input.length; r++) {
            bottom++;
            if (input[r][col] >= input[row][col]) break;
        }
        // to top
        int top = 0;
        for (int r = row - 1; r >= 0; r--) {
            top++;
            if (input[r][col] >= input[row][col]) break;
        }
        return left * right * bottom * top;
    }
    
    private static boolean[] determineVisible(int[] a) {
        var result = new boolean[a.length];
        int maxLeft = -1;
        int maxRight = -1;
        for (int i = 0; i < a.length; i++) {
            int j = a.length - 1 - i;
            if(!result[i] && a[i] > maxLeft) {
                result[i] = true;
                maxLeft = a[i];
            }
            if (!result[j] && a[j] > maxRight) {
                result[j] = true;
                maxRight = a[j];
            }
        }
        return result;
    }
    
    private static int[] getColumn(int[][] a, int col) {
        return IntStream.range(0, a.length).map(i -> a[i][col]).toArray();
    }
}
