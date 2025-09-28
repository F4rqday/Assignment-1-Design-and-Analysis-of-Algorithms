package dnc.cli;

import java.util.Arrays;

import dnc.features.ClosestPair2D;
import dnc.features.DeterministicSelect;
import dnc.features.MergeSort;
import dnc.features.Metrics;
import dnc.features.QuickSort;

public class Main {
    public static void main(String[] args) {
        System.out.println("Divide & Conquer Demo");

        int[] a = {5, 2, 9, 1, 5, 6};
        Metrics mq = new Metrics();
        QuickSort.sort(a, mq);
        System.out.println("QuickSort: " + Arrays.toString(a) + " | " + mq);

        int[] b = {4, 3, 2, 1};
        Metrics mm = new Metrics();
        MergeSort.sort(b, mm);
        System.out.println("MergeSort: " + Arrays.toString(b) + " | " + mm);

        int[] c = {7, 10, 4, 3, 20, 15};
        Metrics ms = new Metrics();
        int k = 2; 
        int ans = DeterministicSelect.kth(c, k, ms);
        System.out.println("Select k=" + k + " => " + ans + " | " + ms);

        double[][] pts = {{0,0},{1,1},{2,2},{2,0},{1.1,1}};
        Metrics md = new Metrics();
        double d = ClosestPair2D.solve(pts, md);
        System.out.println("ClosestPair2D: d=" + d + " | " + md);
    }
}
