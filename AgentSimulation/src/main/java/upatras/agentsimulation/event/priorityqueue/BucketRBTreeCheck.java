/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.agentsimulation.event.priorityqueue;

import upatras.agentsimulation.event.TimeEvent;
import upatras.agentsimulation.event.priorityqueue.BucketRBTree.Node;

/**
 *
 * @author Paris
 */
public class BucketRBTreeCheck {
	final BucketRBTree tree;
	final Node root;


	public BucketRBTreeCheck(BucketRBTree tree) {
		this.tree=tree;
		this.root = tree.root;
		check();
	}
	
	
	
	/**
	 * *************************************************************************
	 * Check integrity of red-black tree data structure.
	 * *************************************************************************
	 */
	boolean check() {
		if (!isBST()) {
			System.out.println("Not in symmetric order");
		}
		if (!isSizeConsistent()) {
			System.out.println("Subtree counts not consistent");
		}
		if (!isRankConsistent()) {
			System.out.println("Ranks not consistent");
		}
		if (!is23()) {
			System.out.println("Not a 2-3 tree");
		}
		if (!isBalanced()) {
			System.out.println("Not balanced");
		}
		return isBST() && isSizeConsistent() && isRankConsistent() && is23() && isBalanced();
	}

	// does this binary tree satisfy symmetric order?
	// Note: this test also ensures that data structure is a binary tree since order is strict
	private boolean isBST() {
		return isBST(root, null, null);
	}

	// is the tree rooted at x a BST with all keys strictly between min and max
	// (if min or max is null, treat as empty constraint)
	// Credit: Bob Dondero's elegant solution
	private boolean isBST(BucketRBTree.Node x, TimeEvent min, TimeEvent max) {
		if (x == null) {
			return true;
		}
		if (min != null && x.event_bucket.compareTo(min) > 0) {
			return false;
		}
		if (max != null && x.event_bucket.compareTo(max) < 0) {
			return false;
		}
		return isBST(x.left, min, x.event_bucket.get(0)) && isBST(x.right, x.event_bucket.get(0), max);
	}

	// are the size fields correct?
	private boolean isSizeConsistent() {
		return isSizeConsistent(root);
	}

	private boolean isSizeConsistent(BucketRBTree.Node x) {
		if (x == null) {
			return true;
		}
		if (x.size != tree.size(x.left) + tree.size(x.right) + 1) {
			return false;
		}
		return isSizeConsistent(x.left) && isSizeConsistent(x.right);
	}

	// check that ranks are consistent
	private boolean isRankConsistent() {
		for (int i = 0; i < tree.size(); i++) {
			if (i != tree.rank(tree.select(i).get(0))) {
				return false;
			}
		}
		for (EventBucket bucket : tree.keys()) {
			TimeEvent key=bucket.get(0);
			if (key.compareTo(tree.select(tree.rank(key)).get(0)) != 0) {
				return false;
			}
		}
		return true;
	}

	// Does the tree have no red right links, and at most one (left)
	// red links in a row on any path?
	private boolean is23() {
		return is23(root);
	}

	private boolean is23(BucketRBTree.Node x) {
		if (x == null) {
			return true;
		}
		if (tree.isRed(x.right)) {
			return false;
		}
		if (x != root && tree.isRed(x) && tree.isRed(x.left)) {
			return false;
		}
		return is23(x.left) && is23(x.right);
	}

	// do all paths from root to leaf have same number of black edges?
	private boolean isBalanced() {
		int black = 0;     // number of black links on path from root to min
		BucketRBTree.Node x = root;
		while (x != null) {
			if (!tree.isRed(x)) {
				black++;
			}
			x = x.left;
		}
		return isBalanced(root, black);
	}

	// does every path from the root to a leaf have the given number of black links?
	private boolean isBalanced(BucketRBTree.Node x, int black) {
		if (x == null) {
			return black == 0;
		}
		if (!tree.isRed(x)) {
			black--;
		}
		return isBalanced(x.left, black) && isBalanced(x.right, black);
	}
}
