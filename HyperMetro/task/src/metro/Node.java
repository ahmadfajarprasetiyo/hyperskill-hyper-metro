package metro;

public class Node {
    private Node prev = null;
    private Node next = null;
    private String val;

    Node(String val) {
        this.val = val;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public Node getNext() {
        return next;
    }

    public Node getPrev() {
        return prev;
    }

    public String getVal() {
        return val;
    }

    public void addTailNode(Node node) {
        this.next = node;
        node.setPrev(this);
    }

    public void addHeadNode(Node node) {
        this.prev = node;
        node.setNext(this);
    }
}
