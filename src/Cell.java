import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Cell implements Runnable {

    /* Contains an internal mailbox for communication, whose capacity is
    determined based on your row. Runs a thread that simulates its cycle
    of life according to the rules of the game (born, live, die), based on
    the information received through your mailbox.
    It communicates its status to neighboring cells and receives their status to
    determine your next state.*/

    // Attributes
    private boolean currentState;
    private BlockingQueue<Boolean> mailbox;
    private int mailboxCapacity;
    
    // Constructor
    public Cell(boolean initialState, int row) {
        this.currentState = initialState;
        this.mailboxCapacity = row + 1; // Assuming the capacity is determined by its row
        this.mailbox = new LinkedBlockingQueue<>(mailboxCapacity);
    }

    @Override
    public void run() {
        // Simulate the cell's life cycle based on the game rules (birth, live, die)
        // This will involve receiving state information from neighboring cells through the mailbox
        // and updating its state accordingly
    }
    
    public void sendStateToNeighbors() {
        // Implementation to send this cell's state to neighboring cells
    }
    
    public void receiveStateFromNeighbors() {
        // Implementation to receive states from neighboring cells and update the mailbox
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
}

