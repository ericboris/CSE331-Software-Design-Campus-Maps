package graph;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <b>Graph</b> is a mutable representation of a directed, labeled graph composed of
 * nodes and edges.
 */
public class Graph<N, E> {
    /**
     * Run the non-loop asserts in checkRep.
     */
    private static final boolean CHECK_REP_SHALLOW = true;

    /**
     * Run the loop and non-loop asserts in checkRep.
     */
    private static final boolean CHECK_REP_DEEP = false;

    /**
     * The mapping of nodes to edges in the graph.
     */
    private final Map<Node<N>, Set<Edge<E>>> nodeMap;

    /**
     * The mapping of edges to nodes in the graph.
     */
    private final Map<Edge<E>, Set<Node<N>>> edgeMap;

    // Abstraction Function:
    // A graph G such that:
    //      nodeMap = the mapping of Nodes N to Edge Sets E in G
    //      nodeMap.get(n) = the Edge Set E_n for a Node n for all n in N
    //      E[i] = the ith Edge e_i for all Edges in E
    //
    //      edgeMap = the mapping of Edges E to Node Sets N in G
    //      edgeMap.get(e) = the Edge Set N_e for an Edge e for all e in E
    //      N[i] = the ith Node e_i for all Nodes in N


    // Representation Invariant:
    // for each Node n in nodeMap:
    //      n != null
    //      for each Set<Edge> k such that n maps to k in nodeMap:
    //          k != null
    //          for each edge e in k:
    //              Node e.src is in nodeMap
    //              Node e.dst is in nodeMap
    //          for each pair of Edges e, f in k:
    //              e != f
    // for each Edge e in edgeMap:
    //      e != null
    //      for each Set<Node> k such that e maps to k in nodeMap:
    //          k != null
    //          for each Node n in k:
    //              Node n is in edgeMap
    // (It's implied that there are no null fields in Graph)

    /**
     * Constructs a new empty Graph G.
     *
     * @spec.effects Constructs a new empty Graph
     */
    public Graph() {
        nodeMap = new HashMap<>();
        edgeMap = new HashMap<>();
        checkRep();
    }

    /**
     * Adds a new Node n to G such that n.label == label.
     * Does nothing if n.label == label already in G.
     *
     * @param label the label of the Node n to add to G
     * @spec.requires label != null
     * @spec.modifies this
     */
    public void addNode(N label) {
        checkRep();
        nodeMap.putIfAbsent(new Node<>(label), new HashSet<>());
        checkRep();
    }

    /**
     * Convenience method for adding non-unique single source edge to the graph
     * Connects src to all other edges with the same label.
     *
     * @param src the label of the source Node s that e connects to
     * @param label the label of this Edge e
     * @spec.requires src != null
     * @spec.requires label != null
     * @spec.modifies this
     * @throws IllegalArgumentException if e.src not already in the graph
     */
    public void addEdge(N src, E label) {
        addEdge(src, label, false);
    }

    /**
     * Adds a single source edge to the graph.
     * Connects src to all other edges with the same label.
     *
     * @param src the label of the source Node s that e connects to
     * @param label the label of this Edge e
     * @param isUnique if true, make the Edge unique
     * @spec.requires src != null
     * @spec.requires label != null
     * @spec.modifies this
     * @throws IllegalArgumentException if e.src not already in the graph
     */
    public void addEdge(N src, E label, boolean isUnique) {
        checkRep();
        Node<N> s = new Node<>(src);
        Edge<E> e = new Edge<>(label, isUnique);
        // Only add the edge if src is already in the graph
        if (nodeMap.containsKey(s)) {
            // Add the edge to the node's edges
            nodeMap.get(s).add(e);
            // Add the node to the edge's nodes
            edgeMap.putIfAbsent(e, new HashSet<>());
            edgeMap.get(e).add(s);
        } else {
            throw new IllegalArgumentException("src must be the label of node already in the Graph.");
        }
        checkRep();
    }

