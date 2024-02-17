import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
    private Boolean currentState;
    private Integer column, row;
    private Buffer mailbox;
    private List<Cell> neighbors;
    private List<Boolean> neighborsState;
    
    // Constructor
    public Cell(boolean initialState, Integer column, Integer row) {
        this.currentState = initialState;
        this.mailbox = new Buffer(row + 1);
        this.column = column;
        this.row = row;
    }

    @Override
    public void run() {
        // Simulate the cell's life cycle based on the game rules (birth, live, die)
        // This will involve receiving state information from neighboring cells through the mailbox
        // and updating its state accordingly
    }
    
    public void sendStateToNeighbors() {
        // Implementation to send this cell's state to neighboring cells
       for (Cell neighbor : neighbors){
        // Try to produce the state of this cell to neighbor's mailbox
        try {
            neighbor.mailbox.produce(this.getCurrentState());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       }
    }
    
    public void receiveStateFromNeighbors() {
        // Implementation to receive states from neighboring cells and update the mailbox
        for(Cell neighbor : neighbors){
        // Try to consume the state of the neighbors cells to update state
        try {
            boolean state = neighbor.mailbox.consume();
            neighborsState.add(state);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    private class Buffer {
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

    public synchronized Boolean consume() throws InterruptedException {
        while (queue.isEmpty()) {
            wait(); // Espera si el buffer está vacío
        }
        Boolean value = queue.poll(); // Remueve y retorna el primer elemento de la cola
        notify(); // Notifica al productor que hay espacio
        return value;
        } 
    }
}

