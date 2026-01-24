public class ArrayDeque<T> {
    private T[] items;
    private int size ;

    public ArrayDeque(){
        items = (T[]) new Object[8];
        size = 0;
    }

    public void addFirst(T item){

    }

    public void addLast(T item){

    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return this.size();
    }

    public void printDeque(){

    }

    public T removeFirst(){

    }
}