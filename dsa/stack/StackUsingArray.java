public class StackUsingArray {
    private int[] stack;
    private int top;

    public StackUsingArray(int size) {
        stack = new int[size];
        top = -1;
    }

    public void push(int value) {
        if (top < stack.length - 1) {
            stack[++top] = value;
        } else {
            System.out.println("Stack is full.");
        }
    }

    public int pop() {
        if (top >= 0) {
            return stack[top--];
        }
        System.out.println("Stack is empty.");
        return -1;
    }

    public static void main(String[] args) {
        StackUsingArray stack = new StackUsingArray(3);
        stack.push(10);
        stack.push(20);
        stack.push(30);
        System.out.println("Popped: " + stack.pop());
    }
}
