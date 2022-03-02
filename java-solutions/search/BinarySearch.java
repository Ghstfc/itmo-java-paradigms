package search;

public class BinarySearch {

    // Pred: for i = 0...a.size-1 : a[i] >= a[i+1] && l < key <= r
    // Post: key >= a[R(принадлежит)[0..a.length-1)] || R == a.length && for i = 0 ... key : a[i] > R
    static int iSearch(int key, int[] a) {
        // Pred
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
        //  a[l'] < key <= a[r']
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
        // a[l'] < key <= a[r'] && l' >= r' - 1
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
    }
*/
    
    // Pred: for i = 0...a.size-1 : a[i] >= a[i+1] && l < key <= r && l >=0 && r <= a.size
    // Post: R = a.size || R >= [0..a.size] && R >= 0 && R <= key && for i = 0 ... key  : a[i] <= R && for i = key + 1 ... a.size: a[i] >= R
    static int rSearch(long key, int[] a, int left, int right) {
        // Pred
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
        // m' = (left' + right') / 2 && ( ( a[m'] <= key && right' = m ) || ( !(a[m'] <= key) && left' = m) )  
        return rSearch(key, a, left, right);
    }

    // Pred: args.length != 0
    // Post: a[r] < a[0..r-1] && a[r]>=a[ r+1 ... a.size ]
    public static void main(String[] args) {
        int[] mas = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            mas[i - 1] = Integer.parseInt(args[i]);
        }
        left = -1;
        int a = rSearch(Long.parseLong(args[0]), mas, -1, mas.length);
        System.out.println(a);
        /*
        int a = iSearch(Long.parseLong(args[0]), mas, mas.length);
        System.out.println(a);
        */
    }
}