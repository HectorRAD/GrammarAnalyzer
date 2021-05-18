//Matematicas Computacionales 
//Hector Alvarez A01636166

class Node<T extends Comparable<?>> {
    Node<T> left, right;
    T data;

    public Node(T data) {
        this.data = data;
    }

    public T getData(){
        return this.data;
    }
}