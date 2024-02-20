import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Cell implements Runnable {

    /* Contains an internal mailbox for communication, whose capacity is
    determined based on your row. Runs a thread that simulates its cycle
    of life according to the rules of the game (born, live, die), based on
    the information received through your mailbox.
    It communicates its status to neighboring cells and receives their status to
    determine your next state.*/

    // Attributes
    private Board actualBoard;
    private Boolean currentState;
    private Integer row, column;
    Buffer mailbox;
    List<Boolean> neighborsState = new ArrayList<Boolean>();
    private List<Cell> neighbors = new ArrayList<Cell>();
    private CyclicBarrier barrierBeforeUpdating;
    private CyclicBarrier barrierAfterUpdating;
    
    // Constructor
    public Cell(boolean initialState, Integer row, Integer column, CyclicBarrier barrierForMessages,
    CyclicBarrier barrierForUpdating, Board actualBoard) {
        this.currentState = initialState;
        this.mailbox = new Buffer(row + 1, row, column);
        this.column = column;
        this.row = row;
        this.barrierBeforeUpdating = barrierForMessages;
        this.barrierAfterUpdating = barrierForUpdating;
        this.actualBoard = actualBoard;
    }

    public Integer getRow() {
        return row;
    }

    public Integer getColumn() {
        return column;
    }

    @Override
    public void run() {

        // Simulate the cell's life cycle based on the game rules (birth, live, die)
        // This will involve receiving state information from neighboring cells through the mailbox
        // and updating its state accordingly

        try {

            // Phase 0: Create threads for producing and consuming
                // Getting the neighbors
                this.actualBoard.updateNeighborBuffers(row, column, this);
                // Creating producer with instance of buffer for cell
                Producer producerThread = new Producer(this, barrierBeforeUpdating);
                
                // Creating consumer with instance of buffer for cell
                Consumer consumerThread = new Consumer(this, barrierBeforeUpdating);

                // Running auxThreads
                producerThread.start();
                consumerThread.start();

                // Wait for the producer and consumer threads to finish
                producerThread.join();
                consumerThread.join();

            // Phase 1: Calculate the new state
                calculateNextState();

            // Phase 3: Wait for all cells
                barrierAfterUpdating.await(); 

        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public Buffer getMailbox() {
        return mailbox;
    }

    public void calculateNextState() {
        // Implementation to calculate the next state of this cell based on its neighbors' states
        int aliveNeighbors = 0;
        for (Boolean neighborState : neighborsState) {
            if (neighborState) {
                aliveNeighbors++;
            }
        }
        if (!currentState && aliveNeighbors == 3) {
            currentState = true; // The cell is born
        } else if (currentState && (aliveNeighbors > 3 || aliveNeighbors == 0)) {
            currentState = false; // The cell dies because of overpopulation or isolation
        } else if (currentState && (aliveNeighbors >= 1 && aliveNeighbors <= 3)) {
            currentState = true; // The cell survives and remains alive
        } else {
            currentState = false; // The cell remains dead
        }
    }

    // Getter and Setter for currentState if needed
    public boolean getCurrentState() {
        return currentState;
    }

    public void setCurrentState(boolean currentState) {
        this.currentState = currentState;
    }

    public void addNeighbor(Cell currentNeighbor) {
        neighbors.add(currentNeighbor);
    }

    public List<Cell> getNeighbors() {
        return neighbors;
    }

}

