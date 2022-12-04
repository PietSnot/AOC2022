/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package aoc2022;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Piet
 */
public class AOC2022 {

    /**
     * @param args the command line arguments
     */
    
    public final static String path = "D:/AOC2022/src/aoc2022/Resources/input_";
    
    public static Path getPath(int day, boolean test) {
        var d = (day < 10 ? "0" : "") + day;
        var t = (test ? "_test" : "") + ".txt";
        var s = path + d + t;
        return Paths.get(s);
        
    }
    public static void main(String[] args) {
        var s = getPath(8, true);
    }
    
}
