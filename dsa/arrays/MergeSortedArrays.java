public class MergeSortedArrays {
    public static int[] merge(int[] a, int[] b) {
        if (a == null) return b == null ? new int[0] : b.clone();
        if (b == null) return a.clone();
        int n = a.length, m = b.length;
        int[] res = new int[n + m];
        int i = 0, j = 0, k = 0;
        while (i < n && j < m) {
            if (a[i] <= b[j]) res[k++] = a[i++];
            else res[k++] = b[j++];
        }
        while (i < n) res[k++] = a[i++];
        while (j < m) res[k++] = b[j++];
        return res;
    }

    private static void printArray(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i + 1 < arr.length) System.out.print(", ");
        }
        System.out.println("]");
    }

    public static void main(String[] args) {
        int[][][] tests = {
            {{1,3,5}, {2,4,6}},
            {{}, {1,2,3}},
            {{1,2,3}, {}},
            {{1,1,2}, {1,3,4}},
            {{-5,0,2}, {-6,-4,3}}
        };
        for (int[][] t : tests) {
            int[] a = t[0];
            int[] b = t[1];
            int[] m = merge(a, b);
            printArray(a);
            System.out.print(" + ");
            printArray(b);
            System.out.print(" = ");
            printArray(m);
            System.out.println();
        }
    }
}
