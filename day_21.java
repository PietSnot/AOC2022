/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aoc2022;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier;

import static java.lang.Long.parseLong;

/**
 *
 * @author Piet
 */
public class day_21 {
    
    private static int day = 21;
    private static boolean test = false;
    
    private static Map<String, Supplier<Long>> numbers = new HashMap<>();
    
    private static String you = "humn";
    private static String num1;
    private static String num2;
    
    public static void main(String... args) throws IOException {
        getInput();
        solveA();
        solveB();
    }
    
    private static void getInput() throws IOException {
        var path = AOC2022.getPath(day, test);
        try (var scanner = new Scanner(path)) {
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();
                var ar = line.split(" ");
                if (ar.length == 2) {
                    var naam = ar[0].substring(0, ar[0].length() - 1);
                    var val = parseLong(ar[1]);
                    numbers.put(naam, () -> val);
                    continue;
                }
                var naam = ar[0].substring(0, ar[0].length() - 1);
                var naam1 = ar[1];
                var naam2 = ar[3];
                if (naam.equals("root")) {
                    num1 = naam1;
                    num2 = naam2;
                }
                if (ar[2].equals("+")) {
                    numbers.put(naam, () -> numbers.get(naam1).get() + numbers.get(naam2).get());
                }
                else if (ar[2].equals("-")) {
                    numbers.put(naam, () -> numbers.get(naam1).get() - numbers.get(naam2).get());
                }
                else if (ar[2].equals("*")) {
                    numbers.put(naam, () -> numbers.get(naam1).get() * numbers.get(naam2).get());
                }
                else if (ar[2].equals("/")) {
                    numbers.put(naam, () -> numbers.get(naam1).get() / numbers.get(naam2).get());
                }
            }
        }
    }
    
    private static void solveA() {
        System.out.println(numbers.get("root").get());
    }
    
    private static void solveB() throws IOException {
        numbers.clear();;
        getInput();
        long lo = 3373760000000L;
        long hi = 3373770000000L;
        while (lo != hi) {
            var high = hi;
            var low = lo;
            var mid = mid(high, low);
            numbers.put(you, () -> mid);
            var num1dup = numbers.get(num1).get();
            var num2dup = numbers.get(num2).get();
            var diff = num1dup - num2dup;
            if (diff == 0L) break;
            
            if (num1dup > num2dup) lo = (hi + lo) / 2;
            else hi = (hi + lo) / 2;
        }
//        numbers.put(you, () -> 3373760000000L);
        numbers.put(you, () -> 3373767893066L + 1);
        System.out.println("num1: " + numbers.get(num1).get());
        System.out.println("num2: " + numbers.get(num2).get());
        System.out.println("B: " + numbers.get("humn").get());
//        System.out.println(hi);
//        System.out.println(lo);
    }
    
    private static long mid(long hi, long lo) {
        return (hi + lo) / 2;
    }
}
