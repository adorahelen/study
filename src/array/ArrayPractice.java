package array;

import java.util.Arrays;
import java.util.Collections;

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

    class one {
         String[] PII = {"주민등록번호", "운전면허번호", "휴대폰번호", "이메일", "집주소"};
         String[] clone = PII.clone();

        int[] problem = {1, 2, 3, 4, 5, -1};
        int[] clone2 = problem.clone();

        void solution () {
            Arrays.sort(clone);
            System.out.println(Arrays.toString(clone));

            Arrays.sort(clone2);
            System.out.println(Arrays.toString(clone2));
        }

    }

    class two { //  스트림을 활용하여 중복을 제거하고 가독성을 높이고자 기본형 배열을 객체로 박싱하여 처리, 최종 반환 타입에 맞춰 다시 언박싱
         int [] solution(int[] arr) {
             Integer[] result = Arrays.stream(arr).boxed().distinct().toArray(Integer[]::new); // 박싱 후 중복제거

             Arrays.sort(result, Collections.reverseOrder()); // 내림차순 정렬

             int [] sum = Arrays.stream(result).mapToInt(Integer::intValue).toArray(); // 언박싱 후 반환
             System.out.println(Arrays.toString(sum));
             return sum;
         }
    }




    public static void main(String[] args) {
//        ArrayPractice obj = new ArrayPractice();
//        System.out.println(obj.hyu);

    //   ✅ 문제점 1: 내부 클래스 zero의 인스턴스를 잘못 생성함
        // zero zero01 = new zero();
        // 원인 : 내부 클래스 zero는 비정적(non-static) 내부 클래스이기 때문에, 외부 클래스 array03의 인스턴스 없이 단독으로 생성할 수 없음
        // 해결 : class zero -> static class zero
       // ArrayPractice.zero zero01 = obj.new zero();    // 외부 클래스 인스턴스 후 내부 클래스 인스턴스 생성 (정석 방법)
        // => outer.new Inner() 형식 적용

        // 문제점 2: zero01.solution(); 메서드 호출 시 인자가 없음
        // zero01.solution();
        // => 파라미터를 없애고 인스턴스 변수 problem을 그대로 사용하도록 수정 (1)
        // => 또는 solution(int[] problem)에 외부에서 배열을 넘기도록 호출 (2)
        int[] input = {5, 4, 3, 2, 1};
//        zero01.solution(input);
//
//        ArrayPractice obj = new ArrayPractice();
//        ArrayPractice.one one1 = obj.new one();
//        one1.solution();

        int[] input2 = {4, 2, 2, 1, 3, 4};
        int[] input3 = {2, 1, 1, 3, 2, 5, 4};

      //  new ArrayPractice().new one().solution();
      //  new ArrayPractice().new zero().solution(input);
        new ArrayPractice().new two().solution(input2);
        new ArrayPractice().new two().solution(input3);


    }


}


