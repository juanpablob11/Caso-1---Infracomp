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
        
        scanner.nextLine(); // Consume the remaining line
        
        System.out.println("Enter the name of the file with the initial state: ");
        String fileName = scanner.nextLine();
        
        Boolean[][] board = loadInitialState(fileName);
        
        // More logic to come
        
        scanner.close();
    }
    
    private static Boolean[][] loadInitialState(String fileName) {
        Boolean[][] board = null;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            // Read the first line to determine the size of the board
            String line = br.readLine();
            if (line != null) {
                int n = Integer.parseInt(line.trim()); 
                board = new Boolean[n][n]; // Initialize the board with size n x n
                
                // Read cell status
                int row = 0;
                while ((line = br.readLine()) != null && row < n) {
                    String[] values = line.split(",");
                    for (int j = 0; j < Math.min(values.length, n); j++) {
                        board[row][j] = "true".equals(values[j].trim());
                    }
                    row++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in the first line of the file: " + e.getMessage());
        }
        
        return board;
    }    
}
