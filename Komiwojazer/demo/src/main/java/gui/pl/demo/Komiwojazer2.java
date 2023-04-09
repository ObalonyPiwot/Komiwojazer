package gui.pl.demo;

import java.util.ArrayList;
import java.util.Arrays;

public class Komiwojazer2 {

    public static ArrayList<HelloApplication.Point> bestPath = new ArrayList<HelloApplication.Point>();
    public static double minDistance;

    //points - tablica punktów      k - indeks początkowego punktu w tablicy
    public static void tsp2(ArrayList<HelloApplication.Point> points, int k) {
        int n_pts = points.size();
        int[] ti = new int[n_pts];
        Arrays.fill(ti, 0);
        ti[k] = 1;
        bestPath.clear();
        double tr = 0;

        HelloApplication.Point p = points.get(k);
        bestPath.add(p);
        for (int i = 1; i < n_pts; i++) {
            int nxt = -1;
            double mx = 1e6, d;
            for (int j = 0; j < n_pts; j++) {
                if (ti[j] == 0 && (d = p.dist(points.get(j))) < mx) {
                    mx = d;
                    nxt = j;
                }
            }
            tr += mx;
            p = points.get(nxt);
            bestPath.add(p);
            ti[nxt] = i + 1;
        }
        minDistance = tr + points.get(k).dist(p);
        System.out.println(bestPath);
    }
}
