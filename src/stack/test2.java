package stack;

import java.util.ArrayDeque;
import java.util.HashMap;

public class test2 {
    static int solution(String s) {
        HashMap<Character, Character> map = new HashMap<>();
        map.put('(', ')');
        map.put('{', '}');
        map.put('[', ']');

        int n = s.length();
        s += s;
        int answer = 0;

        A: for (int i = 0; i < n; i++) {
            ArrayDeque<Character> stack = new ArrayDeque<>();
            for (int j = i; j < i + n; j++) {
                char c = s.charAt(j);

                if (map.containsKey(c)) {
                    stack.push(c); // 여는 괄호는 push
                } else {
                    // 닫는 괄호가 나왔을 때, 스택이 비었거나 짝이 안 맞으면 실패
                    if (stack.isEmpty() || map.get(stack.pop()) != c) {
                        continue A;
                    }
                }
            }

            if (stack.isEmpty()) {
                answer++;
            }
        }
        return answer;
    }

    public static void main(String[] args) {
        System.out.println(solution("}]()[{")); // 출력: 2
        System.out.println(solution("[](){}"));
        System.out.println(solution("[{"));
    }
}