package mazesolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author DELIAMCGRATH
 */
public class MazeSolver {

    private static int numMoves = 0; // Allows us to keep track of how many recursive calls to findPath are required to solve the maze.
    private String[][] maze;
    int numRows;
    int numCols;

    /**
     * Creates a 2D array of String, maze Precondition: mazeStr must begin with
     * the number of rows followed by a space, the the number of columns
     * followed by a space, and finally by a string representation of the maze,
     * with rows separated by the newline "\n". The maze string will contain one
     * "S", one "G", a "." for each open cell, and a "#" for each obstacle.
     *
     * @param mazeStr a string meeting the preconditions described above
     */
    public MazeSolver(String mazeStr) {
        int i = 0;
        int endIndex = mazeStr.indexOf(" ");
        this.numRows = Integer.parseInt(mazeStr.substring(i, endIndex));
        i = endIndex + 1;
        endIndex = mazeStr.indexOf(" ", i);
        this.numCols = Integer.parseInt(mazeStr.substring(i, endIndex));
        i = endIndex + 1; // i now points to the first char in first row of maze

        // Let's make sure our string maze has the proper number of characters in it...
        if (numRows * numCols != mazeStr.substring(i).length() - numRows + 1) {
            throw new IllegalArgumentException("mazeStr is not well-formed.");
        }

        // Now we build the maze from the maze string.
        this.maze = new String[numRows][numCols];  // DON'T FORGET to actually create the array!
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                maze[row][col] = mazeStr.substring(i, i + 1);
                i++;
            }
            i++; // this increment, after each row, skips the newline character '\n'
        }
    }
    
        /**
     * This helper method allows one to create a maze in a text editor. It
     * creates a string formed for use by the MazeSolver constructor.
     *
     * @param fName the name of a text file containing a maze
     * @return The maze string in the form of
     * "<numRows><space><numCols><space><line1>\n<line2>\n...<lastLine>"
     */
    public static String makeMazeStringFromFile(String fName) {
        String str = "";
        // build the mazeStr
        //String mazeStr = "" + rows + " " + cols + " ";
        // read the text file containing maze characters and append to mazeStr
        File file = new File(fName);
        Scanner reader = new Scanner(System.in);
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Maze file not found.");
        }
        String line1 = reader.nextLine();
        str = str + line1 + "\n";
        int numCols = line1.length();
        int numRows = 1;
        while (reader.hasNext()) {
            String line = reader.nextLine();
            str = str + line + "\n";
            if (line.length() > 0) {
                numRows++;
            }
        }

        String mazeStr = "" + numRows + " " + numCols + " " + str.substring(0, str.length());
        if (mazeStr.substring(mazeStr.length() - 1).equals("\n")) // did they press ENTER after the last line?
        {
            mazeStr = mazeStr.substring(0, mazeStr.length() - 1);  // drops the last "\n" character
        }
        return mazeStr;
    }


    /**
     * Creates a 2D matrix view of the contents of the array maze
     * @return the contents of maze with rows separated by newline '\n'
     */
    @Override
    public String toString() {
        String str = "";
        for (String[] row : maze) {
            for (String val : row) {
                str += val;
            }
            str += "\n";
        }
        return str;
    }

    /**
     * Finds and marks the start position as "open", calls recursive helper findPath,
     * and then remarks the start with "S" and prints the solution path.
     */
    public void solve() {
        // First, find "S"
        int col = -1; // x is a column
        int row = -1; // y is a row
        for (int y = 0; y < numRows; y++) {
            for (int x = 0; x < numCols; x++) {
                if (maze[y][x].equals("S")) {
                    row = y;
                    col = x;
                }
            }
        }
        if (col == -1) {
            throw new IllegalStateException("No 'S' starting position found.");
        }

        maze[row][col] = "."; // need to mark the start as an open cell
        if (findPath(col, row)) {
            System.out.println("Solved after " + numMoves + " steps.");
        } else {
            System.out.println("No solution found.");
        }
        maze[row][col] = "S"; // put the 'S' back in place
        System.out.println(this);
        System.out.println("--------------------------------------------------------------------------------\n\n");

    }

    /**
     * findPath tells us whether there is a path to the goal from this (x, y) 
     * or (col, row) position in the maze.
     * 
     * @param col the x-coordinate of the current cell
     * @param row the y-coordinate of the current cell
     * @return true if this path leads to the goal, false otherwise
     */
    private boolean findPath(int col, int row) {
        numMoves++;
        //System.out.println(this);  // Uncomment this line to see the state of the maze after each step
        if ((col < 0) || (col >= numCols) || (row < 0) || (row >= numRows)) {
            return false;
        }
        
        // Your code here
         numMoves++;
        if ((col < 0) || (col >= numCols) || (row < 0) || (row >= numRows)) {
            return false;}
        if (maze[row][col].equals("G"))
            return true;
        if (!maze[row][col].equals("."))
            return false;
       
        maze[row][col] = "+";
       
        if (findPath(col+1, row) == true)
            return true;
        if (findPath(col-1, row) == true)
            return true;
        if (findPath(col, row+1) == true)
            return true;
        if (findPath(col, row-1) == true)
            return true;
        maze[row][col] = "@";

        return false;

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MazeSolver ms = new MazeSolver("6 6 ##S###\n#....#\n#....#\n#....#\n#..#.#\n#G####");
        System.out.println(ms);
        ms.solve();
        
        System.out.println("\nSolving maze in maze1.txt");
        ms = new MazeSolver(makeMazeStringFromFile("maze1.txt"));
        System.out.println(ms);
        ms.solve();
        
        System.out.println("\nSolving maze in maze2.txt");
        ms = new MazeSolver(makeMazeStringFromFile("maze2.txt"));
        System.out.println(ms);
        ms.solve();
        
        System.out.println("\nSolving maze in maze3.txt");
        ms = new MazeSolver(makeMazeStringFromFile("maze3.txt"));
        System.out.println(ms);
        ms.solve();
        
        System.out.println("\nSolving maze in maze4.txt");
        ms = new MazeSolver(makeMazeStringFromFile("maze4NoSolution.txt"));
        System.out.println(ms);
        ms.solve();
    }

    /*CODE OUTPUT:



Solving maze in maze3.txt
####################
#.................##
#..................#
#..................#
##################.#
#...#..............#
###.#..............#
###................#
###...............##
##................##
##.#################
##..################
###.################
##..#..............#
##.#########.#######
#.................##
#.................##
##...............###
##................##
##S##############G##

Solved after 1789 steps.
####################
#.................##
#..................#
#..................#
##################.#
#...#..............#
###.#..............#
###................#
###...............##
##................##
##.#################
##..################
###.################
##..#..............#
##.#########.#######
#.................##
#.................##
##...............###
##++++++++++++++++##
##S##############G##

--------------------------------------------------------------------------------



Solving maze in maze4.txt
####################
#.................##
#..................#
#..................#
##################.#
#...#..............#
###.#..............#
###................#
###...............##
##................##
##.#################
##..################
###.################
##..#..............#
##.#########.#######
#.................##
#.................##
##...............###
##..............#.##
##S##############G##

No solution found.
####################
#@@@@@@@@@@@@@@@@@##
#@@@@@@@@@@@@@@@@@@#
#@@@@@@@@@@@@@@@@@@#
##################@#
#@@@#@@@@@@@@@@@@@@#
###@#@@@@@@@@@@@@@@#
###@@@@@@@@@@@@@@@@#
###@@@@@@@@@@@@@@@##
##@@@@@@@@@@@@@@@@##
##@#################
##@@################
###@################
##@@#@@@@@@@@@@@@@@#
##@#########@#######
#@@@@@@@@@@@@@@@@@##
#@@@@@@@@@@@@@@@@@##
##@@@@@@@@@@@@@@@###
##@@@@@@@@@@@@@@#.##
##S##############G##

--------------------------------------------------------------------------------


BUILD SUCCESSFUL (total time: 0 seconds)
    */

}
