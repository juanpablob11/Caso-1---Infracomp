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
    private List<Boolean> neighborsState = new ArrayList<Boolean>();
    private CyclicBarrier barrierForMessages;
    private CyclicBarrier barrierForUpdating;
    
    // Constructor
    public Cell(boolean initialState, Integer row, Integer column, CyclicBarrier barrierForMessages,
    CyclicBarrier barrierForUpdating, Board actualBoard) {
        this.currentState = initialState;
        this.mailbox = new Buffer(row + 1);
        this.column = column;
        this.row = row;
        this.barrierForMessages = barrierForMessages;
        this.barrierForUpdating = barrierForUpdating;
        this.actualBoard = actualBoard;
    }

    @Override
    public void run() {
        // Simulate the cell's life cycle based on the game rules (birth, live, die)
        // This will involve receiving state information from neighboring cells through the mailbox
        // and updating its state accordingly

        try {
            // Phase 1: Send status to neighbors and Receive states
            this.actualBoard.updateNeighborBuffers(row, column, currentState);
            this.mailbox.consume();
            barrierForMessages.await(); // Wait until all cells have sent their status
            
            // Phase 2: Calculate the new state
            calculateNextState();
            barrierForUpdating.await(); //Wait until all cells have been updated

        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public void calculateNextState() {
        // Implementation to calculate the next state of this cell based on its neighbors' states
    }

    // Getter and Setter for currentState if needed
    public boolean getCurrentState() {
        return currentState;
    }

    public void setCurrentState(boolean currentState) {
        this.currentState = currentState;
    }

    class Buffer {
    private Queue<Boolean> queue = new LinkedList<>();
    private int capacity;

    public Buffer(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void produce(Boolean value) throws InterruptedException {
        while (queue.size() == capacity) {
            wait(); // Espera si el buffer está lleno
        }
        queue.add(value);
        notify(); // Notifica al consumidor que hay datos
    }

    public Boolean consume() throws InterruptedException {
        while (queue.isEmpty()) {
            Thread.yield(); // Instead of wait(), for semi-active wait
        }
        return consumeSynchronized(queue);
    }
    
    }

    public synchronized Boolean consumeSynchronized(Queue<Boolean> queue) throws InterruptedException {
        // Removes and returns the first element in queue
        Boolean value = queue.poll(); 
        neighborsState.add(value);
        notify(); // Notify producers that there is space
        return value;
    }
    
}

