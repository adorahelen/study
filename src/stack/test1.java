package stack;

import java.util.Stack;

public class test1 {

    static class Solution {
        public int solve(String s) {

            Stack<Character> stack = new Stack<>();

            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i); // 문자열에서 특정 위치(인덱스)에 있는 문자를 반환
                // 01. 스택이 비어 있지 않고, 현재 문자와 스택의 맨 위 문자가 같으면
                if (!stack.isEmpty() && stack.peek() == c) {
                    stack.pop();// 02. 스택의 맨 위 문자 제거
                }
                else {
                    stack.push(c);// 03. 스택에 현재 문자 추가
                }
            }

            return stack.isEmpty() ? 1 : 0; // 스택이 비어 있으면 1, 그렇지 않으면 0 반환
        }
    }

    public static void main(String[] args) {
        String input = "baabaa";
        String input2 = "cdcd";
        Solution solution = new Solution();
        System.out.println(solution.solve(input));
        System.out.println(solution.solve(input2));
    }
}
