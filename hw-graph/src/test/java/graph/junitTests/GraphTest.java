package graph.junitTests;

import graph.Graph;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.*;


public class GraphTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    /**
     * Test two graphs not same edges
     */
    @Test
    public void testTwoGraphsNotSameEdges() {
        Graph<String, String> g = new Graph<>();
        Graph<String, String> h = new Graph<>();

        g.addNode("n1");
        g.addNode("n2");

        h.addNode("n1");
        h.addNode("n2");
        Assert.assertEquals(g.listNodeLabels(), h.listNodeLabels());

        g.addEdge("n1", "e1");
        h.addEdge("n1", "e1");
        Assert.assertEquals(g.listNodeLabels(), h.listNodeLabels());
    }

    /**
     * Test adding null node label
     */
    @Test (expected = AssertionError.class)
    public void testAddNullNodeLabel() {
        Graph<String, String> g = new Graph<>();
        g.addNode(null);
    }

    /**
     * Test list Nodes sorted
     */
    @Test
    public void testListNodesSorted() {
        Graph<String, String> g = new Graph<>();
        g.addNode("");
        g.addNode("A");
        g.addNode("b");
        g.addNode("1");

        List<String> nodes = new ArrayList<>();
        nodes.add("1");
        nodes.add("b");
        nodes.add("");
        nodes.add("A");
        Assert.assertNotEquals(nodes, g.listNodeLabels());

        Collections.sort(nodes);
        Assert.assertEquals(nodes, g.listNodeLabels());
    }

    /**
     * Test that adding duplicate node has no effect
     */
    @Test
    public void testAddDuplicateNode() {
        Graph<String, String> g = new Graph<>();

        g.addNode("n1");
        g.addNode("n1");

        List<String> dup = new ArrayList<>();

        dup.add("n1");
        Assert.assertEquals(dup, g.listNodeLabels());

        dup.add("n1");
        Assert.assertNotEquals(dup, g.listNodeLabels());
    }

    /**
     * Test that adding duplicate node of integer type has no effect
     */
    @Test
    public void testAddDuplicateIntNode() {
        Graph<Integer, String> g = new Graph<>();

        g.addNode(1);
        g.addNode(1);

        List<Integer> dup = new ArrayList<>();

        dup.add(1);
        Assert.assertEquals(dup, g.listNodeLabels());

        dup.add(1);
        Assert.assertNotEquals(dup, g.listNodeLabels());
    }

    /**
     * Test adding null edge label
     */
    @Test (expected = AssertionError.class)
    public void testAddNullEdge() {
        Graph<String, String> g = new Graph<>();
        g.addNode("n1");
        g.addNode("n2");
        g.addEdge("n1", "n2", null);
    }

    /**
     * Test adding edge null src label
     */
    @Test (expected = AssertionError.class)
    public void testAddNullEdgeSrcLabel() {
        Graph<String, String> g = new Graph<>();
        g.addNode("n1");
        g.addNode("n2");
        g.addEdge(null, "n2", "e1");
    }

    /**
     * Test adding edge null dst label
     */
    @Test (expected = AssertionError.class)
    public void testAddNullEdgeDstLabel() {
        Graph<String, String> g = new Graph<>();
        g.addNode("n1");
        g.addNode("n2");
        g.addEdge("n1", null, "e1");
    }

    /**
     * Test adding edge with src not in graph
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddEdgeSrcNotInGraph() {
        Graph<String, String> g = new Graph<>();
        g.addNode("n1");
        g.addNode("n2");
        g.addEdge("n3", "n2", "e1");
    }

    /**
     * Test adding edge with dst not in graph
     */
    @Test (expected = IllegalArgumentException.class)
    public void testAddEdgeDstNotInGraph() {
        Graph<String, String> g = new Graph<>();
        g.addNode("n1");
        g.addNode("n2");
        g.addEdge("n1", "n3", "e1");
    }

    /**
     * Test multi edges between nodes
     */
    @Test
    public void testAddMultiEdges() {
        Graph<String, String> g = new Graph<>();
        g.addNode("n1");
        g.addNode("n2");

        g.addEdge("n1", "n2", "e1");
        g.addEdge("n1", "n2", "e2");
        g.addEdge("n2", "n1", "e3");

        List<String> n1Chid = new ArrayList<>();
        n1Chid.add("n2(e1)");
        n1Chid.add("n2(e2)");
        Assert.assertEquals(n1Chid, g.listChildrenLabels("n1"));

        List<String> n2Chid = new ArrayList<>();
        n2Chid.add("n1(e3)");
        Assert.assertEquals(n2Chid, g.listChildrenLabels("n2"));
    }

    /**
     * Test that adding duplicate edges does nothing.
     */
    @Test
    public void testAddDuplicateEdge(){
        Graph<String, String> g = new Graph<>();
        g.addNode("n1");
        g.addNode("n2");

        g.addEdge("n1", "n2", "e1");
        g.addEdge("n1", "n2", "e2");

        List<String> n1Chid = new ArrayList<>();
        n1Chid.add("n2(e1)");
        n1Chid.add("n2(e2)");

        Assert.assertEquals(n1Chid, g.listChildrenLabels("n1"));

        g.addEdge("n1", "n2", "e1");
        g.addEdge("n1", "n2", "e2");

        Assert.assertEquals(n1Chid, g.listChildrenLabels("n1"));
    }

    /**
     * Test list nodes in graph
     */
    @Test
    public void testListNodesInGraph() {
        Graph<String, String> g = new Graph<>();
        List<String> nodes = new ArrayList<>();

        Assert.assertEquals(nodes, g.listNodeLabels());

        g.addNode("n6");
        g.addNode("n4");
        g.addNode("n2");
        g.addNode("n1");
        g.addNode("n3");
        g.addNode("n5");

        nodes.add("n1");
        nodes.add("n2");
        nodes.add("n3");
        nodes.add("n4");
        nodes.add("n5");
        nodes.add("n6");

        Assert.assertEquals(nodes, g.listNodeLabels());
    }

    /**
     * Test list children with null nodeLabel
     */
    @Test (expected = AssertionError.class)
    public void testListChildrenNullNodeLabel() {
        Graph<String, String> g = new Graph<>();
        g.listChildrenLabels(null);
    }

    /**
     * Test that contains null throws error
     */
    @Test (expected = IllegalArgumentException.class)
    public void testContainsThrowsNullError() {
        Graph<String, String> g = new Graph<>();
        g.contains(null);
    }

    /**
     * Test contains for different nodes
     */
    @Test
    public void testContains() {
        Graph<String, String> g = new Graph<>();
        g.addNode("n");
        g.addNode("");
        g.addNode("1");

        Assert.assertTrue(g.contains("n"));
        Assert.assertTrue(g.contains(""));
        Assert.assertTrue(g.contains("1"));
    }

    @Test
    public void testChildrenSet() {
        Graph<String, String> g = new Graph<>();

        g.addNode("n1");
        g.addNode("n2");
        g.addNode("n3");
        g.addNode("n4");
        g.addNode("n5");
        g.addNode("n6");
        g.addNode("n7");
        g.addNode("n8");
        g.addNode("n9");
        g.addNode("n10");

        g.addEdge("n1",  "e1");
        g.addEdge("n2", "e1");
        g.addEdge("n3", "e1");
        g.addEdge("n4", "e1");
        g.addEdge("n1", "e2");
        g.addEdge("n2", "e2");
        g.addEdge("n1", "e3");
        g.addEdge("n3", "e3");
        g.addEdge("n5", "e3");
        g.addEdge("n7", "e3");
        g.addEdge("n3", "e4");
        g.addEdge("n6", "e4");
        g.addEdge("n8", "e4");
        g.addEdge("n4", "e5");
        g.addEdge("n8", "e5");
        g.addEdge("n10", "e5");
        g.addEdge("n10", "e5");
        g.addEdge("n9", "e5");

        Set<String> childrenSet = g.getChildrenLabels("n1");

        Set<String> expected = new HashSet<>();
        expected.add("n2");
        expected.add("n3");
        expected.add("n4");
        expected.add("n5");
        expected.add("n7");

        Assert.assertEquals(expected, childrenSet);
    }

    @Test
    public void testGetNodes() {
        Graph<String, String> g = new Graph<>();

        g.addNode("n1");
        g.addNode("n2");
        g.addNode("n3");
        g.addNode("n4");
        g.addNode("n5");
        g.addNode("n6");
        g.addNode("n7");
        g.addNode("n8");
        g.addNode("n9");
        g.addNode("n10");

        g.addEdge("n1",  "e1");
        g.addEdge("n2", "e1");
        g.addEdge("n3", "e1");
        g.addEdge("n4", "e1");
        g.addEdge("n1", "e2");
        g.addEdge("n2", "e2");
        g.addEdge("n1", "e3");
        g.addEdge("n3", "e3");
        g.addEdge("n5", "e3");
        g.addEdge("n7", "e3");
        g.addEdge("n3", "e4");
        g.addEdge("n6", "e4");
        g.addEdge("n8", "e4");
        g.addEdge("n4", "e5");
        g.addEdge("n8", "e5");
        g.addEdge("n10", "e5");
        g.addEdge("n10", "e5");
        g.addEdge("n9", "e5");

        Set<String> e1Nodes = g.getNodeLabels("e1");
        Set<String> e1Expected = new HashSet<>();
        e1Expected.add("n1");
        e1Expected.add("n2");
        e1Expected.add("n3");
        e1Expected.add("n4");
        Assert.assertEquals(e1Expected, e1Nodes);

        Set<String> e2Nodes = g.getNodeLabels("e2");
        Set<String> e2Expected = new HashSet<>();
        e2Expected.add("n1");
        e2Expected.add("n2");
        Assert.assertEquals(e2Expected, e2Nodes);

        Set<String> e3Nodes = g.getNodeLabels("e3");
        Set<String> e3Expected = new HashSet<>();
        e3Expected.add("n1");
        e3Expected.add("n5");
        e3Expected.add("n3");
        e3Expected.add("n7");
        Assert.assertEquals(e3Expected, e3Nodes);

        Set<String> e6Nodes = g.getNodeLabels("e6");
        Set<String> e6Expected = new HashSet<>();
        Assert.assertEquals(e6Expected, e6Nodes);
    }

    @Test
    public void testGetEdges() {
        Graph<String, String> g = new Graph<>();

        g.addNode("n1");
        g.addNode("n2");
        g.addNode("n3");
        g.addNode("n4");
        g.addNode("n5");
        g.addNode("n6");
        g.addNode("n7");
        g.addNode("n8");
        g.addNode("n9");
        g.addNode("n10");

        g.addEdge("n1",  "e1");
        g.addEdge("n2", "e1");
        g.addEdge("n3", "e1");
        g.addEdge("n4", "e1");
        g.addEdge("n1", "e2");
        g.addEdge("n2", "e2");
        g.addEdge("n1", "e3");
        g.addEdge("n3", "e3");
        g.addEdge("n5", "e3");
        g.addEdge("n7", "e3");
        g.addEdge("n3", "e4");
        g.addEdge("n6", "e4");
        g.addEdge("n8", "e4");
        g.addEdge("n4", "e5");
        g.addEdge("n8", "e5");
        g.addEdge("n10", "e5");
        g.addEdge("n10", "e5");
        g.addEdge("n9", "e5");

        Set<String> n1Edges = g.getEdgeLabels("n1");
        Set<String> n1Expected = new HashSet<>();
        n1Expected.add("e1");
        n1Expected.add("e2");
        n1Expected.add("e3");
        Assert.assertEquals(n1Expected, n1Edges);

        Set<String> n2Edges = g.getEdgeLabels("n2");
        Set<String> n2Expected = new HashSet<>();
        n2Expected.add("e1");
        n2Expected.add("e2");
        Assert.assertEquals(n2Expected, n2Edges);

        Set<String> n10Edges = g.getEdgeLabels("n10");
        Set<String> n10Expected = new HashSet<>();
        n10Expected.add("e5");
        Assert.assertEquals(n10Expected, n10Edges);

        Set<String> n11Edges = g.getEdgeLabels("n11");
        Set<String> n11Expected = new HashSet<>();
        Assert.assertEquals(n11Expected, n11Edges);
    }

    @Test
    public void testGraphOfDifferentDataTypes() {
        Graph<Integer, String> g1 = new Graph<>();
        Graph<String, Double> g2 = new Graph<>();
        Graph<Boolean, Integer> g3 = new Graph<>();

        g1.addNode(1);
        g1.addNode(2);
        g1.addEdge(1, 2, "edge");

        List<String> g1Chid = new ArrayList<>();
        g1Chid.add("2(edge)");
        Assert.assertEquals(g1Chid, g1.listChildrenLabels(1));


        g2.addNode("n1");
        g2.addNode("n2");
        g2.addEdge("n1", "n2", 0.1d);

        List<String> g2Chid = new ArrayList<>();
        g2Chid.add("n2(0.1)");
        Assert.assertEquals(g2Chid, g2.listChildrenLabels("n1"));

        g3.addNode(true);
        g3.addNode(false);
        g3.addEdge(true, false, 1);

        List<String> g3Chid = new ArrayList<>();
        g3Chid.add("false(1)");
        Assert.assertEquals(g3Chid, g3.listChildrenLabels(true));
    }

    @Test
    public void testGraphWithDuplicateEdges() {
        Graph<String, Integer> g = new Graph<>();
        g.addNode("a");
        g.addNode("b");
        g.addNode("c");
        g.addNode("d");
        g.addNode("e");
        g.addNode("f");
        g.addNode("g");
        g.addNode("h");

        g.addBiEdge("a", "f", 2, true);
        g.addBiEdge("a", "c", 1, true);
        g.addBiEdge("a", "b", 3, true);

        g.addBiEdge("b", "c", 2, true);
        g.addBiEdge("b", "d", 4, true);
        g.addBiEdge("b", "e", 1, true);

        g.addBiEdge("c", "d", 1, true);

        g.addBiEdge("d", "h", 2, true);

        g.addBiEdge("e", "f", 3, true);
        g.addBiEdge("e", "h", 1, true);

        g.addBiEdge("f", "g", 1, true);

        g.addBiEdge("g", "h", 6, true);

        List<String> aChid = g.listChildrenLabels("a");
        List<String> exp = new ArrayList<>();
        exp.add("b(3)");
        exp.add("c(1)");
        exp.add("f(2)");

        Assert.assertEquals(exp, aChid);
    }

    /**
     * Test default bidirectional edges
     */
    @Test
    public void testDefaultBidirectionalEdges() {
        Graph<String, Integer> g = new Graph<>();
        g.addNode("a");
        g.addNode("b");
        g.addNode("c");
        g.addNode("d");
        g.addNode("e");
        g.addNode("f");
        g.addNode("g");
        g.addNode("h");

        g.addBiEdge("a", "f", 2);
        g.addBiEdge("a", "c", 1);
        g.addBiEdge("a", "b", 3);

        g.addBiEdge("b", "c", 2);
        g.addBiEdge("b", "d", 4);
        g.addBiEdge("b", "e", 1);

        g.addBiEdge("c", "d", 1);

        g.addBiEdge("d", "h", 2);

        g.addBiEdge("e", "f", 3);
        g.addBiEdge("e", "h", 1);

        g.addBiEdge("f", "g", 1);

        g.addBiEdge("g", "h", 6);

        List<String> aChid = g.listChildrenLabels("a");
        List<String> exp = new ArrayList<>();
        exp.add("b(1)");
        exp.add("b(2)");
        exp.add("b(3)");
        exp.add("c(1)");
        exp.add("c(2)");
        exp.add("d(1)");
        exp.add("d(2)");
        exp.add("e(1)");
        exp.add("e(3)");
        exp.add("f(1)");
        exp.add("f(2)");
        exp.add("f(3)");
        exp.add("g(1)");
        exp.add("h(1)");
        exp.add("h(2)");
        Collections.sort(exp);

        Assert.assertEquals(exp, aChid);
    }
}