    /**
     * Convenience method for adding a non-unique directed Edge e to G from src to dst.
     * Does nothing if for any Edge f in Edges E, e == f.
     *
     * @param src the label of the source Node s that e connects to
     * @param dst the label of the destination Node d that e connects to
     * @param label the label of this Edge e
     * @spec.requires src != null
     * @spec.requires dst != null
     * @spec.requires label != null
     * @spec.modifies this
     * @throws IllegalArgumentException if e.src or e.dst not already in the graph
     */
    public void addEdge(N src, N dst, E label) {
        addEdge(src, dst, label, false);
    }

    /**
     * Adds a new directed Edge e to G from src to dst.
     * Does nothing if for any Edge f in Edges E, e == f.
     *
     * @param src the label of the source Node s that e connects to
     * @param dst the label of the destination Node d that e connects to
     * @param label the label of this Edge e
     * @param isUnique if true, makes the Edge unique
     * @spec.requires src != null
     * @spec.requires dst != null
     * @spec.requires label != null
     * @spec.modifies this
     * @throws IllegalArgumentException if e.src or e.dst not already in the graph
     */
    public void addEdge(N src, N dst, E label, boolean isUnique) {
        checkRep();
        Node<N> s = new Node<>(src);
        Node<N> d = new Node<>(dst);
        boolean isSelfLoop = s.equals(d);
        Edge<E> e = new Edge<>(label, isUnique, isSelfLoop);

        // Only add the directed edge if both src and dst are already in the graph
        if (nodeMap.containsKey(s) && nodeMap.containsKey(d)) {
            // Add the edge to src's edges
            nodeMap.get(s).add(e);
            // Add dst to edge's nodes
            edgeMap.putIfAbsent(e, new HashSet<>());
            edgeMap.get(e).add(d);
        } else {
            throw new IllegalArgumentException("src and dst must be the labels of nodes already in the Graph.");
        }
    checkRep();
    }

    /**
     * Convenience method for adding a non-unique bidirectional Edge e to G between src and dst.
     * Does nothing if for any Edge f in Edges E, e == f.
     *
     * @param src the label of the source Node s that e connects to
     * @param dst the label of the destination Node d that e connects to
     * @param label the label of this Edge e
     * @spec.requires src != null
     * @spec.requires dst != null
     * @spec.requires label != null
     * @spec.modifies this
     * @throws IllegalArgumentException if e.src or e.dst not already in the graph
     */
    public void addBiEdge(N src, N dst, E label) {
        addBiEdge(src, dst, label, false);
    }

    /**
     * Adds a new bidirectional Edge e to G between src and dst.
     * Does nothing if for any Edge f in Edges E, e == f.
     *
     * @param src the label of the source Node s that e connects to
     * @param dst the label of the destination Node d that e connects to
     * @param label the label of this Edge e
     * @param isUnique if true, makes the Edge unique
     * @spec.requires src != null
     * @spec.requires dst != null
     * @spec.requires label != null
     * @spec.modifies this
     * @throws IllegalArgumentException if e.src or e.dst not already in the graph
     */
    public void addBiEdge(N src, N dst, E label, boolean isUnique) {
        checkRep();
        Node<N> s = new Node<>(src);
        Node<N> d = new Node<>(dst);
        Edge<E> e = new Edge<>(label, isUnique);
        // Only add the directed edge if both src and dst are already in the graph
        if (nodeMap.containsKey(s) && nodeMap.containsKey(d)) {
            // Add the edge to src and dst's edges
            nodeMap.get(s).add(e);
            nodeMap.get(d).add(e);
            // Add src and dst to edge's nodes
            edgeMap.putIfAbsent(e, new HashSet<>());
            edgeMap.get(e).add(s);
            edgeMap.get(e).add(d);
        } else {
            throw new IllegalArgumentException("src and dst must be the labels of nodes already in the Graph.");
        }
        checkRep();
    }

    /**
     * Returns a complete, sorted list of Node Labels N in the Graph G.
     * If the graph is empty returns an empty list.
     *
     * @return a sorted list of Node Labels in the Graph.
     */
    public List<N> listNodeLabels() {
        checkRep();
        List<N> nodes = nodeMap.keySet().parallelStream()
                .map(Node::getLabel)
                .sorted()
                .collect(Collectors.toList());
        checkRep();
        return nodes;
    }

