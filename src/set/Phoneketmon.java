package set;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Phoneketmon {

    static String[] inputA = {"Pikachu", "Eevee", "Charmander", "Pikachu"};
    static String[] inputB = {"Squirtle", "Squirtle", "Squirtle", "Bulbasaur", "Bulbasaur", "Snorlax"};
    static String[] inputC = {"Charizard", "Charizard", "Charizard", "Bulbasaur", "Bulbasaur", "Bulbasaur"};


    static class Solution {


          Solution() { // 생성자
              int[] input1 = {3,1,2,3};
              int[] input2 = {3,3,3,2,2,4};
              int[] input3 = {3,3,3,2,2,2};
              System.out.println(solve(input1));
              System.out.println(solve(input2));
              System.out.println(solve(input3));
          }


            public int solve(int[] phoketmon) {
                // 리스트에서 중복을 제거한 집합을 구함
                HashSet<Integer> set = Arrays.stream(phoketmon).boxed().collect(Collectors.toCollection(HashSet::new));
                // 포켓몬 총 개수
                int n = phoketmon.length;
                // 선택할 폰켓몬의 수
                int k = n / 2;
                return Math.min(k, set.size());
            }

            // 오버로딩
          public int solve(String[] phoketmon) {
              HashSet<String> set = new HashSet<>(Arrays.asList(phoketmon));
              int n = phoketmon.length;
              int k = n / 2;
              return Math.min(k, set.size());
          }



        }

    public static void main(String[] args) {
          // 01. Solution 클래스 안에서 생성자를 활용하면, main에서 메서드를 별도로 호출하지 않고도 로직을 자동 실행
          // 02. 모든 입력, 출력, 로직을 main 바깥의 내부 클래스에서 처리하여 main을 깔끔하게 1줄로 유지할 수 있다는 설계 방식

//          new Solution(); // Solution 클래스의 생성자에서 3개의 배열을 테스트하고 결과를 바로 출력함

        Solution solution = new Solution();

        System.out.println("✅ input1: " + solution.solve(inputA)); // 2
        System.out.println("✅ input2: " + solution.solve(inputB)); // 3
        System.out.println("✅ input3: " + solution.solve(inputC)); // 2

    }
}
