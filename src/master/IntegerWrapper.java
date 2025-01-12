package master;

public class IntegerWrapper {
    private int value;

    public IntegerWrapper(int initialValue) {
        this.value = initialValue;
    }

    public synchronized void increment() {
        value++;
    }

    public synchronized void decrement() {
        value--;
    }

    public synchronized int getValue() {
        return value;
    }
}