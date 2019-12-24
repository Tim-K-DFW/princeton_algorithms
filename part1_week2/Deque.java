import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
  private int size;  // how to properly write <Item>?
  private Node first;
  private Node last;

  public Deque() {                           // construct an empty deque
    size = 0;
    first = null;
    last = null;
  }

  private class Node {
    private Item item;
    private Node below;  // same as "next" in single-stack, i.e. item that was added immediately prior
    private Node above;   // next item, i.e. the one right above this one
  }

  public int size() {                        // return the number of items on the deque
    return size;
  }

  public boolean isEmpty() {                 // is the deque empty?
    return size == 0;
  }

  public void addFirst(Item item) {          // add the item to the front (top)
    if (item == null) throw new IllegalArgumentException("Cannot add a NULL item.");

    Node newFirst = new Node();
    if (first != null) first.above = newFirst;
    Node oldfirst = first;
    first = newFirst;
    first.below = oldfirst;    // if this is the first item, it will be the only one with "NULL" in "below" field
    first.item = item;
    if (last == null) last = first;
    size++;
  }

  public void addLast(Item item) {          // add the item to the end (bottom)
    if (item == null) throw new IllegalArgumentException("Cannot add a NULL item.");

    Node newLast = new Node();
    if (last != null) last.below = newLast;
    Node oldlast = last;
    last = newLast;
    last.above = oldlast;
    last.item = item;
    if (first == null) first = last;
    size++; 
  }

  public Item removeFirst() {                // remove and return the item from the front
    if (isEmpty()) throw new java.util.NoSuchElementException("The deque is empty.");

    Item item = first.item;
    first = first.below;                    //      if this is the only item then "first" becomes null
    if (size > 1) first.above = null;       // if size == 1 then first and last point to the same object, we don't want to clean up last's "above"!
    if (size == 1) last = null;
    size--;
    return item;
  }

  public Item removeLast() {                 // remove and return the item from the end
    if (isEmpty()) throw new java.util.NoSuchElementException("The deque is empty.");

    Item item = last.item;
    last = last.above;
    if (size > 1) last.below = null;
    if (size == 1) first = null;
    size--;
    return item;
  }

  public Iterator<Item> iterator() {        // return an iterator over items in order from front to end
    return new DequeIterator(); 
  }
  
  private class DequeIterator implements Iterator<Item> {
    private Node current = first;

    public boolean hasNext() { return current != null; }
    public void remove() { throw new UnsupportedOperationException(); }

    public Item next() {
      if (current == null) throw new java.util.NoSuchElementException();

      Item item = current.item;
      current = current.below;
      return item;
    }
  }

  public static void main(String[] args) {  // unit testing
  }
}
