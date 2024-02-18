import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
public class Board {

    /* Initializes the cells with their initial state and a mailbox
    with capacity based on its row.
    Manages the advancement of generations, ensuring that all the
    cells complete their evolution before proceeding to the next generation.
    This can be implemented through a synchronization barrier (e.g.,
    using CyclicBarrier from Java).
    Prints the state of the board after each generation or at the end of the simulation,
    as required.
    */

    // Attributes
    private Cell[][] cells;
    private final int size;
    private final CyclicBarrier barrierForMessages;
    private final CyclicBarrier barrierForUpdating;
    private int numOfGenerations;
    private int currentGeneration;

    // Constructor Method
    public Board(int size, int numOfGenerations) {
        this.size = size;
        this.barrierForMessages = new CyclicBarrier(size * size, new Runnable() {
            @Override
            public void run() {
                // This action will be executed once all the cells are with the state of all neighbors
                System.out.println("barrera alcanzada");
                System.out.println("xd");
            }
        });
        this.barrierForUpdating = new CyclicBarrier(size * size, new Runnable() {
            @Override
            public void run() {
                // This action will be executed once all the cells are updated
                currentGeneration++;
                printBoard();
            }
        });
        this.currentGeneration = 0;
        this.numOfGenerations = numOfGenerations;
    }

    public Cell[][] initializeCells(String initialStateFile) {
        Cell[][] cells = new Cell[this.size][this.size];
        try (BufferedReader br = new BufferedReader(new FileReader(initialStateFile))) {
            String line = br.readLine();
            if (line != null) {
                int n = Integer.parseInt(line.trim());
                cells = new Cell[n][n];
                int row = 0;
                while ((line = br.readLine()) != null && row < n) {
                    String[] values = line.split(",");
                    for (int j = 0; j < Math.min(values.length, n); j++) {
                        cells[row][j] = new Cell("true".equals(values[j].trim()), row, j, barrierForMessages, barrierForUpdating, this);
                    }
                    row++;
                }
            }
    
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in the first line of the file: " + e.getMessage());
        }
    
        return cells;
    }
    
        
    

    public void runSimulation(int numberOfGenerations) {

        // update each generation for every turn
        while (currentGeneration < numberOfGenerations){
            updateGeneration();
            currentGeneration ++;
        }
    }


    public void printBoard() {
        // Implementation to print the current status of the board
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(cells[i][j].getCurrentState() ? "V " : "M ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void updateGeneration() {
        // Optional implementation if you need to perform specific actions on each generation

        // Create and start the threads for each Cell for this generation
        Thread[][] cellThreads = new Thread[size][size];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cellThreads[i][j] = new Thread(cells[i][j]);
                cellThreads[i][j].start();
            }
        }
    }

    public void updateNeighborBuffers(int row, int col, Boolean currentState){
        // Assign the state to neighbors mailbox
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                // Verify that it is not the current cell and that it is within the limits
                if ((i != 0 || j != 0) && newRow >= 0 && newRow < this.size && newCol >= 0 && newCol < size) {
                    
                    // Trying to produce to other mailboxes of neighbors
                    try {
                        cells[newRow][newCol].mailbox.produce(currentState);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void setBoardCell(Cell[][] boardCells){
        this.cells = boardCells;
    }

    public Cell[][] getBoardCell(){
        return cells;
    }
}
