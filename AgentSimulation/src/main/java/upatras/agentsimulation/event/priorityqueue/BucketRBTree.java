/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.event.priorityqueue;

import upatras.agentsimulation.event.TimeEvent;

import java.util.*;

/**
 * The {@code BST} class represents an ordered symbol table of generic key-value
 * pairs. It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 * <em>delete</em>, <em>size</em>, and <em>is-empty</em> methods. It also
 * provides ordered methods for finding the <em>minimum</em>,
 * <em>maximum</em>, <em>floor</em>, and <em>ceiling</em>. It also provides a
 * <em>keys</em> method for iterating over all of the keys. A symbol table
 * implements the <em>associative array</em> abstraction: when associating a
 * value with a key that is already in the symbol table, the convention is to
 * replace the old value with the new value. Unlike {@link java.util.Map}, this
 * class uses the convention that values cannot be {@code null}—setting the
 * value associated with a key to {@code null} is equivalent to deleting the key
 * from the symbol table.
 * <p>
 * This implementation uses a left-leaning red-black BST. It requires that the
 * key type implements the {@code Comparable} interface and calls the
 * {@code compareTo()} and method to compare two keys. It does not call either
 * {@code equals()} or {@code hashCode()}. The <em>put</em>, <em>contains</em>,
 * <em>remove</em>, <em>minimum</em>,
 * <em>maximum</em>, <em>ceiling</em>, and <em>floor</em> operations each take
 * logarithmic time in the worst case, if the tree becomes unbalanced. The
 * <em>size</em>, and <em>is-empty</em> operations take constant time.
 * Construction takes constant time.
 * <p>
 * For additional documentation, see
 * <a href="https://algs4.cs.princeton.edu/33balanced">Section 3.3</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne. For other
 * implementations of the same API, see
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class BucketRBTree extends AbstractQueue<TimeEvent>{

	private static final boolean RED = true;
	private static final boolean BLACK = false;

	protected Node root;     // root of the BST

	// BST helper node data type
	Node minnode;

	@Override
	public Iterator<TimeEvent> iterator() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean offer(TimeEvent e) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public TimeEvent poll() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	class Node {

		protected EventBucket event_bucket;         // associated data
		protected Node left, right;  // links to left and right subtrees
		protected boolean color;     // color of parent link
		protected int size;          // subtree count

		public Node(TimeEvent key, boolean color, int size) {
			this.event_bucket = new EventBucket(key);
			this.color = color;
			this.size = size;
		}
	}

	/**
	 * Initializes an empty symbol table.
	 */
	public BucketRBTree() {
	}

	/**
	 * *************************************************************************
	 * Node helper methods.
	 * *************************************************************************
	 */
	// is node x red; false if x is null ?
	protected boolean isRed(Node x) {
		if (x == null) {
			return false;
		}
		return x.color == RED;
	}

	// number of node in subtree rooted at x; 0 if x is null
	protected int size(Node x) {
		if (x == null) {
			return 0;
		}
		return x.size;
	}

	/**
	 * Returns the number of key-value pairs in this symbol table.
	 *
	 * @return the number of key-value pairs in this symbol table
	 */
	public int size() {
		return size(root);
	}

	/**
	 * Is this symbol table empty?
	 *
	 * @return {@code true} if this symbol table is empty and {@code false}
	 * otherwise
	 */
	public boolean isEmpty() {
		return root == null;
	}

	/**
	 * *************************************************************************
	 * Standard BST search.
	 * *************************************************************************
	 */
	/**
	 * Returns the value associated with the given key.
	 *
	 * @param key the key
	 * @return the value associated with the given key if the key is in the
	 * symbol table and {@code null} if the key is not in the symbol table
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public EventBucket get(TimeEvent key) {
		if (key == null) {
			throw new IllegalArgumentException("argument to get() is null");
		}
		return get(root, key);
	}

	// value associated with the given key in subtree rooted at x; null if no such key
	private EventBucket get(Node x, TimeEvent key) {
		while (x != null) {
			int cmp = x.event_bucket.compareTo(key);
			if (cmp < 0) {
				x = x.left;
			} else if (cmp > 0) {
				x = x.right;
			} else {
				return x.event_bucket;
			}
		}
		return null;
	}

	/**
	 * Does this symbol table contain the given key?
	 *
	 * @param key the key
	 * @return {@code true} if this symbol table contains {@code key} and
	 * {@code false} otherwise
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public boolean contains(TimeEvent key) {

		return get(key) != null;
	}

	/**
	 * *************************************************************************
	 * Red-black tree insertion.
	 * *************************************************************************
	 */
	/**
	 * Inserts the specified key-value pair into the symbol table, overwriting
	 * the old value with the new value if the symbol table already contains the
	 * specified key. Deletes the specified key (and its associated value) from
	 * this symbol table if the specified value is {@code null}.
	 *
	 * @param key the key
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public void put(TimeEvent key) {
		if (key == null) {
			throw new IllegalArgumentException("first argument to put() is null");
		}

		root = put(root, key, true);
		root.color = BLACK;
		// assert check();
	}

	// insert the key-value pair in the subtree rooted at h
	private Node put(Node h, TimeEvent key, boolean could_be_min) {
		if (h == null) {

			Node new_node = new Node(key, RED, 1);
			if (could_be_min) {
				minnode = new_node;
			}
			return new_node;
		}

		int cmp = h.event_bucket.compareTo(key);
		if (cmp < 0) {
			h.left = put(h.left, key, could_be_min);
		} else if (cmp > 0) {
			h.right = put(h.right, key, false);
		} else {
			h.event_bucket.add(key);
		}

		// fix-up any right-leaning links
		if (isRed(h.right) && !isRed(h.left)) {
			h = rotateLeft(h);
		}
		if (isRed(h.left) && isRed(h.left.left)) {
			h = rotateRight(h);
		}
		if (isRed(h.left) && isRed(h.right)) {
			flipColors(h);
		}
		h.size = size(h.left) + size(h.right) + 1;

		return h;
	}

	/**
	 * Removes the specified key and its associated value from this symbol table
	 * (if the key is in this symbol table).
	 *
	 * @param key the key
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public void delete(EventBucket key) {

		// if both children of root are black, set root to red
		if (!isRed(root.left) && !isRed(root.right)) {
			root.color = RED;
		}

		root = delete(root, null, key);
		if (!isEmpty()) {
			root.color = BLACK;
		}
		// assert check();
	}

	// delete the key-value pair with the given key rooted at h
	private Node delete(Node h, Node parent, EventBucket key) {
		// assert get(h, key) != null;

		if (h.event_bucket.compareTo(key) < 0) {
			if (!isRed(h.left) && !isRed(h.left.left)) {
				h = moveRedLeft(h);
			}
			h.left = delete(h.left, h, key);
		} else {
			if (isRed(h.left)) {
				h = rotateRight(h);
			}
			if (h.event_bucket.compareTo(key) == 0 && (h.right == null)) {
				if (h.equals(minnode)) {
					if (parent != null) {
						minnode = parent;
					} else {
						minnode = null;
					}
				}
				return null;
			}
			if (!isRed(h.right) && !isRed(h.right.left)) {
				h = moveRedRight(h);
			}
			if (h.event_bucket.compareTo(key) == 0) {

				Node x = min(h.right);
				h.event_bucket = x.event_bucket;
				// h.val = get(h.right, min(h.right).key);
				// h.key = min(h.right).key;
				h.right = deleteMin(h.right);

			} else {
				h.right = delete(h.right, h, key);
			}
		}

		return balance(h);

	}

	public TimeEvent remove() {

		EventBucket evb = minnode.event_bucket;
		TimeEvent ev = evb.remove();
		if (evb.isEmpty()) {
			delete(evb);
		}

		return ev;

	}

	public EventBucket removeBucket() {
		EventBucket bucket = minnode.event_bucket;
		delete(bucket);
		return bucket;
	}

	@Override
	public TimeEvent peek() {
		return min().peek();
	}

	@Override
	public boolean add(TimeEvent event) {
		put(event);
		return true;
	}

	//---------------------------------------------------------------------------------------------------------------
	/**
	 * *************************************************************************
	 * Red-black tree helper functions.
	 * *************************************************************************
	 */
	// make a left-leaning link lean to the right
	private Node rotateRight(Node h) {
		// assert (h != null) && isRed(h.left);
		Node x = h.left;
		h.left = x.right;
		x.right = h;
		x.color = x.right.color;
		x.right.color = RED;
		x.size = h.size;
		h.size = size(h.left) + size(h.right) + 1;
		return x;
	}

	// make a right-leaning link lean to the left
	private Node rotateLeft(Node h) {
		// assert (h != null) && isRed(h.right);
		Node x = h.right;
		h.right = x.left;
		x.left = h;
		x.color = x.left.color;
		x.left.color = RED;
		x.size = h.size;
		h.size = size(h.left) + size(h.right) + 1;
		return x;
	}

	// flip the colors of a node and its two children
	private void flipColors(Node h) {
		// h must have opposite color of its two children
		// assert (h != null) && (h.left != null) && (h.right != null);
		// assert (!isRed(h) &&  isRed(h.left) &&  isRed(h.right))
		//    || (isRed(h)  && !isRed(h.left) && !isRed(h.right));
		h.color = !h.color;
		h.left.color = !h.left.color;
		h.right.color = !h.right.color;
	}

	// Assuming that h is red and both h.left and h.left.left
	// are black, make h.left or one of its children red.
	private Node moveRedLeft(Node h) {
		// assert (h != null);
		// assert isRed(h) && !isRed(h.left) && !isRed(h.left.left);

		flipColors(h);
		if (isRed(h.right.left)) {
			h.right = rotateRight(h.right);
			h = rotateLeft(h);
			flipColors(h);
		}
		return h;
	}

	// Assuming that h is red and both h.right and h.right.left
	// are black, make h.right or one of its children red.
	private Node moveRedRight(Node h) {
		// assert (h != null);
		// assert isRed(h) && !isRed(h.right) && !isRed(h.right.left);
		flipColors(h);
		if (isRed(h.left.left)) {
			h = rotateRight(h);
			flipColors(h);
		}
		return h;
	}

	// restore red-black tree invariant
	private Node balance(Node h) {
		// assert (h != null);

		if (isRed(h.right)) {
			h = rotateLeft(h);
		}
		if (isRed(h.left) && isRed(h.left.left)) {
			h = rotateRight(h);
		}
		if (isRed(h.left) && isRed(h.right)) {
			flipColors(h);
		}

		h.size = size(h.left) + size(h.right) + 1;
		return h;
	}

	/**
	 * *************************************************************************
	 * Utility functions.
	 * *************************************************************************
	 */
	/**
	 * Returns the height of the BST (for debugging).
	 *
	 * @return the height of the BST (a 1-node tree has height 0)
	 */
	public int height() {
		return height(root);
	}

	private int height(Node x) {
		if (x == null) {
			return -1;
		}
		return 1 + Math.max(height(x.left), height(x.right));
	}

	/**
	 * *************************************************************************
	 * Ordered symbol table methods.
	 * *************************************************************************
	 */
	/**
	 * Returns the smallest key in the symbol table.
	 *
	 * @return the smallest key in the symbol table
	 * @throws NoSuchElementException if the symbol table is empty
	 */
	public EventBucket min() {
		if (isEmpty()) {
			throw new NoSuchElementException("calls min() with empty symbol table");
		}
		return min(root).event_bucket;
	}

	// the smallest key in subtree rooted at x; null if no such key
	private Node min(Node x) {
		// assert x != null;
		if (x.left == null) {
			return x;
		} else {
			return min(x.left);
		}
	}

	/**
	 * Returns the largest key in the symbol table.
	 *
	 * @return the largest key in the symbol table
	 * @throws NoSuchElementException if the symbol table is empty
	 */
	public EventBucket max() {
		if (isEmpty()) {
			throw new NoSuchElementException("calls max() with empty symbol table");
		}
		return max(root).event_bucket;
	}

	// the largest key in the subtree rooted at x; null if no such key
	private Node max(Node x) {
		// assert x != null;
		if (x.right == null) {
			return x;
		} else {
			return max(x.right);
		}
	}

	/**
	 * Returns the largest key in the symbol table less than or equal to
	 * {@code key}.
	 *
	 * @param key the key
	 * @return the largest key in the symbol table less than or equal to
	 * {@code key}
	 * @throws NoSuchElementException if there is no such key
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public EventBucket floor(TimeEvent key) {
		if (key == null) {
			throw new IllegalArgumentException("argument to floor() is null");
		}
		if (isEmpty()) {
			throw new NoSuchElementException("calls floor() with empty symbol table");
		}
		Node x = floor(root, key);
		if (x == null) {
			return null;
		} else {
			return x.event_bucket;
		}
	}

	// the largest key in the subtree rooted at x less than or equal to the given key
	private Node floor(Node x, TimeEvent key) {
		if (x == null) {
			return null;
		}
		int cmp = x.event_bucket.compareTo(key);
		if (cmp == 0) {
			return x;
		}
		if (cmp < 0) {
			return floor(x.left, key);
		}
		Node t = floor(x.right, key);
		if (t != null) {
			return t;
		} else {
			return x;
		}
	}

	/**
	 * Returns the smallest key in the symbol table greater than or equal to
	 * {@code key}.
	 *
	 * @param key the key
	 * @return the smallest key in the symbol table greater than or equal to
	 * {@code key}
	 * @throws NoSuchElementException if there is no such key
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public EventBucket ceiling(TimeEvent key) {
		if (key == null) {
			throw new IllegalArgumentException("argument to ceiling() is null");
		}
		if (isEmpty()) {
			throw new NoSuchElementException("calls ceiling() with empty symbol table");
		}
		Node x = ceiling(root, key);
		if (x == null) {
			return null;
		} else {
			return x.event_bucket;
		}
	}

	// the smallest key in the subtree rooted at x greater than or equal to the given key
	private Node ceiling(Node x, TimeEvent key) {
		if (x == null) {
			return null;
		}
		int cmp = x.event_bucket.compareTo(key);
		if (cmp == 0) {
			return x;
		}
		if (cmp > 0) {
			return ceiling(x.right, key);
		}
		Node t = ceiling(x.left, key);
		if (t != null) {
			return t;
		} else {
			return x;
		}
	}

	/**
	 * Return the kth smallest key in the symbol table.
	 *
	 * @param k the order statistic
	 * @return the {@code k}th smallest key in the symbol table
	 * @throws IllegalArgumentException unless {@code k} is between 0 and
	 * <em>n</em>–1
	 */
	public EventBucket select(int k) {
		if (k < 0 || k >= size()) {
			throw new IllegalArgumentException("argument to select() is invalid: " + k);
		}
		Node x = select(root, k);
		return x.event_bucket;
	}

	// the key of rank k in the subtree rooted at x
	private Node select(Node x, int k) {
		// assert x != null;
		// assert k >= 0 && k < size(x);
		int t = size(x.left);
		if (t > k) {
			return select(x.left, k);
		} else if (t < k) {
			return select(x.right, k - t - 1);
		} else {
			return x;
		}
	}

	/**
	 * Return the number of keys in the symbol table strictly less than
	 * {@code key}.
	 *
	 * @param key the key
	 * @return the number of keys in the symbol table strictly less than
	 * {@code key}
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public int rank(TimeEvent key) {
		if (key == null) {
			throw new IllegalArgumentException("argument to rank() is null");
		}
		return rank(key, root);
	}

	// number of keys less than key in the subtree rooted at x
	private int rank(TimeEvent key, Node x) {
		if (x == null) {
			return 0;
		}
		int cmp = x.event_bucket.compareTo(key);
		if (cmp < 0) {
			return rank(key, x.left);
		} else if (cmp > 0) {
			return 1 + size(x.left) + rank(key, x.right);
		} else {
			return size(x.left);
		}
	}

	/**
	 * *************************************************************************
	 * Range count and range search.
	 * *************************************************************************
	 */
	/**
	 * Returns all keys in the symbol table as an {@code Iterable}. To iterate
	 * over all of the keys in the symbol table named {@code st}, use the
	 * foreach notation: {@code for (Key key : st.keys())}.
	 *
	 * @return all keys in the symbol table as an {@code Iterable}
	 */
	public Iterable<EventBucket> keys() {
		if (isEmpty()) {
			return new PriorityQueue<>();
		}
		return keys(min().get(0), max().get(0));
	}

	/**
	 * Returns all keys in the symbol table in the given range, as an
	 * {@code Iterable}.
	 *
	 * @param lo minimum endpoint
	 * @param hi maximum endpoint
	 * @return all keys in the sybol table between {@code lo} (inclusive) and
	 * {@code hi} (inclusive) as an {@code Iterable}
	 * @throws IllegalArgumentException if either {@code lo} or {@code hi} is
	 * {@code null}
	 */
	public Iterable<EventBucket> keys(TimeEvent lo, TimeEvent hi) {
		if (lo == null) {
			throw new IllegalArgumentException("first argument to keys() is null");
		}
		if (hi == null) {
			throw new IllegalArgumentException("second argument to keys() is null");
		}

		Queue<EventBucket> queue = new LinkedList<>();
		// if (isEmpty() || lo.compareTo(hi) > 0) return queue;
		keys(root, queue, lo, hi);
		return queue;
	}

	// add the keys between lo and hi in the subtree rooted at x
	// to the queue
	private void keys(Node x, Queue<EventBucket> queue, TimeEvent lo, TimeEvent hi) {
		if (x == null) {
			return;
		}
		int cmplo = x.event_bucket.compareTo(lo);
		int cmphi = x.event_bucket.compareTo(hi);
		if (cmplo < 0) {
			keys(x.left, queue, lo, hi);
		}
		if (cmplo <= 0 && cmphi >= 0) {
			queue.add(x.event_bucket);
		}
		if (cmphi > 0) {
			keys(x.right, queue, lo, hi);
		}
	}

	/**
	 * Returns the number of keys in the symbol table in the given range.
	 *
	 * @param lo minimum endpoint
	 * @param hi maximum endpoint
	 * @return the number of keys in the sybol table between {@code lo}
	 * (inclusive) and {@code hi} (inclusive)
	 * @throws IllegalArgumentException if either {@code lo} or {@code hi} is
	 * {@code null}
	 */
	public int size(TimeEvent lo, TimeEvent hi) {
		if (lo == null) {
			throw new IllegalArgumentException("first argument to size() is null");
		}
		if (hi == null) {
			throw new IllegalArgumentException("second argument to size() is null");
		}

		if (lo.compareTo(hi) > 0) {
			return 0;
		}
		if (contains(hi)) {
			return rank(hi) - rank(lo) + 1;
		} else {
			return rank(hi) - rank(lo);
		}
	}

	/**
	 * *************************************************************************
	 * Red-black tree deletion.
	 * *************************************************************************
	 */
	/**
	 * Removes the smallest key and associated value from the symbol table.
	 *
	 * @throws NoSuchElementException if the symbol table is empty
	 */
	public void deleteMin() {
		if (isEmpty()) {
			throw new NoSuchElementException("BST underflow");
		}

		// if both children of root are black, set root to red
		if (!isRed(root.left) && !isRed(root.right)) {
			root.color = RED;
		}

		root = deleteMin(root);
		if (!isEmpty()) {
			root.color = BLACK;
		}
		// assert check();
	}

	// delete the key-value pair with the minimum key rooted at h
	private Node deleteMin(Node h) {
		if (h.left == null) {
			return null;
		}

		if (!isRed(h.left) && !isRed(h.left.left)) {
			h = moveRedLeft(h);
		}

		h.left = deleteMin(h.left);
		return balance(h);
	}

	/**
	 * Removes the largest key and associated value from the symbol table.
	 *
	 * @throws NoSuchElementException if the symbol table is empty
	 */
	public void deleteMax() {
		if (isEmpty()) {
			throw new NoSuchElementException("BST underflow");
		}

		// if both children of root are black, set root to red
		if (!isRed(root.left) && !isRed(root.right)) {
			root.color = RED;
		}

		root = deleteMax(root);
		if (!isEmpty()) {
			root.color = BLACK;
		}
		// assert check();
	}

	// delete the key-value pair with the maximum key rooted at h
	private Node deleteMax(Node h) {
		if (isRed(h.left)) {
			h = rotateRight(h);
		}

		if (h.right == null) {
			return null;
		}

		if (!isRed(h.right) && !isRed(h.right.left)) {
			h = moveRedRight(h);
		}

		h.right = deleteMax(h.right);

		return balance(h);
	}

}
