package array;

import java.util.Arrays;

public class ArrayPractice {

    String hyu = "한양대 도서관에는 사람이 많다.";

     class zero {

        long i = 601;
        String sunday = "오늘은 일요일이다.";
        int [] problem = {1, 2, 3, 4, 5, -1};

        int[] solution(int [] problem) {
            System.out.println(i);
            System.out.println(sunday);
            System.out.println(Arrays.toString(problem));
            Arrays.sort(problem);
            System.out.println(Arrays.toString(problem));

            return problem;
        }


    }


    public static void main(String[] args) {
        ArrayPractice obj = new ArrayPractice();
        System.out.println(obj.hyu);


    //   ✅ 문제점 1: 내부 클래스 zero의 인스턴스를 잘못 생성함
        // zero zero01 = new zero();
        // 원인 : 내부 클래스 zero는 비정적(non-static) 내부 클래스이기 때문에, 외부 클래스 array03의 인스턴스 없이 단독으로 생성할 수 없음
        // 해결 : class zero -> static class zero
        ArrayPractice.zero zero01 = obj.new zero();    // 외부 클래스 인스턴스 후 내부 클래스 인스턴스 생성 (정석 방법)
        // => outer.new Inner() 형식 적용

        // 문제점 2: zero01.solution(); 메서드 호출 시 인자가 없음
        // zero01.solution();
        // => 파라미터를 없애고 인스턴스 변수 problem을 그대로 사용하도록 수정 (1)
        // => 또는 solution(int[] problem)에 외부에서 배열을 넘기도록 호출 (2)
        int[] input = {5, 4, 3, 2, 1};
        zero01.solution(input);


    }


}