    /**
     * Returns a sorted list of Node Labels M that are the children of node n in Graph G.
     * Each element in the list is of the form dstLabel(edgeLabel).
     *
     * @param srcLabel the label of the node to get the children of
     * @spec.requires nodeLabel != null
     * @return a sorted list of Nodes with Labels that are the children of the Node with nodeLabel
     */
    public List<String> listChildrenLabels(N srcLabel) {
        checkRep();
        Node<N> s = new Node<>(srcLabel);

        // Return an empty list if src is not in the graph
        if (!nodeMap.containsKey(s)) {
            return new ArrayList<>();
        }

        // Get the set of nodes that are children to src
        Set<Node<N>> childrenSet = getChildren(s);
        List<Edge<E>> edgesList = new ArrayList<>(getEdges(s));

        // Add each child and it's incident edges to a list as strings and sort
        List<String> children = new ArrayList<>();
        for (Edge<E> e : edgesList) {
            Set<Node<N>> nodes = getNodes(e);
            for (Node<N> child : childrenSet) {
                boolean includeChildEqualsParent = e.getIsSelfLoop() == child.equals(s);
                if (nodes.contains(child) && includeChildEqualsParent) {
                    String text = child.toString() + "(" + e.toString() + ")";
                    children.add(text);
                }
            }
        }
        Collections.sort(children);

        checkRep();
        return children;
    }

    /**
     * Returns an unmodifiable set of nodes connected by the given edge.
     * Returns an empty set if the edge not in the graph.
     *
     * @spec.requires the edge be in the graph
     * @param edgeLabel the edge connecting the nodes
     * @return the nodes connected by the given edge
     */
    public Set<N> getNodeLabels(E edgeLabel) {
        return getNodeLabels(new Edge<>(edgeLabel));
    }

    /**
     * Returns an unmodifiable set of nodes connected by the given edge.
     * Returns an empty set if the edge not in the graph.
     *
     * @spec.requires the edge be in the graph
     * @param e the edge connecting the nodes
     * @return the nodes connected by the given edge
     */
    public Set<N> getNodeLabels(Edge<E> e) {
        checkRep();
        if (!edgeMap.containsKey(e)) {
            return new HashSet<>();
        }
        checkRep();
        return edgeMap.get(e).parallelStream()
                .map(Node::getLabel)
                .collect(Collectors.toUnmodifiableSet());
    }

    /** Returns the nodes connected to edge E
     *
     * @param e the edge to get the nodes of
     * @return the nodes connected to edge e
     */
    public Set<Node<N>> getNodes(Edge<E> e) {
        checkRep();
        if (!edgeMap.containsKey(e)) {
            return new HashSet<>();
        }
        checkRep();
        return Collections.unmodifiableSet(edgeMap.get(e));
    }

