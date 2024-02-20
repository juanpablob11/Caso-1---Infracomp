import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Consumer extends Thread {

    // Attributes
    private Cell cell;
    private CyclicBarrier barrierBeforeUpdating;

    // Constructor
    public Consumer(Cell cell, CyclicBarrier barrierBeforeUpdating) {
        this.cell = cell;
        this.barrierBeforeUpdating = barrierBeforeUpdating;
    }

    @Override
    public void run() {
        // Consume messages from own mailbox for each neighbor
        try {
            for (int i = 0; i < cell.getNeighbors().size(); i++) {
                Boolean value = consume();
                // Process the consumed value here or after the synchronized block
                this.cell.neighborsState.add(value);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt(); // Handle the InterruptedException
        }

        // Get to the barrier before updating
        try {
            barrierBeforeUpdating.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt(); // Handle the InterruptedException
        }
    }

    public Boolean consume() throws InterruptedException {
        Boolean value = null;
        while (value == null) {
            synchronized (cell.getMailbox()) { // Synchronize on the cell's mailbox object
                if (!cell.getMailbox().getQueue().isEmpty()) {
                    value = cell.getMailbox().getQueue().poll();
                    cell.getMailbox().notifyAll(); // Notify producers that a message has been consumed
                }
            }
            if (value == null) {
                Thread.yield(); // Yield the CPU to allow other threads to execute
            }
        }
        return value;
    }
}
