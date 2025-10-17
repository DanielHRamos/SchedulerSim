/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EDD;

/**
 *
 * @author Daniel
 */
public class SimpleNode<T>{
    
    private T data;
    private SimpleNode pNext;
    
    public SimpleNode(T data){
        
        this.data = data;
        this.pNext = null;
        
    }
    
    public SimpleNode(T data, SimpleNode<T> pNext){
        
        this.data = data;
        this.pNext = pNext;
        
    }  

    /**
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * @return the pNext
     */
    public SimpleNode<T> getpNext() {
        return pNext;
    }

    /**
     * @param pNext the pNext to set
     */
    public void setpNext(SimpleNode<T> pNext) { 
        this.pNext = pNext; 
    }
       
}