import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class GameOfLife {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        for(int i = 0; i < 30; i++) {
            System.out.print("-");
        }

        System.out.println("\n" +
                        "Welcome to the Game of Life!");

        for(int i = 0; i < 30; i++) {
            System.out.print("-");
        }
        
        System.out.println("\n" +
        "Enter the number of generations to simulate: ");
        int generations = scanner.nextInt();
        
        System.out.println("\n" +
        "Enter the size of matrix to simulate: ");
        int size = scanner.nextInt();

        scanner.nextLine(); // Consume the remaining line
        
        System.out.println("\n" +
        "Enter the name of the file with the initial state: ");
        String fileName = scanner.nextLine();
        
        Cell[][] board = loadInitialState(fileName, size);
        
        // More logic to come
        
        scanner.close();
    }
    
    private static Cell[][] loadInitialState(String fileName, int size) {
        Board gameBoard = new Board(size);
        gameBoard.setBoardCell(gameBoard.initializeCells(fileName));
        return gameBoard.getBoardCell();
    }    
}
