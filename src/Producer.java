import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Producer extends Thread{

    // Attributes
    private Cell cell;
    private CyclicBarrier barrierBeforeUpdating;

    // Constructor
    public Producer(Cell cell, CyclicBarrier barrierBeforeUpdating) {
        this.cell = cell;
        this.barrierBeforeUpdating = barrierBeforeUpdating;
    }
    
    @Override
    public void run() {
        // Send Messages to neighbor 
        produce(this.cell.getCurrentState());

        // Get to the barrier before updating
        try {
            barrierBeforeUpdating.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public void produce(Boolean value){
        for (Cell neighbor : this.cell.getNeighbors()){
            try {
                auxProduce(neighbor, value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void auxProduce(Cell neighbor, Boolean value) throws InterruptedException {
    synchronized (neighbor.getMailbox()) { // Sincroniza sobre el buzón del vecino
        Queue<Boolean> neighborQueue = neighbor.getMailbox().getQueue();
        int actualCapacity = neighbor.getMailbox().getCapacity();
        
        // Espera si el buffer está lleno
        while (neighborQueue.size() == actualCapacity) {
            neighbor.getMailbox().wait(); // Espera en el objeto buzón del vecino
        }
        
        // Añade valor a la cola del buzón del vecino
        neighborQueue.add(value);
        
        // Notifica a todos los hilos que esperan en el buzón del vecino
        neighbor.getMailbox().notifyAll();
    }
}

}
