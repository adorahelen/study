package test;

public class arienai {
    public static void main(String[] args) {
        int a = 0, sum =0;
        while (a < 10) { // 위에서 증가하는 경우 10까지 연산하고 30이 나온다
            a++;
            if (a % 2 == 1)
                continue;;
            sum += a;
        }
        System.out.println(sum);

        /* 마지막에 증가하는 경우 위에서 걸리기에 9까지만 연산하고 25가 나오지만
        int a = 0, sum = 0;
        while (a < 10) {
            if (a % 2 == 0) { // 짝수일 때만 더하기
                sum += a;
            }
            a++; // 마지막에 증가
        }
        System.out.println(sum); */
    }

}
