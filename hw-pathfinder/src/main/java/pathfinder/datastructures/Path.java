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

package pathfinder.datastructures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This represents an immutable path particularly
 * Path#getStart() and Path#getEnd(). Also contains a cached
 * version of the total cost along this path, for efficient repeated access.
 */
public class Path<T> implements Iterable<Path<T>.Segment>, Comparable<Path<T>> {

    // AF(this) =
    //      the start of the path => start
    //      each "step" along the path between segments => elements of list path, where
    //          path.get(0) is the first step from the start to an intermediate segment, and
    //          path.get(path.size() - 1) is the final step from an intermediate segment to the end
    //      total cost along the path => cost
    //      the destination in this path, opposite the start => getEnd()

    // Rep Invariant:
    //      cost >= 0 &&
    //      Double.isFinite(cost) &&
    //      start != null &&
    //      path != null &&
    //      path does not contain null elements

    /**
     * The total cost along all the segments in this path.
     */
    private double cost;

    /**
     * The beginning of this path.
     */
    private T start;

    /**
     * The ordered sequence of segments representing a path.
     */
    private List<Segment> path;

    /**
     * Creates a new, empty path. Essentially this represents a path
     * from the start to itself with a total cost of "0".
     *
     * @param start The start of the path.
     */
    public Path(T start) {
        this.start = start;
        this.cost = 0;
        this.path = new ArrayList<>();
        checkRep();
    }

    /**
     * Appends a new single segment to the end of this path, originating at the current last segment
     * in this path and terminating at {@code newEnd}. The cost of adding this additional segment
     * to the existing path is {@code segmentCost}. Thus, the returned Path represents a path
     * from {@code this.getStart()} to {@code newEnd}, with a cost of {@code this.getCost() +
     * segmentCost}.
     *
     * @param newEnd      The segment being appended to this path
     * @param segmentCost The cost of the segment being added to the end of this path.
     * @return A new path representing the current path with the given segment appended to the end.
     */
    public Path<T> extend(T newEnd, double segmentCost) {
        checkRep();
        //
        Path<T> extendedPath = new Path<>(start);
        extendedPath.path.addAll(this.path);
        extendedPath.path.add(new Segment(this.getEnd(), newEnd, segmentCost));
        extendedPath.cost = this.cost + segmentCost;
        //
        extendedPath.checkRep();
        checkRep();
        //
        return extendedPath;
    }

    /**
     * @return The total cost along this path.
     */
    public double getCost() {
        return cost;
    }

    /**
     * @return The start of this path.
     */
    public T getStart() {
        return start;
    }

    /**
     * @return The end of this path, which may be the start if this path
     * contains no segments (i.e. this path is from the start to itself).
     */
    public T getEnd() {
        if (path.size() == 0) {
            return start;
        }
        return path.get(path.size() - 1).getEnd();
    }

    /**
     * @return An iterator of the segments in this path, in order, beginning from the start
     * and ending at the end. In the case that this path represents a path between
     * the start and itself, this iterator contains no elements. This iterator does not
     * support the optional Iterator#remove() operation and will throw an
     * UnsupportedOperationException if Iterator#remove() is called.
     */
    @Override
    public Iterator<Segment> iterator() {
        // Create a wrapping iterator to guarantee exceptional behavior on Iterator#remove.
        return new Iterator<>() {

            private Iterator<Segment> backingIterator = path.iterator();

            @Override
            public boolean hasNext() {
                return backingIterator.hasNext();
            }

            @Override
            public Path<T>.Segment next() {
                return backingIterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Paths may not be modified.");
            }
        };
    }

    /**
     * Ensures that the representation invariant has not been violated. Returns normally if
     * there is no violation.
     */
    private void checkRep() {
        assert cost >= 0;
        assert Double.isFinite(cost);
        assert start != null;
        assert path != null;
        for (Segment segment : path) {
            assert segment != null;
        }
    }

    /**
     * Checks this path for equality with another object. Two paths are equal if and only if
     * they contain exactly the same sequence of segments in the same order. In the case that
     * both paths are empty, they are only equal if their start is equal.
     *
     * @param obj The object to compare with {@code this}.
     * @return {@literal true} if and only if {@code obj} is equal to {@code this}.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Path)) {
            return false;
        }
        Path<?> other = (Path<?>) obj;
        if (this.path.size() != other.path.size()) {
            return false;
        }
        if (this.path.size() == 0 && !this.start.equals(other.start)) {
            return false;
        }
        for (int i = 0; i < this.path.size(); i++) {
            if (!this.path.get(i).equals(other.path.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (31 * start.hashCode()) + path.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(start.toString());
        for (Segment segment : path) {
            sb.append(" =(");
            sb.append(String.format("%.3f", segment.getCost()));
            sb.append(")=> ");
            sb.append(segment.getEnd().toString());
        }
        return sb.toString();
    }

    @Override
    public int compareTo(Path<T> o) {
        return Double.compare(this.getCost(), o.getCost());
    }

    /**
     * Segment represents a single segment as part of a longer, more complex path.
     * Segments are immutable parts of a larger path that cannot be instantiated directly, and
     * are created as part of larger paths by calling Path#extend(T, double).
     */
    public class Segment {

        // AF(this) = the beginning of the path segment => start
        //            the end of the path segment => end
        //            the cost of travelling along this segment => cost

        // Rep. Invariant = start != null
        //                  && end != null
        //                  && Double.isFinite(cost)

        /**
         * The beginning of this segment.
         */
        private final T start;

        /**
         * The end of this segment.
         */
        private final T end;

        /**
         * The cost of travelling this segment.
         */
        private final double cost;

        /**
         * Constructs a new segment with the provided characteristics.
         *
         * @param start The start of this segment.
         * @param end   The end of this segment.
         * @param cost  The cost of travelling this segment.
         * @throws NullPointerException     if either start or end is null.
         * @throws IllegalArgumentException if cost is infinite or NaN
         */
        private Segment(T start, T end, double cost) {
            if (start == null || end == null) {
                throw new NullPointerException("Segments cannot have null ends.");
            }
            if (!Double.isFinite(cost)) {
                throw new IllegalArgumentException("Segment cost may not be NaN or infinite.");
            }
            this.start = start;
            this.end = end;
            this.cost = cost;
            // checkRep not necessary: it's impossible for this constructor to create a Segment that
            // violates the rep invariant because of the exception check, and all fields are final
            // and immutable themselves.
        }

        /**
         * @return The beginning of this segment.
         */
        public T getStart() {
            return this.start;
        }

        /**
         * @return The ending of this segment.
         */
        public T getEnd() {
            return this.end;
        }

        /**
         * @return The cost of this segment.
         */
        public double getCost() {
            return this.cost;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append(start.toString());
            sb.append(" -> ");
            sb.append(end.toString());
            sb.append(" (");
            sb.append(String.format("%.3f", cost));
            sb.append(")]");
            return sb.toString();
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            Segment other = (Path<T>.Segment) obj;
            return other.getStart().equals(this.getStart())
                   && other.getEnd().equals(this.getEnd())
                   && (Double.compare(this.cost, other.cost) == 0);
        }

        @Override
        public int hashCode() {
            int result = start.hashCode();
            result += (31 * result) + end.hashCode();
            result += (31 * result) + Double.hashCode(cost);
            return result;
        }

    }
}