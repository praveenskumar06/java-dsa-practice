public class QueueUsingArray {
    private int[] queue;
    private int front;
    private int rear;

    public QueueUsingArray(int size) {
        queue = new int[size];
        front = 0;
        rear = -1;
    }

    public void enqueue(int value) {
        if (rear < queue.length - 1) {
            queue[++rear] = value;
        } else {
            System.out.println("Queue is full.");
        }
    }

    public int dequeue() {
        if (front <= rear) {
            return queue[front++];
        }
        System.out.println("Queue is empty.");
        return -1;
    }

    public static void main(String[] args) {
        QueueUsingArray queue = new QueueUsingArray(3);
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        System.out.println("Dequeued: " + queue.dequeue());
    }
}
