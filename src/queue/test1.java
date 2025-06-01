package queue;

import java.util.ArrayDeque;

public class test1 { // 요세푸스 문제

    static int human = 5;
    static int target = 2;

    static class Solution {
        int solve(int human, int target) {

            // 1부터 N(human)까지의 번호를 deque 에 추가
            ArrayDeque<Integer> queue = new ArrayDeque<>();
            for (int i = 1; i <= human; i++) {
                queue.addLast(i);
            }

            // deque 에 하나의 요소가 남을 때까지 반복
            while (queue.size() > 1) {
                // K(target) 번째 요소를 찾기 위해 앞에서부터 제거하고 뒤에 추가
                for (int i = 0; i < target -1; i++) {
                    queue.addLast(queue.pollFirst());
                }
                queue.pollFirst(); // k번째 요소 제거
            }

            return queue.pollFirst(); // 마지막 남은 요소 반환
        }
    }

    public static void main(String[] args) {

        Solution solution = new Solution();
        System.out.println(solution.solve(human, target));
    }
}
