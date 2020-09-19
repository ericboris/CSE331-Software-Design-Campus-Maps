package marvel.junitTests;

import graph.Graph;
import org.junit.Assert;
import org.junit.Test;
import marvel.MarvelPaths;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MarvelPathsTest {
    /**
     * Test loadGraph on Staff heroes and check each nodes children
     */
    @Test
    public void testLoadGraphOnStaffHeroes() {
        String filename = "staffSuperheroes.tsv";
        Graph<String, String> graph = MarvelPaths.loadGraph(filename);

        List<String> e = new ArrayList<>();
        e.add("Grossman-the-Youngest-of-them-all(CSE331)");
        e.add("Notkin-of-the-Superhuman-Beard(CSE331)");
        e.add("Notkin-of-the-Superhuman-Beard(CSE403)");
        e.add("Perkins-the-Magical-Singing-Instructor(CSE331)");
        Assert.assertEquals(e, graph.listChildrenLabels("Ernst-the-Bicycling-Wizard"));

        List<String> g = new ArrayList<>();
        g.add("Ernst-the-Bicycling-Wizard(CSE331)");
        g.add("Notkin-of-the-Superhuman-Beard(CSE331)");
        g.add("Perkins-the-Magical-Singing-Instructor(CSE331)");
        Assert.assertEquals(g, graph.listChildrenLabels("Grossman-the-Youngest-of-them-all"));

        List<String> n = new ArrayList<>();
        n.add("Ernst-the-Bicycling-Wizard(CSE331)");
        n.add("Ernst-the-Bicycling-Wizard(CSE403)");
        n.add("Grossman-the-Youngest-of-them-all(CSE331)");
        n.add("Perkins-the-Magical-Singing-Instructor(CSE331)");
        Assert.assertEquals(n, graph.listChildrenLabels("Notkin-of-the-Superhuman-Beard"));

        List<String> p = new ArrayList<>();
        p.add("Ernst-the-Bicycling-Wizard(CSE331)");
        p.add("Grossman-the-Youngest-of-them-all(CSE331)");
        p.add("Notkin-of-the-Superhuman-Beard(CSE331)");
        Assert.assertEquals(p, graph.listChildrenLabels("Perkins-the-Magical-Singing-Instructor"));
    }

    /**
     * Test the time to load the entire marvel dataset
     */
    @Test
    public void testLoadMarvelFull() {
        String filename = "marvel.tsv";
        Graph<String, String> graph = MarvelPaths.loadGraph(filename);
        // No assert, checking for duration only
    }

    /**
     * Test load graph will null argument
     */
    @Test (expected = IllegalArgumentException.class)
    public void testLoadGraphNullArgument() {
        Graph<String, String> graph = MarvelPaths.loadGraph(null);
    }

    /**
     * Test load graph with file that doesn't exist
     */
    @Test (expected = AssertionError.class)
    public void testLoadGraphInvalidFileName() {
        Graph<String, String> graph = MarvelPaths.loadGraph("");
    }

    /**
     * Test a variety of paths between simple heroes graph
     * Paths tested include, start == end, path lengths 1 and 2, and disconnected component
     * All in both directions
     */
    @Test
    public void testFindPathsInStaffHeroes() {
        String filename = "simpleHeroes.tsv";
        Graph<String, String> graph = MarvelPaths.loadGraph(filename);

        List<List<String>> aToA = MarvelPaths.findPath(graph, "a", "a");
        List<List<String>> expAToA = new LinkedList<>();
        Assert.assertEquals(expAToA, aToA);

        List<List<String>> aToB = MarvelPaths.findPath(graph, "a", "b");
        List<List<String>> expAToB = new LinkedList<>();
        List<String> edge1ToB = new ArrayList<>();
        edge1ToB.add("b");
        edge1ToB.add("1");
        expAToB.add(edge1ToB);
        Assert.assertEquals(expAToB, aToB);

        List<List<String>> aToC = MarvelPaths.findPath(graph, "a", "c");
        List<List<String>> expAToC = new LinkedList<>();
        expAToC.add(edge1ToB);
        List<String> edge4ToC = new ArrayList<>();
        edge4ToC.add("c");
        edge4ToC.add("4");
        expAToC.add(edge4ToC);
        Assert.assertEquals(expAToC, aToC);

        List<List<String>> aToD = MarvelPaths.findPath(graph, "a", "d");
        List<List<String>> expAToD = new LinkedList<>();
        List<String> edge2ToD = new ArrayList<>();
        edge2ToD.add("d");
        edge2ToD.add("2");
        expAToD.add(edge2ToD);
        Assert.assertEquals(expAToD, aToD);

        List<List<String>> aToE = MarvelPaths.findPath(graph, "a", "e");
        List<List<String>> expAToE = new LinkedList<>();
        Assert.assertEquals(expAToE, aToE);

        List<List<String>> dToA = MarvelPaths.findPath(graph, "d", "a");
        List<List<String>> expDToA = new LinkedList<>();
        List<String> edge2ToA = new ArrayList<>();
        edge2ToA.add("a");
        edge2ToA.add("2");
        expDToA.add(edge2ToA);
        Assert.assertEquals(expDToA, dToA);

        List<List<String>> dToB = MarvelPaths.findPath(graph, "d", "b");
        List<List<String>> expDToB = new LinkedList<>();
        List<String> edge3ToB = new ArrayList<>();
        edge3ToB.add("b");
        edge3ToB.add("3");
        expDToB.add(edge3ToB);
        Assert.assertEquals(expDToB, dToB);

        List<List<String>> dToC = MarvelPaths.findPath(graph, "d", "c");
        List<List<String>> expDToC = new LinkedList<>();
        expDToC.add(edge3ToB);
        expDToC.add(edge4ToC);
        Assert.assertEquals(expDToC, dToC);

        List<List<String>> dToD = MarvelPaths.findPath(graph, "d", "d");
        List<List<String>> expDToD = new LinkedList<>();
        Assert.assertEquals(expDToD, dToD);

        List<List<String>> dToE = MarvelPaths.findPath(graph, "d", "e");
        List<List<String>> expDToE = new LinkedList<>();
        Assert.assertEquals(expDToE, dToE);
    }

    /**
     * Test a 0 edge hop in marvel 1000
     */
    @Test
    public void testMarvelZeroHop() {
        // LoadGraph marvel marvel.tsv
        String filename = "marvel1000.tsv";
        Graph<String, String> graph = MarvelPaths.loadGraph(filename);

        List<List<String>> path = MarvelPaths.findPath(graph, "MOJO", "MOJO");

        List<List<String>> expected = new LinkedList<>();
        Assert.assertEquals(expected, path);
    }

    /**
     * Test a 1 edge hop in marvel 1000
     */
    @Test
    public void testMarvelOneHop() {
        // LoadGraph marvel marvel.tsv
        String filename = "marvel1000.tsv";
        Graph<String, String> graph = MarvelPaths.loadGraph(filename);

        // FindPath marvel BEAST/HENRY_&HANK&_P WISDOM,_PETER
        List<List<String>> path = MarvelPaths.findPath(graph, "MOJO", "ABCISSA");

        List<List<String>> expected = new LinkedList<>();
        List<String> edge1 = new ArrayList<>();
        edge1.add("ABCISSA");
        edge1.add("W2 52");
        expected.add(edge1);
        Assert.assertEquals(expected, path);
    }

    /**
     * Test a 2 edge hop in simple heroes
     */
    @Test
    public void testTiny2Hop() {
        // LoadGraph marvel marvel.tsv
        String filename = "simpleHeroes.tsv";
        Graph<String, String> graph = MarvelPaths.loadGraph(filename);

        List<List<String>> path = MarvelPaths.findPath(graph, "a", "c");

        List<List<String>> expected = new LinkedList<>();
        List<String> edge1 = new ArrayList<>();
        edge1.add("b");
        edge1.add("1");
        expected.add(edge1);

        List<String> edge2 = new ArrayList<>();
        edge2.add("c");
        edge2.add("4");
        expected.add(edge2);

        Assert.assertEquals(expected, path);
    }

    /**
     * Test a 3 edge hop in marvel 500
     */
    @Test
    public void testMarvel500ThreeHop() {
        // LoadGraph marvel marvel.tsv
        String filename = "marvel500.tsv";
        Graph<String, String> graph = MarvelPaths.loadGraph(filename);

        List<List<String>> path = MarvelPaths.findPath(graph, "RHINO", "BLOK");

        List<List<String>> expected = new LinkedList<>();
        List<String> edge1 = new ArrayList<>();
        edge1.add("HULK/DR. ROBERT BRUC");
        edge1.add("H2 159");
        expected.add(edge1);

        List<String> edge2 = new ArrayList<>();
        edge2.add("NIGHTCRAWLER/KURT WA");
        edge2.add("COC 1");
        expected.add(edge2);

        List<String> edge3 = new ArrayList<>();
        edge3.add("BLOK");
        edge3.add("W2 159");
        expected.add(edge3);

        Assert.assertEquals(expected, path);
    }

    /**
     * Test that exception is thrown if start not in graph
     */
    @Test (expected = IllegalArgumentException.class)
    public void findPathThrowsOnStartNotInGraph(){
        String filename = "marvel500.tsv";
        Graph<String, String> graph = MarvelPaths.loadGraph(filename);

        MarvelPaths.findPath(graph, "a", "BLOK");
    }

    /**
     * Test that exception is thrown if end not in graph
     */
    @Test (expected = IllegalArgumentException.class)
    public void findPathThrowsOnEndNotInGraph(){
        String filename = "marvel500.tsv";
        Graph<String, String> graph = MarvelPaths.loadGraph(filename);

        MarvelPaths.findPath(graph, "RHINO", "b");
    }
}
