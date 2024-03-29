/*
 * Copyright (C) 2020 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2020 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package graph.problem1;

import java.util.LinkedList;
import java.util.List;

/**
 * IntQueue1 is our first implementation of a basic first-in, first-out queue for Integers.
 *
 * <p>An IntQueue can be described as [n1, n2, ..., n_k], where n1 is the least-recently-added item
 * in the queue and is the next item to be removed. n_k is the most-recently-added and will be the
 * last of the current elements to be removed.
 *
 * <p>An IntQueue can also be described constructively, with the append operation, ':', such that
 * [n1, n2, ..., n_k] : n_k+1 is the result of enqueing n_k+1 at the end of the queue.
 */
public final class IntQueue1 {
    // This class represents a queue as a linked list where the front of
    // the list corresponds to the front of the queue.

    // Abstraction Function:
    // IntQueue1, q, represents a mutable queue
    // i.e. sorted by first in first out
    // where the first entry in entries corresponds to the front of the queue,
    // i.e. the front is the first entry that was added.
    // If there are no entries, then IntQueue1 represents an empty queue.
    // front = get(0)
    // [] if q.size() == 0
    // [e_0, ..., e_i, ..., e_k] if q.size() != 0 and where e_0 was added first, e_k was added most recently,
    //                                                        and forall i (e_0 < e_i < e_k) where < designates e_0
    //                                                        was added before e_i which was added before e_k.

    // Representation Invariant for every IntQueue1 q:
    // entries != null
    // for all indices i such that 0 <= i <= k no element at that index e_i may be null
    // In other words:
    // The entries field always points to some usable object
    // and no entry in entries may be null

    /**
     * Elements in the queue.
     */
    List<Integer> entries;

    /**
     * @spec.effects constructs an empty queue
     */
    @SuppressWarnings("JdkObsolete")
    public IntQueue1() {
        entries = new LinkedList<Integer>();
        checkRep();
    }

    /**
     * Enqueue an item.
     *
     * @param entry item to be added to the queue
     * @throws IllegalArgumentException if entry is null
     * @spec.modifies this
     * @spec.effects places entry at the end of the queue
     */
    public void enqueue(Integer entry) {
        if (entry == null) {
            throw new IllegalArgumentException("entry cannot be null");
        }
        entries.add(entry);
        checkRep();
    }

    /**
     * Dequeue an item.
     *
     * @return the item that was first in the queue
     * @spec.requires size() &gt; 0
     * @spec.modifies this
     * @spec.effects removes the item at the front of the queue
     */
    public Integer dequeue() {
        Integer front = entries.remove(0);
        checkRep();
        return front;
    }

    /**
     * See the next item without removing it.
     *
     * @return the item currently first in the queue
     * @spec.requires size() &gt; 0
     */
    public Integer front() {
        return entries.get(0);
    }

    /**
     * @return number of elements in the queue
     */
    public int size() {
        return entries.size();
    }

    /**
     * @return size() == 0
     */
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    /**
     * Throws an exception if the representation invariant is violated.
     */
    private void checkRep() {
        // If I gave this to you, you wouldn't have the fun of figuring out the
        // rep invariant for yourself :)
    }
}
