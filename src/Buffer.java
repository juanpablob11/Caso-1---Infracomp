import java.util.LinkedList;
import java.util.Queue;

public class Buffer {
    private Queue<Boolean> queue = new LinkedList<>();
        private int capacity;
        private int row;
        private int column;

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        public Buffer(int capacity, int row, int column) {
            this.capacity = capacity;
            this.row = row;
            this.column = column;
        }

        public Queue<Boolean> getQueue (){
            return queue;
        }

        public int getCapacity(){
            return capacity;
        }
}
