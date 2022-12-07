/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aoc2022;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Piet
 */
public class day_07 {
    
    private static final int day = 7;
    private static final boolean test = false;
    
    private static List<String> input;
    private static final List<Dir> directories = new ArrayList<>();
    
    public static void main(String... args) throws IOException {
        getInput();
        buildContents();
        solveA();
        solveB();
    }
    
    private static void solveA() {
        long max = 100_000L;
        var result = directories.stream()
            .filter(s -> s.getType() == Type.Directory)
            .filter(s -> s.getSize() <= max)
            .mapToLong(s -> s.getSize())
            .sum()
        ;
        System.out.println("A: " + result);
    }
    
    private static void solveB() {
        long available = 70_000_000L;
        long needed = 30_000_000L;
        long currentSize = directories.get(0).getSize();
        long unused = available - currentSize;
        long toFree = needed - unused;
        var smallestSize = directories.stream()
            .filter(s -> s.getSize() >= toFree)
            .mapToLong(s -> s.getSize())
            .sorted()
            .findFirst()
            .getAsLong()
        ;
        System.out.println("B: " + smallestSize);      
    }
    
    private static void getInput() throws IOException {
        try (var stream = Files.lines(AOC2022.getPath(day, test))) {
            input = stream.toList();
        }
    }
    
    private static void buildContents() {
        
        Dir currentDirectory = null;
        
        for (var line: input) {
            if (line.equals("$ cd /")) {
                var root = new Dir("/", null);
                directories.add(root);
                currentDirectory = root;
            }
            else if (line.equals("$ cd ..")) {
                currentDirectory = currentDirectory.getParent();
            }
            else if (line.startsWith("$ cd")) {
                var temp = line.split(" ");
                var name = temp[2];
                currentDirectory = (Dir) currentDirectory.getContents().stream()
                    .filter(unit -> unit.getName().equals(name))
                    .findFirst()
                    .get()
                ;
            }
            else if (line.startsWith("dir")) {
                var temp = line.split(" ");
                var unit = new Dir(temp[1], currentDirectory);
                currentDirectory.addUnit(unit);
                directories.add(unit);
            }
            else if (Character.isDigit(line.charAt(0))) {
                var temp = line.split(" ");
                var file = new File(temp[1], Long.parseLong(temp[0]), currentDirectory);
                currentDirectory.addUnit(file);
            }
        }   
    }
}

//*********************************************************************//
enum Type {
    File, Directory;
}

//*********************************************************************//
interface Unit {
    Type getType();
    long getSize();
    String getName();
    Dir getParent();
}

//*********************************************************************//
class Dir implements Unit {
    
    private final String name;
    private Dir parent;
    private final List<Unit> contents = new ArrayList<>();
    
    Dir(String name, Dir parent) {
        this.name = name;
        this.parent = parent;
    }
    
    @Override
    public Type getType() {
        return Type.Directory;
    }
    
    @Override
    public long getSize() {
        return contents.stream().mapToLong(unit -> unit.getSize()).sum();
    }
    
    public void addUnit(Unit unit) {
        contents.add(unit);
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public Dir getParent() {
        return parent;
    }
    
    public List<Unit> getContents() {
        return new ArrayList<>(contents);
    }
    
    public void delete(String name) {
        var u = contents.stream().filter(unit -> unit.getName().equals(name)).findFirst();
        u.ifPresent(() -> contents.remove(u));
    }
    
    @Override
    public String toString() {
        return String.format("type: %s, name: %s, size: %d, parent: %s", getType(), getName(), getSize(), getParent());
    }
}

//*********************************************************************//
class File implements Unit {
    
    private final String name;
    private final long size;
    private final Dir parent;
    
    File(String name, long size, Dir parent) {
        this.name = name;
        this.size = size;
        this.parent = parent;
    }
    
    @Override
    public Type getType() {
        return Type.File;
    }
    
    @Override
    public long getSize() {
        return size;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public Dir getParent() {
        return parent;
    }
    
    @Override
    public String toString() {
        return String.format("type: %s, name: %s, size: %d, parent: %s%n", getType(), getName(), getSize(), getParent());
    }    
}
