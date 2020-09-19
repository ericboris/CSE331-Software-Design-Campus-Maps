package pathfinder.junitTests.datastructures;

import graph.Graph;
import org.junit.Assert;
import org.junit.Test;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.ShortestPath;

public class TestShortestPath {
    /**
     * Test basic dijstra
     */
    @Test
    public void testBasicDijkstra() {
        Graph<String, Double> g = new Graph<>();

        g.addNode("a");
        g.addNode("b");
        g.addNode("c");
        g.addNode("d");
        g.addNode("e");
        g.addNode("f");
        g.addNode("g");
        g.addNode("h");

        g.addBiEdge("a", "f", 2d, true);
        g.addBiEdge("a", "c", 1d, true);
        g.addBiEdge("a", "b", 3d, true);

        g.addBiEdge("b", "c", 2d, true);
        g.addBiEdge("b", "d", 4d, true);
        g.addBiEdge("b", "e", 1d, true);

        g.addBiEdge("c", "d", 1d, true);

        g.addBiEdge("d", "h", 2d, true);

        g.addBiEdge("e", "f", 3d, true);
        g.addBiEdge("e", "h", 1d, true);

        g.addBiEdge("f", "g", 1d, true);

        g.addBiEdge("g", "h", 6d, true);

        Path<String> p = ShortestPath.dijkstra(g, "a", "h");
        Path<String> exp = new Path<>("a");
        exp = exp.extend("c", 1d);
        exp = exp.extend("d", 1d);
        exp = exp.extend("h", 2d);
        Assert.assertEquals(exp, p);
    }

    /**
     * Test dijkstra on a graph where no path exists between nodes
     */
    @Test
    public void testNoPathExists(){
        Graph<String, Double> g = new Graph<>();
        g.addNode("a");
        g.addNode("z");

        Path<String> p = ShortestPath.dijkstra(g, "a", "z");
        Path<String> exp = new Path<String>("a");

        Assert.assertEquals(exp, p);
    }

    /**
     * Imitate script file tests simple graph.
     */
    @Test
    public void testScriptTestSimpleGraph(){
        Graph<String, Double> simpleGraph = new Graph<>();
        simpleGraph.addNode("A");
        simpleGraph.addNode("B");
        simpleGraph.addNode("C");
        simpleGraph.addNode("D");

        simpleGraph.addBiEdge("A", "B", 10.0, true);
        simpleGraph.addBiEdge("A", "C", 1.0, true);
        simpleGraph.addBiEdge("C", "D", 1.0, true);
        simpleGraph.addBiEdge("D", "B", 1.0, true);

        Path<String> actualPath = ShortestPath.dijkstra(simpleGraph, "A", "B");

        Path<String> expectedPath = new Path<>("A");
        expectedPath = expectedPath.extend("C", 1.0d);
        expectedPath = expectedPath.extend("D", 1.0d);
        expectedPath = expectedPath.extend("B", 1.0d);

        Assert.assertEquals(expectedPath, actualPath);
    }

    /**
     * Imitate script file tests path to self.
     */
    @Test
    public void testScriptTestPathToSelf(){
        Graph<String, Double> g = new Graph<>();

        g.addNode("A");

        Path<String> actualPath = ShortestPath.dijkstra(g, "A", "A");

        Path<String> expectedPath = new Path<>("A");

        Assert.assertEquals(expectedPath, actualPath);
    }

}