    /**
     * Returns an unmodifiable set of edges connected to the given node.
     * Returns an empty set if the node not in the graph.
     *
     * @spec.requires the node be in the graph
     * @param nodeLabel the node to find the edges connected to
     * @return the edges connected to the given node
     */
    public Set<E> getEdgeLabels(N nodeLabel) {
        checkRep();
        Node<N> n = new Node<>(nodeLabel);
        if (!nodeMap.containsKey(n)) {
            return new HashSet<>();
        }
        checkRep();
        return nodeMap.get(n).parallelStream()
                .map(Edge::getLabel)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns the edges connected to node n
     *
     * @param n the node to get the edges of
     * @return the edges connected to n
     */
    public Set<Edge<E>> getEdges(Node<N> n) {
        checkRep();
        if (!nodeMap.containsKey(n)) {
            return new HashSet<>();
        }
        checkRep();
        return Collections.unmodifiableSet(nodeMap.get(n));
    }

    /**
     * Returns the edges connected to node n
     *
     * @param n the node to get the edges of
     * @return the edges connected to n
     */
    public Set<Edge<E>> getEdges(N n) {
        checkRep();
        Node<N> node = new Node<>(n);
        checkRep();
        return getEdges(node);
    }

    /**
     * Gets the set of children's labels unique to the given node.
     *
     * @param nodeLabel the node to get the children of
     * @return a set of node labels of nodes that are children of the given node
     */
    public Set<N> getChildrenLabels(N nodeLabel) {
        checkRep();
        Node<N> n = new Node<>(nodeLabel);

        // get all edges incident to n
        Set<Edge<E>> edgesSet = new HashSet<>(nodeMap.get(n));

        // get the set of all nodes connected to all edges of n
        Set<Node<N>> nodeSet = new HashSet<>();
        edgesSet.parallelStream().forEach(e -> nodeSet.addAll(edgeMap.get(e)));

        // get the labels of all nodes that are children to n
        Set<N> childrenSet = new HashSet<>();
        nodeSet.parallelStream()
                .filter(node -> !nodeLabel.equals(node.getLabel()))
                .forEach(node -> childrenSet.add(node.getLabel()));

        checkRep();
        return childrenSet;
    }

    /**
     * Returns the children of the given node.
     *
     * @param n the node to get the children of
     * @return the children of the given node
     * @spec.requires n in the graph
     * @spec.requires n != null
     */
    public Set<Node<N>> getChildren(Node<N> n) {
        checkRep();
        // get all edges incident to n
        Set<Edge<E>> edgesSet = new HashSet<>(nodeMap.get(n));

        // get the labels of all nodes that are children to n
        Set<Node<N>> childrenSet = new HashSet<>();
        for (Edge<E> e : edgesSet) {
            Set<Node<N>> nodes = edgeMap.get(e);
            for (Node<N> ne : nodes) {
                if (ne != n) {
                    childrenSet.add(ne);
                }
            }
        }

        checkRep();
        return Collections.unmodifiableSet(childrenSet);
    }

    /**
     * Returns true if the queried Node n is in the graph and false otherwise.
     *
     * @param node the label of the node to query
     * @return true if the node is in the graph and false otherwise
     * @throws IllegalArgumentException if node is null
     */
    public boolean contains(N node) {
        checkRep();
        if (node == null) {
            throw new IllegalArgumentException("node must not be null");
        }
        Node<N> n = new Node<>(node);
        checkRep();
        return nodeMap.containsKey(n);
    }

    /**
     * <b>Node</b> is an immutable representation of a labeled node in a graph.
     */
    public static class Node<T> {
        /**
         * The label of this Node.
         */
        private final T label;

        // Abstraction Function:
        // label = the label of this node

        // Representation Function:
        // (It's implied that there are no null fields in Node)

        /**
         * Construct a labeled node.
         *
         * @spec.requires label != null
         * @spec.effects Constructs a new labeled node
         */
        public Node(T label) {
            this.label = label;
            checkRep();
        }

        /**
         * Gets the label of this Node.
         *
         * @return the label of this Node
         */
        public T getLabel() {
            return label;
        }

        /**
         * Standard hashCode function.
         *
         * @return an int that all objects equal to this will also
         */
        @Override
        public int hashCode() {
            return 31 * label.hashCode();
        }

        /**
         * Standard equality operation.
         *
         * @param obj the object to be compared for equality
         * @return true iff 'obj' is an instance of a Node and 'this' and 'obj' represent the same Node.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            } else {
                Node<?> n = (Node<?>) obj;
                return this.label.equals(n.label);
            }
        }

        /**
         * Standard object to String conversion.
         *
         * @return the object as a string
         */
        @Override
        public String toString() {
            return label.toString();
        }

        /**
         * Throws an exception if the representation invariant is violated.
         *
         * @throws RuntimeException if representation invariant is violated
         */
        private void checkRep() {
            assert (label != null) : "label == null";
        }
    }

    /**
     * <b>Edge</b> is an immutable representation of a directed, labeled, possibly unique Edge in a graph.
     */
    public static class Edge<T> {
        /**
         * The label of this Edge.
         */
        private final T label;

        /**
         * The id of this Edge.
         * Necessary for when the Graph contains edges with the same label
         * but nodes that shouldn't be connected.
         */
        private final String id;

        /**
         * A boolean flag indicating whether the Edge should be interpreted as unique.
         */
        private final boolean isUnique;

        /**
         * A boolean flag indicating whether the Edge is a self loop.
         */
        private final boolean isSelfLoop;

        // Abstraction Function:
        // the label of this Edge = label
        // the id of this Edge = id
        // the flag indicating whether the Edge should be interpreted as unique = isUnique
        // the flag indicating whether the Edge is a self loop = isUnique

        // Representation Function:
        // (It's implied that there are no null fields in Edge)

        /**
         * Create a new edge with the given label.
         *
         * @param label the label of this Edge
         * @spec.effects creates a new Edge
         */
        public Edge(T label) {
            this(label, false);
        }

        /**
         * Create a new edge with the given label and a unique id.
         *
         * @param label the label of this Edge
         * @param isUnique sets the unique flag for this Edge
         * @spec.effects creates a new Edge
         */
        public Edge(T label, boolean isUnique) {
            this(label, isUnique, false);
            checkRep();
        }

        /**
         * Create a new edge with the given label and a unique id.
         *
         * @param label the label of this Edge
         * @param isUnique sets the unique flag for this Edge
         * @param isSelfLoop indicates that this Edge is a self loop
         * @spec.effects creates a new Edge
         */
        public Edge(T label, boolean isUnique, boolean isSelfLoop) {
            this.label = label;
            if (isUnique) {
                this.id = UUID.randomUUID().toString();
                this.isUnique = true;
            } else {
                this.id = "";
                this.isUnique = false;
            }
            this.isSelfLoop = isSelfLoop;
            checkRep();
        }

        /**
         * Gets the label of this Edge.
         *
         * @return the label of this Edge
         */
        public T getLabel() {
            return label;
        }

        /**
         * Gets the id of this Edge.
         *
         * @return the id of this Edge
         */
        public String getId() {
            return id;
        }

        /**
         * Gets the isUnique flag of this Edge.
         *
         * @return whether the Edge is unique
         */
        public boolean getIsUnique() {
            return isUnique;
        }

        /**
         * Returns whether this Edge is a self loop
         *
         * @return whether this Edge is a self loop
         */
        public boolean getIsSelfLoop() {
            return isSelfLoop;
        }

        /**
         * Standard hashCode function.
         *
         * @return an int that all objects equal to this will also
         */
        @Override
        public int hashCode() {
            int result = 31 * label.hashCode();
            if (!isUnique) {
                result = result + 31 * id.hashCode();
            }
            return result;
        }

        /**
         * Standard equality operation.
         *
         * @param obj the object to be compared for equality
         * @return true iff 'obj' is an instance of a Edge and 'this' and 'obj' represent the same Edge.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            } else {
                Edge<?> e = (Edge<?>) obj;
                return this.label.equals(e.getLabel()) &&
                        this.id.equals(e.getId()) &&
                        this.isUnique == e.isUnique;
            }
        }

        /**
         * Standard object to String conversion.
         *
         * @return the object as a string
         */
        @Override
        public String toString() {
            return label.toString();
        }

        /**
         * Throws an exception if the representation invariant is violated.
         *
         * @throws RuntimeException if representation invariant is violated
         */
        private void checkRep() {
            assert (label != null) : "label == null";
            assert (id != null) : "id == null";
        }
    }

    /**
     * Throws an exception if the representation invariant is violated.
     *
     * @throws RuntimeException if representation invariant is violated
     */
    private void checkRep() {
        if (CHECK_REP_SHALLOW) {
            assert (nodeMap != null) : "nodeMap == null";
            assert (edgeMap != null) : "edgeMap == null";

            if (CHECK_REP_DEEP) {
                for (Node<N> n : nodeMap.keySet()) {
                    assert (n != null) : "node == null";
                    Set<Edge<E>> k = nodeMap.get(n);
                    assert (k != null) : "Set<Edge> == null";
                    for (Edge<E> e : k) {
                        assert (e != null) : "Edge == null";
                    }
                }
                for (Edge<E> e : edgeMap.keySet()) {
                    assert (e != null) : "Edge == null";
                    Set<Node<N>> k = edgeMap.get(e);
                    assert (k != null) : "Set<Node> == null";
                    for (Node<N> n : k) {
                        assert (n != null) : "Node == null";
                    }
                }
            }
        }
    }
}
