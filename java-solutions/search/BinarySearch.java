package search;

public class BinarySearch {

    static int iSearch(int key, int[] a) {
        int r = a.length;
        // r' = a.length
        if (r == 0) {
            // r' == 0
            return 0;
        } else {
            // r' != 0
        }
        int l = -1;
        // l' = -1
        //  a[l'] < key < a[r']
        while (l < r - 1) {
            // a[l'] <= key <= a[r'] && l' < r' - 1
            int m = (l + r) / 2;
            // m' = (l' + r') / 2
            if (a[m] <= key) {
            // a[l'] <= key <= a[r'] && l' < r' - 1 && a[m'] <= key
                r = m;
            // a[l'] <= key <= a[r'] && l' < r' - 1 && a[m'] <= key && r' = m'
            } else {
            // a[l'] <= key <= a[r'] && l' < r' - 1 && !(a[m'] <= key)
                l = m;
                // a[l'] <= key <= a[r'] && l' < r' - 1 && !(a[m'] <= key) && l' = m'
            }
        }
        // a[l'] < key < a[r'] && l' >= r' - 1
        // key <= a[r']
        return r;
    }
/*
    private static int left = -1;
    static int rSearch(long key, int[] a, int right) {
        int m = (left + right) / 2;
        if (right - left == 1) {
            return right;
        }
        if (a[m] <= key) {
            return rSearch(key, a, m);
        } else {
            left = m ;
            return rSearch(key, a, right);
        }
    }*/

    static int rSearch(long key, int[] a, int left, int right) {

        if (right == left + 1) {
        // right' = left' + 1
            return right;
        } else {
        // !(right' = left' + 1)
        }
        int m = (left + right) / 2;
        // m' = (left' + right') / 2
        if (a[m] <= key) {
        // !(right' = left' + 1) && a[m'] <= key
            right = m;
        // a[m'] <= key && right' = m
        } else {
        // !(right' = left' + 1) && !(a[m'] <= key)
            left = m;
        // !(a[m'] <= key) && left' = m
        }
        return rSearch(key, a, left, right);
    }

    // args.length != 0
    public static void main(String[] args) {

        int[] mas = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            mas[i - 1] = Integer.parseInt(args[i]);
        }
        int a = rSearch(Long.parseLong(args[0]), mas, -1, mas.length);
        System.out.println(a);
        /*
        int a = iSearch(Long.parseLong(args[0]), mas, mas.length);
        System.out.println(a);
        */
    }
}