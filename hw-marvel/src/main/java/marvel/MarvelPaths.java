package marvel;

import graph.Graph;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Static class for loading a graph from a file and finding shortest paths in the graph.
 */
public class MarvelPaths {
    // Abstraction Function and Representation Invariant
    // would usually go here but MarvelParser is static

    /**
     * Create a bidirectional graph from a tsv file.
     *
     * @spec.requires fileName != null
     * @param fileName the name of the file to create a graph from
     * @return a graph from of the data in fileName
     */
    public static Graph<String, String> loadGraph(String fileName) {
        Graph<String, String> g = new Graph<>();

        Spliterator<HeroAppearance> data = MarvelParser.parseData(fileName);
        Stream<HeroAppearance> dataStream = StreamSupport.stream(data, true);
        dataStream.forEach(ha -> {g.addNode(ha.getHero());
                                  g.addEdge(ha.getHero(), ha.getBook());
        });

        return g;
    }

    /**
     * Find the shortest path between two nodes in graph g.
     * Returns the path as a list of list of strings
     *
     * @param g the graph to find the path in
     * @param start the node to start the search from
     * @param end the node to look for a path to
     * @return the shortest path between start and end in g
     * @spec.requires g.contains(start)
     * @spec.requires g.contains(end)
     * @spec.requires no parameter is null
     * @throws IllegalArgumentException if start or end not in graph
     */
    public static List<List<String>> findPath(Graph<String, String> g, String start, String end) {
        if (!g.contains(start) || !g.contains(end)) {
            throw new IllegalArgumentException("start or end not in graph.");
        }

        Queue<String> visited = new LinkedList<>();
        Set<String> explored = new HashSet<>();
        Map<String, List<List<String>>> paths = new HashMap<>();
        visited.add(start);
        paths.put(start, new LinkedList<>());

        // use bfs to find the shortest path
        while (!visited.isEmpty()) {
            String parent = visited.remove();
            // break early if the current node has already been explored
            if (explored.contains(parent)) {
                continue;
            }
            // or if the start node is the destination node
            if (parent.equals(end)) {
                return paths.get(parent);
            }

            Queue<String> children = new LinkedList<>(g.getChildrenLabels(parent));
            List<String> edgesList = new ArrayList<>(g.getEdgeLabels(parent));
            Collections.sort(edgesList);

            // match each child in children with the lexicographically first
            // edge that connects between child and parent
            while (!children.isEmpty()) {
                String child = children.remove();
                if (!visited.contains(child) && !explored.contains(child)) {
                    visited.add(child);
                    for (String edge : edgesList) {
                        if (g.getNodeLabels(edge).contains(child)) {
                            // and add the path between child and the root parent to paths
                            List<List<String>> path = new ArrayList<>(paths.get(parent));
                            List<String> pair = new LinkedList<>(Arrays.asList(child, edge));
                            path.add(pair);
                            paths.putIfAbsent(child, path);
                            break;
                        }
                    }
                }
            }
            explored.add(parent);
        }
        // if a path was found, return it
        if (paths.containsKey(end)) {
            return paths.get(end);
        }
        // otherwise the path doesn't exist
        return new LinkedList<>();
    }

    public static void main(String[] args) {}
}
