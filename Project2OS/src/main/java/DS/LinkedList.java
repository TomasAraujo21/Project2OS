/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DS;

/**
 *
 * @author Gabriel Flores
 */
public class LinkedList<T> {
    private Node<T> head;
    private int size;
    
    public LinkedList() {
        this.head=null;
        this.size=0;
    }

    public Node<T> getHead() {
        return head;
    }
    
    
    //Agrega un nuevo nodo con los datos proporcionados al final de la lista enlazada.
    public void add(T data){
        Node<T> newOne = new Node<>(data);
        if (head == null){
            head = newOne;
        } else {
            Node<T> actual = head;
            while (actual.next != null){
                actual = actual.next;
            }
            actual.next=newOne;
        }
        size ++;
    }
    
    //Elimina el primer nodo que contenga los datos especificados de la lista enlazada.
    public boolean remove (T data){
        if(head==null) return false;
        
        if (head.getData().equals(data)){
            head = head.next;
            size--;
            return true;
    }
    Node <T> actual = head;
    while (actual.next != null && !actual.next.getData().equals(data)){
        actual = actual.next;
    }
    if (actual.next != null){
        actual.next = actual.next.next;
        size--;
        return true;
    }
    return false;
    }
    
    //Verifica si un nodo se encuentra dentro de la lista enlazada.
    public boolean include (T data){
        Node<T> actual = head;
        while (actual!= null){
            if (actual.getData().equals(data)){
                return true;
            }
            actual = actual.next;
        }
        return false;
    }
    
    //Funcion para mostrar la informacion almacenada dentro del nodo
    public void print(){
        Node<T> actual = head;
        while (actual != null){
            System.out.println(actual.getData()+"===>");
            actual = actual.next;
        }
    }
    
    //Obtiene los datos del nodo en la posición especificada por el índice.
    public T obtain (int index){
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }
        Node<T> actual = head;
        for (int i = 0; i < index; i++) {
            actual = actual.next; 
        }
        return actual.getData();
    }
    
    
    //Inserta un nuevo nodo con los datos proporcionados en la posición especificada.
    public void insert(int index, T data){
        if(index < 0 || index > size){
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }
        Node <T> newOne = new Node<>(data);
        if(index ==0){
            newOne.next = head;
            head = newOne;
        } else {
            Node<T> actual = head;
            for (int i = 0; i < index; i++) {
                actual = actual.next;
            }
            newOne.next=actual.next;
            actual.next=newOne;
        }
        size++;
    }
    
    
    //Funcion para limpiar la lista enlazada
    public void clean(){
        head = null;
        size = 0;
    }
}
