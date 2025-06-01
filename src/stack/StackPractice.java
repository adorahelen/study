package stack;

import java.util.ArrayDeque;
// Deque(Double-Ended Queue)의 배열 기반 구현체
// - “양쪽 끝에서 삽입과 삭제가 가능한 큐”
// - Stack과 Queue 모두를 대체할 수 있어 성능이 우수하고 실무에서 자주 사용
// - 스택 구조로 쓸 때 (push, pop) || 큐 구조로 쓸 때 (offer, poll)

public class StackPractice {

    static class parentheses {

        boolean solution(String s) {
            ArrayDeque<Character> stack = new ArrayDeque<>();
            char[] a = s.toCharArray();

            for (char c : a) {
                if (c == '(') {
                    stack.push(c); // 여는 괄호는 스택에 쌓음
                } else {
                    // 닫는 괄호인데 스택이 비었거나 짝이 안 맞으면 false
                    if (stack.isEmpty() || stack.pop() == c) {
                        return false;
                    }
                }
            }

            return stack.isEmpty(); // 끝났는데 스택이 비어있어야 true
        }


    }

    public static void main(String[] args) {
        String input1 = "()()";
        String input2 = "(()";


        parentheses p = new parentheses();
        System.out.println(p.solution(input1));
        System.out.println(p.solution(input2));

    }
}
