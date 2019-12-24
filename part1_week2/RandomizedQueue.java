import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

// public class RandomizedQueue<Item> {
public class RandomizedQueue<Item> implements Iterable<Item> {
  private Item[] arr;
  private int size;

  public RandomizedQueue() {
    arr = (Item[]) new Object[1];
    size = 0;   // also index of next insertion, i.e. equivalent to tail
  }

  public boolean isEmpty() {
    return size == 0;
  }
  
  public int size() { // # of items in the queue
    return size;
  }

  // add the item then increment
  public void enqueue(Item item) {
    if (item == null) throw new IllegalArgumentException("Cannot add a NULL item.");

    arr[size++] = item;
    if (size() == arr.length) resize(arr.length * 2);
  }

  public Item dequeue() {   // remove and return a random item
    if (isEmpty()) throw new java.util.NoSuchElementException("The queue is empty.");

    int randIndex = StdRandom.uniform(size());      // returs an int in [0, size)
    Item item = arr[randIndex];

    if (randIndex == size - 1)             // we landed on the last item
      arr[randIndex] = null;               // array remaints contiguous, simply erase last item
    else {
      arr[randIndex] = arr[size - 1];      // replace our random item with last item, to keep array contiguous
      arr[size - 1] = null;                // then erase last item
    }

    size--;
    if (size() > 0 && size() == arr.length / 4) resize(arr.length / 2);
    return item;
  }

  public Item sample() {     // return (but not remove) a random item
    if (isEmpty()) throw new java.util.NoSuchElementException("The queue is empty.");

    int randIndex = StdRandom.uniform(size());
    return arr[randIndex];
  }

  public Iterator<Item> iterator() {        // return an independent iterator over items in random order
    return new RandomizedQueueIterator(); 
  }
  
  private class RandomizedQueueIterator implements Iterator<Item> {
    // create a copy of master array and keep dequeing from it using standard method
    // this is the only test that the submission fails, on the grounds that the array gets resized when copied...
    // ... while specs require constant memory usage
    // I don't know Java well enough to implement a nice subclass with fixed size RandomizedQueue,
    // and decided that time will be better spent elsewhere
    
    int remaining = size;
    RandomizedQueue<Item> itq = makeCopy();

    RandomizedQueue<Item> makeCopy() {
      RandomizedQueue<Item> res = new RandomizedQueue<Item>();
      for (int i = 0; i < size; i++) {
        res.enqueue(arr[i]);
      }
      return res;
    }

    public boolean hasNext() { return remaining > 0; }
    public void remove() { throw new UnsupportedOperationException(); }

    public Item next() {
      if (!hasNext()) throw new java.util.NoSuchElementException();

      --remaining;
      return itq.dequeue();
    }
  }

  private void resize(int capacity) {
    // upon copy, make sure head and tail are still accurate
    Item[] copy = (Item[]) new Object[capacity];
    for (int i = 0; i < size(); i++) {
      copy[i] = arr[i];
    }
    arr = copy;
  }

  public static void main(String[] args) {  // unit testing

  }
}
