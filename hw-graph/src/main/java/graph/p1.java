package graph;

import java.util.ArrayList;
import java.util.List;

public class p1 {
    public static int[][] t;

    public static int[][] genTable(int m, int n) {
        int[][] table = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                table[i][j] = -1;
            }
        }
        return table;
    }

    public static int maxCut(int m, int n, List<int[]> C) {
        // Initialize the table if it isn't already
        if (t == null) {
            t = genTable(m, n);
        }

        // no need to recalculate if mxn already found
        if (t[m][n] != -1) {
            return t[m][n];
        }

        // Only need to check tile sizes that are smaller than mxn
        // If mxn is smaller than a tile size in either dimension then
        // remove it from the array
        List<int[]> K = new ArrayList<>(C);
        for (int[] c : C) {
            int x = c[0];
            int y = c[1];

            // If the tile c has the same dimensions as mxn then
            // an optimal size is found
            if (x == m && y == n) {
                t[x][y] = x * y;
                return x * y;
            }

            if (m < x || n < y) {
                K.remove(c);
            }
        }

        // Let vMax and hMax be the maximum values produced by
        // cutting mxn vertically and horizontally
        // Later, we'll return whichever is larger
        int vMax = 0;
        int hMax = 0;

        // Examine each remaining tile size in K
        for (int[] c : K) {
            int x = c[0];
            int y = c[1];

            // We check that x != m to avoid an infinite loop
            // So, if x != m then we can cut mxn vertically into two smaller pieces
            if (x != m) {
                // Let v be the sum of the values of those smaller pieces
                int v = maxCut(m - x, n, C) + maxCut(x, n, C);
                if (v > vMax) {
                    vMax = v;
                }
            }

            // We check that y != n avoid an infinite loop
            // So, if y != n then we can cut mxn horizontally into two smaller pieces
            if (y != n) {
                // Let h be the sum of the values of those smaller pieces
                int h = maxCut(m, n - y, C) + maxCut(m, y, C);
                if (h > hMax) {
                    hMax = h;
                }
            }
        }

        // Let mnMax be the cut, vertically or horizontally,
        // by a tile c such that that cut maximizes the value of mxn
        int mnMax = Math.max(vMax, hMax);

        // And store the result for quick access
        t[m][n] = mnMax;

        return mnMax;
    }

    public static void printTable(int[][] t) {
        for (int r = 0; r < t.length; r++) {
            System.out.println("r = " + r);
            for (int c = 0; c < t[r].length; c++) {
                System.out.print(t[r][c] + " ");
            }
            System.out.println("\n");
        }
    }
}
