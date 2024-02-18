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

        Board gameBoard = new Board(size, generations);

        scanner.nextLine(); // Consume the remaining line
        
        System.out.println("\n" +
        "Enter the name of the file with the initial state: ");
        String fileName = scanner.nextLine();
        
        Cell[][] initialBoard = loadInitialState(fileName, size, gameBoard);
        
        scanner.close();

        System.out.println();
        for(int i = 0; i < 30; i++) {
            System.out.print("-");
        }

        System.out.println("\n" +
            "The final board after "+ generations + " generations is: ");
        
        gameBoard.runSimulation(generations);
        
    }
    
    private static Cell[][] loadInitialState(String fileName, int size, Board gameBoard) {
        gameBoard.setBoardCell(gameBoard.initializeCells(fileName));
        return gameBoard.getBoardCell();
    }    
}
