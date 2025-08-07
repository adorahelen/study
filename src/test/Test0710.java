package test;

public class Test0710 {
    public static void main(String[] args) {
        int i = 0, c = 0;
        while (i < 10) {
            i++ ;
            String s = Integer.toString(i);
            System.out.print(s + " ");
            c *= i;

        }
        System.out.println(" ");
        System.out.println(c); // 최종


    }
}
