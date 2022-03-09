package search;

public class BinarySearchMissing {

    // Pred: for i = 0 .. a.size-1: a[i]<=a[i-1]
    // Post: a[R] <= key || (R = 0 || a[R-1] < key) || ( a[R] != key && R = -R - 1 )
    static int iSearch(int key, int[] a) {
        int r = a.length;
        // r' = a.length
        if (r == 0) {
            // r' == 0
            return -1;
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
            if (a[m] < key) {
                // a[l'] <= key <= a[r'] && l' < r' - 1 && a[m'] <= key
                l = m;
                // a[l'] <= key <= a[r'] && l' < r' - 1 && a[m'] <= key && l' = m'
            } else {
                // a[l'] <= key <= a[r'] && l' < r' - 1 && !(a[m'] <= key)
                r = m;
                // a[l'] <= key <= a[r'] && l' < r' - 1 && !(a[m'] <= key) && r' = m'
            }
        }
        // a[l'] < key < a[r'] && l' >= r' - 1
        // a[r'] >= key
        if (r >= a.length) {
            // r' >= a.length
            return -r - 1;
        } else {
            // !(r' >= a.length)
            if (a[r] != key) {
                // !(r' >= a.length)
                if (r == 0) {
                    // !(r' >= a.length) && r' = 0
                    return -1;
                } else {
                    // !(r >= a.length) && r' != 0
                    return -r - 1;
                }
            } else {
                // a[r'] = key
                return r;
            }
        }

    }

    // Pred: for i = 0 .. a.size-1: a[i] <= a[i-1] && right <= a.size - 1 && left >= -1 && ( ( a[0] <= key <= a[n]) ||
    // (key > a[n] || key < a[0]) ) && left < right

    // Post: a[R] <= key || (R = 0 && a[R-1] < key) || ( a[R] != key && R = -R - 1 )
    static int rSearch(long key, int[] a, int left, int right) {


        if (right == left + 1) {
            // right' = left' + 1
            if (right >= a.length) {
                // right' = left' + 1 && right' >= a.length
                return -right - 1;
            } else {
                // right' = left' + 1 && !(right' >= a.length)
                if (a[right] != key) {
                    // right' = left' + 1 && !(right' >= a.length) && a[right'] != key
                    if (right == 0) {
                        // right' = left' + 1 && !(right' >= a.length) && a[right'] != key && right' = 0
                        return -1;
                    } else {
                        // right' = left' + 1 && !(right' >= a.length) && a[right'] != key && right' != 0
                        return -right - 1;
                    }
                } else{
                    // right' = left' + 1 && !(right' >= a.length) && a[right'] = key
                    return right;
                }
            }
            // right' = left' + 1
        } else {
            // !(right' = left' + 1)
        }
        int m = (left + right) / 2;
        // m' = (left' + right') / 2
        if (a[m] < key) {
            // !(right' = left' + 1) && a[m'] <= key
            left = m;
            // a[m'] <= key && right' = m
        } else {
            // !(right' = left' + 1) && !(a[m'] <= key)
            right = m;
            // !(a[m'] <= key) && left' = m
        }
        return rSearch(key, a, left, right);
    }

    // Pred: args.length != 0
    // Post: a[R] <= key && (R = 0 || a[R-1] < key)
    public static void main(String[] args) {

        int[] mas = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            mas[i - 1] = Integer.parseInt(args[i]);
        }
        int a = rSearch(Long.parseLong(args[0]), mas, -1, mas.length);
        System.out.println(a);

//        int a = iSearch(Integer.parseInt(args[0]), mas);
//        System.out.println(a);

    }
}