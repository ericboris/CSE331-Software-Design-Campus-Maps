package graph.junitTests;

import graph.p1;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class p1Test {
    @Test
    public void testP1() {
        int[] c0 = new int[2];
        c0[0] = 10;
        c0[1] = 4;

        int[] c1 = new int[2];
        c1[0] = 6;
        c1[1] = 2;

        int[] c2 = new int[2];
        c2[0] = 7;
        c2[1] = 5;

        int[] c3 = new int[2];
        c3[0] = 15;
        c3[1] = 10;

        List<int[]> C = new ArrayList<>();
        C.add(c0);
        C.add(c1);
        C.add(c2);
        C.add(c3);

        int m = 21;
        int n = 11;

        //int[][] t = p1.genTable(m, n);

        int mnMax = p1.maxCut(m, n, C);


        System.out.println("m x n = " + (m * n));
        System.out.println(mnMax);
        System.out.println((m * n) - mnMax);
        //Assert.assertEquals(10, (m * n) - mnMax);
    }
}
