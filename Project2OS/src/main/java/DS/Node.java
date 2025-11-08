/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DS;

/**
 *
 * @author Gabriel Flores
 */
public class Node<T>{
    private T data;
    Node<T> next;
    
    public Node(T data){
        this.data=data;
        this.next= null;
    }

    public T getData() {
        return data;
    }

    public Node<T> getNext() {
        return next;
    }
    
    
}
