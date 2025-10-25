/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EDD;

/**
 *
 * @author Daniel
 */
public class Queue<T> {
    private SimpleList<T> list = new SimpleList<>();
    
    public void insert(T data) {
        list.addAtTheEnd(data);
    }

    
    public T pop() {
        if (list.isEmpty()) return null;
        T value = list.getpFirst().getData();
        list.deleteFirst();
        return value;
    }
    
    public boolean isEmpty() {
        return list.isEmpty();
    }
    
    public int getSize() {
        return list.getSize();
    }
    
    public void clearQueue() {
        list.wipeList();
    }
    
    public String printQueue() {
        return list.printToString();
    }
    
    public SimpleNode<T> getpFirst() {
        return list.getpFirst();
    }
    public void delete(T data) {
        list.delete(data);
    }
}
