package pathfinder.datastructures;

import graph.Graph;

import java.util.*;

public class ShortestPath {

    // No AF or RI required

    /**
     * Finds the shortest path between Graph nodes start and end using dijkstra's algorithm.
     * Returns an empty path if no path between points exists
     *
     * @param g the graph to search
     * @param start the node to search from
     * @param end the node to find a path to
     * @param <T> the node data type
     * @return the shortest path between start and end
     * @spec.requires start in graph
     * @spec.requires end in graph
     * @spec.requires no argument is null
     */
    public static <T> Path<T> dijkstra(Graph<T, Double> g, T start, T end) {
        PriorityQueue<Path<T>> visited = new PriorityQueue<>();
        HashSet<T> explored = new HashSet<>();

        visited.add(new Path<>(start));

        while (!visited.isEmpty()) {
            // Let curr be the highest priority (minimum valued) element from the queue
            Path<T> minPath = visited.remove();
            T curr = minPath.getEnd();

            if (curr.equals(end)) {
                return minPath;
            }
            if (explored.contains(curr)) {
                continue;
            }

            // Get the edges incident to curr to search over
            Set<Graph.Edge<Double>> edges = g.getEdges(curr);

            for (Graph.Edge<Double> edge : edges) {
                Set<T> children = new HashSet<>(g.getNodeLabels(edge));
                for (T child : children) {
                    // If the child of curr is being encountered for the first time then
                    if (!explored.contains(child) && !child.equals(curr)) {
                        // Add it to the queue to search
                        Path<T> newPath = minPath.extend(child, edge.getLabel());
                        visited.add(newPath);

                        // And, if child happens to be the destination sought then
                        // break early and return the path
                        /*
                        if (child.equals(end)) {
                            return newPath;
                        }*/
                    }
                }
            }
            // There's no reason to search a previously explored node again
            explored.add(curr);
        }
        // If no path exists then
        // return an empty path
        return new Path<>(start);
    }

}
